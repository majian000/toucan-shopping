package com.toucan.shopping.modules.admin.auth.service;


import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.page.FunctionTreeInfo;
import com.toucan.shopping.modules.admin.auth.vo.FunctionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

public interface FunctionService {

    /**
     * 根据查询对象返回列表
     * @param entity
     * @return
     */
    List<Function> findListByEntity(Function entity);

    /**
     * 根据查询对象返回列表
     * @param entity
     * @return
     */
    List<Function> findListByEntityFieldLike(Function entity);
    /**
     * 根据应用编码查询树
     * @param appCode
     * @return
     */
    List<FunctionTreeVO> queryTreeByAppCode(String appCode);

    /**
     * 保存
     * @param entity
     * @return
     */
    int save(Function entity);

    /**
     * 更新
     * @param entity
     * @return
     */
    int update(Function entity);


    /**
     * 更新子节点得应用编码
     * @param parentEntity
     * @return
     */
    void updateChildAppCode(Function parentEntity);

    /**
     * 判断是否存在
     * @param name
     * @return
     */
    boolean exists(String name);

    /**
     * 查询列表页
     * @param FunctionTreeInfo
     * @return
     */
    PageInfo<Function> queryListPage(FunctionTreeInfo FunctionTreeInfo);

    /**
     *  根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);


    /**
     * 根据应用编码查询列表
     * @param appCode
     * @return
     */
    List<FunctionVO> queryListByAppCode(String appCode);

    /**
     * 查询所有子节点
     * @param children
     * @param query
     */
    void queryChildren(List children,Function query);

    /**
     * 查询当前节点下一级子节点
     * @param query
     */
    List queryOneLevelChildren(Function query);

    /**
     * 查询当前节点下一级子节点
     * @param Id
     */
    List queryOneLevelChildrenById(Long Id);
    /**
     * 查询当前节点下一级子节点
     * @param Id
     */
    List queryOneLevelChildrenByIdAndAppCode(Long Id,String appCode);
    /**
     * 查询树表格
     * @param functionTreeInfo
     * @return
     */
    List<Function> findTreeTable(FunctionTreeInfo functionTreeInfo);


    /**
     * 查询指定角色下所有功能
     * @param roleIds
     * @return
     */
    List<FunctionVO> queryListByRoleIdArray(String[] roleIds);




    /**
     * 查询指定角色下所有功能
     * @param roleIds
     * @return
     */
    List<FunctionVO> queryListByRoleIdArrayAndParentId(String[] roleIds,String url);





}
