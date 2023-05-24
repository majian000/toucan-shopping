package com.toucan.shopping.modules.column.service;

import com.toucan.shopping.modules.column.entity.ColumnRecommendLabel;
import com.toucan.shopping.modules.column.entity.ColumnRecommendProduct;
import com.toucan.shopping.modules.column.vo.ColumnRecommendProductVO;

import java.util.List;

public interface ColumnRecommendProductService {

    int saves(List<ColumnRecommendProduct> entitys);


    int deleteByColumnId(Long columnId);

    List<ColumnRecommendProductVO> queryListCreateDateAscByColumnId(Long columnId);

    List<ColumnRecommendProductVO> queryListCreateDateAscByColumnIds(List<Long> columnIds);

}
