package com.toucan.shopping.modules.content.service.impl;

import com.toucan.shopping.modules.content.entity.Banner;
import com.toucan.shopping.modules.content.mapper.BannerMapper;
import com.toucan.shopping.modules.content.page.BannerPageInfo;
import com.toucan.shopping.modules.content.service.BannerService;
import com.toucan.shopping.modules.content.vo.BannerVO;
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
    public List<BannerVO> queryList(BannerVO entity) {
        return bannerMapper.queryList(entity);
    }

    @Override
    public List<BannerVO> queryIndexList(BannerVO banner) {
        return bannerMapper.queryIndexList(banner);
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
