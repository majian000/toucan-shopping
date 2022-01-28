package com.toucan.shopping.modules.area.mapper;

import com.toucan.shopping.modules.area.entity.SecondKillColumn;
import com.toucan.shopping.modules.area.vo.SecondKillColumnVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SecondKillColumnMapper {

    List<SecondKillColumn> queryList(SecondKillColumnVO entity);

    int insert(SecondKillColumn entity);


    int deleteById(Long id);



}
