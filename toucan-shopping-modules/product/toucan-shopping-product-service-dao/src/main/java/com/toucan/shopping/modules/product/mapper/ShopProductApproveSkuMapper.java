package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ShopProductApproveSku;
import com.toucan.shopping.modules.product.page.ShopProductApproveSkuPageInfo;
import com.toucan.shopping.modules.product.vo.ShopProductApproveSkuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShopProductApproveSkuMapper {

    List<ShopProductApproveSkuVO> queryList(ShopProductApproveSkuVO productSkuVO);

    ShopProductApproveSku queryBySkuIdForUpdate(Long skuId);

    int insert(ShopProductApproveSku productSku);

    int inserts(List<ShopProductApproveSku> entitys);

    ShopProductApproveSku queryById(Long id);


    ShopProductApproveSkuVO queryVOById(Long id);

    ShopProductApproveSku queryByUuid(String uuid);


    int deleteByShopProductApproveId(Long productApproveId);

    int deleteByIdList(List<Long> idList);

    int updateResumeByShopProductApproveId(Long productApproveId);

    int updateProductIdAndProductUuidByApproveId(Long approveId, Long productId, String productUuid);



    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<ShopProductApproveSkuVO> queryListPage(ShopProductApproveSkuPageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(ShopProductApproveSkuPageInfo pageInfo);

    List<ShopProductApproveSkuVO> queryListByProductApproveId(Long productApproveId);

}
