package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.DictCategoryApp;
import com.toucan.shopping.modules.admin.auth.mapper.DictCategoryAppMapper;
import com.toucan.shopping.modules.admin.auth.service.DictCategoryAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictCategoryAppServiceImpl implements DictCategoryAppService {

    @Autowired
    private DictCategoryAppMapper dictCategoryAppMapper;

    @Override
    public List<DictCategoryApp> findListByEntity(DictCategoryApp entity) {
        return dictCategoryAppMapper.findListByEntity(entity);
    }

    @Override
    public int save(DictCategoryApp entity) {
        return dictCategoryAppMapper.insert(entity);
    }

    @Override
    public int deleteByAppCode(String appCode) {
        return dictCategoryAppMapper.deleteByAppCode(appCode);
    }

    @Override
    public int deleteByDictCategoryId(Integer dictCategoryId) {
        return dictCategoryAppMapper.deleteByDictCategoryId(dictCategoryId);
    }


    @Override
    public int deleteById(Integer id) {
        return dictCategoryAppMapper.deleteById(id);
    }
}
