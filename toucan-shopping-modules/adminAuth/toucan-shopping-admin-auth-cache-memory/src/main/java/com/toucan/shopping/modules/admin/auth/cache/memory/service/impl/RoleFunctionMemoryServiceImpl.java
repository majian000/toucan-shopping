package com.toucan.shopping.modules.admin.auth.cache.memory.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.cache.memory.context.AdminAuthMemoryContext;
import com.toucan.shopping.modules.admin.auth.cache.service.RoleFunctionCacheService;
import com.toucan.shopping.modules.admin.auth.constant.RoleFunctionCacheElasticSearchConstant;
import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionCacheVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("roleFunctionMemoryServiceImpl")
public class RoleFunctionMemoryServiceImpl implements RoleFunctionCacheService {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void save(RoleFunctionCacheVO memoryVO) throws Exception {
        AdminAuthMemoryContext.instance().getRoleFunctionHashMap().put(memoryVO.getId(),memoryVO);

    }

    @Override
    public void update(RoleFunctionCacheVO memoryVO) throws Exception{
        AdminAuthMemoryContext.instance().getRoleFunctionHashMap().put(memoryVO.getId(),memoryVO);
    }

    @Override
    public boolean existsIndex() {
        return true;
    }

    @Override
    public void createIndex() {
    }

    @Override
    public boolean deleteIndex() throws Exception {
        return true;
    }


    @Override
    public List<RoleFunctionCacheVO> queryById(Long id) throws Exception{
        List<RoleFunctionCacheVO> roleFunctionCacheVOS = new ArrayList<RoleFunctionCacheVO>();
        roleFunctionCacheVOS.add(AdminAuthMemoryContext.instance().getRoleFunctionHashMap().get(id));
        return roleFunctionCacheVOS;
    }

    @Override
    public List<RoleFunctionCacheVO> queryByEntity(RoleFunctionCacheVO query) throws Exception {
        List<RoleFunctionCacheVO> roleFunctionCacheVOS = new ArrayList<RoleFunctionCacheVO>();
        Set<Long> keys = AdminAuthMemoryContext.instance().getAdminRoleHashMap().keySet();
        for(Long key:keys)
        {
            RoleFunctionCacheVO roleFunctionCacheVO = AdminAuthMemoryContext.instance().getRoleFunctionHashMap().get(key);
            boolean isFind = true;
            //设置查询条件
            if(query.getId()!=null) {
                if(roleFunctionCacheVO.getId()!=null&&roleFunctionCacheVO.getId().longValue()!=query.getId().longValue())
                {
                    isFind = false;
                }
            }
            if(query.getFunctionId()!=null)
            {
                if(roleFunctionCacheVO.getFunctionId()!=null&&!roleFunctionCacheVO.getFunctionId().equals(query.getFunctionId()))
                {
                    isFind = false;
                }
            }
            if(query.getRoleId()!=null)
            {
                if(roleFunctionCacheVO.getRoleId()!=null&&!roleFunctionCacheVO.getRoleId().equals(query.getRoleId()))
                {
                    isFind = false;
                }
            }
            if(query.getDeleteStatus()!=null)
            {
                if(roleFunctionCacheVO.getDeleteStatus()!=null&&roleFunctionCacheVO.getDeleteStatus().intValue()!=query.getDeleteStatus().intValue())
                {
                    isFind = false;
                }
            }
            if(query.getAppCode()!=null)
            {
                if(roleFunctionCacheVO.getAppCode()!=null&&!roleFunctionCacheVO.getAppCode().equals(query.getAppCode()))
                {
                    isFind = false;
                }
            }
            if(isFind) {
                roleFunctionCacheVOS.add(roleFunctionCacheVO);
            }
        }
        return roleFunctionCacheVOS;
    }


    @Override
    public boolean deleteById(String id) throws Exception {
        RoleFunctionCacheVO roleFunctionCacheVO = AdminAuthMemoryContext.instance().getRoleFunctionHashMap().remove(Long.parseLong(id));
        if(roleFunctionCacheVO!=null)
        {
            return true;
        }
        return false;
    }


    @Override
    public boolean deleteByRoleId(String roleId,List<String> deleteFaildIdList) throws Exception {

        Set<Long> keys = AdminAuthMemoryContext.instance().getAdminRoleHashMap().keySet();
        for(Long key:keys)
        {
            RoleFunctionCacheVO roleFunctionCacheVO = AdminAuthMemoryContext.instance().getRoleFunctionHashMap().get(key);
            boolean isFind = false;
            if(roleId!=null)
            {
                if(roleFunctionCacheVO.getFunctionId()!=null&&roleFunctionCacheVO.getRoleId().equals(roleId))
                {
                    isFind = true;
                }
            }
            if(isFind) {
                AdminAuthMemoryContext.instance().getAdminRoleHashMap().remove(key);
            }
        }

        return true;
    }

    @Override
    public void saves(RoleFunctionCacheVO[] roleFunctionCacheVOS)  throws Exception{
        for(RoleFunctionCacheVO roleFunctionCacheVO:roleFunctionCacheVOS)
        {
            save(roleFunctionCacheVO);
        }
    }

    @Override
    public boolean deleteByFunctionId(String functionId,List<String> deleteFaildIdList) throws Exception {
        Set<Long> keys = AdminAuthMemoryContext.instance().getAdminRoleHashMap().keySet();
        for(Long key:keys)
        {
            RoleFunctionCacheVO roleFunctionCacheVO = AdminAuthMemoryContext.instance().getRoleFunctionHashMap().get(key);
            boolean isFind = false;
            if(functionId!=null)
            {
                if(roleFunctionCacheVO.getFunctionId()!=null&&roleFunctionCacheVO.getFunctionId().equals(functionId))
                {
                    isFind = true;
                }
            }
            if(isFind) {
                AdminAuthMemoryContext.instance().getAdminRoleHashMap().remove(key);
            }
        }

        return true;
    }



}
