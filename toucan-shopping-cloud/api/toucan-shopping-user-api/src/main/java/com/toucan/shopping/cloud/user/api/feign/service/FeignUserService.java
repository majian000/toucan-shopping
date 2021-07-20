package com.toucan.shopping.cloud.user.api.feign.service;

import com.toucan.shopping.cloud.user.api.feign.fallback.FeignUserServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.entity.User;
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
    ResultObjectVO registByMobilePhone(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    /**
     * 重置密码
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/reset/password",produces = "application/json;charset=UTF-8")
    ResultObjectVO resetPassword(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);



    /**
     * 关联到用户名
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/connect/username",produces = "application/json;charset=UTF-8")
    ResultObjectVO connectUsername(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);


    /**
     * 关联到邮箱
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/connect/email",produces = "application/json;charset=UTF-8")
    ResultObjectVO connectEmail(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);



    /**
     * 关联到手机号
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/connect/mobile/phone",produces = "application/json;charset=UTF-8")
    ResultObjectVO connectMobilePhone(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);


    /**
     * 修改用户详情
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/detail",produces = "application/json;charset=UTF-8")
    ResultObjectVO updateDetail(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据用户名注册
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/regist/username",produces = "application/json;charset=UTF-8")
    public ResultObjectVO registByUsername(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);


    /**
     * 用户密码登录
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/login/password",produces = "application/json;charset=UTF-8")
    public ResultObjectVO loginByPassword(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);


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
    @RequestMapping(value = "/is/online",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO isOnline(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 获取登录信息
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/login/info",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryLoginInfo(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 查询用户信息(从缓存和数据库中查询)
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/by/user/main/id/for/cache/db",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findByUserMainIdForCacheOrDB(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);

    /**
     * 查询是否实名
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/verify/real/name",produces = "application/json;charset=UTF-8")
    ResultObjectVO verifyRealName(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 根据手机号查询用户
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/mobile/phone",produces = "application/json;charset=UTF-8")
    ResultObjectVO findByMobilePhone(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 列表分页
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO list(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);



    /**
     * 手机号列表分页
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/mobile/phone/list",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO mobilePhoneList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 邮箱列表分页
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/email/list",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO emailList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 用户名列表分页
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/username/list",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO usernameList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 根据用户名查询用户名列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/username/list/by/username",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findUsernameListByUsername(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);




    /**
     * 根据邮箱查询邮箱列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/email/list/by/email",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findEmailListByEmail(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 禁用启用指定用户
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/disabled/enabled/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO disabledEnabledById(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 根据用户ID和手机号 禁用手机号关联
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/mobile/phone/disabled/enabled",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO disabledEnabledMobilePhoneByUserMainIdAndMobilePhone(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 根据用户ID和邮箱 禁用邮箱关联
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/email/disabled/enabled",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO disabledEnabledEmailByUserMainIdAndEmail(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);

    /**
     * 根据用户ID和用户名 禁用用户名关联
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/username/disabled/enabled",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO disabledEnabledUsernameByUserMainIdAndUsername(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 批量禁用
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/disabled/ids",method = RequestMethod.DELETE)
    ResultObjectVO disabledByIds(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    /**
     * 根据用户ID查询
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/by/user/main/id",produces = "application/json;charset=UTF-8")
    ResultObjectVO findByUserMainId(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 刷新用户缓存
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/flush/cache",produces = "application/json;charset=UTF-8")
    public ResultObjectVO flushCache(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


}
