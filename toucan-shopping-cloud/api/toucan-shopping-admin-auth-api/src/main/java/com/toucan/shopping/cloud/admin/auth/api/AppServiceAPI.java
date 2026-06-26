package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.web.bind.annotation.*;

public interface AppServiceAPI {

    /**
     * 保存应用

     * @param requestVo
     * @return
     */
    ResultObjectVO save(   RequestJsonVO requestVo);


    /**
     * 编辑应用

     * @param requestVo
     * @return
     */
    ResultObjectVO update(   RequestJsonVO requestVo);


    /**
     * 查询应用列表

     * @param requestVo
     * @return
     */
    ResultObjectVO listPage(   RequestJsonVO requestVo);


    /**
     * 根据ID删除指定应用

     * @param requestVo
     * @return
     */
    ResultObjectVO deleteById(   RequestJsonVO requestVo);




    /**
     * 根据ID查询

     * @param requestVo
     * @return
     */
    ResultObjectVO findById(   RequestJsonVO requestVo);


    /**
     * 批量删除应用

     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds(   RequestJsonVO requestVo);


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
    ResultObjectVO findByCode(  RequestJsonVO requestVo);


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
