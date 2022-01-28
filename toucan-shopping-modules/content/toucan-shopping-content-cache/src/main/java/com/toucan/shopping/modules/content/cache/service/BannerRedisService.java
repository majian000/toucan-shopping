package com.toucan.shopping.modules.content.cache.service;

import com.toucan.shopping.modules.content.vo.BannerVO;

import java.util.List;

public interface BannerRedisService {

    /**
     * 刷新缓存
     * @param bannerVO
     */
    void flushWebIndexCache(BannerVO bannerVO);

    /**
     * 刷新缓存
     * @param bannerVOS
     */
    void flushWebIndexCaches(List<BannerVO> bannerVOS);

    /**
     * 查询商城首页缓存
     * @return
     */
    List<BannerVO> queryWebIndexBanner();

    /**
     * 查询商城首页缓存
     * @return
     */
    boolean clearWebIndexBanner();

}

