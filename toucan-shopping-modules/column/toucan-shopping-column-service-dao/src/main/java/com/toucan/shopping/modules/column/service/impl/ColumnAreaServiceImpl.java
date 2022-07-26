package com.toucan.shopping.modules.column.service.impl;

import com.toucan.shopping.modules.column.entity.ColumnArea;
import com.toucan.shopping.modules.column.mapper.ColumnAreaMapper;
import com.toucan.shopping.modules.column.service.ColumnAreaService;
import com.toucan.shopping.modules.column.vo.ColumnAreaVO;
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


    @Override
    public int deleteByColumnId(Long columnId) {
        return columnAreaMapper.deleteByColumnId(columnId);
    }

    @Override
    public List<ColumnAreaVO> queryListByColumnId(Long columnId) {
        return columnAreaMapper.queryListByColumnId(columnId);
    }

    @Override
    public List<ColumnAreaVO> queryListByColumnIds(List<Long> columnIds) {
        return columnAreaMapper.queryListByColumnIds(columnIds);
    }
}
