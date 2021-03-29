package com.toucan.shopping.area.mapper;

import com.toucan.shopping.area.entity.Area;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface AreaMapper {

    List<Area> queryList(Area area);


    int insert(Area area);

    Area queryById(Long id);

    List<Area> queryByParentCode(String appCode,String parentCode);

    int deleteById(String appCode,Long id);

    int update(Area area);
}
