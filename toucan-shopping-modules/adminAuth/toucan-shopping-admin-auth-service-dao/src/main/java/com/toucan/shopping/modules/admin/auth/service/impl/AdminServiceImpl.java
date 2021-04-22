package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.page.AdminPageInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminService;
import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.mapper.AdminMapper;
import com.toucan.shopping.modules.admin.auth.service.AdminService;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<Admin> findListByEntity(Admin admin) {
        return adminMapper.findListByEntity(admin);
    }

    @Override
    public int save(Admin admin) {
        int row = adminMapper.insert(admin);
//        throw new IllegalArgumentException("手动测试异常,测试事务回滚");
        return row;
    }

    @Override
    public int update(Admin admin) {
        return adminMapper.update(admin);
    }

    @Override
    public boolean exists(String username) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setDeleteStatus((short)0);
        List<Admin> users = adminMapper.findListByEntity(admin);
        if(!CollectionUtils.isEmpty(users))
        {
            return true;
        }
        return false;
    }

    @Override
    public PageInfo<AdminVO> queryListPage(AdminPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<AdminVO> pageInfo = new PageInfo();
        pageInfo.setList(adminMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(adminMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

}
