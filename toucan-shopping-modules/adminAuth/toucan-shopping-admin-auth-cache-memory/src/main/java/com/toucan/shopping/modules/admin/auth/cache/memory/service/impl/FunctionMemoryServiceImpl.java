package com.toucan.shopping.modules.admin.auth.cache.memory.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.cache.memory.context.AdminAuthMemoryContext;
import com.toucan.shopping.modules.admin.auth.cache.service.FunctionCacheService;
import com.toucan.shopping.modules.admin.auth.constant.FunctionCacheElasticSearchConstant;
import com.toucan.shopping.modules.admin.auth.constant.RoleFunctionCacheElasticSearchConstant;
import com.toucan.shopping.modules.admin.auth.vo.FunctionCacheVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service("functionMemoryServiceImpl")
public class FunctionMemoryServiceImpl implements FunctionCacheService {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void save(FunctionCacheVO memoryVO) throws Exception{
        AdminAuthMemoryContext.instance().getFunctionHashMap().put(memoryVO.getId(),memoryVO);
    }


    @Override
    public boolean deleteIndex() throws Exception {
        return true;
    }


    @Override
    public void update(FunctionCacheVO memoryVO) throws Exception {
        AdminAuthMemoryContext.instance().getFunctionHashMap().put(memoryVO.getId(),memoryVO);
    }

    @Override
    public boolean existsIndex() {
        return true;
    }

    @Override
    public void createIndex() {
    }


    @Override
    public List<FunctionCacheVO> queryById(Long id) throws Exception{
        List<FunctionCacheVO> functionCacheVOS = new ArrayList<FunctionCacheVO>();
        functionCacheVOS.add(AdminAuthMemoryContext.instance().getFunctionHashMap().get(id));
        return functionCacheVOS;
    }

    @Override
    public List<FunctionCacheVO> queryByEntity(FunctionCacheVO query) throws Exception {
        List<FunctionCacheVO> functionCacheVOS = new ArrayList<FunctionCacheVO>();

        Set<Long> keys = AdminAuthMemoryContext.instance().getAdminRoleHashMap().keySet();
        for(Long key:keys)
        {
            FunctionCacheVO functionCacheVO = AdminAuthMemoryContext.instance().getFunctionHashMap().get(key);
            boolean isFind = true;
            //设置查询条件
            if(query.getId()!=null) {
                if(functionCacheVO.getId()!=null&&functionCacheVO.getId().longValue()!=query.getId().longValue())
                {
                    isFind = false;
                }
            }
            if(query.getFunctionId()!=null)
            {
                if(functionCacheVO.getFunctionId()!=null&&!functionCacheVO.getFunctionId().equals(query.getFunctionId()))
                {
                    isFind = false;
                }
            }

            if(query.getUrl()!=null)
            {
                if(functionCacheVO.getUrl()!=null&&!functionCacheVO.getUrl().equals(query.getUrl()))
                {
                    isFind = false;
                }
            }
            if(query.getEnableStatus()!=null)
            {
                if(functionCacheVO.getEnableStatus()!=null&&functionCacheVO.getEnableStatus().intValue()!=query.getEnableStatus().intValue())
                {
                    isFind = false;
                }
            }
            if(query.getPid()!=null)
            {
                if(functionCacheVO.getPid()!=null&&functionCacheVO.getPid().longValue()!=query.getPid().longValue())
                {
                    isFind = false;
                }
            }
            if(query.getAppCode()!=null)
            {
                if(functionCacheVO.getAppCode()!=null&&!functionCacheVO.getAppCode().equals(query.getAppCode()))
                {
                    isFind = false;
                }
            }
            if(isFind) {
                functionCacheVOS.add(functionCacheVO);
            }
        }

        return functionCacheVOS;
    }


    @Override
    public boolean deleteById(String id) throws Exception {
        FunctionCacheVO adminRoleCacheVO = AdminAuthMemoryContext.instance().getFunctionHashMap().remove(Long.parseLong(id));
        if(adminRoleCacheVO!=null)
        {
            return true;
        }
        return false;
    }


}
