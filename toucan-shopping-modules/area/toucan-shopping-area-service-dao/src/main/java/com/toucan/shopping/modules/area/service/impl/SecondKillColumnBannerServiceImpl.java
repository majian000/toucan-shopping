package com.toucan.shopping.modules.area.service.impl;

import com.toucan.shopping.modules.area.entity.SecondKillColumnArea;
import com.toucan.shopping.modules.area.entity.SecondKillColumnBanner;
import com.toucan.shopping.modules.area.mapper.SecondKillColumnAreaMapper;
import com.toucan.shopping.modules.area.mapper.SecondKillColumnBannerMapper;
import com.toucan.shopping.modules.area.service.SecondKillColumnAreaService;
import com.toucan.shopping.modules.area.service.SecondKillColumnBannerService;
import com.toucan.shopping.modules.area.vo.SecondKillColumnAreaVO;
import com.toucan.shopping.modules.area.vo.SecondKillColumnBannerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecondKillColumnBannerServiceImpl implements SecondKillColumnBannerService {

    @Autowired
    private SecondKillColumnBannerMapper secondKillColumnBannerMapper;


    @Override
    public List<SecondKillColumnBanner> queryList(SecondKillColumnBannerVO entity) {
        return secondKillColumnBannerMapper.queryList(entity);
    }

    @Override
    public int save(SecondKillColumnBanner entity) {
        return secondKillColumnBannerMapper.insert(entity);
    }

    @Override
    public int deleteById(Long id) {
        return secondKillColumnBannerMapper.deleteById(id);
    }
}
