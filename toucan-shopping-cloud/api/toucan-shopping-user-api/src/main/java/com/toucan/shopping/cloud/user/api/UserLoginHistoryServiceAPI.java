package com.toucan.shopping.cloud.user.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface UserLoginHistoryServiceAPI {




    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    ResultObjectVO queryListPage(RequestJsonVO requestVo);


    /**
     * 查询10条最近登录的记录
     * @param requestVo
     * @return
     */
    ResultObjectVO queryListByLatest10(RequestJsonVO requestVo);

}
