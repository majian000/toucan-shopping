package com.toucan.shopping.modules.area.cache.service;

import com.toucan.shopping.modules.area.vo.BannerVO;

import java.util.List;

public interface BannerRedisService {

    /**
     * 刷新缓存
     * @param bannerVO
     */
    void flush(BannerVO bannerVO);

    /**
     * 刷新缓存
     * @param bannerVOS
     */
    void flushs(List<BannerVO> bannerVOS);

    /**
     * 查询商城首页缓存
     * @return
     */
    List<BannerVO> queryWebIndexBanner();

}

