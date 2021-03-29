package com.toucan.shopping.area.service.impl;

import com.toucan.shopping.area.entity.Area;
import com.toucan.shopping.area.entity.Banner;
import com.toucan.shopping.area.mapper.AreaMapper;
import com.toucan.shopping.area.mapper.BannerMapper;
import com.toucan.shopping.area.service.AreaService;
import com.toucan.shopping.area.service.BannerService;
import com.toucan.shopping.area.vo.AreaVO;
import com.toucan.shopping.area.vo.BannerVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerMapper bannerMapper;


    @Override
    public List<Banner> queryList(BannerVO entity) {
        return bannerMapper.queryList(entity);
    }

    @Override
    public int save(Banner banner) {
        return bannerMapper.insert(banner);
    }

    @Override
    public int deleteById(Long id) {
        return bannerMapper.deleteById(id);
    }


}
