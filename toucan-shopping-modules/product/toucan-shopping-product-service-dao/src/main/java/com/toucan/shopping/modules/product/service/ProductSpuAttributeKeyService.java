package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.AttributeKey;
import com.toucan.shopping.modules.product.entity.ProductSpuAttributeKey;
import com.toucan.shopping.modules.product.page.AttributeKeyPageInfo;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import com.toucan.shopping.modules.product.vo.ProductSpuAttributeKeyVO;

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
     * 根据查询对象查询列表
     * @param query
     * @return
     */
    List<ProductSpuAttributeKeyVO> queryListBySortDesc(ProductSpuAttributeKeyVO query);

    /**
     * 根据ID删除
     * @param productSpuId
     * @return
     */
    int deleteByProductSpuId(Long productSpuId);


    /**
     * 刷新显示状态和搜索状态
     * @param attributeKeyId
     * @param showStatus
     * @param queryStatus
     * @return
     */
    int updateShowStatusAndSearchStatus(Long attributeKeyId,String attributeKeyName,Short showStatus,Short queryStatus);


}
