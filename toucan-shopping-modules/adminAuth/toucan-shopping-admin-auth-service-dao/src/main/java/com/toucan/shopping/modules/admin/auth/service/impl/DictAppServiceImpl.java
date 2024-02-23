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
    private DictAppMapper dictCategoryAppMapper;

    @Override
    public List<DictApp> findListByEntity(DictApp entity) {
        return dictCategoryAppMapper.findListByEntity(entity);
    }

    @Override
    public int save(DictApp entity) {
        return dictCategoryAppMapper.insert(entity);
    }

    @Override
    public int deleteByAppCode(String appCode) {
        return dictCategoryAppMapper.deleteByAppCode(appCode);
    }

    @Override
    public int deleteByDictId(Long dictId) {
        return dictCategoryAppMapper.deleteByDictId(dictId);
    }


}
