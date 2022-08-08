package com.toucan.shopping.modules.column.service;

import com.toucan.shopping.modules.column.entity.ColumnArea;
import com.toucan.shopping.modules.column.vo.ColumnAreaVO;

import java.util.List;

public interface ColumnAreaService {

    List<ColumnArea> queryList(ColumnArea columnArea);


    int saves(List<ColumnArea> entitys);


    int deleteByColumnId(Long columnId);

    List<ColumnAreaVO> queryListByColumnId(Long columnId);

}
