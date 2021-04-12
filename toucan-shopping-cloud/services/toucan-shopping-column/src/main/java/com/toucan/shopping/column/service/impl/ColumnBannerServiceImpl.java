package com.toucan.shopping.column.service.impl;

import com.toucan.shopping.column.mapper.ColumnAreaMapper;
import com.toucan.shopping.column.mapper.ColumnBannerMapper;
import com.toucan.shopping.column.service.ColumnAreaService;
import com.toucan.shopping.column.service.ColumnBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColumnBannerServiceImpl implements ColumnBannerService {

    @Autowired
    private ColumnBannerMapper columnBannerMapper;


}
