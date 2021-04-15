package com.toucan.shopping.modules.column.service.impl;

import com.toucan.shopping.modules.column.mapper.ColumnImgMapper;
import com.toucan.shopping.modules.column.service.ColumnImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColumnImgServiceImpl implements ColumnImgService {

    @Autowired
    private ColumnImgMapper columnImgMapper;


}
