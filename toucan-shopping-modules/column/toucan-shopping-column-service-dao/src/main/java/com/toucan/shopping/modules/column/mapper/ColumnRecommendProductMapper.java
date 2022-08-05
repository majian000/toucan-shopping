package com.toucan.shopping.modules.column.mapper;

import com.toucan.shopping.modules.column.entity.ColumnRecommendLabel;
import com.toucan.shopping.modules.column.entity.ColumnRecommendProduct;
import com.toucan.shopping.modules.column.vo.ColumnRecommendProductVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 栏目推荐商品
 */
@Mapper
public interface ColumnRecommendProductMapper {


    int inserts(List<ColumnRecommendProduct> entitys);

    int deleteByColumnId(Long columnId);

    List<ColumnRecommendProductVO> queryListSortDescByColumnId(Long columnId);


}
