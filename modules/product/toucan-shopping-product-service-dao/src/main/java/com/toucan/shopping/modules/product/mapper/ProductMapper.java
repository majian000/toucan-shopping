package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ProductMapper {

    public List<Product> queryAllList(Product queryModel);

}
