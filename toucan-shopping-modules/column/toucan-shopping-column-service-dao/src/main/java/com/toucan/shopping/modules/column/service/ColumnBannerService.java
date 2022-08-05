package com.toucan.shopping.modules.column.service;

import com.toucan.shopping.modules.column.entity.ColumnBanner;
import com.toucan.shopping.modules.column.vo.ColumnBannerVO;

import java.util.List;

public interface ColumnBannerService {

    int saves(List<ColumnBanner> entitys);


    int deleteByColumnId(Long columnId);

    List<ColumnBannerVO> queryListByColumnId(Long columnId);

}
