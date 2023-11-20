package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.SellerDesignerPageModel;
import com.toucan.shopping.modules.seller.page.SellerDesignerPageModelPageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SellerDesignerPageModelMapper {

    int insert(SellerDesignerPageModel sellerDesignerPageModel);

    int update(SellerDesignerPageModel sellerDesignerPageModel);

    SellerDesignerPageModel queryLastOne(SellerDesignerPageModel sellerDesignerPageModel);

    List<SellerDesignerPageModel> queryListPage(SellerDesignerPageModelPageInfo queryPageInfo);

    Long queryListPageCount(SellerDesignerPageModelPageInfo queryPageInfo);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);


}
