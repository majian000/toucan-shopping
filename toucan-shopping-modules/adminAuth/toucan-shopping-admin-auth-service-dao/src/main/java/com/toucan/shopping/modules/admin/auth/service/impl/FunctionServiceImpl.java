package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.mapper.FunctionMapper;
import com.toucan.shopping.modules.admin.auth.page.FunctionPageInfo;
import com.toucan.shopping.modules.admin.auth.service.FunctionService;
import com.toucan.shopping.modules.common.page.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class FunctionServiceImpl implements FunctionService {

    @Autowired
    private FunctionMapper functionMapper;

    @Override
    public List<Function> findListByEntity(Function entity) {
        return functionMapper.findListByEntity(entity);
    }

    @Transactional
    @Override
    public int save(Function entity) {
        return functionMapper.insert(entity);
    }

    @Transactional
    @Override
    public int update(Function entity) {
        return functionMapper.update(entity);
    }

    @Override
    public boolean exists(String name) {
        Function entity = new Function();
        entity.setName(name);
        entity.setDeleteStatus((short)0);
        List<Function> users = functionMapper.findListByEntity(entity);
        if(!CollectionUtils.isEmpty(users))
        {
            return true;
        }
        return false;
    }

    @Override
    public PageInfo<Function> queryListPage(FunctionPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        FunctionPageInfo pageInfo = new FunctionPageInfo();
        pageInfo.setList(functionMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(functionMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        return functionMapper.deleteById(id);
    }

}
