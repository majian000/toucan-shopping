package com.toucan.shopping.cloud.content.api.feign.service;

import com.toucan.shopping.cloud.content.api.feign.fallback.FeignColumnServiceFallbackFactory;
import com.toucan.shopping.cloud.content.api.feign.fallback.FeignPcIndexColumnServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * PC首页栏目服务
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-content-proxy/column/pc/index",fallbackFactory = FeignPcIndexColumnServiceFallbackFactory.class)
public interface FeignPcIndexColumnService {


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);



    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);


    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询PC端首页栏目
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/pc/index/columns",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryPcIndexColumns(@RequestBody RequestJsonVO requestVo);


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findById(@RequestBody RequestJsonVO requestVo);

}
