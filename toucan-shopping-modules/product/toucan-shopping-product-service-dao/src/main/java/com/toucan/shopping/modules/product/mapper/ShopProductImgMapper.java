package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ShopProductImg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShopProductImgMapper {

    List<ShopProductImg> queryAllList(ShopProductImg queryModel);

}
