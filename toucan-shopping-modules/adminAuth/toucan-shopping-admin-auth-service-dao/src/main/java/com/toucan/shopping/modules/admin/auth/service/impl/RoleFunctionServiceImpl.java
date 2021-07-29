package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.RoleFunction;
import com.toucan.shopping.modules.admin.auth.mapper.RoleFunctionMapper;
import com.toucan.shopping.modules.admin.auth.mapper.RoleFunctionMapper;
import com.toucan.shopping.modules.admin.auth.page.RoleFunctionPageInfo;
import com.toucan.shopping.modules.admin.auth.service.RoleFunctionService;
import com.toucan.shopping.modules.admin.auth.service.RoleFunctionService;
import com.toucan.shopping.modules.common.page.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleFunctionServiceImpl implements RoleFunctionService {

    @Autowired
    private RoleFunctionMapper roleFunctionMapper;


    @Override
    public List<RoleFunction> findListByEntity(RoleFunction entity) {
        return roleFunctionMapper.findListByEntity(entity);
    }

    @Override
    public int save(RoleFunction entity) {
        return roleFunctionMapper.insert(entity);
    }


    @Override
    public int saves(RoleFunction[] entitys) {
        return roleFunctionMapper.inserts(entitys);
    }

    @Override
    public int deleteByRoleId(String roleId) {
        return roleFunctionMapper.deleteByRoleId(roleId);
    }

    @Override
    public int deleteByFunctionId(String functionId) {
        return roleFunctionMapper.deleteByFunctionId(functionId);
    }

    @Override
    public int deleteByFunctionIdArray(String[] functionIdArray) {
        return roleFunctionMapper.deleteByFunctionIdArray(functionIdArray);
    }

    @Override
    public List<RoleFunction> findListByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(String url, String appCode, String[] roleIdArray) {
        return roleFunctionMapper.findListByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(url,appCode,roleIdArray);
    }

    @Override
    public Long findCountByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(String url, String appCode, String[] roleIdArray) {
        return roleFunctionMapper.findCountByAdminIdAndFunctionUrlAndAppCodeAndRoleIds(url,appCode,roleIdArray);
    }

    @Override
    public PageInfo<RoleFunction> queryListPage(RoleFunctionPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<RoleFunction> pageInfo = new PageInfo();
        pageInfo.setList(roleFunctionMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(roleFunctionMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }
}
