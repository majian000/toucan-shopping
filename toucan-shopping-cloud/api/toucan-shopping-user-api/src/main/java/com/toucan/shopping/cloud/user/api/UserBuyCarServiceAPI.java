package com.toucan.shopping.cloud.user.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 用户购物车
 * @author majian
 */
public interface UserBuyCarServiceAPI {


    ResultObjectVO save(RequestJsonVO requestJsonVO);


    /**
     * 移除购物车
     * @param requestVo
     * @return
     */
    ResultObjectVO removeBuyCar(RequestJsonVO requestVo);


    ResultObjectVO listByUserMainId(RequestJsonVO requestJsonVO);



    /**
     * 清空指定用户的购物车
     * @param requestVo
     * @return
     */
    ResultObjectVO clearByUserMainId(RequestJsonVO requestVo);



    ResultObjectVO updates(RequestJsonVO requestJsonVO);


    ResultObjectVO update(RequestJsonVO requestJsonVO);

}
