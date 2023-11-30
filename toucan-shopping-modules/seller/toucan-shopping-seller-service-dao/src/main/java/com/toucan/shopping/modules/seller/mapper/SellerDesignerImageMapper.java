package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.SellerDesignerImage;
import com.toucan.shopping.modules.seller.page.SellerDesignerImagePageInfo;
import com.toucan.shopping.modules.seller.vo.SellerDesignerImageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 店铺图片
 * @author majian
 */
@Mapper
public interface SellerDesignerImageMapper {

    SellerDesignerImage findById(Long id);


    int insert(SellerDesignerImage entity);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<SellerDesignerImageVO> queryListPage(SellerDesignerImagePageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(SellerDesignerImagePageInfo pageInfo);

    int deleteById(Long id);

    int deleteByIdAndShopId(Long id,Long shopId) ;

    int update(SellerDesignerImage sellerDesignerImage);

}
