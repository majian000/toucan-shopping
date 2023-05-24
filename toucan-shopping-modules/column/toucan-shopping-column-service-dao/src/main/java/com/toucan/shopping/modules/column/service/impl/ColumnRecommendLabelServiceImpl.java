package com.toucan.shopping.modules.column.service.impl;

import com.toucan.shopping.modules.column.entity.ColumnBanner;
import com.toucan.shopping.modules.column.entity.ColumnRecommendLabel;
import com.toucan.shopping.modules.column.mapper.ColumnBannerMapper;
import com.toucan.shopping.modules.column.mapper.ColumnRecommendLabelMapper;
import com.toucan.shopping.modules.column.service.ColumnBannerService;
import com.toucan.shopping.modules.column.service.ColumnRecommendLabelService;
import com.toucan.shopping.modules.column.vo.ColumnRecommendLabelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnRecommendLabelServiceImpl implements ColumnRecommendLabelService {

    @Autowired
    private ColumnRecommendLabelMapper columnRecommendLabelMapper;

    @Override
    public int saves(List<ColumnRecommendLabel> entitys) {
        return columnRecommendLabelMapper.inserts(entitys);
    }

    @Override
    public int deleteByColumnId(Long columnId) {
        return columnRecommendLabelMapper.deleteByColumnId(columnId);
    }

    @Override
    public List<ColumnRecommendLabelVO> queryListByColumnId(Long columnId) {
        return columnRecommendLabelMapper.queryListByColumnId(columnId);
    }

    @Override
    public List<ColumnRecommendLabelVO> queryListByColumnIds(List<Long> columnIds) {
        return columnRecommendLabelMapper.queryListByColumnIds(columnIds);
    }
}
