package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.SellerDesignerPage;
import com.toucan.shopping.modules.seller.entity.ShopBanner;
import com.toucan.shopping.modules.seller.page.ShopBannerPageInfo;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SellerDesignerPageMapper {

    int insert(SellerDesignerPage sellerDesignerPage);

    int update(SellerDesignerPage sellerDesignerPage);

    SellerDesignerPage queryLastOne(SellerDesignerPage sellerDesignerPage);


}
