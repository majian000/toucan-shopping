package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ShopProductApprove;
import com.toucan.shopping.modules.product.page.ShopProductApprovePageInfo;
import com.toucan.shopping.modules.product.vo.ShopProductApproveVO;

import java.util.Date;
import java.util.List;

public interface ShopProductApproveService {

    List<ShopProductApprove> queryAllList(ShopProductApprove queryModel);

    /**
     * 查询最新审核的4个
     * @param shopId
     * @return
     */
    List<ShopProductApproveVO> queryNewestListByShopId(Long shopId,Integer limit);

    int save(ShopProductApprove entity);

    /**
     * 重新发布
     * @param entity
     * @return
     */
    int updateForRepublish(ShopProductApprove entity);

    int deleteById(Long id);

    ShopProductApproveVO findById(Long id);

    int updateApproveStatus(Long id, Integer approveStatus);

    int updateApproveStatusAndRejectText(Long id, Integer approveStatus,String rejectedText);

    int updateApproveStatusAndRejectTextAndUpdateDate(Long id, Integer approveStatus, String rejectedText, Date updateDate);

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

    ShopProductApproveVO queryById(Long id);

    int updateShopProductId(Long id, Long shopProductId);

}
