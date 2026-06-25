package com.toucan.shopping.cloud.product.api.single;

import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandCategoryService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.business.service.BrandCategoryBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandCategoryServiceSingleImpl implements FeignBrandCategoryService {

    @Autowired
    private BrandCategoryBusinessService brandCategoryBusinessService;

    @Override
    public ResultObjectVO findByBrandId(RequestJsonVO requestVo) {
        return brandCategoryBusinessService.findByBrandId(requestVo);
    }
}
