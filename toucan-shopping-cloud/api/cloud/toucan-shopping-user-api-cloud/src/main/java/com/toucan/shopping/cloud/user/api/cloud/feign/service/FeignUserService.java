package com.toucan.shopping.cloud.user.api.cloud.feign.service;

import com.toucan.shopping.cloud.user.api.UserServiceAPI;
import com.toucan.shopping.cloud.user.api.cloud.feign.fallback.FeignUserServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户服务
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/user",fallbackFactory = FeignUserServiceFallbackFactory.class)
public interface FeignUserService extends UserServiceAPI {


    /**
     * 根据手机号注册
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/regist/mobile/phone", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO registByMobilePhone(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 重置密码
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/reset/password", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO resetPassword(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 关联到用户名
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/connect/username", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO connectUsername(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 关联到邮箱
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/connect/email", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO connectEmail(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 关联到手机号
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/connect/mobile/phone", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO connectMobilePhone(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 修改用户详情
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/update/detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO updateDetail(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据用户名注册
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/regist/username", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO registByUsername(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 用户密码登录
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/login/password", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO loginByPassword(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 退出登录
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/logout", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO logout(@RequestBody RequestJsonVO requestVo);

    /**
     * 用户短信验证码登录
     * @param user
     * @return
     */
    @Override
    @RequestMapping(value="/login/vcode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO loginByVCode(@RequestBody User user);


    /**
     * 查询用户是否在线
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value = "/is/online", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO isOnline(@RequestBody RequestJsonVO requestVo);


    /**
     * 校验用户token
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value = "/verify/login/token", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO verifyLoginToken(@RequestBody RequestJsonVO requestVo);


    /**
     * 校验用户token以及判断登录会话
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value = "/verify/login/token/is/online", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO verifyLoginTokenAndIsOnline(@RequestBody RequestJsonVO requestVo);


    /**
     * 获取登录信息
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/query/login/info", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO queryLoginInfo(@RequestBody RequestJsonVO requestVo);


    /**
     * 查询用户信息(从缓存和数据库中查询)
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/find/by/user/main/id/for/cache/db", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO findByUserMainIdForCacheOrDB(@RequestBody RequestJsonVO requestVo);

    /**
     * 查询是否实名
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/verify/real/name", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO verifyRealName(@RequestBody RequestJsonVO requestVo);


    /**
     * 根据手机号查询用户
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/find/mobile/phone", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO findByMobilePhone(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 列表分页
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/list", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO list(@RequestBody RequestJsonVO requestVo);



    /**
     * 手机号列表分页
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/mobile/phone/list", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO mobilePhoneList(@RequestBody RequestJsonVO requestVo);


    /**
     * 邮箱列表分页
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/email/list", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO emailList(@RequestBody RequestJsonVO requestVo);


    /**
     * 用户名列表分页
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/username/list", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO usernameList(@RequestBody RequestJsonVO requestVo);


    /**
     * 根据用户名查询用户名列表
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/find/username/list/by/username", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO findUsernameListByUsername(@RequestBody RequestJsonVO requestJsonVO);




    /**
     * 根据邮箱查询邮箱列表
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/find/email/list/by/email", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO findEmailListByEmail(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 禁用启用指定用户
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/disabled/enabled/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO disabledEnabledById(@RequestBody RequestJsonVO requestVo);


    /**
     * 根据用户ID和手机号 禁用手机号关联
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/mobile/phone/disabled/enabled", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO disabledEnabledMobilePhone(@RequestBody RequestJsonVO requestVo);



    /**
     * 更新关联邮箱
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/update/connect/email", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO updateConnectEmail(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 更新关联手机号
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/update/connect/mobilePhone", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO updateConnectMobilePhone(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据用户ID和邮箱 禁用邮箱关联
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/email/disabled/enabled", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO disabledEnabledEmail(@RequestBody RequestJsonVO requestVo);

    /**
     * 根据用户ID和用户名 禁用用户名关联
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/username/disabled/enabled", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO disabledEnabledUsernameByUserMainIdAndUsername(@RequestBody RequestJsonVO requestVo);


    /**
     * 批量禁用
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/disabled/ids",method = RequestMethod.DELETE)
    ResultObjectVO disabledByIds(@RequestBody RequestJsonVO requestVo);


    /**
     * 根据用户ID查询
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/find/by/user/main/id", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO findByUserMainId(@RequestBody RequestJsonVO requestVo);


    /**
     * 刷新用户缓存
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/flush/cache", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO flushCache(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 编辑用户信息
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/edit/info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO editInfo(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 修改用户是否存在店铺
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/update/is/shop", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO updateIsShop(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 更新头像
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/update/headsculpture", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO updateHeadsculpture(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 根据用户名查询用户信息
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/find/by/username", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO findByUsername(@RequestBody RequestJsonVO requestJsonVO);



}
