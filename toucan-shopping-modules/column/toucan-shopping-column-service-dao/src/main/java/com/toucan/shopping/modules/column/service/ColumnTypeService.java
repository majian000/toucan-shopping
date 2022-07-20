package com.toucan.shopping.modules.column.service;


import com.toucan.shopping.modules.column.entity.ColumnType;
import com.toucan.shopping.modules.column.page.ColumnTypePageInfo;
import com.toucan.shopping.modules.column.vo.ColumnTypeVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

/**
 * 栏目类型
 * @author majian
 * @date 2022-7-20 14:17:54
 */
public interface ColumnTypeService {

    int save(ColumnType entity);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 修改
     * @param entity
     * @return
     */
    int update(ColumnType entity);


    List<ColumnType> findListByEntity(ColumnType query);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<ColumnTypeVO> queryListPage(ColumnTypePageInfo pageInfo);



    List<ColumnTypeVO> queryList(ColumnTypeVO query);
}
