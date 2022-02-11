package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.AttributeKey;
import com.toucan.shopping.modules.product.mapper.AttributeKeyMapper;
import com.toucan.shopping.modules.product.page.AttributeKeyPageInfo;
import com.toucan.shopping.modules.product.service.AttributeKeyService;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public int save(AttributeKey attributeKey) {
        return attributeKeyMapper.insert(attributeKey);
    }

    @Override
    public int update(AttributeKey attributeKey) {
        return attributeKeyMapper.update(attributeKey);
    }

    @Override
    public int deleteById(Long id) {
        return attributeKeyMapper.deleteById(id);
    }

    @Override
    public List<AttributeKeyVO> queryList(AttributeKeyVO query) {
        return attributeKeyMapper.queryList(query);
    }

    @Override
    public List<AttributeKeyVO> queryListBySortDesc(AttributeKeyVO query) {
        return attributeKeyMapper.queryListBySortDesc(query);
    }

}
