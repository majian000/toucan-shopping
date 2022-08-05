package com.toucan.shopping.modules.column.service.impl;

import com.toucan.shopping.modules.column.entity.ColumnRecommendLabel;
import com.toucan.shopping.modules.column.entity.ColumnRecommendProduct;
import com.toucan.shopping.modules.column.mapper.ColumnRecommendLabelMapper;
import com.toucan.shopping.modules.column.mapper.ColumnRecommendProductMapper;
import com.toucan.shopping.modules.column.service.ColumnRecommendLabelService;
import com.toucan.shopping.modules.column.service.ColumnRecommendProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnRecommendProductServiceImpl implements ColumnRecommendProductService {

    @Autowired
    private ColumnRecommendProductMapper columnRecommendProductMapper;

    @Override
    public int saves(List<ColumnRecommendProduct> entitys) {
        return columnRecommendProductMapper.inserts(entitys);
    }


    @Override
    public int deleteByColumnId(Long columnId) {
        return columnRecommendProductMapper.deleteByColumnId(columnId);
    }
}
