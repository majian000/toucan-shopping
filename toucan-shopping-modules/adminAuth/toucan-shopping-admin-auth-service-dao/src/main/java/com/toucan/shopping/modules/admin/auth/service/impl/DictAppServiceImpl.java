package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.DictApp;
import com.toucan.shopping.modules.admin.auth.mapper.DictAppMapper;
import com.toucan.shopping.modules.admin.auth.service.DictAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictAppServiceImpl implements DictAppService {

    @Autowired
    private DictAppMapper dictAppMapper;

    @Override
    public List<DictApp> findListByEntity(DictApp entity) {
        return dictAppMapper.findListByEntity(entity);
    }

    @Override
    public int save(DictApp entity) {
        return dictAppMapper.insert(entity);
    }

    @Override
    public int deleteByAppCode(String appCode) {
        return dictAppMapper.deleteByAppCode(appCode);
    }

    @Override
    public int deleteByDictId(Long dictId) {
        return dictAppMapper.deleteByDictId(dictId);
    }

    @Override
    public int deleteByCategoryId(Integer categoryId) {
        return dictAppMapper.deleteByCategoryId(categoryId);
    }


    @Override
    public int saves(List<DictApp> entitys) {
        return dictAppMapper.inserts(entitys);
    }
    @Override
    public int deleteByCategoryId(Integer categoryId, String appCode) {
        return dictAppMapper.deleteByCategoryIdAndAppCode(categoryId,appCode);
    }

}
