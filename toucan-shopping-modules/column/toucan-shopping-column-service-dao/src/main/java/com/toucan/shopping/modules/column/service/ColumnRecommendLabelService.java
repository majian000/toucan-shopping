package com.toucan.shopping.modules.column.service;

import com.toucan.shopping.modules.column.entity.ColumnBanner;
import com.toucan.shopping.modules.column.entity.ColumnRecommendLabel;

import java.util.List;

public interface ColumnRecommendLabelService {

    int saves(List<ColumnRecommendLabel> entitys);

}
