package com.toucan.shopping.cloud.content.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface ColumnAreaServiceAPI {

    ResultObjectVO queryColumnAreaList(RequestJsonVO requestJsonVO);

}
