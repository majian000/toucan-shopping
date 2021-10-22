package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.Brand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BrandMapper {

    List<Brand> queryAllList(Brand queryModel);

    int insert(Brand entity);
}
