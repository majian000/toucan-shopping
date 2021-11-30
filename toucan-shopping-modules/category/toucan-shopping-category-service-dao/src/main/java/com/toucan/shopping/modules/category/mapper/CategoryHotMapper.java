package com.toucan.shopping.modules.category.mapper;

import com.toucan.shopping.modules.category.entity.CategoryHot;
import com.toucan.shopping.modules.category.page.CategoryHotTreeInfo;
import com.toucan.shopping.modules.category.vo.CategoryHotVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface CategoryHotMapper {

    List<CategoryHot> queryList(CategoryHotVO categoryHot);

    int insert(CategoryHot categoryHot);
    /**
     * 查询表格树
     * @param queryTreeInfo
     * @return
     */
    List<CategoryHotVO> findTreeTableByPageInfo(CategoryHotTreeInfo queryTreeInfo);

    Long queryCount(CategoryHot categoryHot);

}
