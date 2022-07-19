package com.toucan.shopping.modules.area.mapper;

import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.area.page.AreaTreeInfo;
import com.toucan.shopping.modules.area.vo.AreaVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface AreaMapper {

    List<Area> queryList(Area area);

    List<Area> queryListByVO(AreaVO area);

    int insert(Area area);

    Area queryById(Long id);

    List<Area> queryByParentCode(String appCode,String parentCode);

    int deleteById(String appCode,Long id);

    int update(Area area);

    List<AreaVO> queryByCode(String code);

    /**
     * 查询表格树
     * @param areaTreeInfo
     * @return
     */
    List<AreaVO> findTreeTableByPageInfo(AreaTreeInfo areaTreeInfo);

    Long queryCount(Area area);


    List<Area> findListByPid(Long pid);
}
