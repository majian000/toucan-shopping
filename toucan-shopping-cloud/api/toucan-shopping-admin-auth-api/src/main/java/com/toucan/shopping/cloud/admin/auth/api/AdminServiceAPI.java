package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.web.bind.annotation.*;

public interface AdminServiceAPI {

    /**
     * 登录账号
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO login( String signHeader, RequestJsonVO requestVo);

    ResultObjectVO queryLoginToken( String signHeader, RequestJsonVO requestVo);

    ResultObjectVO isOnline( String signHeader, RequestJsonVO requestVo);



    ResultObjectVO queryListByEntity( String signHeader, RequestJsonVO requestVo);

    /**
     * 保存
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO save( String signHeader,  RequestJsonVO requestVo);



    /**
     * 编辑
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO update( String signHeader,  RequestJsonVO requestVo);



    /**
     * 列表分页
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO list( String signHeader, RequestJsonVO requestVo);


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findById( String signHeader, RequestJsonVO requestVo);


    /**
     * 退出登录
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO logout( String signHeader, RequestJsonVO requestVo);


    /**
     * 修改密码
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO updatePassword( String signHeader, RequestJsonVO requestVo);





    /**
     * 根据ID删除指定角色
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteById( String signHeader,  RequestJsonVO requestVo);


    /**
     * 批量删除
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds( String signHeader,  RequestJsonVO requestVo);




}
