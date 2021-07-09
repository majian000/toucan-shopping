package com.toucan.shopping.cloud.user.api.feign.service;

import com.toucan.shopping.cloud.user.api.feign.fallback.FeignUserServiceFallbackFactory;
import com.toucan.shopping.cloud.user.api.feign.fallback.FeignUserTrueNameApproveServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户实名审核服务
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/user/true/name/approve",fallbackFactory = FeignUserTrueNameApproveServiceFallbackFactory.class)
public interface FeignUserTrueNameApproveService {


    /**
     * 保存用户实名
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据用户主ID查询
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/queryByUserMainId",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByUserMainId(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO listPage(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);

}
