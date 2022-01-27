package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.AttributeValue;
import com.toucan.shopping.modules.product.mapper.AttributeValueMapper;
import com.toucan.shopping.modules.product.page.AttributeValuePageInfo;
import com.toucan.shopping.modules.product.service.AttributeValueService;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeValueServiceImpl implements AttributeValueService {


    @Autowired
    private AttributeValueMapper attributeValueMapper;


    @Override
    public PageInfo<AttributeValueVO> queryListPage(AttributeValuePageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<AttributeValueVO> pageInfo = new PageInfo();
        pageInfo.setList(attributeValueMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(attributeValueMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public int save(AttributeValue entity) {
        return attributeValueMapper.insert(entity);
    }

    @Override
    public int saves(List<AttributeValue> attributeValues) {
        return attributeValueMapper.inserts(attributeValues);
    }

    @Override
    public int update(AttributeValue entity) {
        return attributeValueMapper.update(entity);
    }

    @Override
    public int deleteById(Long id) {
        return attributeValueMapper.deleteById(id);
    }

    @Override
    public int deleteByAttributeKeyId(Long attributeKeyId) {
        return attributeValueMapper.deleteByAttributeKeyId(attributeKeyId);
    }
    @Override
    public List<AttributeValueVO> queryList(AttributeValueVO query) {
        return attributeValueMapper.queryList(query);
    }


}
