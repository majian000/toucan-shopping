package com.toucan.shopping.cloud.content.api.feign.service;

import com.toucan.shopping.cloud.content.api.feign.fallback.FeignBannerServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-content-proxy/banner",fallbackFactory = FeignBannerServiceFallbackFactory.class)
public interface FeignBannerService {


    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader,@RequestBody RequestJsonVO requestJsonVO);



    @RequestMapping(value="/query/list",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryList(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 清空首页缓存
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/index/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO flushWebIndexCache(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 查询首页列表
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/index/list",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryIndexList(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader,@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 清空首页缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/clear/index/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO clearWebIndexCache(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader,@RequestBody RequestJsonVO requestVo);

    /**
     * 保存
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",method = RequestMethod.POST)
    ResultObjectVO save(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    /**
     * 编辑
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",method = RequestMethod.POST)
    ResultObjectVO update(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findById(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);




    /**
     * 根据ID删除指定
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    /**
     * 批量删除
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);



}
