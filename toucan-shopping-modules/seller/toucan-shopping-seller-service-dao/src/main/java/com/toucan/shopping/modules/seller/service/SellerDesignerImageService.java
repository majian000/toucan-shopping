package com.toucan.shopping.modules.seller.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.SellerDesignerImage;
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

}
