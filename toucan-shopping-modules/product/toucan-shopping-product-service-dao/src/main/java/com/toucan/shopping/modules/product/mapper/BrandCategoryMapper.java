package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.BrandCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BrandCategoryMapper {

    List<BrandCategory> queryAllList(BrandCategory queryModel);


    int insert(BrandCategory entity);


}
