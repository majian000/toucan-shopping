package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.AttributeValue;
import com.toucan.shopping.modules.product.entity.ProductSpuAttributeValue;
import com.toucan.shopping.modules.product.mapper.AttributeValueMapper;
import com.toucan.shopping.modules.product.mapper.ProductSpuAttributeValueMapper;
import com.toucan.shopping.modules.product.page.AttributeValuePageInfo;
import com.toucan.shopping.modules.product.service.AttributeValueService;
import com.toucan.shopping.modules.product.service.ProductSpuAttributeValueService;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;
import com.toucan.shopping.modules.product.vo.ProductSpuAttributeValueVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSpuAttributeValueServiceImpl implements ProductSpuAttributeValueService {


    @Autowired
    private ProductSpuAttributeValueMapper attributeValueMapper;


    @Override
    public int save(ProductSpuAttributeValue entity) {
        return attributeValueMapper.insert(entity);
    }

    @Override
    public int saves(List<ProductSpuAttributeValue> entitys) {
        return attributeValueMapper.inserts(entitys);
    }

    @Override
    public int deleteByProductSpuId(Long productSpuId) {
        return attributeValueMapper.deleteByProductSpuId(productSpuId);
    }

    @Override
    public List<ProductSpuAttributeValueVO> queryListBySortDesc(ProductSpuAttributeValueVO query) {
        return attributeValueMapper.queryListBySortDesc(query);
    }


}
