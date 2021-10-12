package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.BrandFile;
import com.toucan.shopping.modules.product.mapper.BrandFileMapper;
import com.toucan.shopping.modules.product.mapper.BrandMapper;
import com.toucan.shopping.modules.product.service.BrandFileService;
import com.toucan.shopping.modules.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 品牌证明材料表
 */
@Service
public class BrandFileServiceImpl implements BrandFileService {

    @Autowired
    private BrandFileMapper brandFileMapper;

    @Override
    public List<BrandFile> queryAllList(BrandFile queryModel) {
        return brandFileMapper.queryAllList(queryModel);
    }
}
