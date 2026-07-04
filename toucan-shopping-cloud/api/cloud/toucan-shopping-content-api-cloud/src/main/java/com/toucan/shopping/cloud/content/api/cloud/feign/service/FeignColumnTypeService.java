package com.toucan.shopping.cloud.content.api.cloud.feign.service;

import com.toucan.shopping.cloud.content.api.ColumnTypeServiceAPI;
import com.toucan.shopping.cloud.content.api.cloud.feign.fallback.FeignColumnTypeServiceFallbackFactory;
import com.toucan.shopping.modules.column.vo.ColumnTypeVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-content-proxy/columnType", fallbackFactory = FeignColumnTypeServiceFallbackFactory.class)
public interface FeignColumnTypeService extends ColumnTypeServiceAPI {

    @Override
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo);

    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO update(@RequestBody RequestJsonVO requestVo);

    @Override
    @RequestMapping(value = "/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO findById(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/query/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryList(@RequestBody RequestJsonVO requestJsonVO);

    @Override
    @RequestMapping(value = "/find/one/code", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultTypeObjectVO<ColumnTypeVO> findOneByCode(@RequestBody RequestJsonVO requestVo);

}
