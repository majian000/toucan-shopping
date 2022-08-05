package com.toucan.shopping.modules.column.service;

import com.toucan.shopping.modules.column.entity.ColumnArea;

import java.util.List;

public interface ColumnAreaService {

    List<ColumnArea> queryList(ColumnArea columnArea);


    int saves(List<ColumnArea> entitys);


    int deleteByColumnId(Long columnId);

}
