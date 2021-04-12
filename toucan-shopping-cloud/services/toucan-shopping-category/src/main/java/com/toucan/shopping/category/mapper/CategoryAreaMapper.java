package com.toucan.shopping.category.mapper;

import com.toucan.shopping.category.entity.Category;
import com.toucan.shopping.category.entity.CategoryArea;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface CategoryAreaMapper {

    List<CategoryArea> queryList(CategoryArea categoryArea);


    int insert(CategoryArea categoryArea);

    CategoryArea queryById(Long id);


    int deleteById(Long id);

    List<CategoryArea> queryListByAreaCode(String areaCode);

}
