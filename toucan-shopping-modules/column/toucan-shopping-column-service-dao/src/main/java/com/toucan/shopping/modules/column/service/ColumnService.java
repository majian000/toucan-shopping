package com.toucan.shopping.modules.column.service;

import com.toucan.shopping.modules.column.entity.Column;

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
}
