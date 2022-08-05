package com.toucan.shopping.modules.column.service.impl;

import com.toucan.shopping.modules.column.entity.ColumnArea;
import com.toucan.shopping.modules.column.mapper.ColumnAreaMapper;
import com.toucan.shopping.modules.column.service.ColumnAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnAreaServiceImpl implements ColumnAreaService {

    @Autowired
    private ColumnAreaMapper columnAreaMapper;


    @Override
    public List<ColumnArea> queryList(ColumnArea columnArea) {
        return null;
    }

    @Override
    public int saves(List<ColumnArea> entitys) {
        return columnAreaMapper.inserts(entitys);
    }
}
