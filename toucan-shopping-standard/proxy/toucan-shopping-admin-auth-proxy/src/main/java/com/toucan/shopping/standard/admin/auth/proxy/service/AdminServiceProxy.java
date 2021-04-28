package com.toucan.shopping.standard.admin.auth.proxy.service;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AdminServiceProxy {

    /**
     * 登录账号
     *
     * @param requestVo
     * @return
     */
    ResultObjectVO login(  RequestJsonVO requestVo);

    ResultObjectVO queryLoginToken(  RequestJsonVO requestVo);

    ResultObjectVO isOnline(  RequestJsonVO requestVo);



    ResultObjectVO queryListByEntity(  RequestJsonVO requestVo);

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
     * 列表分页
     *
     * @param requestVo
     * @return
     */
    ResultObjectVO list(  RequestJsonVO requestVo);


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findById(  RequestJsonVO requestVo);


    /**
     * 退出登录
     *
     * @param requestVo
     * @return
     */
    ResultObjectVO logout(  RequestJsonVO requestVo);


    /**
     * 修改密码
     *
     * @param requestVo
     * @return
     */
    ResultObjectVO updatePassword(  RequestJsonVO requestVo);

}
