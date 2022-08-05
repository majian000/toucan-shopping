package com.toucan.shopping.modules.column.mapper;

import com.toucan.shopping.modules.column.entity.ColumnBanner;
import com.toucan.shopping.modules.column.entity.ColumnRecommendLabel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 栏目推荐标签
 */
@Mapper
public interface ColumnRecommendLabelMapper {


    int inserts(List<ColumnRecommendLabel> entitys);

}
