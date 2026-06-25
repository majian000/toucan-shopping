package com.toucan.shopping.cloud.product.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;

public interface ShopProductApproveServiceAPI {

    /**
     * 发布商品
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO publish(RequestJsonVO requestJsonVO);

    /**
     * 发布商品
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO republish(RequestJsonVO requestJsonVO);

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryByProductApproveId(RequestJsonVO requestJsonVO);

    /**
     * 根据ID和店铺ID查询
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryByProductApproveIdAndShopId(RequestJsonVO requestJsonVO);

    /**
     * 审核驳回
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO reject(RequestJsonVO requestJsonVO);

    /**
     * 审核通过
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO pass(RequestJsonVO requestJsonVO);

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO deleteById(RequestJsonVO requestJsonVO);

    /**
     * 根据ID和店铺ID删除
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO deleteByProductApproveIdAndShopId(RequestJsonVO requestJsonVO);

    /**
     * 查询这个店铺最新发布的那几条审核
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryNewestListByShopId(RequestJsonVO requestJsonVO);

    /**
     * 根据运费模板ID查询审核中的信息(一条)
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO findOneUnderReviewByFreightTemplateId(RequestJsonVO requestJsonVO);

    /**
     * 根据店铺ID查询所有审核中
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryApproveListByShopId(RequestJsonVO requestJsonVO);

    /**
     * 查询审核中数量
     * @param requestJsonVO
     * @return
     */
    ResultTypeObjectVO<Long> queryApproveCountByShopId(RequestJsonVO requestJsonVO);

}
