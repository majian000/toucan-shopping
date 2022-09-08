package com.toucan.shopping.modules.area.service;



import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.area.page.AreaTreeInfo;
import com.toucan.shopping.modules.area.vo.AreaTreeVO;
import com.toucan.shopping.modules.area.vo.AreaVO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface AreaService {

    List<Area> queryList(Area area);

    List<Area> queryListByVO(AreaVO area);

    Long queryCount(Area area);

    /**
     * 保存实体
     * @param area
     * @return
     */
    int save(Area area);


    int update(Area area);

    int updateBatch(List<Area> areas);

    Area queryById(Long id);

    int deleteById(String appCode,Long id);

    List<Area> findByParentCode(String appCode,String parentCode);

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
    void setChildrenByParentCode(AreaVO areaVO) throws InvocationTargetException, IllegalAccessException;




    /**
     * 查询树表格
     * @param areaTreeInfo
     * @return
     */
    List<AreaVO> findTreeTable(AreaTreeInfo areaTreeInfo);


    /**
     * 从一个集合中找到所有子节点并设置上
     * @param areaVOS
     * @param currentNode
     */
    void setChildren(List<Area> areaVOS, AreaTreeVO currentNode) throws InvocationTargetException, IllegalAccessException ;



    /**
     * 查询所有子节点
     * @param children
     * @param query
     */
    void queryChildren(List<Area> children,Area query);


}
