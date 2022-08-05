package com.toucan.shopping.modules.column.service.impl;

import com.toucan.shopping.modules.column.entity.Column;
import com.toucan.shopping.modules.column.mapper.ColumnMapper;
import com.toucan.shopping.modules.column.page.ColumnPageInfo;
import com.toucan.shopping.modules.column.service.ColumnService;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnServiceImpl implements ColumnService {

    @Autowired
    private ColumnMapper columnMapper;


    @Override
    public List<Column> queryAreaColumnList(String areaCode, Integer type, Integer position) {
        return columnMapper.queryAreaColumnList(areaCode,type,position);
    }

    @Override
    public PageInfo<ColumnVO> queryListPage(ColumnPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<ColumnVO> pageInfo = new PageInfo();
        pageInfo.setList(columnMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(columnMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public List<ColumnVO> queryList(ColumnVO query) {
        return columnMapper.queryList(query);
    }

    @Override
    public ColumnVO findById(Long id) {
        return columnMapper.findById(id);
    }

    @Override
    public int save(Column column) {
        return columnMapper.insert(column);
    }

    @Override
    public int deleteById(Long id) {
        return columnMapper.deleteById(id);
    }
}
