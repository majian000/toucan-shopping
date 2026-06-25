package com.toucan.shopping.cloud.search.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface ProductSearchServiceAPI {

    /**
     * 搜索商品
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO search(RequestJsonVO requestJsonVO);

    /**
     * 保存到搜索
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO save(RequestJsonVO requestJsonVO);

    /**
     * 根据SKUID查询
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryBySkuId(RequestJsonVO requestJsonVO);

    /**
     * 更新到搜索
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO update(RequestJsonVO requestJsonVO);

    /**
     * 从搜索中删除
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO removeById(RequestJsonVO requestJsonVO);

    /**
     * 清空
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO clear(RequestJsonVO requestJsonVO);

    /**
     * 搜索商品数量
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO count(RequestJsonVO requestJsonVO);

}
