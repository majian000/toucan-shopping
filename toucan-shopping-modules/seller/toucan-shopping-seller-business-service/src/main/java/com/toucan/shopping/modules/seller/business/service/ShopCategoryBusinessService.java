package com.toucan.shopping.modules.seller.business.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.Check;
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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ShopCategoryBusinessService {

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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        String userMainId = String.valueOf(shopCategory.getUserMainId());
        try {

            boolean lockStatus = skylarkLock.lock(ShopCategoryKey.getSaveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            Check.notEmpty(shopCategory.getName(), ResultVO.FAILD, "分类名称不能为空!");
            Check.notNull(shopCategory.getUserMainId(), ResultVO.FAILD, "用户ID不能为空!");

            SellerShop sellerShop = sellerShopService.findByUserMainId(shopCategory.getUserMainId());
            if(sellerShop!=null)
            {
                shopCategory.setShopId(sellerShop.getId());
            }

            Check.notNull(shopCategory.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");
            Check.notNull(shopCategory.getParentId(), ResultVO.FAILD, "上级ID不能为空!");

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
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("上级分类不存在");
                    return resultObjectVO;
                }

                ShopCategory parentShopCategory = shopCategories.get(0);
                if(parentShopCategory.getParentId()!=-1)
                {
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
        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO saveForAdmin(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);

        if(shopCategory.getShopId()==null)
        {
            logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(shopCategory));
            return ResultObjectVO.fail(ResultVO.FAILD, "店铺ID不能为空!");
        }
        String shopId = String.valueOf(shopCategory.getShopId());
        try {

            boolean lockStatus = skylarkLock.lock(ShopCategoryKey.getSaveLockKey(shopId), shopId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            Check.notEmpty(shopCategory.getName(), ResultVO.FAILD, "分类名称不能为空!");

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setId(shopCategory.getShopId());
            List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
            if(!CollectionUtils.isEmpty(sellerShops))
            {
                shopCategory.setUserMainId(sellerShops.get(0).getUserMainId());
            }

            Check.notNull(shopCategory.getUserMainId(), ResultVO.FAILD, "用户ID不能为空!");
            Check.notNull(shopCategory.getParentId(), ResultVO.FAILD, "上级ID不能为空!");

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
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("上级分类不存在");
                    return resultObjectVO;
                }

                ShopCategory parentShopCategory = shopCategories.get(0);
                if(parentShopCategory.getParentId()!=-1)
                {
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
        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        String userMainId = String.valueOf(shopCategory.getUserMainId());
        try {

            boolean lockStatus = skylarkLock.lock(ShopCategoryKey.getUpdateLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("修改失败,请稍后重试");
                return resultObjectVO;
            }

            Check.notEmpty(shopCategory.getName(), ResultVO.FAILD, "分类名称不能为空!");
            Check.notNull(shopCategory.getId(), ResultVO.FAILD, "分类ID不能为空!");

            shopCategory.setUpdateDate(new Date());
            int row = shopCategoryService.updateName(shopCategory);
            if (row < 1) {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getUpdateLockKey(userMainId), userMainId);
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }
        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO updateForAdmin(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        String shopCategoryId = String.valueOf(shopCategory.getId());
        try {

            boolean lockStatus = skylarkLock.lock(ShopCategoryKey.getUpdateLockKey(shopCategoryId), shopCategoryId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("修改失败,请稍后重试");
                return resultObjectVO;
            }

            Check.notEmpty(shopCategory.getName(), ResultVO.FAILD, "分类名称不能为空!");
            Check.notNull(shopCategory.getId(), ResultVO.FAILD, "分类ID不能为空!");

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setId(shopCategory.getShopId());
            List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
            if(!CollectionUtils.isEmpty(sellerShops))
            {
                shopCategory.setUserMainId(sellerShops.get(0).getUserMainId());
            }

            Check.notNull(shopCategory.getUserMainId(), ResultVO.FAILD, "用户ID不能为空!");

            shopCategory.setUpdateDate(new Date());
            int row = shopCategoryService.updateName(shopCategory);
            if (row < 1) {
                //释放锁
                skylarkLock.unLock(ShopCategoryKey.getUpdateLockKey(shopCategoryId), shopCategoryId);
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }
        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO moveTop(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            Check.notNull(shopCategory.getId(), ResultVO.FAILD, "分类ID不能为空!");

            SellerShop sellerShop = sellerShopService.findByUserMainId(shopCategory.getUserMainId());
            if(sellerShop!=null)
            {
                shopCategory.setShopId(sellerShop.getId());
            }

            Check.notNull(shopCategory.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");

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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }



    /**
     * 置顶(后台管理端)
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO moveTopForAdmin(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            Check.notNull(shopCategory.getId(), ResultVO.FAILD, "分类ID不能为空!");
            Check.notNull(shopCategory.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");
            Check.notNull(shopCategory.getParentId(), ResultVO.FAILD, "上级分类ID不能为空!");

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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO moveBottom(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            Check.notNull(shopCategory.getId(), ResultVO.FAILD, "分类ID不能为空!");

            SellerShop sellerShop = sellerShopService.findByUserMainId(shopCategory.getUserMainId());
            if(sellerShop!=null)
            {
                shopCategory.setShopId(sellerShop.getId());
            }

            Check.notNull(shopCategory.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");

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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("移动失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }



    /**
     * 置底(后台管理端)
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO moveBottomForAdmin(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            Check.notNull(shopCategory.getId(), ResultVO.FAILD, "分类ID不能为空!");
            Check.notNull(shopCategory.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");
            Check.notNull(shopCategory.getParentId(), ResultVO.FAILD, "上级分类ID不能为空!");

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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO moveUp(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            Check.notNull(shopCategory.getId(), ResultVO.FAILD, "分类ID不能为空!");

            SellerShop sellerShop = sellerShopService.findByUserMainId(shopCategory.getUserMainId());
            if(sellerShop!=null)
            {
                shopCategory.setShopId(sellerShop.getId());
            }

            Check.notNull(shopCategory.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");

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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO moveUpForAdmin(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            Check.notNull(shopCategory.getId(), ResultVO.FAILD, "分类ID不能为空!");
            Check.notNull(shopCategory.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");
            Check.notNull(shopCategory.getParentId(), ResultVO.FAILD, "上级分类ID不能为空!");

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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO moveDown(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            Check.notNull(shopCategory.getId(), ResultVO.FAILD, "分类ID不能为空!");

            SellerShop sellerShop = sellerShopService.findByUserMainId(shopCategory.getUserMainId());
            if(sellerShop!=null)
            {
                shopCategory.setShopId(sellerShop.getId());
            }

            Check.notNull(shopCategory.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");

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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO moveDownForAdmin(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);
        try {

            Check.notNull(shopCategory.getId(), ResultVO.FAILD, "分类ID不能为空!");
            Check.notNull(shopCategory.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");
            Check.notNull(shopCategory.getParentId(), ResultVO.FAILD, "上级分类ID不能为空!");

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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryById(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);

            Check.notNull(shopCategory.getId(), ResultVO.FAILD, "ID不能为空!");
            Check.notNull(shopCategory.getUserMainId(), ResultVO.FAILD, "用户ID不能为空!");

            SellerShop sellerShop = sellerShopService.findByUserMainId(shopCategory.getUserMainId());
            if(sellerShop!=null)
            {
                shopCategory.setShopId(sellerShop.getId());
            }

            Check.notNull(shopCategory.getShopId(), ResultVO.FAILD, "没有查询到关联店铺!");
            resultObjectVO.setData(shopCategoryService.queryByIdAndUserMainIdAndShopId(shopCategory.getId(),shopCategory.getUserMainId(),shopCategory.getShopId()));
        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryByIdList(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
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
        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO flushCache(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ShopCategoryVO shopCategoryVO = requestVo.formatEntity(ShopCategoryVO.class);
            Check.notNull(shopCategoryVO.getShopId(), ResultVO.FAILD, "刷新失败,店铺ID不能为空");
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
        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO clearCache(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ShopCategoryVO shopCategoryVO = requestVo.formatEntity(ShopCategoryVO.class);
            Check.notNull(shopCategoryVO.getShopId(), ResultVO.FAILD, "清空失败,店铺ID不能为空");
            toucanStringRedisService.delete(ShopCategoryKey.getCacheKey(shopCategoryVO.getShopId()));
        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ShopCategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),ShopCategoryVO.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到ID");

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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findIdPathById(RequestJsonVO requestVo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ShopCategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),ShopCategoryVO.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到ID");

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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findByIdArray(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ShopCategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),ShopCategoryVO.class);
            Check.notNull(entity.getIdArray(), ResultVO.FAILD, "没有找到ID数组");

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


        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryTree(RequestJsonVO requestJsonVO)
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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryWebIndexTree(RequestJsonVO requestJsonVO)
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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryTreeTable(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ShopCategoryTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategoryTreeInfo.class);

            Check.notNull(queryPageInfo.getShopId(), ResultVO.FAILD, "没有找到店铺ID");
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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListByPid(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ShopCategoryVO queryShopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategoryVO.class);
            resultObjectVO.setData(shopCategoryService.queryListOrderByCategorySortAsc(queryShopCategory));

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryAllList(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ShopCategoryVO queryShopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategoryVO.class);

            Check.notNull(queryShopCategory.getUserMainId(), ResultVO.FAILD, "店铺ID不能为空!");
            SellerShop sellerShop = sellerShopService.findByUserMainId(queryShopCategory.getUserMainId());
            if(sellerShop!=null)
            {
                queryShopCategory.setShopId(sellerShop.getId());
            }

            Check.notNull(queryShopCategory.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");
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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListByShopId(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ShopCategoryVO queryShopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategoryVO.class);

            Check.notNull(queryShopCategory.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");
            List<ShopCategory> shopCategorys = shopCategoryService.queryListOrderByCategorySortAsc(queryShopCategory);
            List<ShopCategoryVO> shopCategoryVOS = new ArrayList<ShopCategoryVO>();
            for(ShopCategory shopCategory:shopCategorys)
            {
                ShopCategoryVO shopCategoryVO = new ShopCategoryVO();
                BeanUtils.copyProperties(shopCategoryVO,shopCategory);

                shopCategoryVOS.add(shopCategoryVO);

            }
            resultObjectVO.setData(shopCategoryVOS);

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);

        if(shopCategory.getId()==null)
        {
            logger.warn("ID为空 param:"+ requestJsonVO.getEntityJson());
            return ResultObjectVO.fail(ResultVO.FAILD, "ID不能为空!");
        }


        if(shopCategory.getUserMainId()==null)
        {
            logger.warn("用户ID为空 param:"+ requestJsonVO.getEntityJson());
            return ResultObjectVO.fail(ResultVO.FAILD, "用户ID不能为空!");
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

            Check.notNull(shopCategory.getShopId(), ResultVO.FAILD, "没有查询到关联店铺!");

            ShopCategoryVO queryShopCategory = new ShopCategoryVO();
            queryShopCategory.setParentId(shopCategory.getId());
            queryShopCategory.setUserMainId(shopCategory.getUserMainId());

            List<ShopCategory> shopCategoryList = shopCategoryService.queryList(queryShopCategory);
            if(!CollectionUtils.isEmpty(shopCategoryList))
            {
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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIdForAdmin(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopCategory shopCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopCategory.class);

        if(shopCategory.getId()==null)
        {
            logger.warn("ID为空 param:"+ requestJsonVO.getEntityJson());
            return ResultObjectVO.fail(ResultVO.FAILD, "ID不能为空!");
        }


        if(shopCategory.getShopId()==null)
        {
            logger.warn("店铺ID为空 param:"+ requestJsonVO.getEntityJson());
            return ResultObjectVO.fail(ResultVO.FAILD, "店铺ID不能为空!");
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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

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

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

}
