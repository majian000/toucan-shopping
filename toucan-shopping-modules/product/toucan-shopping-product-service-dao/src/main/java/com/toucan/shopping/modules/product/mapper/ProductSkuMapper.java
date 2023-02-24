package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
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

    List<ProductSkuVO> queryVOListByShopProductIdAndStatus(Long shopProductId,int status) ;

    int update(ProductSku productSku);

    int deleteByShopProductId(Long shopProductId);

    int updateStock(Long id, Integer stockNum);

    int updatePrice(Long id, BigDecimal price);

    ProductSkuVO queryVOByIdAndShelves(Long id);

    ProductSkuVO queryVOById(Long id);

    ProductSkuVO queryVOByIdAndStatus(Long id,int status);

    ProductSkuVO queryFirstOneByShopProductId(Long shopProductId);

    ProductSkuVO queryFirstOneByShopProductIdAndStatus(Long shopProductId,int status);

    int updateStatusByShopProductId(Long shopProductId, Long shopId, Integer status);

    int updateStatusById(Long id,Long shopId,Integer status);

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

    List<ProductSkuVO> queryProductSkuListByShopProductId(Long shopProductId);


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


    /**
     * 查询上架商品数量
     * @param shopProductId
     * @return
     */
    Long queryShelvesCountByShopProductId(Long shopProductId);

}
