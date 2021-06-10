package com.toucan.shopping.modules.area.service.impl;

import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.area.entity.Banner;
import com.toucan.shopping.modules.area.mapper.AreaMapper;
import com.toucan.shopping.modules.area.mapper.BannerMapper;
import com.toucan.shopping.modules.area.page.BannerPageInfo;
import com.toucan.shopping.modules.area.service.AreaService;
import com.toucan.shopping.modules.area.service.BannerService;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.area.vo.BannerVO;
import com.toucan.shopping.modules.common.page.PageInfo;
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

    @Override
    public int update(Banner banner) {
        return bannerMapper.update(banner);
    }

    @Override
    public PageInfo<BannerVO> queryListPage(BannerPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<BannerVO> pageInfo = new PageInfo();
        pageInfo.setList(bannerMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(bannerMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }


}
