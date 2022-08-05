package com.toucan.shopping.modules.column.mapper;

import com.toucan.shopping.modules.column.entity.ColumnArea;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ColumnAreaMapper {

    List<ColumnArea> queryList(ColumnArea entity);


    int inserts(List<ColumnArea> entitys);


    int deleteByColumnId(Long columnId);

}
