package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.SellerLoginHistory;
import com.toucan.shopping.modules.seller.page.SellerLoginHistoryPageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SellerLoginHistoryMapper {

    int insert(SellerLoginHistory entity);

    List<SellerLoginHistory> queryListPage(SellerLoginHistoryPageInfo queryPageInfo);

    Long queryListPageCount(SellerLoginHistoryPageInfo queryPageInfo);


}
