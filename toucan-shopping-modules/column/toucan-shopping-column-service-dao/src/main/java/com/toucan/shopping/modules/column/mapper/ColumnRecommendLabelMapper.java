package com.toucan.shopping.modules.column.mapper;

import com.toucan.shopping.modules.column.entity.ColumnBanner;
import com.toucan.shopping.modules.column.entity.ColumnRecommendLabel;
import com.toucan.shopping.modules.column.vo.ColumnRecommendLabelVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 栏目推荐标签
 */
@Mapper
public interface ColumnRecommendLabelMapper {


    int inserts(List<ColumnRecommendLabel> entitys);


    int deleteByColumnId(Long columnId);

    List<ColumnRecommendLabelVO> queryListByColumnId(Long columnId);

    List<ColumnRecommendLabelVO> queryListByColumnIds(List<Long> columnIds);

}
