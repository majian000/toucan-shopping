package com.toucan.shopping.modules.column.service;

import com.toucan.shopping.modules.column.entity.ColumnBanner;
import com.toucan.shopping.modules.column.entity.ColumnRecommendLabel;
import com.toucan.shopping.modules.column.vo.ColumnRecommendLabelVO;

import java.util.List;

public interface ColumnRecommendLabelService {

    int saves(List<ColumnRecommendLabel> entitys);


    int deleteByColumnId(Long columnId);

    List<ColumnRecommendLabelVO> queryListByColumnId(Long columnId);

    List<ColumnRecommendLabelVO> queryListByColumnIds(List<Long> columnIds);

}
