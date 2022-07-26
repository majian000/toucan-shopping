package com.toucan.shopping.cloud.user.api.feign.service;

import com.toucan.shopping.cloud.user.api.feign.fallback.FeignConsigneeAddressServiceFallbackFactory;
import com.toucan.shopping.cloud.user.api.feign.fallback.FeignUserTrueNameApproveServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 收货地址服务
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/consignee/address",fallbackFactory = FeignConsigneeAddressServiceFallbackFactory.class)
public interface FeignConsigneeAddressService {



    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo);



    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id/userMainId/appCode",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIdAndUserMainIdAndAppCode(@RequestBody RequestJsonVO requestVo);



    /**
     * 设置默认
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/set/default/id/userMainId",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO setDefaultByIdAndUserMainId(@RequestBody RequestJsonVO requestVo);



    /**
     * 查询单条数据
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id/userMainId/appCode",produces = "application/json;charset=UTF-8")
    ResultObjectVO findByIdAndUserMainIdAndAppcode(@RequestBody RequestJsonVO requestVo);



    /**
     * 查询设置为默认的收货信息,如果没有默认就查询最新一条
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/default/by/userMainId/appCode",produces = "application/json;charset=UTF-8")
    ResultObjectVO findDefaultByUserMainIdAndAppcode(@RequestBody RequestJsonVO requestVo);

}
