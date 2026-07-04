package com.toucan.shopping.cloud.user.api.cloud.feign.service;

import com.toucan.shopping.cloud.user.api.ConsigneeAddressServiceAPI;
import com.toucan.shopping.cloud.user.api.cloud.feign.fallback.FeignConsigneeAddressServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 收货地址服务
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/consignee/address",fallbackFactory = FeignConsigneeAddressServiceFallbackFactory.class)
public interface FeignConsigneeAddressService extends ConsigneeAddressServiceAPI {



    @Override
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo);



    @Override
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/delete/id/userMainId/appCode", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO deleteByIdAndUserMainIdAndAppCode(@RequestBody RequestJsonVO requestVo);



    /**
     * 设置默认
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/set/default/id/userMainId", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO setDefaultByIdAndUserMainId(@RequestBody RequestJsonVO requestVo);



    /**
     * 查询单条数据
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/find/id/userMainId/appCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO findByIdAndUserMainIdAndAppcode(@RequestBody RequestJsonVO requestVo);



    /**
     * 查询设置为默认的收货信息,如果没有默认就查询最新一条
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/find/default/by/userMainId/appCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO findDefaultByUserMainIdAndAppcode(@RequestBody RequestJsonVO requestVo);

}
