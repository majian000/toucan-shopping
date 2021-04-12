package com.toucan.shopping.modules.column.service.impl;

import com.toucan.shopping.modules.column.mapper.ColumnSkuCategoryMapper;
import com.toucan.shopping.modules.column.service.ColumnSkuCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColumnSkuCategoryServiceImpl implements ColumnSkuCategoryService {

    @Autowired
    private ColumnSkuCategoryMapper columnSkuCategoryMapper;


}
