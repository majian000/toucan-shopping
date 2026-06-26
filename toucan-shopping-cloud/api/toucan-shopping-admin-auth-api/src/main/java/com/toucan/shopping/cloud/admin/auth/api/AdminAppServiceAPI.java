package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AdminAppServiceAPI {

    ResultObjectVO save(RequestJsonVO requestVo);


    ResultObjectVO queryListByEntity(RequestJsonVO requestVo);


    ResultObjectVO deleteByAppCode(RequestJsonVO requestVo);


    ResultObjectVO queryAppListByAdminId(RequestJsonVO requestVo);


    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    ResultObjectVO list( RequestJsonVO requestVo);



    /**
     * 查询在线用户列表分页
     * @param requestVo
     * @return
     */
    ResultObjectVO onlineList( RequestJsonVO requestVo);


    /**
     * 查询登录列表分页
     * @param requestVo
     * @return
     */
    ResultObjectVO loginList( RequestJsonVO requestVo);



    /**
     * 修改账号登录状态
     * @param requestVo
     * @return
     */
    ResultObjectVO batchUpdateLoginStatus( RequestJsonVO requestVo);



    /**
     * 查询在线用户列表分页
     * @param requestVo
     * @return
     */
    ResultObjectVO logout( RequestJsonVO requestVo);


    /**
     * 查询APP登录用户信息
     * @param requestVo
     * @return
     */
    ResultObjectVO queryAppLoginUserCountList( RequestJsonVO requestVo);




}
