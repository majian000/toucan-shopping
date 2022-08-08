package com.toucan.shopping.modules.column.mapper;

import com.toucan.shopping.modules.column.entity.Column;
import com.toucan.shopping.modules.column.page.ColumnPageInfo;
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


    /**
     * 根据ID查询
     * @param id
     * @return
     */
    ColumnVO findById(Long id);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<ColumnVO> queryListPage(ColumnPageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(ColumnPageInfo pageInfo);

    int insert(Column column);

    int update(Column column);

    List<ColumnVO> queryList(ColumnVO query);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

}
