package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.mapper.AttributeKeyMapper;
import com.toucan.shopping.modules.product.page.AttributeKeyPageInfo;
import com.toucan.shopping.modules.product.service.AttributeKeyService;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeKeyServiceImpl implements AttributeKeyService {


    @Autowired
    private AttributeKeyMapper attributeKeyMapper;


    @Override
    public PageInfo<AttributeKeyVO> queryListPage(AttributeKeyPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<AttributeKeyVO> pageInfo = new PageInfo();
        pageInfo.setList(attributeKeyMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(attributeKeyMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }


}
