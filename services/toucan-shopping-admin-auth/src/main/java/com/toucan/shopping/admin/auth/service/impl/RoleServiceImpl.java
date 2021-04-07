package com.toucan.shopping.admin.auth.service.impl;

import com.toucan.shopping.admin.auth.entity.Role;
import com.toucan.shopping.admin.auth.entity.Role;
import com.toucan.shopping.admin.auth.mapper.RoleMapper;
import com.toucan.shopping.admin.auth.page.RolePageInfo;
import com.toucan.shopping.admin.auth.service.RoleService;
import com.toucan.shopping.admin.auth.service.RoleService;
import com.toucan.shopping.common.page.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> findListByEntity(Role entity) {
        return roleMapper.findListByEntity(entity);
    }

    @Transactional
    @Override
    public int save(Role entity) {
        return roleMapper.insert(entity);
    }

    @Transactional
    @Override
    public int update(Role entity) {
        return roleMapper.update(entity);
    }

    @Override
    public boolean exists(String name) {
        Role Role = new Role();
        Role.setName(name);
        Role.setDeleteStatus((short)0);
        List<Role> users = roleMapper.findListByEntity(Role);
        if(!CollectionUtils.isEmpty(users))
        {
            return true;
        }
        return false;
    }

    @Override
    public PageInfo<Role> queryListPage(RolePageInfo RolePageInfo) {
        List<Role> Roles = roleMapper.queryListPage(RolePageInfo);
        PageInfo<Role> pageInfo = new PageInfo<>(Roles);
        return pageInfo;
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        return roleMapper.deleteById(id);
    }

}
