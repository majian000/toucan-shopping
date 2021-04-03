package com.toucan.shopping.user.api.feign.service;

import com.toucan.shopping.user.export.entity.User;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.user.api.feign.fallback.FeignUserServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户服务
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/user",fallbackFactory = FeignUserServiceFallbackFactory.class)
public interface FeignUserService {


    /**
     * 根据手机号注册
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/regist/mobile/phone",produces = "application/json;charset=UTF-8")
    public ResultObjectVO registByMobilePhone(@RequestHeader("bbs-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    /**
     * 用户密码登录
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/login/password",produces = "application/json;charset=UTF-8")
    public ResultObjectVO loginByPassword(@RequestHeader("bbs-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);


    /**
     * 用户短信验证码登录
     * @param user
     * @return
     */
    @RequestMapping(value="/login/vcode",produces = "application/json;charset=UTF-8")
    public ResultObjectVO loginByVCode(@RequestBody User user);


    /**
     * 查询用户是否在线
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping("/is/online")
    ResultObjectVO isOnline(@RequestHeader("bbs-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 根据手机号查询用户
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/mobile/phone",produces = "application/json;charset=UTF-8")
    ResultObjectVO findByMobilePhone(@RequestHeader("bbs-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 列表分页
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO list(@RequestHeader("bbs-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


}
