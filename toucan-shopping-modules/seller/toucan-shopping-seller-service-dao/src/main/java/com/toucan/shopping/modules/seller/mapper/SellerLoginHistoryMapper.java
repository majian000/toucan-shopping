package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.SellerLoginHistory;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface SellerLoginHistoryMapper {


    int insert(SellerLoginHistory entity);



}
