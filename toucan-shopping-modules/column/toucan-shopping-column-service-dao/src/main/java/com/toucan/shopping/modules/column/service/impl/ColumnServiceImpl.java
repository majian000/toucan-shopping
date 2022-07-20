package com.toucan.shopping.modules.column.service.impl;

import com.toucan.shopping.modules.column.entity.Column;
import com.toucan.shopping.modules.column.mapper.ColumnMapper;
import com.toucan.shopping.modules.column.service.ColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnServiceImpl implements ColumnService {

    @Autowired
    private ColumnMapper columnMapper;


    @Override
    public List<Column> queryAreaColumnList(String areaCode, Integer type, Integer position) {
        return columnMapper.queryAreaColumnList(areaCode,type,position);
    }
}
