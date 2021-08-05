package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.SellerShopApproveRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SellerShopApproveRecordMapper {

    int insert(SellerShopApproveRecord entity);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    int update(SellerShopApproveRecord entity);

    List<SellerShopApproveRecord> findListByEntity(SellerShopApproveRecord query);

}
