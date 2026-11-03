package com.toucan.shopping.cloud.apps.h5.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.apps.h5.service.IndexService;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.cloud.content.api.BannerServiceAPI;
import com.toucan.shopping.cloud.content.api.HotProductServiceAPI;
import com.toucan.shopping.modules.category.cache.service.CategoryRedisService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.column.vo.HotProductVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.content.cache.service.BannerRedisService;
import com.toucan.shopping.modules.content.vo.BannerVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private BannerServiceAPI bannerService;

    @Autowired
    private CategoryServiceAPI categoryService;

    @Autowired
    private HotProductServiceAPI hotProductService;

    @Autowired
    private BannerRedisService bannerRedisService;

    @Autowired
    private CategoryRedisService categoryRedisService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Override
    public List<BannerVO> queryBanners() {
        List<BannerVO> banners;
        try {
            banners = bannerRedisService.queryWebIndexBanner();
            if (CollectionUtils.isEmpty(banners)) {
                BannerVO bannerVO = new BannerVO();
                bannerVO.setStartShowDate(new Date());
                bannerVO.setEndShowDate(new Date());
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), bannerVO);
                ResultObjectVO resultObjectVO = bannerService.queryIndexList(requestJsonVO);
                if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                    banners = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), BannerVO.class);
                    bannerRedisService.flushWebIndexCaches(banners);
                    banners = bannerRedisService.queryWebIndexBanner();
                } else {
                    banners = new ArrayList<>();
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            banners = new ArrayList<>();
        }
        return banners;
    }

    @Override
    public List<CategoryVO> queryCategorys() {
        try {
            List<CategoryVO> categoryVOS = categoryRedisService.queryWebIndexCache();
            if (!CollectionUtils.isEmpty(categoryVOS)) {
                return categoryVOS;
            }
            CategoryVO categoryVO = new CategoryVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categoryVO);
            ResultObjectVO resultObjectVO = categoryService.flushWebIndexCache(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                return categoryRedisService.queryWebIndexCache();
            }
            return new ArrayList<>();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<CategoryVO> queryNavigationCategorys() {
        try {
            List<CategoryVO> categoryVOS = categoryRedisService.queryWebNavigationCache();
            if (!CollectionUtils.isEmpty(categoryVOS)) {
                return categoryVOS;
            }
            CategoryVO categoryVO = new CategoryVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categoryVO);
            ResultObjectVO resultObjectVO = categoryService.flushNavigationMiniTreeCache(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                return categoryRedisService.queryWebNavigationCache();
            }
            return new ArrayList<>();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<HotProductVO> queryHotProduces() {
        try {
            HotProductVO query = new HotProductVO();
            query.setAppCode(toucan.getAppCode());
            query.setShowStatus(1);
            query.setType(1);
            query.setPosition(1);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), query);
            ResultObjectVO resultObjectVO = hotProductService.queryPcIndexHotProducts(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<HotProductVO> hotProductVOS = resultObjectVO.formatDataList(HotProductVO.class);
                if (!CollectionUtils.isEmpty(hotProductVOS)) {
                    for (HotProductVO hotProductVO : hotProductVOS) {
                        hotProductVO.setHttpImgPath(imageUploadService.getImageHttpPrefix() + hotProductVO.getImgPath());
                    }
                }
                return hotProductVOS;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }
}
