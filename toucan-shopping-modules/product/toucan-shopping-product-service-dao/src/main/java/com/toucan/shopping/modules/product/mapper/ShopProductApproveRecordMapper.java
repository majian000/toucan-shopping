package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ShopProductApproveRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShopProductApproveRecordMapper {

    List<ShopProductApproveRecord> queryAllList(ShopProductApproveRecord queryModel);

    int insert(ShopProductApproveRecord entity);
}
