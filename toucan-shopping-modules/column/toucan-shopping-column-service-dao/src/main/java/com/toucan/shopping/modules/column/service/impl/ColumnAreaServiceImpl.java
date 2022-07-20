package com.toucan.shopping.modules.column.service.impl;

import com.toucan.shopping.modules.column.mapper.ColumnAreaMapper;
import com.toucan.shopping.modules.column.service.ColumnAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColumnAreaServiceImpl implements ColumnAreaService {

    @Autowired
    private ColumnAreaMapper columnAreaMapper;


}
