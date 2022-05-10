package com.toucan.shopping.modules.product.service;



import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ShopProductApproveSku;
import com.toucan.shopping.modules.product.page.ShopProductApproveSkuPageInfo;
import com.toucan.shopping.modules.product.vo.ShopProductApproveSkuVO;

import java.util.List;

public interface ShopProductApproveSkuService {

    List<ShopProductApproveSkuVO> queryList(ShopProductApproveSkuVO productSkuVO);



    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<ShopProductApproveSkuVO> queryListPage(ShopProductApproveSkuPageInfo queryPageInfo);

    /**
     * 更新审核SKU表中 商品ID和商品UUID
     * @param approveId
     * @param productId
     * @param productUuid
     * @return
     */
    int updateProductIdAndProductUuidByApproveId(Long approveId,  Long productId, String productUuid);

    /**
     * 保存sku
     * @param productSku
     * @return
     */
    int save(ShopProductApproveSku productSku);

    int saves(List<ShopProductApproveSku> productSkus);

    ShopProductApproveSku queryById(Long id);

    ShopProductApproveSku queryByUuid(String uuid);


    int deleteByShopProductApproveId(Long productApproveId);


    int deleteByIdList(List<Long> idList);


    int updateResumeByShopProductApproveId(Long productApproveId);

    List<ShopProductApproveSkuVO> queryListByProductApproveId(Long productApproveId);


}
