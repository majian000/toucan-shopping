package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.OrgnazitionApp;
import com.toucan.shopping.modules.admin.auth.mapper.OrgnazitionAppMapper;
import com.toucan.shopping.modules.admin.auth.service.OrgnazitionAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrgnazitionAppServiceImpl implements OrgnazitionAppService {

    @Autowired
    private OrgnazitionAppMapper orgnazitionAppMapper;

    @Override
    public List<OrgnazitionApp> findListByEntity(OrgnazitionApp entity) {
        return orgnazitionAppMapper.findListByEntity(entity);
    }

    @Override
    public int save(OrgnazitionApp entity) {
        return orgnazitionAppMapper.insert(entity);
    }

    @Override
    public int deleteByAppCode(String appCode) {
        return orgnazitionAppMapper.deleteByAppCode(appCode);
    }

    @Override
    public int deleteByOrgnazitionId(String orgnazitionId) {
        return orgnazitionAppMapper.deleteByOrgnazitionId(orgnazitionId);
    }


}
