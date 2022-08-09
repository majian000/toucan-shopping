package com.toucan.shopping.modules.column.mapper;

import com.toucan.shopping.modules.column.entity.ColumnArea;
import com.toucan.shopping.modules.column.vo.ColumnAreaVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ColumnAreaMapper {

    List<ColumnArea> queryList(ColumnArea entity);


    int inserts(List<ColumnArea> entitys);


    int deleteByColumnId(Long columnId);

    List<ColumnAreaVO> queryListByColumnId(Long columnId);

    List<ColumnAreaVO> queryListByColumnIds(List<Long> columnIds);
}
