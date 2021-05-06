package com.toucan.shopping.modules.admin.auth.service;


import com.toucan.shopping.modules.admin.auth.entity.Orgnazition;
import com.toucan.shopping.modules.admin.auth.page.OrgnazitionTreeInfo;
import com.toucan.shopping.modules.admin.auth.vo.OrgnazitionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.OrgnazitionVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

public interface OrgnazitionService {

    /**
     * 根据查询对象返回列表
     * @param entity
     * @return
     */
    List<OrgnazitionVO> findListByEntity(Orgnazition entity);

    /**
     * 根据应用编码查询树
     * @param appCode
     * @return
     */
    List<OrgnazitionTreeVO> queryTreeByAppCode(String appCode);



    /**
     * 查询全部树结构
     * @return
     */
    List<OrgnazitionTreeVO> queryTree();

    /**
     * 查询指定应用树结构
     * @return
     */
    List<OrgnazitionTreeVO> queryTree(String appCode);

    /**
     * 根据应用编码查询树
     * @param appCodeArray
     * @return
     */
    List<OrgnazitionTreeVO> queryTreeByAppCodeArray(String[] appCodeArray);
    /**
     * 保存
     * @param entity
     * @return
     */
    int save(Orgnazition entity);

    /**
     * 更新
     * @param entity
     * @return
     */
    int update(Orgnazition entity);



    /**
     * 判断是否存在
     * @param name
     * @return
     */
    boolean exists(String name);

    /**
     * 查询列表页
     * @param OrgnazitionTreeInfo
     * @return
     */
    PageInfo<Orgnazition> queryListPage(OrgnazitionTreeInfo OrgnazitionTreeInfo);

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
    List<OrgnazitionVO> queryListByAppCode(String appCode);

    /**
     * 查询所有子节点
     * @param children
     * @param query
     */
    void queryChildren(List<Orgnazition> children, Orgnazition query);


    /**
     * 查询树表格
     * @param OrgnazitionTreeInfo
     * @return
     */
    List<Orgnazition> findTreeTable(OrgnazitionTreeInfo OrgnazitionTreeInfo);





}
