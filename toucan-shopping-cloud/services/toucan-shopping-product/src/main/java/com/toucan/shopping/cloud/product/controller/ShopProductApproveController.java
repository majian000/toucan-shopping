package com.toucan.shopping.cloud.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.constant.ProductConstant;
import com.toucan.shopping.modules.product.entity.ShopProductApproveSku;
import com.toucan.shopping.modules.product.entity.ShopProductApproveImg;
import com.toucan.shopping.modules.product.entity.ShopProductApproveRecord;
import com.toucan.shopping.modules.product.page.ShopProductApprovePageInfo;
import com.toucan.shopping.modules.product.redis.PublishProductRedisLockKey;
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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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
            skylarkLock.lock(PublishProductRedisLockKey.getPublishProductLockKey(shopId), shopId);

            //保存店铺商品
            if(CollectionUtils.isNotEmpty(publishProductApproveVO.getShopProductApproveSkuVOList())) {
                publishProductApproveVO.setId(idGenerator.id());
                publishProductApproveVO.setUuid(UUID.randomUUID().toString().replace("-", ""));
                publishProductApproveVO.setCreateDate(new Date());
                publishProductApproveVO.setApproveStatus((short) 1); //审核中
                publishProductApproveVO.setStatus((short) 0);
                publishProductApproveVO.setIsResubmit((short)0);
                int ret = shopProductApproveService.save(publishProductApproveVO);

                if(ret<=0)
                {
                    logger.warn("发布商品失败 原因:插入数据库影响行数返回小于等于0 {}",requestJsonVO.getEntityJson());
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("发布失败");
                }
                List<ShopProductApproveSku> productSkus = new LinkedList<>();
                for(ShopProductApproveSkuVO productSkuVO : publishProductApproveVO.getShopProductApproveSkuVOList())
                {
                    ShopProductApproveSku productSku = new ShopProductApproveSku();
                    BeanUtils.copyProperties(productSku,productSkuVO);

                    productSku.setId(idGenerator.id());
                    productSku.setCreateUserId(publishProductApproveVO.getCreateUserId());
                    productSku.setCreateDate(new Date());
                    productSku.setStatus((short) 0);
                    productSku.setAppCode(publishProductApproveVO.getAppCode());
                    productSku.setShopId(publishProductApproveVO.getShopId()); //设置店铺ID
                    productSku.setProductApproveId(publishProductApproveVO.getId()); //设置店铺发布的商品ID
                    productSku.setProductApproveUuid(publishProductApproveVO.getUuid());
                    productSku.setBrankId(publishProductApproveVO.getBrandId()); //设置品牌ID

                    productSkus.add(productSku);

                }
                ret = shopProductApproveSkuService.saves(productSkus);

                if(ret< publishProductApproveVO.getShopProductApproveSkuVOList().size())
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

                    //保存商品主图
                    List<ShopProductApproveImg> shopProductImgs = new LinkedList<>();
                    ShopProductApproveImg shopProductImg = new ShopProductApproveImg();
                    shopProductImg.setId(idGenerator.id());
                    shopProductImg.setAppCode(publishProductApproveVO.getAppCode());
                    shopProductImg.setCreateDate(new Date());
                    shopProductImg.setCreateUserId(publishProductApproveVO.getCreateUserId());
                    shopProductImg.setDeleteStatus(0);
                    shopProductImg.setIsMainPhoto((short) 1);
                    shopProductImg.setProductApproveId(publishProductApproveVO.getId());
                    shopProductImg.setFilePath(publishProductApproveVO.getMainPhotoFilePath());
                    shopProductImgs.add(shopProductImg);

                    //保存预览图
                    if (CollectionUtils.isNotEmpty(publishProductApproveVO.getPreviewPhotoPaths())) {
                        for (String pewviewPhotoPath : publishProductApproveVO.getPreviewPhotoPaths()) {
                            ShopProductApproveImg productImg = new ShopProductApproveImg();
                            productImg.setId(idGenerator.id());
                            productImg.setAppCode(publishProductApproveVO.getAppCode());
                            productImg.setCreateDate(new Date());
                            productImg.setCreateUserId(publishProductApproveVO.getCreateUserId());
                            productImg.setDeleteStatus(0);
                            productImg.setIsMainPhoto((short) 0);
                            productImg.setProductApproveId(publishProductApproveVO.getId());
                            productImg.setFilePath(pewviewPhotoPath);

                            shopProductImgs.add(productImg);
                        }

                        ret = shopProductApproveImgService.saves(shopProductImgs);
                        if (ret <= publishProductApproveVO.getPreviewPhotoPaths().size()) {
                            logger.warn("发布商品失败 原因:商品图片影响返回行和保存数量不一致 {}", JSONObject.toJSONString(shopProductImgs));
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg("发布失败");

                            ret = shopProductApproveService.deleteById(publishProductApproveVO.getId());
                            if (ret <= 0) {
                                //发送异常邮件,通知运营处理
                                logger.warn("发布商品失败 回滚店铺商品表失败 id {}", publishProductApproveVO.getId());
                            }

                            ret = shopProductApproveSkuService.deleteByShopProductApproveId(publishProductApproveVO.getId());
                            if (ret< publishProductApproveVO.getShopProductApproveSkuVOList().size()) {
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
                shopProductVO.setShopProductApproveSkuVOList(productSkuVOS);

                //查询商品图片
                ShopProductApproveImgVO shopProductImgVO = new ShopProductApproveImgVO();
                shopProductImgVO.setProductApproveId(shopProductVO.getId());
                List<ShopProductApproveImg> shopProductImgs = shopProductApproveImgService.queryList(shopProductImgVO);
                if (CollectionUtils.isNotEmpty(shopProductImgs)) {
                    for (ShopProductApproveImg shopProductImg : shopProductImgs) {
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
            shopProductApproveService.updateApproveStatus(shopProductApproveRecordVO.getApproveId(),ProductConstant.REJECT);
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
        try{
            ShopProductApproveVO shopProductVO = requestJsonVO.formatEntity(ShopProductApproveVO.class);
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
            shopProductApproveService.updateApproveStatusAndProductId(shopProductVO.getId(),ProductConstant.PASS,shopProductVO.getProductId(),shopProductVO.getProductUuid());
            ShopProductApproveRecord shopProductApproveRecord = new ShopProductApproveRecord();
            shopProductApproveRecord.setApproveText("审核通过");
            shopProductApproveRecord.setApproveStatus(ProductConstant.REJECT);
            shopProductApproveRecord.setCreateAdminId(shopProductVO.getCreateAdminId());
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



}
