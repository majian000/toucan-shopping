package com.toucan.shopping.modules.color.table.mapper;

import com.toucan.shopping.modules.color.table.entity.ColorTable;
import com.toucan.shopping.modules.color.table.page.ColorTablePageInfo;
import com.toucan.shopping.modules.color.table.vo.ColorTableVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface ColorTableMapper {


    List<ColorTableVO> queryList(ColorTableVO entity);


    int insert(ColorTable entity);

    int deleteById(Long id);

    int update(ColorTable entity);



    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<ColorTableVO> queryListPage(ColorTablePageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(ColorTablePageInfo pageInfo);

}
