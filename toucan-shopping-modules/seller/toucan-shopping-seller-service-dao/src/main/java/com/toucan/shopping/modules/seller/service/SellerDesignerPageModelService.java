package com.toucan.shopping.modules.seller.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.SellerDesignerPageModel;
import com.toucan.shopping.modules.seller.page.SellerDesignerPageModelPageInfo;

import java.util.List;

public interface SellerDesignerPageModelService {


    int save(SellerDesignerPageModel sellerDesignerPageModel);

    int update(SellerDesignerPageModel sellerDesignerPageModel);

    SellerDesignerPageModel queryLastOne(SellerDesignerPageModel sellerDesignerPageModel);

    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<SellerDesignerPageModel> queryListPage(SellerDesignerPageModelPageInfo queryPageInfo);

    List<SellerDesignerPageModel> queryList(SellerDesignerPageModel sellerDesignerPageModel);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);


}
