package com.toucan.shopping.cloud.seller.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 店铺轮播图
 * @author majian
 */
public interface ShopBannerServiceAPI {

    /**
     * 查询列表页
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    /**
     * 保存轮播图
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO save(RequestJsonVO requestJsonVO);

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO deleteById(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findById(RequestJsonVO requestVo);

    /**
     * 修改轮播图
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO update(RequestJsonVO requestJsonVO);

    /**
     * 管理员根据ID删除
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO deleteByIdForAdmin(RequestJsonVO requestJsonVO);

    /**
     * 查询首页列表
     * @param requestVo
     * @return
     */
    ResultObjectVO queryIndexList(RequestJsonVO requestVo);

}
