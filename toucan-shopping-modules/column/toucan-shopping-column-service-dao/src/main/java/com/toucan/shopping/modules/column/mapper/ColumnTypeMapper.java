package com.toucan.shopping.modules.column.mapper;

import com.toucan.shopping.modules.column.entity.ColumnType;
import com.toucan.shopping.modules.column.page.ColumnTypePageInfo;
import com.toucan.shopping.modules.column.vo.ColumnTypeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ColumnTypeMapper {

    int insert(ColumnType entity);


    List<ColumnTypeVO> queryList(ColumnTypeVO entity);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    int update(ColumnType entity);

    List<ColumnType> findListByEntity(ColumnType query);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<ColumnTypeVO> queryListPage(ColumnTypePageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(ColumnTypePageInfo pageInfo);
}
