package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ShopProductDescriptionImg;
import com.toucan.shopping.modules.product.vo.ShopProductDescriptionImgVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShopProductDescriptionImgMapper {

    List<ShopProductDescriptionImg> queryAllList(ShopProductDescriptionImg queryModel);


    List<ShopProductDescriptionImg> queryList(ShopProductDescriptionImgVO queryModel);

    int inserts(List<ShopProductDescriptionImg> entitys);

    List<ShopProductDescriptionImg> queryByIdList(List<Long> ids);

    int deleteByShopProductId(Long shopProductId);

    List<ShopProductDescriptionImgVO> queryVOListByProductIdAndDescriptionIdOrderBySortDesc(Long shopProductId, Long descriptionId);
    List<ShopProductDescriptionImgVO> queryVOListBySkuIdAndDescriptionIdOrderBySortDesc(Long skuId, Long descriptionId);

    int updateResumeByIdList(List<Long> idList);

}
