package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface OperateLogServiceAPI {


    /**
     * 批量保存
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO saves( RequestJsonVO requestJsonVO);


    /**
     * 查询操作日志统计表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryOperateChart( RequestJsonVO requestJsonVO);


    /**
     * 查询列表
     * @param requestVo
     * @return
     */
    ResultObjectVO listPage( RequestJsonVO requestVo);


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findById( RequestJsonVO requestVo);



}
