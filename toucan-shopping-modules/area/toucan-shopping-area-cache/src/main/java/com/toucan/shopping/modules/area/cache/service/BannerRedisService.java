package com.toucan.shopping.modules.area.cache.service;

import com.toucan.shopping.modules.area.vo.BannerVO;

public interface BannerRedisService {

    void flush(BannerVO bannerVO);

}

