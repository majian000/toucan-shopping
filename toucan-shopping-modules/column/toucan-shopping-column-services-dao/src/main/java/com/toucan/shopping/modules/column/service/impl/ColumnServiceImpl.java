package com.toucan.shopping.modules.column.service.impl;

import com.toucan.shopping.modules.column.mapper.ColumnMapper;
import com.toucan.shopping.modules.column.service.ColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColumnServiceImpl implements ColumnService {

    @Autowired
    private ColumnMapper columnMapper;


}
