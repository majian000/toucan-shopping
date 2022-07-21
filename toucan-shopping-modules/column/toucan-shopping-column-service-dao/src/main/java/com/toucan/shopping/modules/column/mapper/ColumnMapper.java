package com.toucan.shopping.modules.column.mapper;

import com.toucan.shopping.modules.column.entity.Column;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface ColumnMapper {


    /**
     * 返回指定地区下栏目列表
     * @param areaCode
     * @param type
     * @param position
     * @return
     */
    List<Column> queryAreaColumnList(String areaCode,Integer type,Integer position);


}