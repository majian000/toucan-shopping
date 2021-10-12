package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.BrandCategory;
import com.toucan.shopping.modules.product.mapper.BrandCategoryMapper;
import com.toucan.shopping.modules.product.mapper.BrandMapper;
import com.toucan.shopping.modules.product.service.BrandCategoryService;
import com.toucan.shopping.modules.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandCategoryServiceImpl implements BrandCategoryService {

    @Autowired
    private BrandCategoryMapper brandCategoryMapper;

    @Override
    public List<BrandCategory> queryAllList(BrandCategory queryModel) {
        return brandCategoryMapper.queryAllList(queryModel);
    }
}
