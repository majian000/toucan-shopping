package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ShopProductSpuApproveRecord;
import com.toucan.shopping.modules.product.entity.ShopProductSpuImg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShopProductSpuImgMapper {

    List<ShopProductSpuImg> queryAllList(ShopProductSpuImg queryModel);

}
