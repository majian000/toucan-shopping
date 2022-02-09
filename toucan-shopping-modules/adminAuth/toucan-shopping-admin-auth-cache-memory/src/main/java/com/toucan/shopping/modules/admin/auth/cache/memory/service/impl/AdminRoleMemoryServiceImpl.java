package com.toucan.shopping.modules.admin.auth.cache.memory.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.cache.memory.context.AdminAuthMemoryContext;
import com.toucan.shopping.modules.admin.auth.cache.service.AdminRoleCacheService;
import com.toucan.shopping.modules.admin.auth.constant.AdminRoleCacheElasticSearchConstant;
import com.toucan.shopping.modules.admin.auth.constant.RoleFunctionCacheElasticSearchConstant;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleCacheVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("adminRoleMemoryServiceImpl")
public class AdminRoleMemoryServiceImpl implements AdminRoleCacheService {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void save(AdminRoleCacheVO memoryVO) throws Exception{
        AdminAuthMemoryContext.instance().getAdminRoleHashMap().put(memoryVO.getId(),memoryVO);
    }

    @Override
    public void update(AdminRoleCacheVO memoryVO) throws Exception {
        AdminAuthMemoryContext.instance().getAdminRoleHashMap().put(memoryVO.getId(),memoryVO);
    }

    @Override
    public boolean existsIndex() {
        return false;
    }

    @Override
    public void createIndex() {
    }


    @Override
    public List<AdminRoleCacheVO> queryById(Long id) throws Exception{
        List<AdminRoleCacheVO> AdminRoleCacheVOS = new ArrayList<AdminRoleCacheVO>();
        AdminRoleCacheVOS.add(AdminAuthMemoryContext.instance().getAdminRoleHashMap().get(id));
        return AdminRoleCacheVOS;
    }

    @Override
    public List<AdminRoleCacheVO> queryByEntity(AdminRoleCacheVO query) throws Exception {
        List<AdminRoleCacheVO> adminRoleCacheVOS = new ArrayList<AdminRoleCacheVO>();
        Set<Long> keys = AdminAuthMemoryContext.instance().getAdminRoleHashMap().keySet();
        for(Long key:keys)
        {
            AdminRoleCacheVO adminRoleCacheVO = AdminAuthMemoryContext.instance().getAdminRoleHashMap().get(key);
            boolean isFind = true;
            //设置查询条件
            if(query.getId()!=null) {
               if(adminRoleCacheVO.getId()!=null&&adminRoleCacheVO.getId().longValue()!=query.getId().longValue())
               {
                   isFind = false;
               }
            }
            if(query.getAdminId()!=null)
            {
                if(adminRoleCacheVO.getAdminId()!=null&&!adminRoleCacheVO.getAdminId().equals(query.getAdminId()))
                {
                    isFind = false;
                }
            }
            if(query.getRoleId()!=null)
            {
                if(adminRoleCacheVO.getRoleId()!=null&&!adminRoleCacheVO.getRoleId().equals(query.getRoleId()))
                {
                    isFind = false;
                }
            }
            if(query.getAppCode()!=null)
            {
                if(adminRoleCacheVO.getAppCode()!=null&&!adminRoleCacheVO.getAppCode().equals(query.getAppCode()))
                {
                    isFind = false;
                }
            }

            if(isFind) {
                adminRoleCacheVOS.add(adminRoleCacheVO);
            }
        }

        return adminRoleCacheVOS;
    }


    @Override
    public boolean deleteById(String id) throws Exception {
        AdminRoleCacheVO adminRoleCacheVO = AdminAuthMemoryContext.instance().getAdminRoleHashMap().remove(Long.parseLong(id));
        if(adminRoleCacheVO!=null)
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteIndex() throws Exception {
        return true;
    }



    @Override
    public boolean deleteByAdminIdAndAppCodes(String adminId,String appCode,List<String> deleteFaildIdList) throws Exception {
        Set<Long> keys = AdminAuthMemoryContext.instance().getAdminRoleHashMap().keySet();
        for(Long key:keys)
        {
            AdminRoleCacheVO adminRoleCacheVO = AdminAuthMemoryContext.instance().getAdminRoleHashMap().get(key);
            if(adminRoleCacheVO.getAdminId()!=null&&adminRoleCacheVO.getAdminId().equals(adminId)
                &&adminRoleCacheVO.getAppCode()!=null&&adminRoleCacheVO.getAppCode().equals(appCode))
            {
                AdminAuthMemoryContext.instance().getAdminRoleHashMap().remove(key);
            }
        }
        return true;
    }

    @Override
    public void saves(AdminRoleCacheVO[] adminRoleCacheVOS) throws Exception {

        for(AdminRoleCacheVO adminRoleCacheVO:adminRoleCacheVOS)
        {
            save(adminRoleCacheVO);
        }

    }



}
