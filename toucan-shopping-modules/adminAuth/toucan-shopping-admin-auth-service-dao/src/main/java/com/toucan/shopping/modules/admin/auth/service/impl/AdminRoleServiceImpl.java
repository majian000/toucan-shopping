package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.es.service.AdminRoleElasticSearchService;
import com.toucan.shopping.modules.admin.auth.mapper.AdminAppMapper;
import com.toucan.shopping.modules.admin.auth.mapper.AdminRoleMapper;
import com.toucan.shopping.modules.admin.auth.page.AdminRolePageInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AdminRoleService;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleElasticSearchVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdminRoleServiceImpl implements AdminRoleService {

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private AdminAppService adminAppService;

    @Autowired
    private AdminRoleElasticSearchService adminRoleElasticSearchService;


    @Override
    public List<AdminRole> findListByEntity(AdminRole adminRole) {
        return adminRoleMapper.findListByEntity(adminRole);
    }

    @Override
    public int save(AdminRole adminRole) {
        return adminRoleMapper.insert(adminRole);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saves(AdminRole[] adminRole) throws Exception {
        int row = adminRoleMapper.inserts(adminRole);

        //同步缓存,在这里要求缓存和数据库是一致的,如果缓存同步失败的话,数据库也会进行回滚
        List<String> deleteFaildIdList = new ArrayList<String>();
        for(int i=0;i<adminRole.length;i++)
        {
            //删除指定账号下的指定所有应用下的所有账号角色关联
            adminRoleElasticSearchService.deleteByAdminIdAndAppCodes(adminRole[i].getAdminId(),adminRole[i].getAppCode(),deleteFaildIdList);
        }
        if(adminRole!=null&&adminRole.length>0)
        {
            AdminRoleElasticSearchVO[] adminRoleElasticSearchVOS = new AdminRoleElasticSearchVO[adminRole.length];
            for(int i=0;i<adminRole.length;i++)
            {
                AdminRoleElasticSearchVO adminRoleElasticSearchVO = new AdminRoleElasticSearchVO();
                if(adminRole[i]!=null) {
                    BeanUtils.copyProperties(adminRoleElasticSearchVO, adminRole[i]);
                }
                adminRoleElasticSearchVOS[i] = adminRoleElasticSearchVO;
            }
            adminRoleElasticSearchService.saves(adminRoleElasticSearchVOS);
        }
        return row;
    }
    @Override
    public int deleteByRoleId(String roleId) {
        return adminRoleMapper.deleteByRoleId(roleId);
    }

    @Override
    public int deleteByAdminId(String adminId) {
        return adminRoleMapper.deleteByAdminId(adminId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteByAdminIdAndAppCodes(String adminId, AdminRoleVO entity) throws Exception {

        AdminApp queryAdminApp = new AdminApp();
        queryAdminApp.setAdminId(entity.getCreateAdminId());

        List<AdminApp> adminApps = adminAppService.findListByEntity(queryAdminApp);

        if(CollectionUtils.isEmpty(adminApps))
        {
            throw new IllegalArgumentException("当前登录用户,关联应用列表为空");
        }

        //拿到当前这个账户的操作人可管理的所有应用
        String[] appCodes = new String[adminApps.size()];
        for(int i=0;i<adminApps.size();i++)
        {
            appCodes[i] = adminApps.get(i).getAppCode();
        }

        int row = adminRoleMapper.deleteByAdminIdAndAppCodes(adminId,appCodes);
        int length=0;
        for(AdminRole adminRole:entity.getRoles())
        {
            //-1为应用节点
            if(!"-1".equals(adminRole.getRoleId())) {
                adminRole.setAdminId(entity.getAdminId());
                adminRole.setCreateAdminId(entity.getCreateAdminId());
                adminRole.setCreateDate(new Date());
                adminRole.setDeleteStatus((short) 0);
                length++;
            }
        }
        AdminRole[] adminRoles = new AdminRole[length];
        int pos = 0;
        for(AdminRole adminRole:entity.getRoles()) {
            //-1为应用节点
            if (!"-1".equals(adminRole.getRoleId())) {
                adminRoles[pos] = adminRole;
                pos++;
            }
        }

        //保存账号角色关联
        adminRoleService.saves(adminRoles);

        return row;
    }

    @Override
    public List<AdminRole> findListByAdminId(String adminId) {
        return adminRoleMapper.findListByAdminId(adminId);
    }

    @Override
    public List<AdminRole> listByAdminIdAndAppCode(String adminId, String appCode) {
        return adminRoleMapper.listByAdminIdAndAppCode(adminId,appCode);
    }

    @Override
    public PageInfo<AdminRole> queryListPage(AdminRolePageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<AdminRole> pageInfo = new PageInfo();
        pageInfo.setList(adminRoleMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(adminRoleMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }
}
