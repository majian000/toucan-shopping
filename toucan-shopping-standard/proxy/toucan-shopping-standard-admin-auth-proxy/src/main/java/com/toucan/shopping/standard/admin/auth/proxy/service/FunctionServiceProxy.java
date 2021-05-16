package com.toucan.shopping.standard.admin.auth.proxy.service;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface FunctionServiceProxy {

    /**
     * 保存
     *
     * @param requestVo
     * @return
     */
    ResultObjectVO save(RequestJsonVO requestVo);


    /**
     * 编辑
     *
     * @param requestVo
     * @return
     */
    ResultObjectVO update(RequestJsonVO requestVo);


    /**
     * 查询树表格
     *
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryAppFunctionTreeTable(  RequestJsonVO requestJsonVO);
    
    
    /**
     * 根据ID删除指定角色
     *
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteById(RequestJsonVO requestVo);




    /**
     * 根据ID查询
     *
     * @param requestVo
     * @return
     */
    ResultObjectVO findById(RequestJsonVO requestVo);


    /**
     * 批量删除
     *
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds(RequestJsonVO requestVo);


    /**
     * 查询应用以及下面所有功能树
     *
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryAppFunctionTree(RequestJsonVO requestJsonVO);


    /**
     * 查询指定用户和应用的权限树
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryFunctionTree(  RequestJsonVO requestJsonVO);



    /**
     * 查询指定管理员应用所有角色的功能项
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryAdminAppFunctions(  RequestJsonVO requestJsonVO);


    /**
     * 查询下一级子节点
     *
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryChildren(  RequestJsonVO requestJsonVO);



    /**
     * 返回指定人的指定应用的某个上级功能项下的按钮列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryOneChildsByAdminIdAndAppCodeAndParentUrl(  RequestJsonVO requestJsonVO);

}
