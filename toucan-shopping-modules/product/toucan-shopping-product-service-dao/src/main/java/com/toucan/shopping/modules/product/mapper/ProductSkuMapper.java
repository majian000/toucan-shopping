package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;



@Mapper
public interface ProductSkuMapper {

    List<ProductSkuVO> queryList(ProductSkuVO productSkuVO);

    ProductSku queryBySkuIdForUpdate(Long skuId);

    int insert(ProductSku productSku);

    int inserts(List<ProductSku> entitys);

    ProductSku queryById(Long id);

    ProductSku queryByUuid(String uuid);

    List<ProductSkuVO> queryShelvesVOListByShopProductId(Long shopProductId);

    int deleteByShopProductId(Long shopProductId);


    ProductSkuVO queryVOByIdAndShelves(Long id);

    ProductSkuVO queryVOById(Long id);

    ProductSkuVO queryFirstOneByShopProductId(Long shopProductId);

    int updateStatusByShopProductId(Long shopProductId, Long shopId, Integer status);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<ProductSkuVO> queryListPage(ProductSkuPageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(ProductSkuPageInfo pageInfo);

    List<ProductSkuVO> queryProductSkuListByShopProductUuid(String shopProductUuid);

    List<ProductSku> queryShelvesListByIdList(List<Long> idList);

    /**
     * 扣库存
     * @param skuId
     * @param stockNum
     * @return
     */
    int inventoryReduction(Long skuId,Integer stockNum);

    /**
     * 还原扣的库存
     * @param skuId
     * @param stockNum
     * @return
     */
    int restoreStock(Long skuId,Integer stockNum);

}
