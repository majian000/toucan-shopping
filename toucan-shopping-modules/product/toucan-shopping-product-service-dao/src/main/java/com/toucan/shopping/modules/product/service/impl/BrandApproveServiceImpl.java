package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.BrandApprove;
import com.toucan.shopping.modules.product.mapper.BrandApproveMapper;
import com.toucan.shopping.modules.product.mapper.BrandMapper;
import com.toucan.shopping.modules.product.service.BrandApproveService;
import com.toucan.shopping.modules.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandApproveServiceImpl implements BrandApproveService {

    @Autowired
    private BrandApproveMapper brandApproveMapper;

    @Override
    public List<BrandApprove> queryAllList(BrandApprove queryModel) {
        return brandApproveMapper.queryAllList(queryModel);
    }
}
