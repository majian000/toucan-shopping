package com.toucan.shopping.modules.column.service;

import com.toucan.shopping.modules.column.entity.Column;
import com.toucan.shopping.modules.column.page.ColumnPageInfo;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface ColumnService {

    /**
     * 返回指定地区下栏目列表
     * @param areaCode
     * @param type
     * @param position
     * @return
     */
    List<Column> queryAreaColumnList(String areaCode, Integer type, Integer position);



    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<ColumnVO> queryListPage(ColumnPageInfo queryPageInfo);


    List<ColumnVO> queryList(ColumnVO query);

    int save(Column column);
}
