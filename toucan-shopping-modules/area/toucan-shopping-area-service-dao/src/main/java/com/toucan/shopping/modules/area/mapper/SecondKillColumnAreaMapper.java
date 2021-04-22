package com.toucan.shopping.modules.area.mapper;

import com.toucan.shopping.modules.area.entity.SecondKillColumn;
import com.toucan.shopping.modules.area.entity.SecondKillColumnArea;
import com.toucan.shopping.modules.area.vo.SecondKillColumnAreaVO;
import com.toucan.shopping.modules.area.vo.SecondKillColumnVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SecondKillColumnAreaMapper {

    List<SecondKillColumnArea> queryList(SecondKillColumnAreaVO entity);

    int insert(SecondKillColumnArea entity);


    int deleteById(Long id);



}
