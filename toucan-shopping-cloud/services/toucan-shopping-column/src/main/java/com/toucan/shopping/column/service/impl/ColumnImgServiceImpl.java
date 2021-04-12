package com.toucan.shopping.column.service.impl;

import com.toucan.shopping.column.mapper.ColumnImgMapper;
import com.toucan.shopping.column.mapper.ColumnMapper;
import com.toucan.shopping.column.service.ColumnImgService;
import com.toucan.shopping.column.service.ColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ColumnImgServiceImpl implements ColumnImgService {

    @Autowired
    private ColumnImgMapper columnImgMapper;


}
