package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.mapper.BrandMapper;
import com.toucan.shopping.modules.product.service.BrandService;
import com.toucan.shopping.modules.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<Brand> queryAllList(Brand queryModel) {
        return brandMapper.queryAllList(queryModel);
    }



    @Override
    public int save(Brand entity) {
        return brandMapper.insert(entity);
    }



}
