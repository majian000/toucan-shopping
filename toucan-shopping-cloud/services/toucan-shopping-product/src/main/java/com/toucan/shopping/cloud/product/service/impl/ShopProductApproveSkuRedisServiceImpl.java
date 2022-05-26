package com.toucan.shopping.cloud.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.product.service.ShopProductApproveSkuRedisService;
import com.toucan.shopping.modules.product.constant.ProductConstant;
import com.toucan.shopping.modules.product.redis.ProductApproveSkuRedisKey;
import com.toucan.shopping.modules.product.vo.ShopProductApproveSkuVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class ShopProductApproveSkuRedisServiceImpl implements ShopProductApproveSkuRedisService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Override
    public ShopProductApproveSkuVO queryProductApproveSku(String id) {
        Object shopProductApproveSkuObject = toucanStringRedisService.get(ProductApproveSkuRedisKey.getPreviewSkuKey(id));
        if(shopProductApproveSkuObject!=null) {
            return JSONObject.parseObject(String.valueOf(shopProductApproveSkuObject),ShopProductApproveSkuVO.class);
        }
        return null;
    }

    @Override
    public void addToCache(ShopProductApproveSkuVO shopProductApproveSkuVO) {
        toucanStringRedisService.set(ProductApproveSkuRedisKey.getPreviewSkuKey(String.valueOf(shopProductApproveSkuVO.getId())),JSONObject.toJSONString(shopProductApproveSkuVO), ProductConstant.PRODUCT_APPROVE_SKU_REDIS_MAX_AGE, TimeUnit.SECONDS);
    }

    @Override
    public void deleteCache(String id) {
        try {
            Object shopProductApproveSkuObject = toucanStringRedisService.get(ProductApproveSkuRedisKey.getPreviewSkuKey(id));
            if(shopProductApproveSkuObject!=null) {
                toucanStringRedisService.delete(ProductApproveSkuRedisKey.getPreviewSkuKey(String.valueOf(id)));
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }
}
