package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.AttributeValue;
import com.toucan.shopping.modules.product.entity.ProductSpuAttributeValue;
import com.toucan.shopping.modules.product.page.AttributeValuePageInfo;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;
import com.toucan.shopping.modules.product.vo.ProductSpuAttributeValueVO;

import java.util.List;

public interface ProductSpuAttributeValueService {




    /**
     * 保存实体
     * @param entity
     * @return
     */
    int save(ProductSpuAttributeValue entity);

    /**
     * 保存实体
     * @param entitys
     * @return
     */
    int saves(List<ProductSpuAttributeValue> entitys);

    /**
     * 根据ID删除
     * @param productSpuId
     * @return
     */
    int deleteByProductSpuId(Long productSpuId);

    List<ProductSpuAttributeValueVO> queryListBySortDesc(ProductSpuAttributeValueVO query);
}
