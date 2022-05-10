package com.toucan.shopping.modules.product.service;



import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ShopProductApproveDescription;
import com.toucan.shopping.modules.product.entity.ShopProductApproveSku;
import com.toucan.shopping.modules.product.page.ShopProductApproveSkuPageInfo;
import com.toucan.shopping.modules.product.vo.ShopProductApproveSkuVO;

import java.util.List;

public interface ShopProductApproveDescriptionService {


    int save(ShopProductApproveDescription entity);

    int deleteByShopProductApproveId(Long productApproveId);

    ShopProductApproveDescription queryByApproveId(Long productApproveId);

}
