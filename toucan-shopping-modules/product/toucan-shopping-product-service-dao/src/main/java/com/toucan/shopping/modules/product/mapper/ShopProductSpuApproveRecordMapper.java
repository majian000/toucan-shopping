package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ShopProductSpu;
import com.toucan.shopping.modules.product.entity.ShopProductSpuApproveRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShopProductSpuApproveRecordMapper {

    List<ShopProductSpuApproveRecord> queryAllList(ShopProductSpuApproveRecord queryModel);

}
