package com.toucan.shopping.modules.admin.auth.service.impl;

import com.toucan.shopping.modules.admin.auth.entity.Dict;
import com.toucan.shopping.modules.admin.auth.mapper.DictMapper;
import com.toucan.shopping.modules.admin.auth.page.DictPageInfo;
import com.toucan.shopping.modules.admin.auth.service.DictService;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl implements DictService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DictMapper dictMapper;

    @Override
    public List<DictVO> findListByEntity(Dict entity) {
        return dictMapper.findListByEntity(entity);
    }

    @Override
    public DictVO findById(Long id) {
        return dictMapper.findById(id);
    }


    @Override
    public int save(Dict entity) {
        return dictMapper.insert(entity);
    }

    @Override
    public int update(Dict entity) {
        return dictMapper.update(entity);
    }

    @Override
    public Integer queryMaxSort() {
        Integer maxSort = dictMapper.queryMaxSort();
        if(maxSort==null){
            maxSort=0;
        }
        return maxSort;
    }

    @Override
    public int updateIsActiveByBatchId(short isActive, String batchId) {
        return dictMapper.updateIsActiveByBatchId(isActive,batchId);
    }

    @Override
    public int deleteByBatchId(String batchId) {
        return dictMapper.deleteByBatchId(batchId);
    }

    @Override
    public int queryMaxVersion(String batchId) {
        return dictMapper.queryMaxVersion(batchId);
    }


    @Override
    public List<DictVO> queryListByCodeAndAppCodes(String code,List<String> appCodes) {
        DictVO query = new DictVO();
        query.setCode(code);
        query.setEnableStatus((short)1);
        query.setAppCodes(appCodes);
        return dictMapper.findListByVO(query);
    }

    @Override
    public PageInfo<DictVO> queryListPage(DictPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        DictPageInfo pageInfo = new DictPageInfo();
        pageInfo.setList(dictMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(dictMapper.queryListPageCount(queryPageInfo));
        pageInfo.setLimit(queryPageInfo.getLimit());
        pageInfo.setPage(queryPageInfo.getPage());
        pageInfo.setSize(queryPageInfo.getSize());
        return pageInfo;
    }

    @Override
    public int deleteById(Long id) {
        return dictMapper.deleteById(id);
    }

    @Override
    public int deleteByIdList(List<Long> ids) {
        return dictMapper.deleteByIdList(ids);
    }

    @Override
    public int deleteByCategoryId(Integer categoryId) {
        return dictMapper.deleteByCategoryId(categoryId);
    }

    @Override
    public List<DictVO> queryListByAppCode(String appCode) {
        return dictMapper.queryListByAppCode(appCode);
    }

    @Override
    public List<DictVO> queryList(DictVO query) {
        return dictMapper.queryList(query);
    }

    @Override
    public Long queryListCount(DictVO query) {
        return dictMapper.queryListCount(query);
    }


    @Override
    public Long queryOneChildCountByPid(Long pid,String appCode) {
        return dictMapper.queryOneChildCountByPid(pid,appCode);
    }

    @Override
    public Long queryOneChildCountByPid(Long pid,String appCode,Integer categoryId) {
        return dictMapper.queryOneChildCountByPidAndCategoryId(pid,appCode,categoryId);
    }

    @Override
    public int updateAppCodeByCategoryId(Integer categoryId, String appCode) {
        return dictMapper.updateAppCodeByCategoryId(categoryId,appCode);
    }

    @Override
    public void queryChildren(List children, Dict query) {
        List<DictVO> dictVOS = dictMapper.findListByPid(query.getId());
        children.addAll(dictVOS);
        for(DictVO dictVO:dictVOS)
        {
            queryChildren(children,dictVO);
        }
    }

}
