package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.Role;
import com.toucan.shopping.modules.admin.auth.entity.Role;
import com.toucan.shopping.modules.admin.auth.mapper.RoleMapper;
import com.toucan.shopping.modules.admin.auth.page.RolePageInfo;
import com.toucan.shopping.modules.admin.auth.service.RoleService;
import com.toucan.shopping.modules.admin.auth.service.RoleService;
import com.toucan.shopping.modules.common.page.PageInfo;
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

    @Override
    public int save(Role entity) {
        return roleMapper.insert(entity);
    }

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
    public PageInfo<Role> queryListPage(RolePageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        RolePageInfo pageInfo = new RolePageInfo();
        pageInfo.setList(roleMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(roleMapper.queryListPageCount(queryPageInfo));
        pageInfo.setLimit(queryPageInfo.getLimit());
        pageInfo.setPage(queryPageInfo.getPage());
        pageInfo.setSize(queryPageInfo.getSize());
        return pageInfo;
    }

    @Override
    public int deleteById(Long id) {
        return roleMapper.deleteById(id);
    }

}
