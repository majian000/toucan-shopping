package com.toucan.shopping.modules.column.service.impl;

import com.toucan.shopping.modules.column.mapper.ColumnBannerMapper;
import com.toucan.shopping.modules.column.service.ColumnBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColumnBannerServiceImpl implements ColumnBannerService {

    @Autowired
    private ColumnBannerMapper columnBannerMapper;


}
