package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.SellerDesignerPageModel;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface SellerDesignerPageModelMapper {

    int insert(SellerDesignerPageModel sellerDesignerPageModel);

    int update(SellerDesignerPageModel sellerDesignerPageModel);

    SellerDesignerPageModel queryLastOne(SellerDesignerPageModel sellerDesignerPageModel);


}
