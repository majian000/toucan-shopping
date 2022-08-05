package com.toucan.shopping.modules.column.mapper;

import com.toucan.shopping.modules.column.entity.ColumnBanner;
import com.toucan.shopping.modules.column.vo.ColumnBannerVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ColumnBannerMapper {


    int inserts(List<ColumnBanner> entitys);

    int deleteByColumnId(Long columnId);

    List<ColumnBannerVO> queryListByColumnId(Long columnId);
}
