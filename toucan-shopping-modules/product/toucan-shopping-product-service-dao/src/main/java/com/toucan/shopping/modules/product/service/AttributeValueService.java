package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.AttributeValue;
import com.toucan.shopping.modules.product.page.AttributeValuePageInfo;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;

import java.util.List;

public interface AttributeValueService {

    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<AttributeValueVO> queryListPage(AttributeValuePageInfo queryPageInfo);



    /**
     * 保存实体
     * @param attributeValue
     * @return
     */
    int save(AttributeValue attributeValue);

    /**
     * 保存实体
     * @param attributeValues
     * @return
     */
    int saves(List<AttributeValue> attributeValues);

    /**
     * 修改实体
     * @param attributeValue
     * @return
     */
    int update(AttributeValue attributeValue);


    int deleteById(Long id);



    List<AttributeValueVO> queryList(AttributeValueVO query);


    List<AttributeValueVO> queryListBySortDesc(AttributeValueVO query);

    int deleteByAttributeKeyId(Long attributeKeyId);

}
