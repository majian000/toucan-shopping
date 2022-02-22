package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ShopProductImg;
import com.toucan.shopping.modules.product.vo.ShopProductImgVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShopProductImgMapper {

    List<ShopProductImg> queryAllList(ShopProductImg queryModel);


    List<ShopProductImg> queryList(ShopProductImgVO queryModel);

    int inserts(List<ShopProductImg> entitys);

    List<ShopProductImg> queryByIdList(List<Long> ids);


}
