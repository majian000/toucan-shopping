package com.toucan.shopping.cloud.product.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface ShopProductApproveSkuServiceAPI {

    ResultObjectVO queryById(RequestJsonVO requestJsonVO);

    ResultObjectVO queryByIdList(RequestJsonVO requestJsonVO);

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryByIdForFront(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询,只查询1个sku(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryOneByProductApproveIdForFront(RequestJsonVO requestJsonVO);

}
