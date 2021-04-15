package com.toucan.shopping.modules.area.service;



import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.area.vo.AreaVO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface AreaService {

    List<Area> queryList(Area area);

    /**
     * 保存实体
     * @param area
     * @return
     */
    int save(Area area);


    int update(Area area);

    Area queryById(Long id);

    int deleteById(String appCode,Long id);

    public List<Area> findByParentCode(String appCode,String parentCode);

    void deleteChildrenByParentCode(String appCode,String parentCode);

    /**
     * 填充类别树
     * @param appCode
     * @return
     */
    List<AreaVO> queryTree(String appCode)  throws Exception;

    /**
     * 填充类别子节点
     * @param areaVO
     */
    void setChildrenByParentId(AreaVO areaVO) throws InvocationTargetException, IllegalAccessException;
}
