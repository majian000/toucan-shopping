package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ShopProduct;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.vo.ShopProductVO;

import java.util.List;

public interface ShopProductService {

    List<ShopProduct> queryAllList(ShopProduct queryModel);

    int save(ShopProduct entity);

    int deleteById(Long id);

    ShopProductVO findById(Long id);

    int updateApproveStatus(Long id,Integer approveStatus);

    int updateApproveStatusAndProductId(Long id,Integer approveStatus,Long productId,String productUuid);

    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<ShopProductVO> queryListPage(ShopProductPageInfo queryPageInfo);

    List<ShopProductVO> queryList(ShopProductVO shopProductVO);

}
