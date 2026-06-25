package com.toucan.shopping.cloud.content.api;

import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;

/**
 * 栏目服务
 */
public interface ColumnServiceAPI {

    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    ResultObjectVO save(RequestJsonVO requestJsonVO);

    ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO);

    ResultObjectVO queryColumnTreeByPid(RequestJsonVO requestJsonVO);

    ResultObjectVO deleteById(RequestJsonVO requestVo);

    ResultObjectVO deleteByIds(RequestJsonVO requestVo);

    ResultTypeObjectVO<ColumnVO> findById(RequestJsonVO requestVo);

    ResultObjectVO update(RequestJsonVO requestJsonVO);

    ResultObjectVO queryListByPid(RequestJsonVO requestJsonVO);
}
