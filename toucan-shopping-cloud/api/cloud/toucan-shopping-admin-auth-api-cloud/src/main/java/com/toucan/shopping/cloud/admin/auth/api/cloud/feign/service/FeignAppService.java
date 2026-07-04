package com.toucan.shopping.cloud.admin.auth.api.cloud.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.AppServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.cloud.feign.fallback.FeignAppServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/app",fallbackFactory = FeignAppServiceFallbackFactory.class)
public interface FeignAppService extends AppServiceAPI {

    /**
     * 保存应用
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",method = RequestMethod.POST)
    ResultObjectVO save( @RequestBody RequestJsonVO requestVo);


    /**
     * 编辑应用
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",method = RequestMethod.POST)
    ResultObjectVO update( @RequestBody RequestJsonVO requestVo);


    /**
     * 查询应用列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/list/page",method = RequestMethod.POST)
    ResultObjectVO listPage( @RequestBody RequestJsonVO requestVo);


    /**
     * 根据ID删除指定应用
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",method = RequestMethod.DELETE)
    ResultObjectVO deleteById( @RequestBody RequestJsonVO requestVo);




    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",method = RequestMethod.POST)
    ResultObjectVO findById( @RequestBody RequestJsonVO requestVo);


    /**
     * 批量删除应用
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds( @RequestBody RequestJsonVO requestVo);


    /**
     * 应用列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO list(@RequestBody RequestJsonVO requestVo);


    /**
     * 根据编码查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/code", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO findByCode(@RequestBody RequestJsonVO requestVo);


    /**
     * 根据编码查询启用状态
     * @param requestVo
     * @return true:启用 false:停用
     */
    @RequestMapping(value="/enable/status/by/code", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO enableStatusByCode(@RequestBody RequestJsonVO requestVo);



    /**
     * 查询列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryListByCodes", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListByCodes(@RequestBody RequestJsonVO requestVo);

}
