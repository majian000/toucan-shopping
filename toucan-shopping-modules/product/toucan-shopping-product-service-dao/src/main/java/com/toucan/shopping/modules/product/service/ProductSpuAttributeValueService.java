package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.AttributeValue;
import com.toucan.shopping.modules.product.entity.ProductSpuAttributeValue;
import com.toucan.shopping.modules.product.page.AttributeValuePageInfo;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;
import com.toucan.shopping.modules.product.vo.ProductSpuAttributeKeyValueTreeVO;
import com.toucan.shopping.modules.product.vo.ProductSpuAttributeKeyValueVO;
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

    /**
     * 查询属性树
     * @param allList
     * @param parentAttributeTree
     */
    void queryAttributeTree(List<ProductSpuAttributeKeyValueVO> allList, ProductSpuAttributeKeyValueTreeVO parentAttributeTree);



    /**
     * 刷新显示状态和搜索状态
     * @param attributeValueId
     * @param showStatus
     * @param queryStatus
     * @return
     */
    int updateShowStatusAndSearchStatus(Long attributeValueId,String attributeValueName,Short showStatus,Short queryStatus);



}
