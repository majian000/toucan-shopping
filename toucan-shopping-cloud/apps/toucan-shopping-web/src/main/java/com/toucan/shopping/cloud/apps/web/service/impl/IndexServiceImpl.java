package com.toucan.shopping.cloud.apps.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.service.IndexService;
import com.toucan.shopping.cloud.apps.web.service.PayService;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignBannerService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignPcIndexColumnService;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.column.vo.PcIndexColumnVO;
import com.toucan.shopping.modules.content.cache.service.BannerRedisService;
import com.toucan.shopping.modules.content.vo.BannerVO;
import com.toucan.shopping.modules.category.cache.service.CategoryRedisService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.order.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private FeignAreaService feignAreaService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignBannerService feignBannerService;

    @Autowired
    private FeignCategoryService feignCategoryService;

    @Autowired
    private BannerRedisService bannerRedisService;

    @Autowired
    private CategoryRedisService categoryRedisService;

    @Autowired
    private FeignPcIndexColumnService feignPcIndexColumnService;

    /**
     * 查询轮播图
     */
    public List<BannerVO> queryBanners()
    {
        List<BannerVO> banners = null;
        try {
            banners = bannerRedisService.queryWebIndexBanner();
            if(CollectionUtils.isEmpty(banners)) {
                BannerVO bannerVO = new BannerVO();
                bannerVO.setStartShowDate(new Date());
                bannerVO.setEndShowDate(new Date());
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), bannerVO);
                ResultObjectVO resultObjectVO = feignBannerService.queryIndexList(SignUtil.sign(requestJsonVO), requestJsonVO);
                if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                    banners = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), BannerVO.class);
                    //批量刷新轮播图到缓存
                    bannerRedisService.flushWebIndexCaches(banners);
                    //重新查询一次
                    banners = bannerRedisService.queryWebIndexBanner();
                }else{
                    banners = new ArrayList<BannerVO>();
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);
            banners = new ArrayList<BannerVO>();
        }

        //TODO:增加判断 当前地区是否和轮播图的地区一致

        return banners;
    }


    /**
     * 查询类别列表
     */
    public List<CategoryVO> queryCategorys()
    {
        try {
            ResultObjectVO resultObjectVO = new ResultObjectVO();
            List<CategoryVO>  categoryVOS = categoryRedisService.queryWebIndexCache();
            if(!CollectionUtils.isEmpty(categoryVOS))
            {
                if(!CollectionUtils.isEmpty(categoryVOS))
                {
                    for(int i=0;i<categoryVOS.size();i++)
                    {
                        CategoryVO categoryTreeVO = categoryVOS.get(i);
                        categoryTreeVO.setPcIndexStyle("top:"+(0-(i*40))+"px"); //控制首页右侧面板位置置顶
                    }
                }
                return categoryVOS;
            }else {
                CategoryVO categoryVO = new CategoryVO();
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categoryVO);
                resultObjectVO = feignCategoryService.flushWebIndexCache(SignUtil.sign(requestJsonVO.getAppCode(), requestJsonVO.getEntityJson()), requestJsonVO);
                if (resultObjectVO.isSuccess()) {
                    List<CategoryVO> categoryVOList = categoryRedisService.queryWebIndexCache();
                    if(!CollectionUtils.isEmpty(categoryVOList))
                    {
                        for(int i=0;i<categoryVOList.size();i++)
                        {
                            CategoryVO categoryTreeVO = categoryVOList.get(i);
                            categoryTreeVO.setPcIndexStyle(String.valueOf(0-(i*40))+"px"); //控制首页右侧面板位置置顶
                        }
                    }
                    return categoryVOList;
                }else{
                    return new ArrayList<CategoryVO>();
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);

            return new ArrayList<CategoryVO>();
        }

    }


    /**
     * 查询类别列表
     */
    public List<CategoryVO> queryNavigationCategorys()
    {
        try {
            ResultObjectVO resultObjectVO = new ResultObjectVO();
            List<CategoryVO>  categoryVOS = categoryRedisService.queryWebNavigationCache();
            if(!CollectionUtils.isEmpty(categoryVOS))
            {
                if(!CollectionUtils.isEmpty(categoryVOS))
                {
                    for(int i=0;i<categoryVOS.size();i++)
                    {
                        CategoryVO categoryTreeVO = categoryVOS.get(i);
                        categoryTreeVO.setPcIndexStyle("top:"+(0-(i*40))+"px"); //控制首页右侧面板位置置顶
                    }
                }
                return categoryVOS;
            }else {
                CategoryVO categoryVO = new CategoryVO();
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categoryVO);
                resultObjectVO = feignCategoryService.flushNavigationMiniTreeCache(requestJsonVO);
                if (resultObjectVO.isSuccess()) {
                    List<CategoryVO> categoryVOList = categoryRedisService.queryWebNavigationCache();
                    if(!CollectionUtils.isEmpty(categoryVOList))
                    {
                        for(int i=0;i<categoryVOList.size();i++)
                        {
                            CategoryVO categoryTreeVO = categoryVOList.get(i);
                            categoryTreeVO.setPcIndexStyle(String.valueOf(0-(i*40))+"px"); //控制首页右侧面板位置置顶
                        }
                    }
                    return categoryVOList;
                }else{
                    return new ArrayList<CategoryVO>();
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);

            return new ArrayList<CategoryVO>();
        }

    }

    @Override
    public List<PcIndexColumnVO> queryColumns() {
        try {
            ColumnVO query = new ColumnVO();
            query.setAppCode(toucan.getAppCode());
            query.setShowStatus(1);
            query.setType(1);
            query.setPosition(1);
            query.setColumnTypeCode(toucan.getShoppingPC().getPcIndexColumnTypeCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), query);
            ResultObjectVO resultObjectVO = feignPcIndexColumnService.queryPcIndexColumns(requestJsonVO);
            return resultObjectVO.formatDataList(PcIndexColumnVO.class);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return new LinkedList<>();
        }
    }

}
