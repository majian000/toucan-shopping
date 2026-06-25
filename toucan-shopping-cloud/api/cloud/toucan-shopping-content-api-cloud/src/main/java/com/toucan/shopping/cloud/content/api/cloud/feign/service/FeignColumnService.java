package com.toucan.shopping.cloud.content.api.cloud.feign.service;

import com.toucan.shopping.cloud.content.api.ColumnServiceAPI;
import com.toucan.shopping.cloud.content.api.cloud.feign.fallback.FeignColumnServiceFallbackFactory;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-content-proxy/column", fallbackFactory = FeignColumnServiceFallbackFactory.class)
public interface FeignColumnService extends ColumnServiceAPI {

    @Override
    @RequestMapping(value = "/query/list/page", produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/query/tree/table/by/pid", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/query/column/tree/pid", method = RequestMethod.POST)
    ResultObjectVO queryColumnTreeByPid(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo);

    @Override
    @RequestMapping(value = "/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo);

    @Override
    @RequestMapping(value = "/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultTypeObjectVO<ColumnVO> findById(@RequestBody RequestJsonVO requestVo);

    @Override
    @RequestMapping(value = "/update", produces = "application/json;charset=UTF-8")
    ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/query/list/by/pid", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO queryListByPid(@RequestBody RequestJsonVO requestJsonVO);

}
