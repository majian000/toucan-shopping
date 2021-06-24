package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.AttributeKey;
import com.toucan.shopping.modules.product.page.AttributeKeyPageInfo;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;

import java.util.List;

public interface AttributeKeyService {

    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<AttributeKeyVO> queryListPage(AttributeKeyPageInfo queryPageInfo);



    /**
     * 保存实体
     * @param attributeKey
     * @return
     */
    int save(AttributeKey attributeKey);


    /**
     * 修改实体
     * @param attributeKey
     * @return
     */
    int update(AttributeKey attributeKey);


    int deleteById(Long id);



    List<AttributeKeyVO> queryList(AttributeKeyVO query);

}
