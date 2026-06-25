package com.toucan.shopping.cloud.seller.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 商家登录历史
 * @author majian
 */
public interface SellerLoginHistoryServiceAPI {

    /**
     * 保存
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO save(RequestJsonVO requestJsonVO);

    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    ResultObjectVO queryListPage(RequestJsonVO requestVo);

    /**
     * 查询最近10条登录记录
     * @param requestVo
     * @return
     */
    ResultObjectVO queryListByLatest10(RequestJsonVO requestVo);

}
