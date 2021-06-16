package com.toucan.shopping.modules.category.cache.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.category.cache.service.CategoryRedisService;
import com.toucan.shopping.modules.category.constant.CategoryRedisKey;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CategoryRedisServiceImpl implements CategoryRedisService {

    @Autowired
    @Qualifier("categoryCacheRedisTemplate")
    private RedisTemplate redisTemplate;


    @Override
    public void flushWebIndexCaches(List<CategoryVO> categoryVOS) {
        Object bannersRedisObject = redisTemplate.opsForValue().get(CategoryRedisKey.getWebIndexKey());
        if(bannersRedisObject!=null) {
            //先删除已有缓存
            redisTemplate.delete(CategoryRedisKey.getWebIndexKey());
        }
        //保存到redis
        redisTemplate.opsForValue().set(CategoryRedisKey.getWebIndexKey(), JSONObject.toJSONString(categoryVOS));
    }

    @Override
    public List<CategoryVO> queryWebIndexCache() {
        Object bannersRedisObject = redisTemplate.opsForValue().get(CategoryRedisKey.getWebIndexKey());
        List<CategoryVO> categoryVOList=null;
        if(bannersRedisObject!=null) {
            categoryVOList=JSONArray.parseArray(String.valueOf(bannersRedisObject), CategoryVO.class);
        }else{
            categoryVOList=new ArrayList<CategoryVO>();
        }
        return categoryVOList;
    }

    @Override
    public boolean clearWebIndexCache() {
        return redisTemplate.delete(CategoryRedisKey.getWebIndexKey());
    }


}
