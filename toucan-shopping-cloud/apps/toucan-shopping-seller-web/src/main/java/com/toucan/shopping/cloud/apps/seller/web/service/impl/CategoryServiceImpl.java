package com.toucan.shopping.cloud.apps.seller.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.service.CategoryService;
import com.toucan.shopping.cloud.common.data.api.AreaServiceAPI;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.cloud.content.api.BannerServiceAPI;
import com.toucan.shopping.modules.category.cache.service.CategoryRedisService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import groovy.lang.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private AreaServiceAPI areaService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private BannerServiceAPI bannerService;

    @Autowired
    private CategoryServiceAPI categoryService;

    @Autowired
    private CategoryRedisService categoryRedisService;



    /**
     * 查询类别列表
     */
    public List<CategoryVO> queryMiniCategorys()
    {
        try {
            ResultObjectVO resultObjectVO = new ResultObjectVO();
            List<CategoryVO>  categoryVOS = categoryRedisService.queryMiniTree();
            if(!CollectionUtils.isEmpty(categoryVOS))
            {
                return categoryVOS;
            }else {
                CategoryVO categoryVO = new CategoryVO();
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categoryVO);
                resultObjectVO = categoryService.flushWMiniTreeCache(requestJsonVO);
                if (resultObjectVO.isSuccess()) {
                    return categoryRedisService.queryMiniTree();
                }else{
                    return new ArrayList<CategoryVO>();
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);

            return new ArrayList<CategoryVO>();
        }

    }


}
