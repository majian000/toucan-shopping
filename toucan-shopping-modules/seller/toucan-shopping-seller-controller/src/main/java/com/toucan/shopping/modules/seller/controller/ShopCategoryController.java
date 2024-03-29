package com.toucan.shopping.modules.seller.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.entity.ShopCategory;
import com.toucan.shopping.modules.seller.page.ShopCategoryTreeInfo;
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
import java.util.LinkedList;
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
                resultObjectVO.setMsg("请稍后重试");
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

            SellerShop sellerShop = sellerShopService.findByUserMainId(shopCategory.getUserMainId());
            if(sellerShop!=null)
            {
                shopCategory.setShopId(sellerShop.getId());
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
            List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
            for(SellerShop entity:sellerShops)
            {
                int categoryMaxCount = entity.getCategoryMaxCount()!=null?entity.getCategoryMaxCount():toucan.getSeller().getShopCategoryMaxCount();
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
                    resultObjectVO.setMsg("上级分类不存在");
                    return resultObjectVO;
                }

                ShopCategory parentShopCategory = shopCategories.get(0);
                if(parentShopCategory.getParentId()!=-1)
                {
                    //释放锁
                    skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(userMainId), userMainId);

                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("只能添加二级分类");
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
                resultObjectVO.setMsg("请重试!");
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }


    /**
     * 保存分类(后台管理端)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO saveForAdmin(@RequestBody RequestJsonVO requestJsonVO)
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

        if(shopCategory.getShopId()==null)
        {
            logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(shopCategory));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("店铺ID不能为空!");
            return resultObjectVO;
        }
        String shopId = String.valueOf(shopCategory.getShopId());
        try {

            boolean lockStatus = skylarkLock.lock(ShopCategoryKey.getSaveLockKey(shopId), shopId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }


            if(StringUtils.isEmpty(shopCategory.getName()))
            {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(shopId), shopId);

                logger.warn("分类名称为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类名称不能为空!");

                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setId(shopCategory.getShopId());
            List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
            if(!CollectionUtils.isEmpty(sellerShops))
            {
                shopCategory.setUserMainId(sellerShops.get(0).getUserMainId());
            }

            if(shopCategory.getUserMainId()==null)
            {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(shopId), shopId);

                logger.warn("用户ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空!");
                return resultObjectVO;
            }


            if(shopCategory.getParentId()==null)
            {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(shopId), shopId);

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

            querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(shopCategory.getUserMainId());
            querySellerShop.setId(shopCategory.getShopId());
            sellerShops = sellerShopService.findListByEntity(querySellerShop);
            for(SellerShop sellerShop:sellerShops)
            {
                int categoryMaxCount = sellerShop.getCategoryMaxCount()!=null?sellerShop.getCategoryMaxCount():toucan.getSeller().getShopCategoryMaxCount();
                if(count+1>categoryMaxCount)
                {
                    //释放锁
                    skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(shopId), shopId);

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
                    skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(shopId), shopId);

                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("上级分类不存在");
                    return resultObjectVO;
                }

                ShopCategory parentShopCategory = shopCategories.get(0);
                if(parentShopCategory.getParentId()!=-1)
                {
                    //释放锁
                    skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(shopId), shopId);

                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("只能添加二级分类");
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
                resultObjectVO.setMsg("请重试!");
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(shopId), shopId);
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

            shopCategory.setUpdateDate(new Date());
            int row = shopCategoryService.updateName(shopCategory);
            if (row < 1) {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getUpdateLockKey(userMainId), userMainId);
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
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
     * 更新分类(后台管理端)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateForAdmin(@RequestBody RequestJsonVO requestJsonVO)
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
        String shopCategoryId = String.valueOf(shopCategory.getId());
        try {

            boolean lockStatus = skylarkLock.lock(ShopCategoryKey.getUpdateLockKey(shopCategoryId), shopCategoryId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("修改失败,请稍后重试");
                return resultObjectVO;
            }


            if(StringUtils.isEmpty(shopCategory.getName()))
            {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getUpdateLockKey(shopCategoryId), shopCategoryId);

                logger.warn("分类名称为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类名称不能为空!");
                return resultObjectVO;
            }


            if(shopCategory.getId()==null)
            {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getUpdateLockKey(shopCategoryId), shopCategoryId);

                logger.warn("分类ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类ID不能为空!");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setId(shopCategory.getShopId());
            List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
            if(!CollectionUtils.isEmpty(sellerShops))
            {
                shopCategory.setUserMainId(sellerShops.get(0).getUserMainId());
            }

            if(shopCategory.getUserMainId()==null)
            {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getSaveLockKey(shopCategoryId), shopCategoryId);

                logger.warn("用户ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空!");
                return resultObjectVO;
            }

            shopCategory.setUpdateDate(new Date());
            int row = shopCategoryService.updateName(shopCategory);
            if (row < 1) {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getUpdateLockKey(shopCategoryId), shopCategoryId);
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            skylarkLock.unLock(ShopCategoryKey.getUpdateLockKey(shopCategoryId), shopCategoryId);
        }

        return resultObjectVO;
    }




    /**
     * 置顶
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/move/top",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO moveTop(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            return resultObjectVO;
        }

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            if(shopCategory.getId()==null)
            {
                logger.warn("分类ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类ID不能为空!");
                return resultObjectVO;
            }

            SellerShop sellerShop = sellerShopService.findByUserMainId(shopCategory.getUserMainId());
            if(sellerShop!=null)
            {
                shopCategory.setShopId(sellerShop.getId());
            }

            if(shopCategory.getShopId()==null)
            {
                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }

            ShopCategoryVO queryShopCategory = new ShopCategoryVO();
            queryShopCategory.setUserMainId(shopCategory.getUserMainId());
            queryShopCategory.setShopId(shopCategory.getShopId());
            queryShopCategory.setParentId(shopCategory.getParentId());
            List<ShopCategory> shopCategories = shopCategoryService.queryTop1(queryShopCategory);
            if(CollectionUtils.isEmpty(shopCategories))
            {
                logger.warn("类别列表为空 param:{}",requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }

            ShopCategory currentShopCategory = shopCategoryService.queryByIdAndUserMainIdAndShopId(shopCategory.getId(),shopCategory.getUserMainId(),shopCategory.getShopId());

            if(currentShopCategory!=null) {
                //置顶分类
                ShopCategory shopCategoryTop = shopCategories.get(0);
                if(currentShopCategory.getId().longValue()!=shopCategoryTop.getId().longValue()) {
                    long topSort = shopCategoryTop.getCategorySort();
                    shopCategoryTop.setCategorySort(currentShopCategory.getCategorySort());
                    currentShopCategory.setCategorySort(topSort);

                    //更新排序字段
                    shopCategoryService.updateCategorySort(currentShopCategory);
                    shopCategoryService.updateCategorySort(shopCategoryTop);
                }
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }



    /**
     * 置顶
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/move/top",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO moveTopForAdmin(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            return resultObjectVO;
        }

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            if(shopCategory.getId()==null)
            {
                logger.warn("分类ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类ID不能为空!");
                return resultObjectVO;
            }

            if(shopCategory.getShopId()==null)
            {
                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }


            if(shopCategory.getParentId()==null)
            {
                logger.warn("上级分类ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("上级分类ID不能为空!");
                return resultObjectVO;
            }

            ShopCategoryVO queryShopCategory = new ShopCategoryVO();
            queryShopCategory.setUserMainId(shopCategory.getUserMainId());
            queryShopCategory.setShopId(shopCategory.getShopId());
            queryShopCategory.setParentId(shopCategory.getParentId());
            List<ShopCategory> shopCategories = shopCategoryService.queryTop1(queryShopCategory);
            if(CollectionUtils.isEmpty(shopCategories))
            {
                logger.warn("类别列表为空 param:{}",requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }

            ShopCategory currentShopCategory = shopCategoryService.queryByIdAndShopId(shopCategory.getId(),shopCategory.getShopId());

            if(currentShopCategory!=null) {
                //置顶分类
                ShopCategory shopCategoryTop = shopCategories.get(0);
                if(currentShopCategory.getId().longValue()!=shopCategoryTop.getId().longValue()) {
                    long topSort = shopCategoryTop.getCategorySort();
                    shopCategoryTop.setCategorySort(currentShopCategory.getCategorySort());
                    currentShopCategory.setCategorySort(topSort);

                    //更新排序字段
                    shopCategoryService.updateCategorySort(currentShopCategory);
                    shopCategoryService.updateCategorySort(shopCategoryTop);
                }
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }

    /**
     * 置底
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/move/bottom",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO moveBottom(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            return resultObjectVO;
        }

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            if(shopCategory.getId()==null)
            {
                logger.warn("分类ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类ID不能为空!");
                return resultObjectVO;
            }

            SellerShop sellerShop = sellerShopService.findByUserMainId(shopCategory.getUserMainId());
            if(sellerShop!=null)
            {
                shopCategory.setShopId(sellerShop.getId());
            }

            if(shopCategory.getShopId()==null)
            {
                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }

            ShopCategoryVO queryShopCategory = new ShopCategoryVO();
            queryShopCategory.setUserMainId(shopCategory.getUserMainId());
            queryShopCategory.setShopId(shopCategory.getShopId());
            queryShopCategory.setParentId(shopCategory.getParentId());
            List<ShopCategory> shopCategories = shopCategoryService.queryBottom1(queryShopCategory);
            if(CollectionUtils.isEmpty(shopCategories))
            {
                logger.warn("类别列表为空 param:{}",requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }

            ShopCategory currentShopCategory = shopCategoryService.queryByIdAndUserMainIdAndShopId(shopCategory.getId(),shopCategory.getUserMainId(),shopCategory.getShopId());

            if(currentShopCategory!=null) {
                //置底分类
                ShopCategory shopCategoryBottom = shopCategories.get(0);
                if(currentShopCategory.getId().longValue()!=shopCategoryBottom.getId().longValue()) {
                    long topSort = shopCategoryBottom.getCategorySort();
                    shopCategoryBottom.setCategorySort(currentShopCategory.getCategorySort());
                    currentShopCategory.setCategorySort(topSort);

                    //更新排序字段
                    shopCategoryService.updateCategorySort(currentShopCategory);
                    shopCategoryService.updateCategorySort(shopCategoryBottom);
                }
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }



    /**
     * 置底
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/move/bottom",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO moveBottomForAdmin(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            return resultObjectVO;
        }

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            if(shopCategory.getId()==null)
            {
                logger.warn("分类ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类ID不能为空!");
                return resultObjectVO;
            }

            if(shopCategory.getShopId()==null)
            {
                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }

            if(shopCategory.getParentId()==null)
            {
                logger.warn("上级分类ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("上级分类ID不能为空!");
                return resultObjectVO;
            }

            ShopCategoryVO queryShopCategory = new ShopCategoryVO();
            queryShopCategory.setUserMainId(shopCategory.getUserMainId());
            queryShopCategory.setShopId(shopCategory.getShopId());
            queryShopCategory.setParentId(shopCategory.getParentId());
            List<ShopCategory> shopCategories = shopCategoryService.queryBottom1(queryShopCategory);
            if(CollectionUtils.isEmpty(shopCategories))
            {
                logger.warn("类别列表为空 param:{}",requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }

            ShopCategory currentShopCategory = shopCategoryService.queryByIdAndShopId(shopCategory.getId(),shopCategory.getShopId());

            if(currentShopCategory!=null) {
                //置底分类
                ShopCategory shopCategoryBottom = shopCategories.get(0);
                if(currentShopCategory.getId().longValue()!=shopCategoryBottom.getId().longValue()) {
                    long topSort = shopCategoryBottom.getCategorySort();
                    shopCategoryBottom.setCategorySort(currentShopCategory.getCategorySort());
                    currentShopCategory.setCategorySort(topSort);

                    //更新排序字段
                    shopCategoryService.updateCategorySort(currentShopCategory);
                    shopCategoryService.updateCategorySort(shopCategoryBottom);
                }
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }


    /**
     * 向上
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/move/up",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO moveUp(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            return resultObjectVO;
        }

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            if(shopCategory.getId()==null)
            {
                logger.warn("分类ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类ID不能为空!");
                return resultObjectVO;
            }

            SellerShop sellerShop = sellerShopService.findByUserMainId(shopCategory.getUserMainId());
            if(sellerShop!=null)
            {
                shopCategory.setShopId(sellerShop.getId());
            }

            if(shopCategory.getShopId()==null)
            {
                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }

            ShopCategoryVO queryShopCategory = new ShopCategoryVO();
            queryShopCategory.setUserMainId(shopCategory.getUserMainId());
            queryShopCategory.setShopId(shopCategory.getShopId());
            queryShopCategory.setParentId(shopCategory.getParentId());
            List<ShopCategory> shopCategories = shopCategoryService.queryListOrderByCategorySortAsc(queryShopCategory);
            if(CollectionUtils.isEmpty(shopCategories))
            {
                logger.warn("类别列表为空 param:{}",requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }

            ShopCategory currentShopCategory = null;
            ShopCategory shopCategoryUp = null; //上一个分类

            //查询出当前分类
            for(int i=0;i<shopCategories.size();i++)
            {
                if(shopCategories.get(i).getId().longValue()==shopCategory.getId().longValue())
                {
                    currentShopCategory = shopCategories.get(i);
                    if(i==0) //如果当前排序已经置顶
                    {
                        shopCategoryUp = currentShopCategory;
                    }else{
                        shopCategoryUp = shopCategories.get(i-1);
                    }
                }
            }


            if(currentShopCategory!=null) {
                if(currentShopCategory.getId().longValue()!=shopCategoryUp.getId().longValue()) {
                    long topSort = shopCategoryUp.getCategorySort();
                    shopCategoryUp.setCategorySort(currentShopCategory.getCategorySort());
                    currentShopCategory.setCategorySort(topSort);

                    //更新排序字段
                    shopCategoryService.updateCategorySort(currentShopCategory);
                    shopCategoryService.updateCategorySort(shopCategoryUp);
                }
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }




    /**
     * 向上(后台管理端)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/move/up",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO moveUpForAdmin(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            return resultObjectVO;
        }

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            if(shopCategory.getId()==null)
            {
                logger.warn("分类ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类ID不能为空!");
                return resultObjectVO;
            }

            if(shopCategory.getShopId()==null)
            {
                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }
            if(shopCategory.getParentId()==null)
            {
                logger.warn("上级分类ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("上级分类ID不能为空!");
                return resultObjectVO;
            }

            ShopCategoryVO queryShopCategory = new ShopCategoryVO();
            queryShopCategory.setUserMainId(shopCategory.getUserMainId());
            queryShopCategory.setShopId(shopCategory.getShopId());
            queryShopCategory.setParentId(shopCategory.getParentId());
            List<ShopCategory> shopCategories = shopCategoryService.queryListOrderByCategorySortAsc(queryShopCategory);
            if(CollectionUtils.isEmpty(shopCategories))
            {
                logger.warn("类别列表为空 param:{}",requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }

            ShopCategory currentShopCategory = null;
            ShopCategory shopCategoryUp = null; //上一个分类

            //查询出当前分类
            for(int i=0;i<shopCategories.size();i++)
            {
                if(shopCategories.get(i).getId().longValue()==shopCategory.getId().longValue())
                {
                    currentShopCategory = shopCategories.get(i);
                    if(i==0) //如果当前排序已经置顶
                    {
                        shopCategoryUp = currentShopCategory;
                    }else{
                        shopCategoryUp = shopCategories.get(i-1);
                    }
                }
            }


            if(currentShopCategory!=null) {
                if(currentShopCategory.getId().longValue()!=shopCategoryUp.getId().longValue()) {
                    long topSort = shopCategoryUp.getCategorySort();
                    shopCategoryUp.setCategorySort(currentShopCategory.getCategorySort());
                    currentShopCategory.setCategorySort(topSort);

                    //更新排序字段
                    shopCategoryService.updateCategorySort(currentShopCategory);
                    shopCategoryService.updateCategorySort(shopCategoryUp);
                }
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }


    /**
     * 向下
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/move/down",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO moveDown(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            return resultObjectVO;
        }

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            if(shopCategory.getId()==null)
            {
                logger.warn("分类ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类ID不能为空!");
                return resultObjectVO;
            }

            SellerShop sellerShop = sellerShopService.findByUserMainId(shopCategory.getUserMainId());
            if(sellerShop!=null)
            {
                shopCategory.setShopId(sellerShop.getId());
            }

            if(shopCategory.getShopId()==null)
            {
                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }

            ShopCategoryVO queryShopCategory = new ShopCategoryVO();
            queryShopCategory.setUserMainId(shopCategory.getUserMainId());
            queryShopCategory.setShopId(shopCategory.getShopId());
            queryShopCategory.setParentId(shopCategory.getParentId());
            List<ShopCategory> shopCategories = shopCategoryService.queryListOrderByCategorySortAsc(queryShopCategory);
            if(CollectionUtils.isEmpty(shopCategories))
            {
                logger.warn("类别列表为空 param:{}",requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }

            ShopCategory currentShopCategory = null;
            ShopCategory shopCategoryDown = null; //上一个分类

            //查询出当前分类
            for(int i=0;i<shopCategories.size();i++)
            {
                if(shopCategories.get(i).getId().longValue()==shopCategory.getId().longValue())
                {
                    currentShopCategory = shopCategories.get(i);
                    if((i+1)==shopCategories.size()) //如果当前排序已经置底
                    {
                        shopCategoryDown = currentShopCategory;
                    }else{
                        shopCategoryDown = shopCategories.get(i+1);
                    }
                }
            }


            if(currentShopCategory!=null) {
                if(currentShopCategory.getId().longValue()!=shopCategoryDown.getId().longValue()) {
                    long topSort = shopCategoryDown.getCategorySort();
                    shopCategoryDown.setCategorySort(currentShopCategory.getCategorySort());
                    currentShopCategory.setCategorySort(topSort);

                    //更新排序字段
                    shopCategoryService.updateCategorySort(currentShopCategory);
                    shopCategoryService.updateCategorySort(shopCategoryDown);
                }
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }




    /**
     * 向下(后台管理端)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/move/down",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO moveDownForAdmin(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            return resultObjectVO;
        }

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            if(shopCategory.getId()==null)
            {
                logger.warn("分类ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类ID不能为空!");
                return resultObjectVO;
            }

            if(shopCategory.getShopId()==null)
            {
                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }

            if(shopCategory.getParentId()==null)
            {
                logger.warn("上级分类ID为空 param:"+ JSONObject.toJSONString(shopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("上级分类ID不能为空!");
                return resultObjectVO;
            }

            ShopCategoryVO queryShopCategory = new ShopCategoryVO();
            queryShopCategory.setUserMainId(shopCategory.getUserMainId());
            queryShopCategory.setShopId(shopCategory.getShopId());
            queryShopCategory.setParentId(shopCategory.getParentId());
            List<ShopCategory> shopCategories = shopCategoryService.queryListOrderByCategorySortAsc(queryShopCategory);
            if(CollectionUtils.isEmpty(shopCategories))
            {
                logger.warn("类别列表为空 param:{}",requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }

            ShopCategory currentShopCategory = null;
            ShopCategory shopCategoryDown = null; //上一个分类

            //查询出当前分类
            for(int i=0;i<shopCategories.size();i++)
            {
                if(shopCategories.get(i).getId().longValue()==shopCategory.getId().longValue())
                {
                    currentShopCategory = shopCategories.get(i);
                    if((i+1)==shopCategories.size()) //如果当前排序已经置底
                    {
                        shopCategoryDown = currentShopCategory;
                    }else{
                        shopCategoryDown = shopCategories.get(i+1);
                    }
                }
            }


            if(currentShopCategory!=null) {
                if(currentShopCategory.getId().longValue()!=shopCategoryDown.getId().longValue()) {
                    long topSort = shopCategoryDown.getCategorySort();
                    shopCategoryDown.setCategorySort(currentShopCategory.getCategorySort());
                    currentShopCategory.setCategorySort(topSort);

                    //更新排序字段
                    shopCategoryService.updateCategorySort(currentShopCategory);
                    shopCategoryService.updateCategorySort(shopCategoryDown);
                }
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
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
    public ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO)
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

            SellerShop sellerShop = sellerShopService.findByUserMainId(shopCategory.getUserMainId());
            if(sellerShop!=null)
            {
                shopCategory.setShopId(sellerShop.getId());
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
            resultObjectVO.setMsg("没有找到实体对象");
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
            resultObjectVO.setMsg("请稍后重试");
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
            resultObjectVO.setMsg("请稍后重试");
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
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ShopCategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),ShopCategoryVO.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该数据
            ShopCategoryVO query=new ShopCategoryVO();
            query.setId(entity.getId());
            List<ShopCategory> shopCategories = shopCategoryService.queryList(query);
            if(CollectionUtils.isEmpty(shopCategories))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("对象不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(shopCategories);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 根据ID查询返回分类ID路径
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/path/by/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findIdPathById(@RequestBody RequestJsonVO requestVo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ShopCategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),ShopCategoryVO.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该分类
            ShopCategoryVO query=new ShopCategoryVO();
            query.setId(entity.getId());
            List<ShopCategory> categorys = shopCategoryService.queryList(query);
            if(CollectionUtils.isEmpty(categorys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("对象不存在!");
                return resultObjectVO;
            }
            ShopCategory shopCategory = categorys.get(0);
            ShopCategoryVO shopCategoryVO = new ShopCategoryVO();
            BeanUtils.copyProperties(shopCategoryVO,shopCategory);
            shopCategoryVO.setIdPath(new ArrayList<Long>());
            shopCategoryVO.setNamePaths(new ArrayList());
            shopCategoryVO.getIdPath().add(shopCategory.getId());
            shopCategoryVO.getNamePaths().add(shopCategory.getName());
            shopCategoryService.setIdPath(shopCategoryVO);

            resultObjectVO.setData(shopCategoryVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
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
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ShopCategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),ShopCategoryVO.class);
            if(entity.getIdArray()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID数组");
                return resultObjectVO;
            }

            //查询是否存在该功能项
            ShopCategoryVO query=new ShopCategoryVO();
            query.setIdArray(entity.getIdArray());
            List<ShopCategory> shopCategorys = shopCategoryService.queryList(query);
            if(CollectionUtils.isEmpty(shopCategorys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺分类为空!");
                return resultObjectVO;
            }

            List<ShopCategoryVO> shopCategoryVOS = new ArrayList<ShopCategoryVO>();
            if(!CollectionUtils.isEmpty(shopCategorys))
            {
                List<Long> parentIds = new LinkedList<Long>();
                for(ShopCategory shopCategory:shopCategorys)
                {
                    ShopCategoryVO shopCategoryVO = new ShopCategoryVO();
                    BeanUtils.copyProperties(shopCategoryVO,shopCategory);
                    shopCategoryVO.setNamePath(shopCategoryVO.getName());
                    shopCategoryVO.setParentIdPoint(shopCategoryVO.getParentId());
                    shopCategoryVOS.add(shopCategoryVO);
                    if(shopCategoryVO.getParentId()!=null&&shopCategoryVO.getParentId().longValue()!=-1L) {
                        parentIds.add(shopCategoryVO.getParentId());
                    }
                }
                shopCategoryService.setNamePath(shopCategoryVOS,parentIds);
            }
            resultObjectVO.setData(shopCategoryVOS);


        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
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
            ShopCategoryVO query = requestJsonVO.formatEntity(ShopCategoryVO.class);

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
            resultObjectVO.setMsg("请稍后重试");
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
            resultObjectVO.setMsg("请稍后重试");
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
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ShopCategoryTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategoryTreeInfo.class);

            if(queryPageInfo.getShopId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到店铺ID");
                return resultObjectVO;
            }
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
            resultObjectVO.setMsg("请稍后重试");
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
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }
        try {
            ShopCategoryVO queryShopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategoryVO.class);
            resultObjectVO.setData(shopCategoryService.queryListOrderByCategorySortAsc(queryShopCategory));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
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
            resultObjectVO.setMsg("没有找到实体对象");
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
            SellerShop sellerShop = sellerShopService.findByUserMainId(queryShopCategory.getUserMainId());
            if(sellerShop!=null)
            {
                queryShopCategory.setShopId(sellerShop.getId());
            }

            if(queryShopCategory.getShopId()==null)
            {
                logger.warn("店铺ID为空 param:{}",JSONObject.toJSONString(queryShopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }
            List<ShopCategory> shopCategorys = shopCategoryService.queryListOrderByCategorySortAsc(queryShopCategory);
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
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 根据店铺ID查询所有分类
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/shopId",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryListByShopId(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }
        try {
            ShopCategoryVO queryShopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategoryVO.class);

            if(queryShopCategory.getShopId()==null)
            {
                logger.warn("店铺ID为空 param:{}",JSONObject.toJSONString(queryShopCategory));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }
            List<ShopCategory> shopCategorys = shopCategoryService.queryListOrderByCategorySortAsc(queryShopCategory);
            List<ShopCategoryVO> shopCategoryVOS = new ArrayList<ShopCategoryVO>();
            for(ShopCategory shopCategory:shopCategorys)
            {
                ShopCategoryVO shopCategoryVO = new ShopCategoryVO();
                BeanUtils.copyProperties(shopCategoryVO,shopCategory);

                shopCategoryVOS.add(shopCategoryVO);

            }
            resultObjectVO.setData(shopCategoryVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
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
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ShopCategoryTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategoryTreeInfo.class);

            List<ShopCategoryTreeVO> ShopCategoryTreeVOS = new ArrayList<ShopCategoryTreeVO>();
            //按指定条件查询
            if(StringUtils.isNotEmpty(queryPageInfo.getNameLike()))
            {
                ShopCategoryVO queryShopCategory = new ShopCategoryVO();
                queryShopCategory.setShopId(queryPageInfo.getShopId());
                queryShopCategory.setNameLike(queryPageInfo.getNameLike());
                List<ShopCategory> categories = shopCategoryService.queryListOrderByCategorySortAsc(queryShopCategory);
                for (int i = 0; i < categories.size(); i++) {
                    ShopCategory ShopCategory = categories.get(i);
                    ShopCategoryTreeVO ShopCategoryTreeVO = new ShopCategoryTreeVO();
                    BeanUtils.copyProperties(ShopCategoryTreeVO, ShopCategory);
                    ShopCategoryTreeVOS.add(ShopCategoryTreeVO);
                }
            }else {
                //查询当前节点下的所有子节点
                ShopCategoryVO queryShopCategory = new ShopCategoryVO();
                queryShopCategory.setShopId(queryPageInfo.getShopId());
                queryShopCategory.setParentId(queryPageInfo.getParentId());
                List<ShopCategory> categories = shopCategoryService.queryListOrderByCategorySortAsc(queryShopCategory);
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
            resultObjectVO.setMsg("请稍后重试");
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
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }


            //查询出当前掌柜关联的店铺
            SellerShop sellerShopEntity = sellerShopService.findByUserMainId(shopCategory.getUserMainId());
            if(sellerShopEntity!=null)
            {
                shopCategory.setShopId(sellerShopEntity.getId());
                //将掌柜ID设置为空
                shopCategory.setUserMainId(null);
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

            int row = shopCategoryService.deleteByIdAndShopId(shopCategory.getId(),shopCategory.getShopId());
            if (row <=0) {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getDeleteLockKey(userMainId), userMainId);

                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            //释放锁
            skylarkLock.unLock(ShopCategoryKey.getDeleteLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }


    /**
     * 根据ID删除(后台管理)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIdForAdmin(@RequestBody RequestJsonVO requestJsonVO)
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


        if(shopCategory.getShopId()==null)
        {
            logger.warn("店铺ID为空 param:"+ requestJsonVO.getEntityJson());
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("店铺ID不能为空!");
            return resultObjectVO;
        }

        String shopId = String.valueOf(shopCategory.getShopId());
        try {

            boolean lockStatus = skylarkLock.lock(ShopCategoryKey.getDeleteLockKey(shopId), shopId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }


            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setId(shopCategory.getShopId());
            List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
            if(!CollectionUtils.isEmpty(sellerShops))
            {
                shopCategory.setUserMainId(sellerShops.get(0).getUserMainId());
            }


            ShopCategoryVO queryShopCategory = new ShopCategoryVO();
            queryShopCategory.setParentId(shopCategory.getId());
            queryShopCategory.setUserMainId(shopCategory.getUserMainId());

            List<ShopCategory> shopCategoryList = shopCategoryService.queryList(queryShopCategory);
            if(!CollectionUtils.isEmpty(shopCategoryList))
            {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getDeleteLockKey(shopId), shopId);

                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请先删除所有子分类!");
                return resultObjectVO;
            }

            int row = shopCategoryService.deleteById(shopCategory.getId());
            if (row <=0) {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getDeleteLockKey(shopId), shopId);

                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            //释放锁
            skylarkLock.unLock(ShopCategoryKey.getDeleteLockKey(shopId), shopId);
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
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<ShopCategory> ShopCategorys = JSONObject.parseArray(requestVo.getEntityJson(),ShopCategory.class);
            if(CollectionUtils.isEmpty(ShopCategorys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找ID");
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
                            resultObjectVO.setMsg("请重试!");
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
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



}
