package com.toucan.shopping.modules.column.service.impl;

import com.toucan.shopping.modules.column.entity.ColumnType;
import com.toucan.shopping.modules.column.mapper.ColumnTypeMapper;
import com.toucan.shopping.modules.column.page.ColumnTypePageInfo;
import com.toucan.shopping.modules.column.service.ColumnTypeService;
import com.toucan.shopping.modules.column.vo.ColumnTypeVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息类型服务
 * @author majian
 * @date 2021-12-23 18:10:54
 */
@Service
public class ColumnTypeServiceImpl implements ColumnTypeService {

    @Autowired
    private ColumnTypeMapper messageTypeMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(ColumnType entity) {
        return messageTypeMapper.insert(entity);
    }



    @Override
    public int deleteById(Long id) {
        return messageTypeMapper.deleteById(id);
    }

    @Override
    public int update(ColumnType entity) {
        return messageTypeMapper.update(entity);
    }

    @Override
    public List<ColumnType> findListByEntity(ColumnType query) {
        return messageTypeMapper.findListByEntity(query);
    }

    @Override
    public PageInfo<ColumnTypeVO> queryListPage(ColumnTypePageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<ColumnTypeVO> pageInfo = new PageInfo();
        pageInfo.setList(messageTypeMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(messageTypeMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public List<ColumnTypeVO> queryList(ColumnTypeVO query) {
        return messageTypeMapper.queryList(query);
    }

}
