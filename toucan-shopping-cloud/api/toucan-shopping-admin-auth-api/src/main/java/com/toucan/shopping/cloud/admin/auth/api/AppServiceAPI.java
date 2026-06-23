package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.web.bind.annotation.*;

public interface AppServiceAPI {

    /**
     * 保存应用
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO save( String signHeader,  RequestJsonVO requestVo);


    /**
     * 编辑应用
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO update( String signHeader,  RequestJsonVO requestVo);


    /**
     * 查询应用列表
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO listPage( String signHeader,  RequestJsonVO requestVo);


    /**
     * 根据ID删除指定应用
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteById( String signHeader,  RequestJsonVO requestVo);




    /**
     * 根据ID查询
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO findById( String signHeader,  RequestJsonVO requestVo);


    /**
     * 批量删除应用
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds( String signHeader,  RequestJsonVO requestVo);


    /**
     * 应用列表
     * @param requestVo
     * @return
     */
    ResultObjectVO list( String signHeader, RequestJsonVO requestVo);


    /**
     * 根据编码查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findByCode( String signHeader, RequestJsonVO requestVo);


    /**
     * 根据编码查询启用状态
     * @param requestVo
     * @return true:启用 false:停用
     */
    ResultObjectVO enableStatusByCode( RequestJsonVO requestVo);



    /**
     * 查询列表
     * @param requestVo
     * @return
     */
    ResultObjectVO queryListByCodes( RequestJsonVO requestVo);

}
