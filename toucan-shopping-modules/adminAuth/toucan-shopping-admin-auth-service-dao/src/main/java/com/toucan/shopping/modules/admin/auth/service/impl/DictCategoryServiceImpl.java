package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.DictCategory;
import com.toucan.shopping.modules.admin.auth.mapper.DictCategoryMapper;
import com.toucan.shopping.modules.admin.auth.page.DictCategoryPageInfo;
import com.toucan.shopping.modules.admin.auth.service.DictCategoryService;
import com.toucan.shopping.modules.admin.auth.vo.DictCategoryVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class DictCategoryServiceImpl implements DictCategoryService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DictCategoryMapper dictCategoryMapper;

    @Override
    public List<DictCategoryVO> findListByEntity(DictCategory entity) {
        return dictCategoryMapper.findListByEntity(entity);
    }


    @Override
    public int save(DictCategory entity) {
        return dictCategoryMapper.insert(entity);
    }

    @Override
    public int update(DictCategory entity) {
        return dictCategoryMapper.update(entity);
    }

    @Override
    public Integer queryMaxSort() {
        Integer maxSort = dictCategoryMapper.queryMaxSort();
        if(maxSort==null){
            maxSort=0;
        }
        return maxSort;
    }


    @Override
    public List<DictCategoryVO> queryListByCodeAndAppCodes(String code,List<String> appCodes) {
        DictCategoryVO query = new DictCategoryVO();
        query.setCode(code);
        query.setEnableStatus((short)1);
        query.setAppCodes(appCodes);
        return dictCategoryMapper.findListByVO(query);
    }

    @Override
    public PageInfo<DictCategoryVO> queryListPage(DictCategoryPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        DictCategoryPageInfo pageInfo = new DictCategoryPageInfo();
        pageInfo.setList(dictCategoryMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(dictCategoryMapper.queryListPageCount(queryPageInfo));
        pageInfo.setLimit(queryPageInfo.getLimit());
        pageInfo.setPage(queryPageInfo.getPage());
        pageInfo.setSize(queryPageInfo.getSize());
        return pageInfo;
    }

    @Override
    public List<DictCategoryVO> queryList(DictCategoryVO query) {
        return dictCategoryMapper.findListByVO(query);
    }

    @Override
    public int deleteById(Integer id) {
        return dictCategoryMapper.deleteById(id);
    }

    @Override
    public List<DictCategoryVO> queryListByAppCode(String appCode) {
        return dictCategoryMapper.queryListByAppCode(appCode);
    }



}
