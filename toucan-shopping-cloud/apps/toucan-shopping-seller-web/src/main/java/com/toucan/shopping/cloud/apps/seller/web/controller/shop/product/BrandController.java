package com.toucan.shopping.cloud.apps.seller.web.controller.shop.product;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.vo.selectPage.SelectPageTableVO;
import com.toucan.shopping.cloud.category.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.page.BrandPageInfo;
import com.toucan.shopping.modules.product.vo.BrandVO;
import com.toucan.shopping.modules.product.vo.ui.SelectPageBrandVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 品牌管理
 */
@Controller
@RequestMapping("/product/brand")
public class BrandController  {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignBrandService feignBrandService;

    @Autowired
    private FeignCategoryService feignCategoryService;

    @Autowired
    private FeignBrandCategoryService feignBrandCategoryService;




    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public SelectPageTableVO list(BrandPageInfo pageInfo)
    {
        SelectPageTableVO selectPageTableVO = new SelectPageTableVO();
        try {
            if(StringUtils.isNotEmpty(pageInfo.getQ_word())) {
                pageInfo.setName(pageInfo.getQ_word());
            }
            pageInfo.setLimit(pageInfo.getPageSize());
            pageInfo.setPage(pageInfo.getPageNumber());
            selectPageTableVO.getValues().getGridResult().setPageNumber(pageInfo.getPageNumber());
            selectPageTableVO.getValues().getGridResult().setPageSize(pageInfo.getPageSize());

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignBrandService.queryListPage(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    selectPageTableVO.getValues().getGridResult().setTotalRow(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    selectPageTableVO.getValues().getGridResult().setPageNumber(pageInfo.getPage());
                    List<BrandVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),BrandVO.class);
                    List<SelectPageBrandVO> selectPageBrandVOS = new ArrayList<SelectPageBrandVO>();
                    if(selectPageTableVO.getValues().getGridResult().getTotalRow()>0) {
                        for(BrandVO brandVO:list)
                        {
                            SelectPageBrandVO selectPageBrandVO = new SelectPageBrandVO();
                            selectPageBrandVO.setId(brandVO.getId());
                            if(StringUtils.isNotEmpty(brandVO.getChineseName())&&StringUtils.isNotEmpty(brandVO.getEnglishName()))
                            {
                                selectPageBrandVO.setName(brandVO.getChineseName()+"/"+brandVO.getEnglishName());
                            }else {
                                if (StringUtils.isNotEmpty(brandVO.getChineseName())) {
                                    selectPageBrandVO.setName(brandVO.getChineseName());
                                }
                                if (StringUtils.isNotEmpty(brandVO.getEnglishName())) {
                                    selectPageBrandVO.setName(brandVO.getEnglishName());
                                }
                            }
                            selectPageBrandVOS.add(selectPageBrandVO);
                        }
                    }
                    selectPageTableVO.getValues().getGridResult().setList((List)selectPageBrandVOS);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return selectPageTableVO;
    }




}

