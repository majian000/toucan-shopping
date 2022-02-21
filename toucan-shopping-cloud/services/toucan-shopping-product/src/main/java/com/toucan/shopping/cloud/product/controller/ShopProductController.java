package com.toucan.shopping.cloud.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.entity.ShopProduct;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.redis.PublishProductRedisLockKey;
import com.toucan.shopping.modules.product.service.ProductSkuService;
import com.toucan.shopping.modules.product.service.ShopProductService;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.product.vo.PublishProductVO;
import com.toucan.shopping.modules.product.vo.ShopProductVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
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
                int ret = shopProductService.save(publishProductVO);

                if(ret<=0)
                {
                    logger.warn("发布商品失败 原因:插入数据库影响行数返回小于等于0 {}",requestJsonVO.getEntityJson());
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("发布失败!");
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
                    productSku.setShopId(publishProductVO.getShopId()); //设置店铺ID
                    productSku.setShopProductId(publishProductVO.getId()); //设置店铺发布的商品ID
                    productSku.setShopProductUuid(publishProductVO.getUuid());
                    productSku.setBrankId(publishProductVO.getBrandId()); //设置品牌ID

                    productSkus.add(productSku);

                }
                ret = productSkuService.saves(productSkus);

                if(ret!=productSkus.size())
                {
                    logger.warn("发布商品失败 原因:保存SKU影响返回行和保存数量不一致 {}",JSONObject.toJSONString(productSkus));
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("发布失败!");

                    ret = shopProductService.deleteById(publishProductVO.getId());
                    if(ret<=0)
                    {
                        //发送异常邮件,通知运营处理
                        logger.warn("发布商品失败 回滚店铺商品表失败 id {}",publishProductVO.getId());
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("发布失败!");
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
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到对象: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到对象!");
            return resultObjectVO;
        }
        try {
            ShopProductPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductPageInfo.class);
            PageInfo<ShopProductVO> pageInfo =  shopProductService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }




}
