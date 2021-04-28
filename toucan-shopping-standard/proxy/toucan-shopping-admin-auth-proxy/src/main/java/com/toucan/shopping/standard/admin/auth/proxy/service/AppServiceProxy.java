package com.toucan.shopping.standard.admin.auth.proxy.service;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AppServiceProxy {

    /**
     * 保存应用
     * @param requestVo
     * @return
     */
    ResultObjectVO save(RequestJsonVO requestVo);


    /**
     * 编辑应用
     * @param requestVo
     * @return
     */
    ResultObjectVO update(RequestJsonVO requestVo);


    /**
     * 查询应用列表
     * @param requestVo
     * @return
     */
    ResultObjectVO listPage(RequestJsonVO requestVo);


    /**
     * 根据ID删除指定应用
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteById(RequestJsonVO requestVo);




    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findById(RequestJsonVO requestVo);


    /**
     * 批量删除应用
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds(RequestJsonVO requestVo);


    /**
     * 应用列表
     * @param requestVo
     * @return
     */
    ResultObjectVO list(  RequestJsonVO requestVo);


    /**
     * 根据编码查询
     * @param requestVo
     * @return
     */
    public ResultObjectVO findByCode(  RequestJsonVO requestVo);

}
