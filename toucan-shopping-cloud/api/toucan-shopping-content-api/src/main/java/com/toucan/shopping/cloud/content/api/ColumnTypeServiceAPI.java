package com.toucan.shopping.cloud.content.api;

import com.toucan.shopping.modules.column.vo.ColumnTypeVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;

/**
 * 栏目类型服务
 */
public interface ColumnTypeServiceAPI {

    ResultObjectVO save(RequestJsonVO requestJsonVO);

    ResultObjectVO deleteById(RequestJsonVO requestJsonVO);

    ResultObjectVO deleteByIds(RequestJsonVO requestVo);

    ResultObjectVO update(RequestJsonVO requestVo);

    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    ResultObjectVO findById(RequestJsonVO requestJsonVO);

    ResultObjectVO queryList(RequestJsonVO requestJsonVO);

    ResultTypeObjectVO<ColumnTypeVO> findOneByCode(RequestJsonVO requestVo);
}
