package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.AttributeValue;
import com.toucan.shopping.modules.product.entity.ProductSpuAttributeValue;
import com.toucan.shopping.modules.product.page.AttributeValuePageInfo;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;
import com.toucan.shopping.modules.product.vo.ProductSpuAttributeValueVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ProductSpuAttributeValueMapper {


    int insert(ProductSpuAttributeValue productSpuAttributeValue);

    int inserts(List<ProductSpuAttributeValue> entitys);

    int deleteByProductSpuId(Long productSpuId);


    List<ProductSpuAttributeValueVO> queryListBySortDesc(ProductSpuAttributeValueVO entity);

}
