package com.toucan.shopping.cloud.content.api.feign.service;

import com.toucan.shopping.cloud.content.api.feign.fallback.FeignColumnServiceFallbackFactory;
import com.toucan.shopping.cloud.content.api.feign.fallback.FeignColumnTypeServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 栏目服务
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-content-proxy/column",fallbackFactory = FeignColumnServiceFallbackFactory.class)
public interface FeignColumnService {


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);



    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 查询栏目树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/column/tree/pid",method = RequestMethod.POST)
    ResultObjectVO queryColumnTreeByPid(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 删除指定栏目
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo);



    /**
     * 批量删除栏目
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo);

}
