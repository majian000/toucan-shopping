package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.entity.SellerShopLoginHistory;
import com.toucan.shopping.modules.seller.page.SellerShopPageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SellerShopLoginHistoryMapper {


    int insert(SellerShopLoginHistory entity);



}
