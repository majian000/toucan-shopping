package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ShopProductApproveDescriptionImg;
import com.toucan.shopping.modules.product.vo.ShopProductApproveDescriptionImgVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShopProductApproveDescriptionImgMapper {

    List<ShopProductApproveDescriptionImg> queryAllList(ShopProductApproveDescriptionImg queryModel);


    List<ShopProductApproveDescriptionImg> queryList(ShopProductApproveDescriptionImgVO queryModel);

    int inserts(List<ShopProductApproveDescriptionImg> entitys);

    List<ShopProductApproveDescriptionImg> queryByIdList(List<Long> ids);

    int deleteByProductApproveId(Long productApproveId);


}
