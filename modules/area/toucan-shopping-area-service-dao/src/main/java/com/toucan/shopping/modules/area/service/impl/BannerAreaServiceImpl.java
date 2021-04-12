package com.toucan.shopping.modules.area.service.impl;

import com.toucan.shopping.modules.area.entity.Banner;
import com.toucan.shopping.modules.area.entity.BannerArea;
import com.toucan.shopping.modules.area.mapper.BannerAreaMapper;
import com.toucan.shopping.modules.area.mapper.BannerMapper;
import com.toucan.shopping.modules.area.service.BannerAreaService;
import com.toucan.shopping.modules.area.service.BannerService;
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
    public int deleteByBannerId(Long bannerId) {
        return bannerAreaMapper.deleteByBannerId(bannerId);
    }


}
