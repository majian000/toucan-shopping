package com.toucan.shopping.modules.content.cache.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.content.cache.service.BannerRedisService;
import com.toucan.shopping.modules.content.constant.BannerRedisKey;
import com.toucan.shopping.modules.content.vo.BannerVO;
import com.toucan.shopping.modules.common.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class BannerRedisServiceImpl implements BannerRedisService {

    @Autowired
    @Qualifier("contentCacheRedisTemplate")
    private RedisTemplate redisTemplate;

    @Override
    public void flushWebIndexCache(BannerVO bannerVO) {
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

    @Override
    public void flushWebIndexCaches(List<BannerVO> bannerVOS) {
        Object bannersRedisObject = redisTemplate.opsForValue().get(BannerRedisKey.getWebIndexBanner());
        List<BannerVO> bannerVOList=null;
        if(bannersRedisObject!=null) {
            bannerVOList=JSONArray.parseArray(String.valueOf(bannersRedisObject), BannerVO.class);
        }else{
            bannerVOList=new ArrayList<BannerVO>();
        }
        boolean find =false;
        //开始批量刷新或者新增
        for(int j=0;j<bannerVOS.size();j++) {
            BannerVO bannerVO = bannerVOS.get(j);
            //比较缓存中是否存在该对象,如果存在就刷新
            for(int i=0;i<bannerVOList.size();i++){
                if (bannerVO.getId().longValue() == bannerVOList.get(i).getId().longValue()) {
                    bannerVOList.set(i, bannerVO);
                    find = true;
                    break;
                }
            }
            if(!find)
            {
                bannerVOList.add(bannerVO);
            }
        }
        //保存轮播图到redis
        redisTemplate.opsForValue().set(BannerRedisKey.getWebIndexBanner(), JSONObject.toJSONString(bannerVOList));
    }

    @Override
    public List<BannerVO> queryWebIndexBanner() {
        Object bannersRedisObject = redisTemplate.opsForValue().get(BannerRedisKey.getWebIndexBanner());
        List<BannerVO> bannerVOList=null;
        if(bannersRedisObject!=null) {
            bannerVOList=JSONArray.parseArray(String.valueOf(bannersRedisObject), BannerVO.class);
        }else{
            bannerVOList=new ArrayList<BannerVO>();
        }
        List<BannerVO> aliveBanner = new ArrayList<BannerVO>();
        //过滤当前时间和显示状态
        for(BannerVO bannerVO:bannerVOList) {
            //过滤开始展示时间和结束展示时间
            if(bannerVO.getStartShowDate()!=null&&DateUtils.currentDate().getTime()>=bannerVO.getStartShowDate().getTime()) {
                if(bannerVO.getEndShowDate()!=null&&DateUtils.currentDate().getTime()<=bannerVO.getEndShowDate().getTime()) {
                    if(bannerVO.getShowStatus()!=null&&bannerVO.getShowStatus().shortValue()==1) {
                        aliveBanner.add(bannerVO);
                    }
                }
            }
        }
        BannerVO[] bannerVOArray = new BannerVO[aliveBanner.size()];
        for(int i=0;i<aliveBanner.size();i++)
        {
            bannerVOArray[i] = aliveBanner.get(i);
        }
        //进行排序
        for(int j=0;j<bannerVOArray.length;j++)
        {
            for(int p=j+1;p<bannerVOArray.length;p++)
            {
                //降序排列,值越大越在前面
                if(bannerVOArray[j].getBannerSort()!=null&&bannerVOArray[p].getBannerSort()!=null&&bannerVOArray[j].getBannerSort().intValue()<=bannerVOArray[p].getBannerSort().intValue()){
                    BannerVO tmp = bannerVOArray[j];
                    bannerVOArray[j] = bannerVOArray[p];
                    bannerVOArray[p] = tmp;
                }
            }
        }
        return Arrays.asList(bannerVOArray);
    }

    @Override
    public boolean clearWebIndexBanner() {
        return redisTemplate.delete(BannerRedisKey.getWebIndexBanner());
    }


}
