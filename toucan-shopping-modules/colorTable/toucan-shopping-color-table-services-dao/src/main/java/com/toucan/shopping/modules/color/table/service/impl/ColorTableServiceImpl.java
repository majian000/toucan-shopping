package com.toucan.shopping.modules.color.table.service.impl;

import com.toucan.shopping.modules.color.table.entity.ColorTable;
import com.toucan.shopping.modules.color.table.mapper.ColorTableMapper;
import com.toucan.shopping.modules.color.table.page.ColorTablePageInfo;
import com.toucan.shopping.modules.color.table.service.ColorTableService;
import com.toucan.shopping.modules.color.table.vo.ColorTableVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorTableServiceImpl implements ColorTableService {

    @Autowired
    private ColorTableMapper colorTableMapper;


    @Override
    public List<ColorTableVO> queryList(ColorTableVO entity) {
        return colorTableMapper.queryList(entity);
    }


    @Override
    public int save(ColorTable entity) {
        return colorTableMapper.insert(entity);
    }

    @Override
    public int deleteById(Long id) {
        return colorTableMapper.deleteById(id);
    }

    @Override
    public int update(ColorTable entity) {
        return colorTableMapper.update(entity);
    }

    @Override
    public PageInfo<ColorTableVO> queryListPage(ColorTablePageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<ColorTableVO> pageInfo = new PageInfo();
        pageInfo.setList(colorTableMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(colorTableMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }
}
