package com.toucan.shopping.modules.content.service.impl;

import com.toucan.shopping.modules.content.entity.Banner;
import com.toucan.shopping.modules.content.entity.BannerArea;
import com.toucan.shopping.modules.content.mapper.BannerAreaMapper;
import com.toucan.shopping.modules.content.mapper.BannerMapper;
import com.toucan.shopping.modules.content.service.BannerAreaService;
import com.toucan.shopping.modules.content.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerAreaServiceImpl implements BannerAreaService {

    @Autowired
    private BannerAreaMapper bannerAreaMapper;


    @Override
    public List<BannerArea> queryList(BannerArea entity) {
        return bannerAreaMapper.queryList(entity);
    }

    @Override
    public int save(BannerArea entity) {
        return bannerAreaMapper.insert(entity);
    }

    @Override
    public int saves(BannerArea[] entitys) {
        return bannerAreaMapper.inserts(entitys);
    }

    @Override
    public int deleteByBannerId(Long bannerId) {
        return bannerAreaMapper.deleteByBannerId(bannerId);
    }


}
