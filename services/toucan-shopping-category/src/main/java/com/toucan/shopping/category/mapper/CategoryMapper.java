package com.toucan.shopping.category.mapper;

import com.toucan.shopping.category.entity.Category;
import com.toucan.shopping.category.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface CategoryMapper {

    List<Category> queryList(CategoryVO category);


    int insert(Category category);

    Category queryById(Long id);

    List<Category> queryByParentId(Long parentId);

    int deleteById(Long id);

    int update(Category category);
}
