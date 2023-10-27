package com.toucan.shopping.modules.seller.service;


import com.toucan.shopping.modules.seller.entity.SellerDesignerPageModel;

public interface SellerDesignerPageModelService {


    int save(SellerDesignerPageModel sellerDesignerPageModel);

    int update(SellerDesignerPageModel sellerDesignerPageModel);

    SellerDesignerPageModel queryLastOne(SellerDesignerPageModel sellerDesignerPageModel);


}
