package com.toucan.shopping.modules.column.service;

import com.toucan.shopping.modules.column.entity.ColumnRecommendLabel;
import com.toucan.shopping.modules.column.entity.ColumnRecommendProduct;

import java.util.List;

public interface ColumnRecommendProductService {

    int saves(List<ColumnRecommendProduct> entitys);

}
