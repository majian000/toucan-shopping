package com.toucan.shopping.modules.category.mapper;

import com.toucan.shopping.modules.category.entity.CategoryImg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface CategoryImgMapper {

    List<CategoryImg> queryList(CategoryImg entity);


    int insert(CategoryImg entity);

    CategoryImg queryById(Long id);


    int deleteById(Long id);

    List<CategoryImg> queryListByCategoryId(Long categoryId);

}
