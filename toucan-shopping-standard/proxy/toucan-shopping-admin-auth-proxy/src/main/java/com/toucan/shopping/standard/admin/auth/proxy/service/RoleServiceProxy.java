package com.toucan.shopping.standard.admin.auth.proxy.service;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface RoleServiceProxy {

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
     * 查询列表
     *
     * @param requestVo
     * @return
     */
    ResultObjectVO listPage(RequestJsonVO requestVo);



    /**
     * 查询指定用户的角色树
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryAdminRoleTree(  RequestJsonVO requestJsonVO);




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



}
