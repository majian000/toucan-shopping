package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.AttributeValue;
import com.toucan.shopping.modules.product.entity.ProductSpuAttributeValue;
import com.toucan.shopping.modules.product.page.AttributeValuePageInfo;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;

import java.util.List;

public interface ProductSpuAttributeValueService {




    /**
     * 保存实体
     * @param entity
     * @return
     */
    int save(ProductSpuAttributeValue entity);


}