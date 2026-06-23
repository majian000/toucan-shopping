package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.web.bind.annotation.*;


public interface FunctionServiceAPI {

    /**
     * 保存
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO save( String signHeader,  RequestJsonVO requestVo);



    /**
     * 添加功能项
     * @param requestVo
     * @return
     */
    ResultObjectVO saves( RequestJsonVO requestVo);

    /**
     * 编辑
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO update( String signHeader,  RequestJsonVO requestVo);


    /**
     * 查询树表格
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryAppFunctionTreeTable( String signHeader, RequestJsonVO requestJsonVO);



    /**
     * 根据PID查询树表格
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryAppFunctionTreeTableByPid( String signHeader, RequestJsonVO requestJsonVO);
    
    /**
     * 根据ID删除
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteById( String signHeader,  RequestJsonVO requestVo);




    /**
     * 删除指定功能项
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByAppCode( RequestJsonVO requestVo);


    /**
     * 根据ID查询
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO findById( String signHeader,  RequestJsonVO requestVo);


    /**
     * 批量删除
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds( String signHeader,  RequestJsonVO requestVo);


    /**
     * 查询应用以及下面所有功能树
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryAppFunctionTree( String signHeader,  RequestJsonVO requestJsonVO);



    /**
     * 查询应用权限列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryAppFunctionTreeByPid( RequestJsonVO requestJsonVO);

    /**
     * 查询指定用户和应用的权限树
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryFunctionTree( String signHeader, RequestJsonVO requestJsonVO);



    /**
     * 查询指定管理员应用所有角色的功能项
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryAdminAppFunctions( String signHeader, RequestJsonVO requestJsonVO);


    /**
     * 查询下一级子节点
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryChildren( String signHeader, RequestJsonVO requestJsonVO);



    /**
     * 返回指定人的指定应用的某个上级功能项下的按钮列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryOneChildsByAdminIdAndAppCodeAndParentUrl( String signHeader, RequestJsonVO requestJsonVO);


    /**
     * 列表分页
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO list( String signHeader, RequestJsonVO requestVo);


}
