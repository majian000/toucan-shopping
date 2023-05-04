package com.toucan.shopping.modules.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.product.service.ProductSkuRedisService;
import com.toucan.shopping.modules.product.constant.ProductConstant;
import com.toucan.shopping.modules.product.redis.ProductSkuRedisKey;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class ProductSkuRedisServiceImpl implements ProductSkuRedisService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Override
    public ProductSkuVO queryProductSkuById(String id) {
        Object shopProductSkuObject = toucanStringRedisService.get(ProductSkuRedisKey.getPreviewSkuKeyBySkuId(id));
        if(shopProductSkuObject!=null) {
            return JSONObject.parseObject(String.valueOf(shopProductSkuObject),ProductSkuVO.class);
        }
        return null;
    }

    @Override
    public void addToCache(ProductSkuVO shopProductSkuVO) {
        toucanStringRedisService.set(ProductSkuRedisKey.getPreviewSkuKeyBySkuId(String.valueOf(shopProductSkuVO.getId())),JSONObject.toJSONString(shopProductSkuVO), ProductConstant.PRODUCT_APPROVE_SKU_REDIS_MAX_AGE, TimeUnit.SECONDS);
    }

    @Override
    public void deleteCache(String id) {
        try {
            Object shopProductSkuObject = toucanStringRedisService.get(ProductSkuRedisKey.getPreviewSkuKeyBySkuId(id));
            if(shopProductSkuObject!=null) {
                toucanStringRedisService.delete(ProductSkuRedisKey.getPreviewSkuKeyBySkuId(String.valueOf(id)));
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }
}
