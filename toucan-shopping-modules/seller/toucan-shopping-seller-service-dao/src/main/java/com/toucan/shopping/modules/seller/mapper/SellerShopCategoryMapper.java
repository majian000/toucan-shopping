package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.SellerShopCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SellerShopCategoryMapper {

    int insert(SellerShopCategory entity);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    int update(SellerShopCategory entity);

    List<SellerShopCategory> findListByEntity(SellerShopCategory query);

}
