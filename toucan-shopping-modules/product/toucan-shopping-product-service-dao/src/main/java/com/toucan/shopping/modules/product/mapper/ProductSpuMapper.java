package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ProductSpu;
import com.toucan.shopping.modules.product.vo.ProductSpuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ProductSpuMapper {

    List<ProductSpu> queryAllList(ProductSpu queryModel);

    int insert(ProductSpu entity);

    List<ProductSpuVO> queryList(ProductSpuVO query);

}
