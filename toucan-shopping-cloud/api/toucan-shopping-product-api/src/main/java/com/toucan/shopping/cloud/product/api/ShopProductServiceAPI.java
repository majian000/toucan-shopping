package com.toucan.shopping.cloud.product.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface ShopProductServiceAPI {

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    ResultObjectVO queryListByShopProductUuid(RequestJsonVO requestJsonVO);

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryList(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryByShopProductId(RequestJsonVO requestJsonVO);

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO deleteById(RequestJsonVO requestJsonVO);

    /**
     * 商品上架/下架
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO shelves(RequestJsonVO requestJsonVO);

    /**
     * 根据运费模板ID查询关联的商品
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryOneByFreightTemplateId(RequestJsonVO requestJsonVO);

    /**
     * 修改运费模板
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updateFreightTemplate(RequestJsonVO requestJsonVO);

}
