package com.toucan.shopping.modules.category.mapper;

import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.page.CategoryTreeInfo;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface CategoryMapper {

    List<Category> queryList(CategoryVO category);

    List<Category> queryPcIndexList(CategoryVO category);

    int insert(Category category);

    int inserts(Category[] entitys);

    Category queryById(Long id);

    List<Category> queryByParentId(Long parentId);

    int deleteById(Long id);

    int update(Category category);

    Long queryCount(Category category);

    /**
     * 查询表格树
     * @param queryTreeInfo
     * @return
     */
    List<CategoryVO> findTreeTableByPageInfo(CategoryTreeInfo queryTreeInfo);


}
