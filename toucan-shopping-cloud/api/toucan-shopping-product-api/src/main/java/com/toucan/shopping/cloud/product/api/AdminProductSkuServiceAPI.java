package com.toucan.shopping.cloud.product.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AdminProductSkuServiceAPI {

    ResultObjectVO saveSku(RequestJsonVO requestJsonVO);

}
