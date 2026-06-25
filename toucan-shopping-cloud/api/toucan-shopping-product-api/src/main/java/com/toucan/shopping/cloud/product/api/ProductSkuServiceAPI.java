package com.toucan.shopping.cloud.product.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultListVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;

public interface ProductSkuServiceAPI {

    /**
     * 查询上架列表
     * @param requestJsonVO
     * @return
     */
    ResultListVO queryShelvesList(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryById(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryByIdList(RequestJsonVO requestJsonVO);

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryList(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryByIdForFront(RequestJsonVO requestJsonVO);

    /**
     * 修改库存
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updateStock(RequestJsonVO requestJsonVO);

    /**
     * 修改单价
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updatePrice(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询,只查询1个sku(商城PC端预览使用)
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryOneByShopProductIdForFrontPreview(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询(商城PC端预览使用)
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryByIdForFrontPreview(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询,只查询1个sku(商城PC端使用)
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryOneByShopProductIdForFront(RequestJsonVO requestJsonVO);

    /**
     * 商品上架/下架
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO shelves(RequestJsonVO requestJsonVO);

    /**
     * 扣库存
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO inventoryReduction(RequestJsonVO requestJsonVO);

    /**
     * 恢复扣库存
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO restoreStock(RequestJsonVO requestJsonVO);

    /**
     * 修改商品预览图
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updatePreviewPhoto(RequestJsonVO requestJsonVO);

    /**
     * 修改商品介绍图
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updateDescriptionPhoto(RequestJsonVO requestJsonVO);

    /**
     * 移除商品介绍图
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO removeDescriptionPhoto(RequestJsonVO requestJsonVO);

    /**
     * 根据店铺商品ID查询SKU列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListByShopProductIdList(RequestJsonVO requestJsonVO);

    /**
     * 根据店铺ID查询上架商品数量
     * @param requestJsonVO
     * @return
     */
    ResultTypeObjectVO<Long> queryShelvesCountByShopId(RequestJsonVO requestJsonVO);

}
