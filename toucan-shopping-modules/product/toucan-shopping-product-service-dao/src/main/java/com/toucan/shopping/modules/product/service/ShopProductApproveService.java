package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ShopProductApprove;
import com.toucan.shopping.modules.product.page.ShopProductApprovePageInfo;
import com.toucan.shopping.modules.product.vo.ShopProductApproveVO;

import java.util.List;

public interface ShopProductApproveService {

    List<ShopProductApprove> queryAllList(ShopProductApprove queryModel);

    int save(ShopProductApprove entity);

    int deleteById(Long id);

    ShopProductApproveVO findById(Long id);

    int updateApproveStatus(Long id, Integer approveStatus);

    int updateApproveStatusAndRejectText(Long id, Integer approveStatus,String rejectedText);

    int updateApproveStatusAndProductId(Long id, Integer approveStatus, Long productId, String productUuid);

    int updateApproveStatusAndProductIdAndRejectText(Long id, Integer approveStatus, Long productId, String productUuid,String rejectedText);

    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<ShopProductApproveVO> queryListPage(ShopProductApprovePageInfo queryPageInfo);

    List<ShopProductApproveVO> queryList(ShopProductApproveVO shopProductVO);

    ShopProductApproveVO queryOne(ShopProductApproveVO shopProductVO);

    int updateShopProductId(Long id, Long shopProductId);

}
