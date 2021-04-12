package com.toucan.shopping.column.service.impl;

import com.toucan.shopping.column.mapper.ColumnAreaMapper;
import com.toucan.shopping.column.mapper.ColumnMapper;
import com.toucan.shopping.column.service.ColumnAreaService;
import com.toucan.shopping.column.service.ColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ColumnAreaServiceImpl implements ColumnAreaService {

    @Autowired
    private ColumnAreaMapper columnAreaMapper;


}
