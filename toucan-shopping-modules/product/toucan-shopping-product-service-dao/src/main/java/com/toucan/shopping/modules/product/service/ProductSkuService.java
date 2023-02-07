package com.toucan.shopping.modules.product.service;



import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;

import java.util.List;
import java.util.Map;

public interface ProductSkuService {

    List<ProductSkuVO> queryList(ProductSkuVO productSkuVO);

    ProductSkuVO queryVOByIdAndShelves(Long id);

    ProductSkuVO queryVOById(Long id);


    /**
     * 修改上架/下架状态(根据店铺商品ID)
     * @param shopProductId
     * @param status
     * @return
     */
    int updateStatusByShopProductId(Long shopProductId,Long shopId,Integer status);



    /**
     * 修改上架/下架状态(根据SKU ID)
     * @param id
     * @param status
     * @return
     */
    int updateStatusById(Long id,Long shopId,Integer status);

    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<ProductSkuVO> queryListPage(ProductSkuPageInfo queryPageInfo);


    List<ProductSkuVO> queryShelvesVOListByShopProductId(Long approveId);


    List<ProductSkuVO> queryProductSkuListByShopProductUuid(String shopProductUuid);

    /**
     * 根据店铺商品ID查询一个商品
     * @param shopProductId
     * @return
     */
    ProductSkuVO queryFirstOneByShopProductId(Long shopProductId);

    /**
     * 查询上架商品数量
     * @param shopProductId
     * @return
     */
    Long queryShelvesCountByShopProductId(Long shopProductId);

    /**
     * 保存sku
     * @param productSku
     * @return
     */
    int save(ProductSku productSku);

    int saves(List<ProductSku> productSkus);

    ProductSku queryById(Long id);

    ProductSku queryByUuid(String uuid);


    int deleteByShopProductId(Long shopProductId);



    /**
     * 删减库存
     * @param skuId
     * @param stockNum
     * @return
     */
    int inventoryReduction(Long skuId,Integer stockNum);

    /**
     * 还原库存
     * @param skuId
     * @return
     */
    int restoreStock(Long skuId,Integer stockNum);

    /**
     * 查询所有上架中的商品
     * @param idList
     * @return
     */
    List<ProductSku> queryShelvesListByIdList(List<Long> idList);
}
