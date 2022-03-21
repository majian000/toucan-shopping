package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.AttributeKey;
import com.toucan.shopping.modules.product.entity.ProductSpuAttributeKey;
import com.toucan.shopping.modules.product.page.AttributeKeyPageInfo;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import com.toucan.shopping.modules.product.vo.ProductSpuAttributeKeyVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ProductSpuAttributeKeyMapper {


    int insert(ProductSpuAttributeKey attributeKey);

    int inserts(List<ProductSpuAttributeKey> entitys);

    int deleteByProductSpuId(Long productSpuId);

    List<ProductSpuAttributeKeyVO> queryListBySortDesc(ProductSpuAttributeKeyVO entity);
}
