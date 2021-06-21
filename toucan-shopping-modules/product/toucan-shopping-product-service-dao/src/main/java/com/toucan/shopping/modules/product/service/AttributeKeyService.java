package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
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



}
