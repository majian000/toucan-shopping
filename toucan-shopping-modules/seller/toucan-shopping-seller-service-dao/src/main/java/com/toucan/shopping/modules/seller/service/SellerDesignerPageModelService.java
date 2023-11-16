package com.toucan.shopping.modules.seller.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.SellerDesignerPageModel;
import com.toucan.shopping.modules.seller.page.SellerDesignerPageModelPageInfo;

public interface SellerDesignerPageModelService {


    int save(SellerDesignerPageModel sellerDesignerPageModel);

    int update(SellerDesignerPageModel sellerDesignerPageModel);

    SellerDesignerPageModel queryLastOne(SellerDesignerPageModel sellerDesignerPageModel);

    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<SellerDesignerPageModel> queryListPage(SellerDesignerPageModelPageInfo queryPageInfo);


}
