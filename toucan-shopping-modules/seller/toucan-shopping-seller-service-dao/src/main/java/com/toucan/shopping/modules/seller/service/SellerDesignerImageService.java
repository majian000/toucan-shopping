package com.toucan.shopping.modules.seller.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.SellerDesignerImage;
import com.toucan.shopping.modules.seller.entity.ShopBanner;
import com.toucan.shopping.modules.seller.page.SellerDesignerImagePageInfo;
import com.toucan.shopping.modules.seller.vo.SellerDesignerImageVO;

public interface SellerDesignerImageService {


    SellerDesignerImage findById(Long id);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<SellerDesignerImageVO> queryListPage(SellerDesignerImagePageInfo pageInfo);


    /**
     * 保存实体
     * @param entity
     * @return
     */
    int save(SellerDesignerImage entity);


    int deleteById(Long id);

    int deleteByIdAndShopId(Long id,Long shopId);

    int update(SellerDesignerImage entity);


}
