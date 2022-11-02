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
     * 修改上架/下架状态
     * @param shopProductId
     * @param status
     * @return
     */
    int updateStatusByShopProductId(Long shopProductId,Long shopId,Integer status);

    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<ProductSkuVO> queryListPage(ProductSkuPageInfo queryPageInfo);


    List<ProductSkuVO> queryShelvesVOListByShopProductId(Long approveId);


    List<ProductSkuVO> queryProductSkuListByShopProductUuid(String shopProductUuid);

    ProductSkuVO queryFirstOneByShopProductId(Long shopProductId);

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


}
