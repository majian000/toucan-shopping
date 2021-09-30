package com.toucan.shopping.cloud.seller.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.entity.ShopCategory;
import com.toucan.shopping.modules.seller.page.ShopCategoryTreeInfo;
import com.toucan.shopping.modules.seller.redis.SellerShopKey;
import com.toucan.shopping.modules.seller.redis.ShopCategoryKey;
import com.toucan.shopping.modules.seller.service.SellerShopService;
import com.toucan.shopping.modules.seller.service.ShopCategoryService;
import com.toucan.shopping.modules.seller.vo.ShopCategoryTreeVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 店铺分类控制器
 */
@RestController
@RequestMapping("/shop/category")
public class ShopCategoryController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private SellerShopService sellerShopService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Autowired
    private SkylarkLock skylarkLock;


    /**
     * 保存分类
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestHeader(value = "toucan-sign-header",defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        String userMainId = String.valueOf(shopCategory.getUserMainId());
        try {

            boolean lockStatus = skylarkLock.lock(ShopCategoryKey.getSaveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存失败,请稍后重试");
                return resultObjectVO;
            }


            if(StringUtils.isEmpty(shopCategory.getName()))
            {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(userMainId), userMainId);

                logger.warn("分类名称为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类名称不能为空!");


                return resultObjectVO;
            }
            if(shopCategory.getUserMainId()==null)
            {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(userMainId), userMainId);

                logger.warn("用户ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空!");
                return resultObjectVO;
            }

            List<SellerShop> sellerShops = sellerShopService.findEnabledByUserMainId(shopCategory.getUserMainId());
            if(!CollectionUtils.isEmpty(sellerShops))
            {
                shopCategory.setShopId(sellerShops.get(0).getId());
            }

            if(shopCategory.getShopId()==null)
            {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(userMainId), userMainId);

                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }

            if(shopCategory.getParentId()==null)
            {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(userMainId), userMainId);

                logger.warn("上级ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("上级ID不能为空!");
                return resultObjectVO;

            }


            ShopCategoryVO queryShopCategory = new ShopCategoryVO();
            queryShopCategory.setUserMainId(shopCategory.getUserMainId());
            queryShopCategory.setShopId(shopCategory.getShopId());
            queryShopCategory.setDeleteStatus((short)0);

            //查询出当前店铺下分类数量
            long count = shopCategoryService.queryCount(queryShopCategory);

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(shopCategory.getUserMainId());
            querySellerShop.setId(shopCategory.getShopId());
            sellerShops = sellerShopService.findListByEntity(querySellerShop);
            for(SellerShop sellerShop:sellerShops)
            {
                int categoryMaxCount = sellerShop.getCategoryMaxCount()!=null?sellerShop.getCategoryMaxCount():toucan.getSeller().getShopCategoryMaxCount();
                if(count+1>categoryMaxCount)
                {
                    //释放锁
                    skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(userMainId), userMainId);

                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("分类数量已达到上限");
                    return resultObjectVO;
                }
            }

            //判断上级节点是否存在
            if(shopCategory.getParentId()!=null&&shopCategory.getParentId()!=-1)
            {
                ShopCategoryVO queryParentShopCategory = new ShopCategoryVO();
                queryParentShopCategory.setId(shopCategory.getParentId());
                queryParentShopCategory.setUserMainId(shopCategory.getUserMainId());
                queryParentShopCategory.setShopId(shopCategory.getShopId());
                List<ShopCategory> shopCategories = shopCategoryService.queryList(queryParentShopCategory);
                if(CollectionUtils.isEmpty(shopCategories))
                {
                    //释放锁
                    skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(userMainId), userMainId);

                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("保存失败,上级分类不存在");
                    return resultObjectVO;
                }

                ShopCategory parentShopCategory = shopCategories.get(0);
                if(parentShopCategory.getParentId()!=-1)
                {
                    //释放锁
                    skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(userMainId), userMainId);

                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("保存失败,只能添加二级分类");
                    return resultObjectVO;

                }
            }

            //查询最大排序值,进行排序递增
            Long categorySort = shopCategoryService.queryMaxSort(shopCategory.getUserMainId(),shopCategory.getShopId());
            if(categorySort==null)
            {
                categorySort=0L;
            }

            shopCategory.setCategorySort(categorySort+1);
            shopCategory.setId(idGenerator.id());
            shopCategory.setCreateDate(new Date());
            int row = shopCategoryService.save(shopCategory);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("保存失败,请重试!");
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("保存失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }



    /**
     * 更新分类
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        String userMainId = String.valueOf(shopCategory.getUserMainId());
        try {

            boolean lockStatus = skylarkLock.lock(ShopCategoryKey.getUpdateLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("修改失败,请稍后重试");
                return resultObjectVO;
            }


            if(StringUtils.isEmpty(shopCategory.getName()))
            {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getUpdateLockKey(userMainId), userMainId);

                logger.warn("分类名称为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类名称不能为空!");
                return resultObjectVO;
            }


            if(shopCategory.getId()==null)
            {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getUpdateLockKey(userMainId), userMainId);

                logger.warn("分类ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类ID不能为空!");
                return resultObjectVO;
            }

            ShopCategoryVO queryShopCategory = new ShopCategoryVO();
            queryShopCategory.setName(shopCategory.getName());
            queryShopCategory.setShopId(shopCategory.getShopId());
            queryShopCategory.setUserMainId(shopCategory.getUserMainId());
            queryShopCategory.setDeleteStatus((short)0);

            List<ShopCategory> shopCategoryList = shopCategoryService.queryList(queryShopCategory);
            if(!CollectionUtils.isEmpty(shopCategoryList))
            {
                if(shopCategory.getId().longValue() != shopCategoryList.get(0).getId().longValue())
                {
                    //释放锁
                    skylarkLock.unLock(ShopCategoryKey.getUpdateLockKey(userMainId), userMainId);
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("该分类名称已存在!");
                    return resultObjectVO;
                }
            }

            shopCategory.setUpdateDate(new Date());
            int row = shopCategoryService.updateName(shopCategory);
            if (row < 1) {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getUpdateLockKey(userMainId), userMainId);
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            skylarkLock.unLock(ShopCategoryKey.getUpdateLockKey(userMainId), userMainId);
        }

        return resultObjectVO;
    }




    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryById(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        try {
            ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);

            if(shopCategory.getId()==null)
            {
                logger.warn("ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("ID不能为空!");
                return resultObjectVO;
            }

            if(shopCategory.getUserMainId()==null)
            {
                logger.warn("用户ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空!");
                return resultObjectVO;
            }

            List<SellerShop> sellerShops = sellerShopService.findEnabledByUserMainId(shopCategory.getUserMainId());
            if(!CollectionUtils.isEmpty(sellerShops))
            {
                shopCategory.setShopId(sellerShops.get(0).getId());
            }

            if(shopCategory.getShopId()==null)
            {
                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有查询到关联店铺!");
                return resultObjectVO;
            }
            resultObjectVO.setData(shopCategoryService.queryByIdAndUserMainIdAndShopId(shopCategory.getId(),shopCategory.getUserMainId(),shopCategory.getShopId()));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/ids",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByIdList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        try {
            List<ShopCategory> shopCategorys = JSONArray.parseArray(requestJsonVO.getEntityJson(),ShopCategory.class);
            if(!CollectionUtils.isEmpty(shopCategorys)) {
                List<ShopCategory> ShopCategoryList = new ArrayList<ShopCategory>();
                for(ShopCategory ShopCategory:shopCategorys) {
                    ShopCategory ShopCategoryEntity = shopCategoryService.queryById(ShopCategory.getId());
                    if(ShopCategoryEntity!=null) {
                        ShopCategoryList.add(ShopCategoryEntity);
                    }
                }
                resultObjectVO.setData(ShopCategoryList);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }



    /**
     * 批量刷新缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO flushCache(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }
        try {
            ShopCategoryVO shopCategoryVO = requestVo.formatEntity(ShopCategoryVO.class);
            if(shopCategoryVO.getShopId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("刷新失败,店铺ID不能为空");
                return resultObjectVO;
            }
            ShopCategoryVO query = new ShopCategoryVO();
            List<ShopCategory> shopCategoryList = shopCategoryService.queryPcIndexList(query);
            if(!CollectionUtils.isEmpty(shopCategoryList)) {
                List<ShopCategoryVO> shopCategoryTreeVOS = new ArrayList<ShopCategoryVO>();
                for(ShopCategory ShopCategory : shopCategoryList)
                {
                    if(ShopCategory.getParentId().longValue()==-1) {
                        ShopCategoryTreeVO treeVO = new ShopCategoryTreeVO();
                        BeanUtils.copyProperties(treeVO, ShopCategory);
                        treeVO.setTitle(ShopCategory.getName());
                        treeVO.setText(ShopCategory.getName());
                        if(StringUtils.isNotEmpty(treeVO.getName()))
                        {
                            StringBuilder linkhtml = new StringBuilder();
                            if(treeVO.getName().indexOf("/")!=-1&&treeVO.getHref().indexOf("&toucan_spliter_2021&")!=-1)
                            {
                                String[] names = treeVO.getName().split("/");
                                String[] hrefs = treeVO.getHref().split("&toucan_spliter_2021&");
                                if(names.length==hrefs.length) {
                                    for (int i = 0; i < names.length; i++) {
                                        linkhtml.append("<a class=\"ShopCategory_a\" href=\""+hrefs[i]+"\">");
                                        linkhtml.append(names[i]);
                                        linkhtml.append("</a>");
                                        if(i+1<names.length)
                                        {
                                            linkhtml.append("<a class=\"ShopCategory_a\" >/</a>");
                                        }
                                    }
                                }else{
                                    for (int i = 0; i < names.length; i++) {
                                        linkhtml.append("<a class=\"ShopCategory_a\" href=\"#\">");
                                        linkhtml.append(names[i]);
                                        linkhtml.append("</a>");
                                        if(i+1<names.length)
                                        {
                                            linkhtml.append("<a class=\"ShopCategory_a\" >/</a>");
                                        }
                                    }
                                }
                            }else{
                                linkhtml.append("<a class=\"ShopCategory_a\" href=\""+treeVO.getHref()+"\">"+treeVO.getName()+"</a>");
                            }
                            treeVO.setRootLinks(linkhtml.toString());
                        }
                        shopCategoryTreeVOS.add(treeVO);

                        treeVO.setChildren(new ArrayList<ShopCategoryVO>());
                        shopCategoryService.setChildren(shopCategoryList,treeVO);
                    }
                }
                toucanStringRedisService.set(ShopCategoryKey.getCacheKey(shopCategoryVO.getShopId()),JSONArray.toJSONString(shopCategoryTreeVOS));
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 清空缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/clear/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO clearCache(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ShopCategoryVO shopCategoryVO = requestVo.formatEntity(ShopCategoryVO.class);
            if(shopCategoryVO.getShopId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("清空失败,店铺ID不能为空");
                return resultObjectVO;
            }
            toucanStringRedisService.delete(ShopCategoryKey.getCacheKey(shopCategoryVO.getShopId()));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ShopCategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),ShopCategoryVO.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该功能项
            ShopCategoryVO query=new ShopCategoryVO();
            query.setId(entity.getId());
            List<ShopCategory> ShopCategorys = shopCategoryService.queryList(query);
            if(CollectionUtils.isEmpty(ShopCategorys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,对象不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(ShopCategorys);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据ID数组查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/idArray",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findByIdArray(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ShopCategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),ShopCategoryVO.class);
            if(entity.getIdArray()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID数组");
                return resultObjectVO;
            }

            //查询是否存在该功能项
            ShopCategoryVO query=new ShopCategoryVO();
            query.setIdArray(entity.getIdArray());
            List<ShopCategory> ShopCategorys = shopCategoryService.queryList(query);
            if(CollectionUtils.isEmpty(ShopCategorys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(ShopCategorys);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ShopCategoryVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategoryVO.class);

            List<ShopCategory> ShopCategoryList = shopCategoryService.queryList(query);
            if(!CollectionUtils.isEmpty(ShopCategoryList))
            {
                List<ShopCategoryVO> ShopCategoryTreeVOS = new ArrayList<ShopCategoryVO>();
                for(ShopCategory ShopCategory : ShopCategoryList)
                {
                    if(ShopCategory.getParentId().longValue()==-1) {
                        ShopCategoryTreeVO treeVO = new ShopCategoryTreeVO();
                        BeanUtils.copyProperties(treeVO, ShopCategory);

                        treeVO.setTitle(ShopCategory.getName());
                        treeVO.setText(ShopCategory.getName());

                        ShopCategoryTreeVOS.add(treeVO);

                        treeVO.setChildren(new ArrayList<ShopCategoryVO>());
                        shopCategoryService.setChildren(ShopCategoryList,treeVO);
                    }
                }

                ShopCategoryTreeVO rootTreeVO = new ShopCategoryTreeVO();
                rootTreeVO.setTitle("根节点");
                rootTreeVO.setParentId(-1L);
                rootTreeVO.setId(-1L);
                rootTreeVO.setText("根节点");
                rootTreeVO.setChildren(ShopCategoryTreeVOS);
                List<ShopCategoryVO> rootShopCategoryTreeVOS = new ArrayList<ShopCategoryVO>();
                rootShopCategoryTreeVOS.add(rootTreeVO);
                resultObjectVO.setData(rootShopCategoryTreeVOS);

            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询PC端首页分类树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/web/index/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryWebIndexTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ShopCategoryVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategoryVO.class);
            List<ShopCategory> ShopCategoryList = shopCategoryService.queryPcIndexList(query);
            if(!CollectionUtils.isEmpty(ShopCategoryList)) {
                List<ShopCategoryVO> ShopCategoryTreeVOS = new ArrayList<ShopCategoryVO>();
                for(ShopCategory ShopCategory : ShopCategoryList)
                {
                    if(ShopCategory.getParentId().longValue()==-1) {
                        ShopCategoryTreeVO treeVO = new ShopCategoryTreeVO();
                        BeanUtils.copyProperties(treeVO, ShopCategory);
                        treeVO.setTitle(ShopCategory.getName());
                        treeVO.setText(ShopCategory.getName());
                        ShopCategoryTreeVOS.add(treeVO);

                        treeVO.setChildren(new ArrayList<ShopCategoryVO>());
                        shopCategoryService.setChildren(ShopCategoryList,treeVO);
                    }
                }
                resultObjectVO.setData(ShopCategoryTreeVOS);
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTable(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ShopCategoryTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategoryTreeInfo.class);

            //查询所有结构树
            List<ShopCategoryVO>  ShopCategoryVOS = shopCategoryService.findTreeTable(queryPageInfo);

            //判断每个节点,如果集合中不存在父节点,那么就将这个节点设置为顶级节点
            boolean isFind =false;
            for(ShopCategoryVO c:ShopCategoryVOS)
            {
                isFind=false;
                for(ShopCategoryVO cv:ShopCategoryVOS)
                {
                    if(c.getParentId().longValue()==cv.getId().longValue())
                    {
                        isFind=true;
                        break;
                    }
                }
                if(!isFind)
                {
                    c.setParentId(-1L);
                }
            }

            resultObjectVO.setData(ShopCategoryVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryListByPid(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }
        try {
            ShopCategoryVO queryShopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategoryVO.class);
            resultObjectVO.setData(shopCategoryService.queryList(queryShopCategory));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询全部类别
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/all/list",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAllList(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }
        try {
            ShopCategoryVO queryShopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategoryVO.class);

            if(queryShopCategory.getUserMainId()==null)
            {
                logger.warn("用户ID为空 param:{}", requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }
            List<SellerShop> sellerShops = sellerShopService.findEnabledByUserMainId(queryShopCategory.getUserMainId());
            if(!CollectionUtils.isEmpty(sellerShops))
            {
                queryShopCategory.setShopId(sellerShops.get(0).getId());
            }

            if(queryShopCategory.getShopId()==null)
            {
                logger.warn("店铺ID为空 param:{}",JSONObject.toJSONString(queryShopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }
            List<ShopCategory> shopCategorys = shopCategoryService.queryList(queryShopCategory);
            List<ShopCategoryVO> shopCategoryVOS = new ArrayList<ShopCategoryVO>();
            for(ShopCategory shopCategory:shopCategorys)
            {
                ShopCategoryVO shopCategoryVO = new ShopCategoryVO();
                BeanUtils.copyProperties(shopCategoryVO,shopCategory);

                if(shopCategoryVO.getParentId().longValue()==-1L)
                {
                    shopCategoryVO.setChildren(new ArrayList<ShopCategoryVO>());
                    for(ShopCategory childShopCategory:shopCategorys)
                    {
                        if(childShopCategory.getParentId().longValue()==shopCategoryVO.getId().longValue())
                        {
                            ShopCategoryVO childShopCategoryVO = new ShopCategoryVO();
                            BeanUtils.copyProperties(childShopCategoryVO,childShopCategory);
                            shopCategoryVO.getChildren().add(childShopCategoryVO);
                        }
                    }
                    shopCategoryVOS.add(shopCategoryVO);
                }

            }
            resultObjectVO.setData(shopCategoryVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ShopCategoryTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategoryTreeInfo.class);

            List<ShopCategoryTreeVO> ShopCategoryTreeVOS = new ArrayList<ShopCategoryTreeVO>();
            //按指定条件查询
            if(StringUtils.isNotEmpty(queryPageInfo.getName()))
            {
                ShopCategoryVO queryShopCategory = new ShopCategoryVO();
                queryShopCategory.setName(queryPageInfo.getName());
                List<ShopCategory> categories = shopCategoryService.queryList(queryShopCategory);
                for (int i = 0; i < categories.size(); i++) {
                    ShopCategory ShopCategory = categories.get(i);
                    ShopCategoryTreeVO ShopCategoryTreeVO = new ShopCategoryTreeVO();
                    BeanUtils.copyProperties(ShopCategoryTreeVO, ShopCategory);
                    ShopCategoryTreeVOS.add(ShopCategoryTreeVO);
                }
            }else {
                //查询当前节点下的所有子节点
                ShopCategoryVO queryShopCategory = new ShopCategoryVO();
                queryShopCategory.setParentId(queryPageInfo.getParentId());
                List<ShopCategory> categories = shopCategoryService.queryList(queryShopCategory);
                for (int i = 0; i < categories.size(); i++) {
                    ShopCategory ShopCategory = categories.get(i);
                    ShopCategoryTreeVO ShopCategoryTreeVO = new ShopCategoryTreeVO();
                    BeanUtils.copyProperties(ShopCategoryTreeVO, ShopCategory);

                    queryShopCategory = new ShopCategoryVO();
                    queryShopCategory.setParentId(ShopCategory.getId());
                    Long childCount = shopCategoryService.queryCount(queryShopCategory);
                    if (childCount > 0) {
                        ShopCategoryTreeVO.setHaveChild(true);
                    }
                    ShopCategoryTreeVOS.add(ShopCategoryTreeVO);
                }
            }


            //将查询的这个节点设置为顶级节点
            if(StringUtils.isNotEmpty(queryPageInfo.getName())) {
                if(!CollectionUtils.isEmpty(ShopCategoryTreeVOS)) {
                    for (ShopCategory ShopCategory : ShopCategoryTreeVOS) {
                        ShopCategory.setParentId(-1L);
                    }
                }
            }

            resultObjectVO.setData(ShopCategoryTreeVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);

        if(shopCategory.getId()==null)
        {
            logger.warn("ID为空 param:"+ requestJsonVO.getEntityJson());
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("ID不能为空!");
            return resultObjectVO;
        }


        if(shopCategory.getUserMainId()==null)
        {
            logger.warn("用户ID为空 param:"+ requestJsonVO.getEntityJson());
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("用户ID不能为空!");
            return resultObjectVO;
        }

        String userMainId = String.valueOf(shopCategory.getUserMainId());
        try {

            boolean lockStatus = skylarkLock.lock(ShopCategoryKey.getDeleteLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("删除失败,请稍后重试");
                return resultObjectVO;
            }


            List<SellerShop> sellerShops = sellerShopService.findEnabledByUserMainId(shopCategory.getUserMainId());
            if(!CollectionUtils.isEmpty(sellerShops))
            {
                shopCategory.setShopId(sellerShops.get(0).getId());
            }

            if(shopCategory.getShopId()==null)
            {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getDeleteLockKey(userMainId), userMainId);

                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有查询到关联店铺!");
                return resultObjectVO;
            }


            ShopCategoryVO queryShopCategory = new ShopCategoryVO();
            queryShopCategory.setParentId(shopCategory.getId());
            queryShopCategory.setUserMainId(shopCategory.getUserMainId());

            List<ShopCategory> shopCategoryList = shopCategoryService.queryList(queryShopCategory);
            if(!CollectionUtils.isEmpty(shopCategoryList))
            {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getDeleteLockKey(userMainId), userMainId);

                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请先删除所有子分类!");
                return resultObjectVO;
            }

            int row = shopCategoryService.deleteById(shopCategory.getId());
            if (row <=0) {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getDeleteLockKey(userMainId), userMainId);

                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            //释放锁
            skylarkLock.unLock(ShopCategoryKey.getDeleteLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }




    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<ShopCategory> ShopCategorys = JSONObject.parseArray(requestVo.getEntityJson(),ShopCategory.class);
            if(CollectionUtils.isEmpty(ShopCategorys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(ShopCategory ShopCategory:ShopCategorys) {
                if(ShopCategory.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(ShopCategory);


                    List<ShopCategory> chidlren = new ArrayList<ShopCategory>();
                    shopCategoryService.queryChildren(chidlren,ShopCategory);

                    //把当前节点添加进去,循环这个集合
                    chidlren.add(ShopCategory);

                    for(ShopCategory c:chidlren) {
                        //删除当前功能项
                        int row = shopCategoryService.deleteById(c.getId());
                        if (row < 1) {
                            logger.warn("删除分类失败 {} ",JSONObject.toJSONString(c));
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg("请求失败,请重试!");
                            continue;
                        }

                    }

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



}
