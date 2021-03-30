package com.toucan.shopping.column.service.impl;

import com.toucan.shopping.column.mapper.ColumnMapper;
import com.toucan.shopping.column.mapper.ColumnSkuCategoryMapper;
import com.toucan.shopping.column.service.ColumnService;
import com.toucan.shopping.column.service.ColumnSkuCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ColumnSkuCategoryServiceImpl implements ColumnSkuCategoryService {

    @Autowired
    private ColumnSkuCategoryMapper columnSkuCategoryMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

}
