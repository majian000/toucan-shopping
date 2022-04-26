package com.toucan.shopping.cloud.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.netflix.discovery.converters.Auto;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.constant.ProductConstant;
import com.toucan.shopping.modules.product.entity.*;
import com.toucan.shopping.modules.product.page.ShopProductApprovePageInfo;
import com.toucan.shopping.modules.product.redis.ProductApproveRedisLockKey;
import com.toucan.shopping.modules.product.service.*;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 店铺商品审核
 * @auth majian
 */
@RestController
@RequestMapping("/shopProductApprove")
public class ShopProductApproveController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private ShopProductApproveSkuService shopProductApproveSkuService;

    @Autowired
    private ShopProductApproveService shopProductApproveService;

    @Autowired
    private ShopProductApproveImgService shopProductApproveImgService;

    @Autowired
    private ShopProductApproveRecordService shopProductRecordService;

    @Autowired
    private ShopProductService shopProductService;

    @Autowired
    private ShopProductImgService shopProductImgService;

    @Autowired
    private ProductSkuService productSkuService;


    /**
     * 发布商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/publish",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO publish(@RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }
        String shopId ="";
        try {
            logger.info("发布商品 {} ",requestJsonVO.getEntityJson());
            PublishProductApproveVO publishProductApproveVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), PublishProductApproveVO.class);
            if(publishProductApproveVO.getShopId()==null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }
            shopId = String.valueOf(publishProductApproveVO.getShopId());
            skylarkLock.lock(ProductApproveRedisLockKey.getSaveProductLockKey(shopId), shopId);

            //保存店铺商品
            if(CollectionUtils.isNotEmpty(publishProductApproveVO.getProductSkuVOList())) {
                publishProductApproveVO.setId(idGenerator.id());
                publishProductApproveVO.setUuid(UUID.randomUUID().toString().replace("-", ""));
                publishProductApproveVO.setCreateDate(new Date());
                publishProductApproveVO.setUpdateDate(new Date());
                publishProductApproveVO.setUpdateUserId(publishProductApproveVO.getCreateUserId());
                publishProductApproveVO.setApproveStatus((short) 1); //审核中
                int ret = shopProductApproveService.save(publishProductApproveVO);

                if(ret<=0)
                {
                    logger.warn("发布商品失败 原因:插入数据库影响行数返回小于等于0 {}",requestJsonVO.getEntityJson());
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("发布失败");
                    return resultObjectVO;
                }
                List<ShopProductApproveSku> productSkus = new LinkedList<>();
                for(ShopProductApproveSkuVO productSkuVO : publishProductApproveVO.getProductSkuVOList())
                {
                    ShopProductApproveSku productSku = new ShopProductApproveSku();
                    BeanUtils.copyProperties(productSku,productSkuVO);

                    productSku.setId(idGenerator.id());
                    productSku.setUuid(UUID.randomUUID().toString().replace("-", ""));
                    productSku.setCreateUserId(publishProductApproveVO.getCreateUserId());
                    productSku.setCreateDate(new Date());
                    productSku.setStatus((short) 0);
                    productSku.setAppCode(publishProductApproveVO.getAppCode());
                    productSku.setShopId(publishProductApproveVO.getShopId()); //设置店铺ID
                    productSku.setProductApproveId(publishProductApproveVO.getId()); //设置店铺发布的商品ID
                    productSku.setProductApproveUuid(publishProductApproveVO.getUuid());
                    productSku.setBrankId(publishProductApproveVO.getBrandId()); //设置品牌ID
                    productSku.setCategoryId(publishProductApproveVO.getCategoryId()); //商品分类ID
                    productSku.setShopCategoryId(publishProductApproveVO.getShopCategoryId()); //店铺商品分类ID

                    productSkus.add(productSku);

                }
                ret = shopProductApproveSkuService.saves(productSkus);

                if(ret< publishProductApproveVO.getProductSkuVOList().size())
                {
                    logger.warn("发布商品失败 原因:保存SKU影响返回行和保存数量不一致 {}",JSONObject.toJSONString(productSkus));
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("发布失败");

                    ret = shopProductApproveService.deleteById(publishProductApproveVO.getId());
                    if(ret<=0)
                    {
                        //发送异常邮件,通知运营处理
                        logger.warn("发布商品失败 回滚店铺商品表失败 id {}", publishProductApproveVO.getId());
                    }
                }else {

                    int imgSort = 0 ;
                    //保存商品主图
                    List<ShopProductApproveImg> shopProductImgs = new LinkedList<>();
                    ShopProductApproveImg shopProductImg = new ShopProductApproveImg();
                    shopProductImg.setId(idGenerator.id());
                    shopProductImg.setAppCode(publishProductApproveVO.getAppCode());
                    shopProductImg.setCreateDate(new Date());
                    shopProductImg.setCreateUserId(publishProductApproveVO.getCreateUserId());
                    shopProductImg.setDeleteStatus(0);
                    shopProductImg.setImgType((short) 1);
                    shopProductImg.setImgSort(imgSort);
                    shopProductImg.setProductApproveId(publishProductApproveVO.getId());
                    shopProductImg.setFilePath(publishProductApproveVO.getMainPhotoFilePath());
                    shopProductImgs.add(shopProductImg);
                    imgSort++;
                    if (CollectionUtils.isNotEmpty(publishProductApproveVO.getPreviewPhotoPaths())) {
                        for (String pewviewPhotoPath : publishProductApproveVO.getPreviewPhotoPaths()) {
                            ShopProductApproveImg productImg = new ShopProductApproveImg();
                            productImg.setId(idGenerator.id());
                            productImg.setAppCode(publishProductApproveVO.getAppCode());
                            productImg.setCreateDate(new Date());
                            productImg.setCreateUserId(publishProductApproveVO.getCreateUserId());
                            productImg.setDeleteStatus(0);
                            productImg.setImgType((short) 2);
                            productImg.setImgSort(imgSort);
                            productImg.setProductApproveId(publishProductApproveVO.getId());
                            productImg.setFilePath(pewviewPhotoPath);
                            imgSort++;

                            shopProductImgs.add(productImg);
                        }

                    }

                    //保存预览图
                    ret = shopProductApproveImgService.saves(shopProductImgs);
                    if (ret < shopProductImgs.size()) {
                        logger.warn("发布商品失败 原因:商品图片影响返回行和保存数量不一致 {}", JSONObject.toJSONString(shopProductImgs));
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("发布失败");

                        ret = shopProductApproveService.deleteById(publishProductApproveVO.getId());
                        if (ret <= 0) {
                            //发送异常邮件,通知运营处理
                            logger.warn("发布商品失败 回滚店铺商品表失败 id {}", publishProductApproveVO.getId());
                        }

                        ret = shopProductApproveSkuService.deleteByShopProductApproveId(publishProductApproveVO.getId());
                        if (ret< publishProductApproveVO.getProductSkuVOList().size()) {
                            logger.warn("发布商品失败 回滚店铺商品SKU失败 {}", JSONObject.toJSONString(productSkus));
                        }

                        //TODO:删除服务器中图片资源
                        ret = shopProductApproveImgService.deleteByProductApproveId(publishProductApproveVO.getId());
                        if (ret< publishProductApproveVO.getPreviewPhotoPaths().size()) {
                            logger.warn("发布商品失败 回滚店铺商品SKU失败 {}", JSONObject.toJSONString(productSkus));
                        }

                    }

                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("发布失败");
        }finally{
            skylarkLock.unLock(ProductApproveRedisLockKey.getSaveProductLockKey(shopId), shopId);
        }
        return resultObjectVO;
    }




    /**
     * 重新发布商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/republish",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO republish(@RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }
        String shopId ="";
        try {
            logger.info("发布商品 {} ",requestJsonVO.getEntityJson());
            RePublishProductApproveVO rePublishProductApproveVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), RePublishProductApproveVO.class);
            if(rePublishProductApproveVO.getShopId()==null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }
            shopId = String.valueOf(rePublishProductApproveVO.getShopId());
            skylarkLock.lock(ProductApproveRedisLockKey.getResaveProductLockKey(shopId), shopId);

            //修改店铺商品
            if(CollectionUtils.isNotEmpty(rePublishProductApproveVO.getProductSkuVOList())) {
                rePublishProductApproveVO.setApproveStatus((short) 1);
                rePublishProductApproveVO.setUpdateDate(new Date());
                int ret = shopProductApproveService.updateForRepublish(rePublishProductApproveVO);

                if(ret<=0)
                {
                    logger.warn("重新发布商品失败 原因:插入数据库影响行数返回小于等于0 {}",requestJsonVO.getEntityJson());
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("发布失败");
                    return resultObjectVO;
                }

                //清空SKU关联
                shopProductApproveSkuService.deleteByShopProductApproveId(rePublishProductApproveVO.getId());

                List<ShopProductApproveSku> productSkus = new LinkedList<>();
                for(ShopProductApproveSkuVO productSkuVO : rePublishProductApproveVO.getProductSkuVOList())
                {
                    ShopProductApproveSku productSku = new ShopProductApproveSku();
                    BeanUtils.copyProperties(productSku,productSkuVO);

                    productSku.setId(idGenerator.id());
                    productSku.setUuid(UUID.randomUUID().toString().replace("-", ""));
                    productSku.setCreateUserId(rePublishProductApproveVO.getCreateUserId());
                    productSku.setCreateDate(new Date());
                    productSku.setStatus((short) 0);
                    productSku.setAppCode(rePublishProductApproveVO.getAppCode());
                    productSku.setShopId(rePublishProductApproveVO.getShopId()); //设置店铺ID
                    productSku.setProductApproveId(rePublishProductApproveVO.getId()); //设置店铺发布的商品ID
                    productSku.setProductApproveUuid(rePublishProductApproveVO.getUuid());
                    productSku.setBrankId(rePublishProductApproveVO.getBrandId()); //设置品牌ID
                    productSku.setCategoryId(rePublishProductApproveVO.getCategoryId()); //商品分类ID
                    productSku.setShopCategoryId(rePublishProductApproveVO.getShopCategoryId()); //店铺商品分类ID

                    productSkus.add(productSku);

                }
                ret = shopProductApproveSkuService.saves(productSkus);

                if(ret< rePublishProductApproveVO.getProductSkuVOList().size())
                {
                    logger.warn("发布商品失败 原因:保存SKU影响返回行和保存数量不一致 {}",JSONObject.toJSONString(productSkus));
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("发布失败");

                    ret = shopProductApproveService.deleteById(rePublishProductApproveVO.getId());
                    if(ret<=0)
                    {
                        //发送异常邮件,通知运营处理
                        logger.warn("发布商品失败 回滚店铺商品表失败 id {}", rePublishProductApproveVO.getId());
                    }
                }else {

                    //删除商品图片关联
                    shopProductApproveImgService.deleteByProductApproveId(rePublishProductApproveVO.getId());

                    int imgSort=0;
                    //保存商品主图
                    List<ShopProductApproveImg> shopProductImgs = new LinkedList<>();
                    ShopProductApproveImg shopProductImg = new ShopProductApproveImg();
                    shopProductImg.setId(idGenerator.id());
                    shopProductImg.setAppCode(rePublishProductApproveVO.getAppCode());
                    shopProductImg.setCreateDate(new Date());
                    shopProductImg.setCreateUserId(rePublishProductApproveVO.getUpdateUserId());
                    shopProductImg.setDeleteStatus(0);
                    shopProductImg.setImgType((short) 1);
                    shopProductImg.setImgSort(imgSort);
                    shopProductImg.setProductApproveId(rePublishProductApproveVO.getId());
                    shopProductImg.setFilePath(rePublishProductApproveVO.getMainPhotoFilePath());
                    shopProductImgs.add(shopProductImg);
                    imgSort++;

                    //保存预览图
                    if (CollectionUtils.isNotEmpty(rePublishProductApproveVO.getPreviewPhotoPaths())) {
                        for (String pewviewPhotoPath : rePublishProductApproveVO.getPreviewPhotoPaths()) {
                            ShopProductApproveImg productImg = new ShopProductApproveImg();
                            productImg.setId(idGenerator.id());
                            productImg.setAppCode(rePublishProductApproveVO.getAppCode());
                            productImg.setCreateDate(new Date());
                            productImg.setCreateUserId(rePublishProductApproveVO.getUpdateUserId());
                            productImg.setDeleteStatus(0);
                            productImg.setImgType((short) 2);
                            productImg.setImgSort(imgSort);
                            productImg.setProductApproveId(rePublishProductApproveVO.getId());
                            productImg.setFilePath(pewviewPhotoPath);
                            imgSort++;
                            shopProductImgs.add(productImg);
                        }

                    }

                    ret = shopProductApproveImgService.saves(shopProductImgs);
                    if (ret < shopProductImgs.size()) {
                        logger.warn("发布商品失败 原因:商品图片影响返回行和保存数量不一致 {}", JSONObject.toJSONString(shopProductImgs));
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("发布失败");

                        ret = shopProductApproveService.deleteById(rePublishProductApproveVO.getId());
                        if (ret <= 0) {
                            //发送异常邮件,通知运营处理
                            logger.warn("发布商品失败 回滚店铺商品表失败 id {}", rePublishProductApproveVO.getId());
                        }

                        ret = shopProductApproveSkuService.deleteByShopProductApproveId(rePublishProductApproveVO.getId());
                        if (ret< rePublishProductApproveVO.getProductSkuVOList().size()) {
                            logger.warn("发布商品失败 回滚店铺商品SKU失败 {}", JSONObject.toJSONString(productSkus));
                        }

                        //TODO:删除服务器中图片资源
                        ret = shopProductApproveImgService.deleteByProductApproveId(rePublishProductApproveVO.getId());
                        if (ret< rePublishProductApproveVO.getPreviewPhotoPaths().size()) {
                            logger.warn("发布商品失败 回滚店铺商品SKU失败 {}", JSONObject.toJSONString(productSkus));
                        }

                    }

                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("发布失败");
        }finally{
            skylarkLock.unLock(ProductApproveRedisLockKey.getResaveProductLockKey(shopId), shopId);
        }
        return resultObjectVO;
    }




    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.warn("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }
        try {
            ShopProductApprovePageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductApprovePageInfo.class);
            PageInfo<ShopProductApproveVO> pageInfo =  shopProductApproveService.queryListPage(queryPageInfo);

            if(pageInfo.getTotal()!=null&&pageInfo.getTotal().longValue()>0)
            {
                List<Long> shopProductIdList = new LinkedList<>();
                for(ShopProductApproveVO shopProductVO : pageInfo.getList())
                {
                    shopProductVO.setPreviewPhotoPaths(new LinkedList<>());

                    shopProductIdList.add(shopProductVO.getId());
                }
                ShopProductApproveImgVO shopProductImgVO = new ShopProductApproveImgVO();
                shopProductImgVO.setProductApproveIdList(shopProductIdList);
                List<ShopProductApproveImg> shopProductImgs = shopProductApproveImgService.queryList(shopProductImgVO);
                if(CollectionUtils.isNotEmpty(shopProductImgs))
                {
                    for(ShopProductApproveImg shopProductImg:shopProductImgs)
                    {
                        for(ShopProductApproveVO shopProductVO:pageInfo.getList()) {
                            if (shopProductImg.getProductApproveId() != null &&
                                    shopProductImg.getProductApproveId().longValue()==shopProductVO.getId().longValue())
                            {
                                //如果是商品主图
                                if(shopProductImg.getImgType().intValue()==1)
                                {
                                    shopProductVO.setMainPhotoFilePath(shopProductImg.getFilePath());
                                }else if(shopProductImg.getImgType().intValue()==2)
                                {
                                    shopProductVO.getPreviewPhotoPaths().add(shopProductImg.getFilePath());
                                }
                                break;
                            }

                        }
                    }
                }
            }

            resultObjectVO.setData(pageInfo);
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
    @RequestMapping(value="/query/id",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByProductApproveId(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.warn("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }
        try {
            ShopProductApproveVO shopProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductApproveVO.class);
            if(shopProductVO==null||shopProductVO.getId()==null||shopProductVO.getId().longValue()==-1)
            {
                logger.warn("没有找到ID: param:"+ JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID!");
                return resultObjectVO;
            }
            ShopProductApproveVO queryShopProductApproveVO = new ShopProductApproveVO();
            queryShopProductApproveVO.setId(shopProductVO.getId());
            List<ShopProductApproveVO> shopProductVOS = shopProductApproveService.queryList(queryShopProductApproveVO);

            if(CollectionUtils.isNotEmpty(shopProductVOS)) {

                shopProductVO = shopProductVOS.get(0);
                shopProductVO.setPreviewPhotoPaths(new LinkedList<>());

                ShopProductApproveSkuVO queryShopProductApproveSku = new ShopProductApproveSkuVO();
                queryShopProductApproveSku.setProductApproveId(shopProductVO.getId());
                //查询SKU
                List<ShopProductApproveSkuVO> productSkuVOS = shopProductApproveSkuService.queryList(queryShopProductApproveSku);
                shopProductVO.setProductSkuVOList(productSkuVOS);

                //查询商品图片
                ShopProductApproveImgVO shopProductImgVO = new ShopProductApproveImgVO();
                shopProductImgVO.setProductApproveId(shopProductVO.getId());
                List<ShopProductApproveImg> shopProductImgs = shopProductApproveImgService.queryListOrderByImgSortAsc(shopProductImgVO);
                if (CollectionUtils.isNotEmpty(shopProductImgs)) {
                    for (ShopProductApproveImg shopProductImg : shopProductImgs) {
                        //如果是商品主图
                        if (shopProductImg.getImgType().intValue() == 1) {
                            shopProductVO.setMainPhotoFilePath(shopProductImg.getFilePath());
                        } else if (shopProductImg.getImgType().intValue() == 2) {
                            shopProductVO.getPreviewPhotoPaths().add(shopProductImg.getFilePath());
                        }
                    }
                }
            }
            resultObjectVO.setData(shopProductVOS);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }



    /**
     * 根据ID和店铺ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id/shopId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByProductApproveIdAndShopId(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.warn("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }
        try {
            ShopProductApproveVO shopProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductApproveVO.class);
            if(shopProductVO==null||shopProductVO.getId()==null||shopProductVO.getId().longValue()==-1)
            {
                logger.warn("没有找到ID: param:"+ JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID!");
                return resultObjectVO;
            }
            if(shopProductVO==null||shopProductVO.getShopId()==null||shopProductVO.getShopId().longValue()==-1)
            {
                logger.warn("没有找到店铺ID: param:"+ JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到店铺ID!");
                return resultObjectVO;
            }
            ShopProductApproveVO queryShopProductApproveVO = new ShopProductApproveVO();
            queryShopProductApproveVO.setId(shopProductVO.getId());
            queryShopProductApproveVO.setShopId(shopProductVO.getShopId());

            shopProductVO = shopProductApproveService.queryOne(queryShopProductApproveVO);

            if(shopProductVO!=null) {

                shopProductVO.setPreviewPhotoPaths(new LinkedList<>());

                ShopProductApproveSkuVO queryShopProductApproveSku = new ShopProductApproveSkuVO();
                queryShopProductApproveSku.setProductApproveId(shopProductVO.getId());
                //查询SKU
                List<ShopProductApproveSkuVO> productSkuVOS = shopProductApproveSkuService.queryList(queryShopProductApproveSku);
                shopProductVO.setProductSkuVOList(productSkuVOS);

                //查询商品图片
                ShopProductApproveImgVO shopProductImgVO = new ShopProductApproveImgVO();
                shopProductImgVO.setProductApproveId(shopProductVO.getId());
                List<ShopProductApproveImg> shopProductImgs = shopProductApproveImgService.queryListOrderByImgSortAsc(shopProductImgVO);
                if (CollectionUtils.isNotEmpty(shopProductImgs)) {
                    for (ShopProductApproveImg shopProductImg : shopProductImgs) {
                        //如果是商品主图
                        if (shopProductImg.getImgType().intValue() == 1) {
                            shopProductVO.setMainPhotoFilePath(shopProductImg.getFilePath());
                        } else if (shopProductImg.getImgType().intValue() == 2) {
                            shopProductVO.getPreviewPhotoPaths().add(shopProductImg.getFilePath());
                        }
                    }
                }
                resultObjectVO.setData(shopProductVO);
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
     * 审核驳回
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/reject",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO reject(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            ShopProductApproveRecordVO shopProductApproveRecordVO = requestJsonVO.formatEntity(ShopProductApproveRecordVO.class);
            if(shopProductApproveRecordVO.getApproveId()==null)
            {
                resultObjectVO.setMsg("审核ID不能为空");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            logger.info("驳回店铺商品 {} ",requestJsonVO.getEntityJson());
            shopProductApproveService.updateApproveStatusAndRejectTextAndUpdateDate(shopProductApproveRecordVO.getApproveId(),ProductConstant.REJECT,shopProductApproveRecordVO.getApproveText(),new Date());
            ShopProductApproveRecord shopProductApproveRecord = new ShopProductApproveRecord();
            BeanUtils.copyProperties(shopProductApproveRecord,shopProductApproveRecordVO);
            shopProductApproveRecord.setApproveStatus(ProductConstant.REJECT);
            shopProductApproveRecord.setCreateAdminId(shopProductApproveRecordVO.getCreateAdminId());
            shopProductApproveRecord.setCreateDate(new Date());
            shopProductApproveRecord.setId(idGenerator.id());
            int ret = shopProductRecordService.save(shopProductApproveRecord);
            if(ret<1)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("保存审核记录失败");
                return resultObjectVO;
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败");
        }

        return resultObjectVO;

    }





    /**
     * 审核通过
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/pass",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO pass(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        String productApproveId = "-1";
        Long shopProductId = idGenerator.id();
        try{
            ShopProductApproveVO shopProductApproveVO = requestJsonVO.formatEntity(ShopProductApproveVO.class);

            if(shopProductApproveVO.getId()==null)
            {
                resultObjectVO.setMsg("审核ID不能为空");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            if(shopProductApproveVO.getProductId()==null)
            {
                resultObjectVO.setMsg("平台商品ID不能为空");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(shopProductApproveVO.getProductUuid()))
            {
                resultObjectVO.setMsg("平台商品UUID不能为空");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            productApproveId = String.valueOf(shopProductApproveVO.getId());
            skylarkLock.lock(ProductApproveRedisLockKey.getProductApprovePassLockKey(productApproveId), productApproveId);

            logger.info("通过店铺商品 {} ",requestJsonVO.getEntityJson());

            //更新商品审核主表
            shopProductApproveService.updateApproveStatusAndProductIdAndRejectText(shopProductApproveVO.getId(),ProductConstant.PASS,shopProductApproveVO.getProductId(),shopProductApproveVO.getProductUuid(),"");

            //更新商品审核SKU表
            shopProductApproveSkuService.updateProductIdAndProductUuidByApproveId(shopProductApproveVO.getId(),shopProductApproveVO.getProductId(),shopProductApproveVO.getProductUuid());

            ShopProductApproveRecord shopProductApproveRecord = new ShopProductApproveRecord();
            shopProductApproveRecord.setApproveId(shopProductApproveVO.getId());
            shopProductApproveRecord.setApproveText("审核通过");
            shopProductApproveRecord.setApproveStatus(ProductConstant.REJECT);
            shopProductApproveRecord.setCreateAdminId(shopProductApproveVO.getCreateAdminId());
            shopProductApproveRecord.setCreateDate(new Date());
            shopProductApproveRecord.setId(idGenerator.id());
            int ret = shopProductRecordService.save(shopProductApproveRecord);
            if(ret<1)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("保存审核记录失败");
                return resultObjectVO;
            }

            //保存店铺商品信息
            ShopProductApprove shopProductApprove = shopProductApproveService.findById(shopProductApproveVO.getId());
            if(shopProductApprove!=null&&shopProductApprove.getId()!=-1&&shopProductApprove.getApproveStatus().intValue()==ProductConstant.PASS.intValue()) {

                ShopProductVO queryShopProduct = new ShopProductVO();
                queryShopProduct.setProductApproveId(shopProductApprove.getId());
                List<ShopProductVO> shopProductVOS = shopProductService.queryList(queryShopProduct);
                if(CollectionUtils.isNotEmpty(shopProductVOS))
                {
                    logger.info("审核通过,已存在店铺商品 approveId {} ",productApproveId);
                    resultObjectVO.setCode(ResultVO.SUCCESS);
                    resultObjectVO.setMsg("审核通过");
                    return resultObjectVO;
                }

                ShopProduct shopProduct = new ShopProduct();
                BeanUtils.copyProperties(shopProduct, shopProductApprove);
                //立即上架
                if(shopProductApprove.getShelvesStatus()!=null&&shopProductApprove.getShelvesStatus().intValue()==2)
                {
                    shopProduct.setStatus((short)1);
                }else{
                    shopProduct.setStatus((short)0);
                }
                shopProduct.setId(shopProductId);
                shopProduct.setUuid(UUID.randomUUID().toString().replace("-", ""));
                shopProduct.setProductApproveId(shopProductApprove.getId());
                ret = shopProductService.save(shopProduct);

                if(ret>0) {
                    //保存商品图片
                    List<ShopProductImg> shopProductImgs = new ArrayList<>();
                    List<ShopProductApproveImg> shopProductApproveImgs = shopProductApproveImgService.queryListByApproveId(shopProductApprove.getId());
                    if (CollectionUtils.isNotEmpty(shopProductApproveImgs)) {
                        for (ShopProductApproveImg shopProductApproveImg : shopProductApproveImgs) {
                            ShopProductImg shopProductImg = new ShopProductImg();
                            BeanUtils.copyProperties(shopProductImg, shopProductApproveImg);
                            shopProductImg.setId(idGenerator.id());
                            shopProductImg.setShopProductId(shopProduct.getId());
                            shopProductImgs.add(shopProductImg);
                        }
                    }
                    if (CollectionUtils.isEmpty(shopProductImgs)) {
                        throw new IllegalArgumentException("保存商品图片失败");
                    }

                    ret = shopProductImgService.saves(shopProductImgs);
                    if(ret!=shopProductImgs.size()) {
                        throw new IllegalArgumentException("保存商品图片失败");
                    }
                    //保存商品SKU
                    List<ShopProductApproveSkuVO> shopProductApproveSkuVOS = shopProductApproveSkuService.queryListByProductApproveId(shopProductApprove.getId());
                    List<ProductSku> productSkus = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(shopProductApproveSkuVOS)) {
                        for (ShopProductApproveSkuVO shopProductApproveSkuVO : shopProductApproveSkuVOS) {
                            ProductSku productSku = new ProductSku();
                            BeanUtils.copyProperties(productSku, shopProductApproveSkuVO);
                            productSku.setId(idGenerator.id());

                            productSku.setShopProductId(shopProduct.getId());
                            productSku.setShopProductUuid(shopProduct.getUuid());
                            productSkus.add(productSku);
                        }
                    }

                    if (CollectionUtils.isEmpty(productSkus)) {
                        throw new IllegalArgumentException("保存商品SKU失败");
                    }

                    ret = productSkuService.saves(productSkus);
                    if(ret!=productSkus.size())
                    {
                        throw new IllegalArgumentException("保存商品SKU失败");
                    }

                    //更新关联店铺商品ID
                    shopProductApproveService.updateShopProductId(shopProductApproveVO.getId(),shopProduct.getId());

                }else{
                    throw new IllegalArgumentException("保存商品图片失败");
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败");

            logger.warn("开始回滚数据 shopProductId {}",shopProductId);
            shopProductService.deleteById(shopProductId);
            shopProductImgService.deleteByShopProductId(shopProductId);
            productSkuService.deleteByShopProductId(shopProductId);
        }finally {
            skylarkLock.unLock(ProductApproveRedisLockKey.getProductApprovePassLockKey(productApproveId),productApproveId);
        }

        return resultObjectVO;

    }



}
