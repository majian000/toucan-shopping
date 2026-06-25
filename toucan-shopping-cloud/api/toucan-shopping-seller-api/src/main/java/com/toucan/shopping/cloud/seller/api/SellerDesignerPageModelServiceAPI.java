package com.toucan.shopping.cloud.seller.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 店铺装修页面模型
 * @author majian
 */
public interface SellerDesignerPageModelServiceAPI {

    /**
     * 只保存一条
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO onlySaveOne(RequestJsonVO requestJsonVO);

    /**
     * 查询最近一条
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryLastOne(RequestJsonVO requestJsonVO);

    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    ResultObjectVO queryListPage(RequestJsonVO requestVo);

    /**
     * 管理员根据ID删除
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO deleteByIdForAdmin(RequestJsonVO requestJsonVO);

}
