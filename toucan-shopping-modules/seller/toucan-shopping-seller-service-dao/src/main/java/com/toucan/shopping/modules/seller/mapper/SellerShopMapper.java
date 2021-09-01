package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.page.SellerShopPageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SellerShopMapper {


    int insert(SellerShop entity);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    int update(SellerShop entity);

    int updateLogo(SellerShop entity);

    int updateNameAndIntroduce(SellerShop entity);

    List<SellerShop> findListByEntity(SellerShop query);

    List<SellerShop> findEnabledByUserMainId(Long userMainId);


    List<SellerShop> queryListPage(SellerShopPageInfo queryPageInfo);

    Long queryListPageCount(SellerShopPageInfo queryPageInfo);

}
