package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.AttributeKey;
import com.toucan.shopping.modules.product.entity.ProductSpuAttributeKey;
import com.toucan.shopping.modules.product.page.AttributeKeyPageInfo;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface ProductSpuAttributeKeyService {


    /**
     * 保存实体
     * @param entity
     * @return
     */
    int save(ProductSpuAttributeKey entity);


    /**
     * 保存实体
     * @param entitys
     * @return
     */
    int saves(List<ProductSpuAttributeKey> entitys);


    /**
     * 根据ID删除
     * @param productSpuId
     * @return
     */
    int deleteByProductSpuId(Long productSpuId);




}
