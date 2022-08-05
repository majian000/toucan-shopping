package com.toucan.shopping.modules.column.service.impl;

import com.toucan.shopping.modules.column.entity.ColumnBanner;
import com.toucan.shopping.modules.column.mapper.ColumnAreaMapper;
import com.toucan.shopping.modules.column.mapper.ColumnBannerMapper;
import com.toucan.shopping.modules.column.service.ColumnAreaService;
import com.toucan.shopping.modules.column.service.ColumnBannerService;
import com.toucan.shopping.modules.column.vo.ColumnBannerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnBannerServiceImpl implements ColumnBannerService {

    @Autowired
    private ColumnBannerMapper columnBannerMapper;


    @Override
    public int saves(List<ColumnBanner> entitys) {
        return columnBannerMapper.inserts(entitys);
    }

    @Override
    public int deleteByColumnId(Long columnId) {
        return columnBannerMapper.deleteByColumnId(columnId);
    }
}
