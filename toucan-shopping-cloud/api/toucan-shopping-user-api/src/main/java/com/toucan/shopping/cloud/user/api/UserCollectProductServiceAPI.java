package com.toucan.shopping.cloud.user.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 用户收藏商品
 */
public interface UserCollectProductServiceAPI {



    ResultObjectVO save(RequestJsonVO requestJsonVO);



    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteBySkuIdAndUserMainIdAndAppCode(RequestJsonVO requestVo);



    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteById(RequestJsonVO requestVo);

    /**
     * 查询收藏列表
     * @param requestVo
     * @return
     */
    ResultObjectVO queryCollectProducts(RequestJsonVO requestVo);



    /**
     * 查询列表页
     */
    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);



    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds(RequestJsonVO requestVo);


}
