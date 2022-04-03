package com.toucan.shopping.cloud.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.constant.ProductConstant;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.entity.ShopProduct;
import com.toucan.shopping.modules.product.entity.ShopProductApproveRecord;
import com.toucan.shopping.modules.product.entity.ShopProductImg;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.redis.PublishProductRedisLockKey;
import com.toucan.shopping.modules.product.service.ProductSkuService;
import com.toucan.shopping.modules.product.service.ShopProductApproveRecordService;
import com.toucan.shopping.modules.product.service.ShopProductImgService;
import com.toucan.shopping.modules.product.service.ShopProductService;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * 店铺商品
 * @auth majian
 */
@RestController
@RequestMapping("/shopProduct")
public class ShopProductController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private ShopProductService shopProductService;

    @Autowired
    private ShopProductImgService shopProductImgService;

    @Autowired
    private ShopProductApproveRecordService shopProductApproveRecordService;


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
            PublishProductVO publishProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), PublishProductVO.class);
            if(publishProductVO.getShopId()==null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }
            shopId = String.valueOf(publishProductVO.getShopId());
            skylarkLock.lock(PublishProductRedisLockKey.getPublishProductLockKey(shopId), shopId);

            //保存店铺商品
            if(CollectionUtils.isNotEmpty(publishProductVO.getProductSkuVOList())) {
                publishProductVO.setId(idGenerator.id());
                publishProductVO.setUuid(UUID.randomUUID().toString().replace("-", ""));
                publishProductVO.setCreateDate(new Date());
                publishProductVO.setApproveStatus((short) 1); //审核中
                publishProductVO.setStatus((short) 0);
                publishProductVO.setIsResubmit((short)0);
                int ret = shopProductService.save(publishProductVO);

                if(ret<=0)
                {
                    logger.warn("发布商品失败 原因:插入数据库影响行数返回小于等于0 {}",requestJsonVO.getEntityJson());
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("发布失败");
                }
                List<ProductSku> productSkus = new LinkedList<>();
                for(ProductSkuVO productSkuVO : publishProductVO.getProductSkuVOList())
                {
                    ProductSku productSku = new ProductSku();
                    BeanUtils.copyProperties(productSku,productSkuVO);

                    productSku.setId(idGenerator.id());
                    productSku.setCreateUserId(publishProductVO.getCreateUserId());
                    productSku.setCreateDate(new Date());
                    productSku.setStatus((short) 0);
                    productSku.setAppCode(publishProductVO.getAppCode());
                    productSku.setShopId(publishProductVO.getShopId()); //设置店铺ID
                    productSku.setShopProductId(publishProductVO.getId()); //设置店铺发布的商品ID
                    productSku.setShopProductUuid(publishProductVO.getUuid());
                    productSku.setBrankId(publishProductVO.getBrandId()); //设置品牌ID

                    productSkus.add(productSku);

                }
                ret = productSkuService.saves(productSkus);

                if(ret<publishProductVO.getProductSkuVOList().size())
                {
                    logger.warn("发布商品失败 原因:保存SKU影响返回行和保存数量不一致 {}",JSONObject.toJSONString(productSkus));
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("发布失败");

                    ret = shopProductService.deleteById(publishProductVO.getId());
                    if(ret<=0)
                    {
                        //发送异常邮件,通知运营处理
                        logger.warn("发布商品失败 回滚店铺商品表失败 id {}",publishProductVO.getId());
                    }
                }else {

                    //保存商品主图
                    List<ShopProductImg> shopProductImgs = new LinkedList<>();
                    ShopProductImg shopProductImg = new ShopProductImg();
                    shopProductImg.setId(idGenerator.id());
                    shopProductImg.setAppCode(publishProductVO.getAppCode());
                    shopProductImg.setCreateDate(new Date());
                    shopProductImg.setCreateUserId(publishProductVO.getCreateUserId());
                    shopProductImg.setDeleteStatus(0);
                    shopProductImg.setIsMainPhoto((short) 1);
                    shopProductImg.setShopProductId(publishProductVO.getId());
                    shopProductImg.setFilePath(publishProductVO.getMainPhotoFilePath());
                    shopProductImgs.add(shopProductImg);

                    //保存预览图
                    if (CollectionUtils.isNotEmpty(publishProductVO.getPreviewPhotoPaths())) {
                        for (String pewviewPhotoPath : publishProductVO.getPreviewPhotoPaths()) {
                            ShopProductImg productImg = new ShopProductImg();
                            productImg.setId(idGenerator.id());
                            productImg.setAppCode(publishProductVO.getAppCode());
                            productImg.setCreateDate(new Date());
                            productImg.setCreateUserId(publishProductVO.getCreateUserId());
                            productImg.setDeleteStatus(0);
                            productImg.setIsMainPhoto((short) 0);
                            productImg.setShopProductId(publishProductVO.getId());
                            productImg.setFilePath(pewviewPhotoPath);

                            shopProductImgs.add(productImg);
                        }

                        ret = shopProductImgService.saves(shopProductImgs);
                        if (ret <= publishProductVO.getPreviewPhotoPaths().size()) {
                            logger.warn("发布商品失败 原因:商品图片影响返回行和保存数量不一致 {}", JSONObject.toJSONString(shopProductImgs));
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg("发布失败");

                            ret = shopProductService.deleteById(publishProductVO.getId());
                            if (ret <= 0) {
                                //发送异常邮件,通知运营处理
                                logger.warn("发布商品失败 回滚店铺商品表失败 id {}", publishProductVO.getId());
                            }

                            ret = productSkuService.deleteByShopProductId(publishProductVO.getId());
                            if (ret<publishProductVO.getProductSkuVOList().size()) {
                                logger.warn("发布商品失败 回滚店铺商品SKU失败 {}", JSONObject.toJSONString(productSkus));
                            }

                            //TODO:删除服务器中图片资源
                            ret = shopProductImgService.deleteByShopProductId(publishProductVO.getId());
                            if (ret<publishProductVO.getPreviewPhotoPaths().size()) {
                                logger.warn("发布商品失败 回滚店铺商品SKU失败 {}", JSONObject.toJSONString(productSkus));
                            }

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
            skylarkLock.unLock(PublishProductRedisLockKey.getPublishProductLockKey(shopId), shopId);
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
            ShopProductPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductPageInfo.class);
            PageInfo<ShopProductVO> pageInfo =  shopProductService.queryListPage(queryPageInfo);

            if(pageInfo.getTotal()!=null&&pageInfo.getTotal().longValue()>0)
            {
                List<Long> shopProductIdList = new LinkedList<>();
                for(ShopProductVO shopProductVO : pageInfo.getList())
                {
                    shopProductVO.setPreviewPhotoPaths(new LinkedList<>());

                    shopProductIdList.add(shopProductVO.getId());
                }
                ShopProductImgVO shopProductImgVO = new ShopProductImgVO();
                shopProductImgVO.setShopProductIdList(shopProductIdList);
                List<ShopProductImg> shopProductImgs = shopProductImgService.queryList(shopProductImgVO);
                if(CollectionUtils.isNotEmpty(shopProductImgs))
                {
                    for(ShopProductImg shopProductImg:shopProductImgs)
                    {
                        for(ShopProductVO shopProductVO:pageInfo.getList()) {
                            if (shopProductImg.getShopProductId() != null &&
                                    shopProductImg.getShopProductId().longValue()==shopProductVO.getId().longValue())
                            {
                                //如果是商品主图
                                if(shopProductImg.getIsMainPhoto().intValue()==1)
                                {
                                    shopProductVO.setMainPhotoFilePath(shopProductImg.getFilePath());
                                }else{
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
    public ResultObjectVO queryByShopProductId(@RequestBody RequestJsonVO requestJsonVO)
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
            ShopProductVO shopProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductVO.class);
            if(shopProductVO==null||shopProductVO.getId()==null||shopProductVO.getId().longValue()==-1)
            {
                logger.warn("没有找到ID: param:"+ JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID!");
                return resultObjectVO;
            }
            ShopProductVO queryShopProductVO = new ShopProductVO();
            queryShopProductVO.setId(shopProductVO.getId());
            List<ShopProductVO> shopProductVOS = shopProductService.queryList(queryShopProductVO);

            if(CollectionUtils.isNotEmpty(shopProductVOS)) {

                shopProductVO = shopProductVOS.get(0);
                shopProductVO.setPreviewPhotoPaths(new LinkedList<>());

                ProductSkuVO queryProductSku = new ProductSkuVO();
                queryProductSku.setShopProductId(shopProductVO.getId());
                //查询SKU
                List<ProductSkuVO> productSkuVOS = productSkuService.queryList(queryProductSku);
                shopProductVO.setProductSkuVOList(productSkuVOS);

                //查询商品图片
                ShopProductImgVO shopProductImgVO = new ShopProductImgVO();
                shopProductImgVO.setShopProductId(shopProductVO.getId());
                List<ShopProductImg> shopProductImgs = shopProductImgService.queryList(shopProductImgVO);
                if (CollectionUtils.isNotEmpty(shopProductImgs)) {
                    for (ShopProductImg shopProductImg : shopProductImgs) {
                        //如果是商品主图
                        if (shopProductImg.getIsMainPhoto().intValue() == 1) {
                            shopProductVO.setMainPhotoFilePath(shopProductImg.getFilePath());
                        } else {
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
            shopProductService.updateApproveStatus(shopProductApproveRecordVO.getApproveId(),ProductConstant.REJECT);
            ShopProductApproveRecord shopProductApproveRecord = new ShopProductApproveRecord();
            BeanUtils.copyProperties(shopProductApproveRecord,shopProductApproveRecordVO);
            shopProductApproveRecord.setApproveStatus(ProductConstant.REJECT);
            shopProductApproveRecord.setCreateAdminId(shopProductApproveRecordVO.getCreateAdminId());
            shopProductApproveRecord.setCreateDate(new Date());
            shopProductApproveRecord.setId(idGenerator.id());
            int ret = shopProductApproveRecordService.save(shopProductApproveRecord);
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
        try{
            ShopProductVO shopProductVO = requestJsonVO.formatEntity(ShopProductVO.class);
            if(shopProductVO.getId()==null)
            {
                resultObjectVO.setMsg("商品ID不能为空");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            if(shopProductVO.getProductId()==null)
            {
                resultObjectVO.setMsg("平台商品ID不能为空");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(shopProductVO.getProductUuid()))
            {
                resultObjectVO.setMsg("平台商品UUID不能为空");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            logger.info("通过店铺商品 {} ",requestJsonVO.getEntityJson());
            shopProductService.updateApproveStatusAndProductId(shopProductVO.getId(),ProductConstant.PASS,shopProductVO.getProductId(),shopProductVO.getProductUuid());
            ShopProductApproveRecord shopProductApproveRecord = new ShopProductApproveRecord();
            shopProductApproveRecord.setApproveText("审核通过");
            shopProductApproveRecord.setApproveStatus(ProductConstant.REJECT);
            shopProductApproveRecord.setCreateAdminId(shopProductVO.getCreateAdminId());
            shopProductApproveRecord.setCreateDate(new Date());
            shopProductApproveRecord.setId(idGenerator.id());
            int ret = shopProductApproveRecordService.save(shopProductApproveRecord);
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



}
