package com.toucan.shopping.modules.area.cache.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.area.cache.service.BannerRedisService;
import com.toucan.shopping.modules.area.constant.BannerRedisKey;
import com.toucan.shopping.modules.area.vo.BannerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BannerRedisServiceImpl implements BannerRedisService {

    @Autowired
    @Qualifier("areaCacheRedisTemplate")
    private RedisTemplate redisTemplate;


    @Override
    public void flush(BannerVO bannerVO) {
        Object bannersRedisObject = redisTemplate.opsForValue().get(BannerRedisKey.getWebIndexBanner());
        List<BannerVO> bannerVOList=null;
        if(bannersRedisObject!=null) {
            bannerVOList=JSONArray.parseArray(String.valueOf(bannersRedisObject), BannerVO.class);
        }else{
            bannerVOList=new ArrayList<BannerVO>();
        }
        boolean find =false;
        for(int i=0;i<bannerVOList.size();i++)
        {
            if(bannerVO.getId().longValue()==bannerVOList.get(i).getId().longValue()) {
                bannerVOList.set(i, bannerVO);
                find = true;
                break;
            }
        }
        if(!find)
        {
            bannerVOList.add(bannerVO);
        }
        //保存轮播图到redis
        redisTemplate.opsForValue().set(BannerRedisKey.getWebIndexBanner(), JSONObject.toJSONString(bannerVOList));
    }
}
