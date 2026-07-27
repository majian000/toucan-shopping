package com.toucan.shopping.modules.user.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户注册、用户登录
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserBusinessService userBusinessService;

    @RequestMapping(value="/find/mobile/phone", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO findByMobilePhone(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.findByMobilePhone(requestJsonVO);
    }

    @RequestMapping(value="/regist/mobile/phone", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO registByMobilePhone(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.registByMobilePhone(requestJsonVO);
    }

    /**
     * 更新头像
     */
    @RequestMapping(value="/update/headsculpture", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO updateHeadsculpture(@RequestBody RequestJsonVO requestJsonVO) {
        return userBusinessService.updateHeadsculpture(requestJsonVO);
    }

    @RequestMapping(value="/reset/password", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO resetPassword(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.resetPassword(requestJsonVO);
    }

    /**
     * 关联用户名
     */
    @RequestMapping(value="/connect/username", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO connectUsername(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.connectUsername(requestJsonVO);
    }

    /**
     * 查询用户名列表
     */
    @RequestMapping(value="/find/username/list/by/username", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO findUsernameListByUsername(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.findUsernameListByUsername(requestJsonVO);
    }

    /**
     * 关联邮箱
     */
    @RequestMapping(value="/connect/email", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO connectEmail(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.connectEmail(requestJsonVO);
    }

    /**
     * 更新关联邮箱
     */
    @RequestMapping(value="/update/connect/email", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO updateConnectEmail(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.updateConnectEmail(requestJsonVO);
    }

    /**
     * 更新关联手机号
     */
    @RequestMapping(value="/update/connect/mobilePhone", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO updateConnectMobilePhone(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.updateConnectMobilePhone(requestJsonVO);
    }

    /**
     * 查询邮箱列表
     */
    @RequestMapping(value="/find/email/list/by/email", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO findEmailListByEmail(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.findEmailListByEmail(requestJsonVO);
    }

    /**
     * 修改用户详情
     */
    @RequestMapping(value="/update/detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO updateDetail(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.updateDetail(requestJsonVO);
    }

    /**
     * 修改用户信息
     */
    @RequestMapping(value="/edit/info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO editInfo(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.editInfo(requestJsonVO);
    }

    /**
     * 修改用户是否存在店铺
     */
    @RequestMapping(value="/update/is/shop", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO updateIsShop(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.updateIsShop(requestJsonVO);
    }

    /**
     * 密码登录
     */
    @RequestMapping(value="/login/password", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO loginByPassword(@RequestBody RequestJsonVO requestJsonVO) {
        return userBusinessService.loginByPassword(requestJsonVO);
    }

    /**
     * 根据用户名查询用户信息
     */
    @RequestMapping(value="/find/by/username", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO findByUsername(@RequestBody RequestJsonVO requestJsonVO) {
        return userBusinessService.findByUsername(requestJsonVO);
    }

    /**
     * 判断是否在线
     */
    @RequestMapping(value="/query/login/info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryLoginInfo(@RequestBody RequestJsonVO requestVo) {
        return userBusinessService.queryLoginInfo(requestVo);
    }

    /**
     * 退出登录
     */
    @RequestMapping(value="/logout", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO logout(@RequestBody RequestJsonVO requestVo) {
        return userBusinessService.logout(requestVo);
    }

    /**
     * 判断是否在线
     */
    @RequestMapping(value="/is/online", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO isOnline(@RequestBody RequestJsonVO requestVo) {
        return userBusinessService.isOnline(requestVo);
    }

    /**
     * 判断userId和token是否一致
     */
    @RequestMapping(value="/verify/login/token", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO verifyLoginToken(@RequestBody RequestJsonVO requestVo) {
        return userBusinessService.verifyLoginToken(requestVo);
    }

    /**
     * 校验用户token以及判断登录会话
     */
    @RequestMapping(value="/verify/login/token/is/online", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO verifyLoginTokenAndIsOnline(@RequestBody RequestJsonVO requestVo) {
        return userBusinessService.verifyLoginTokenAndIsOnline(requestVo);
    }

    /**
     * 判断是否实名
     */
    @RequestMapping(value="/verify/real/name", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO verifyRealName(@RequestBody RequestJsonVO requestVo) {
        return userBusinessService.verifyRealName(requestVo);
    }

    /**
     * 查询列表分页
     */
    @RequestMapping(value="/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO list(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.list(requestVo);
    }

    /**
     * 用户绑定手机号列表
     */
    @RequestMapping(value="/mobile/phone/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO mobilePhoneList(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.mobilePhoneList(requestVo);
    }

    /**
     * 用户绑定邮箱列表
     */
    @RequestMapping(value="/email/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO emailList(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.emailList(requestVo);
    }

    /**
     * 用户名列表
     */
    @RequestMapping(value="/username/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO usernameList(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.usernameList(requestVo);
    }

    /**
     * 禁用启用指定用户
     */
    @RequestMapping(value="/disabled/enabled/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO disabledEnabledById(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.disabledEnabledById(requestVo);
    }

    /**
     * 禁用启用指定用户手机号
     */
    @RequestMapping(value="/mobile/phone/disabled/enabled", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO disabledEnabledMobilePhone(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.disabledEnabledMobilePhone(requestVo);
    }

    /**
     * 禁用启用指定用户邮箱
     */
    @RequestMapping(value="/email/disabled/enabled", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO disabledEnabledEmail(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.disabledEnabledEmail(requestVo);
    }

    /**
     * 禁用启用指定用户名
     */
    @RequestMapping(value="/username/disabled/enabled", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO disabledEnabledUsernameByUserMainIdAndUsername(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.disabledEnabledUsernameByUserMainIdAndUsername(requestVo);
    }

    /**
     * 批量删除
     */
    @RequestMapping(value="/disabled/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO disabledByIds(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.disabledByIds(requestVo);
    }

    /**
     * 根据用户主ID查询
     */
    @RequestMapping(value="/find/by/user/main/id", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO findByUserMainId(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.findByUserMainId(requestVo);
    }

    /**
     * 根据用户主ID查询
     */
    @RequestMapping(value="/find/by/user/main/id/for/cache/db", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO findByUserMainIdForCacheOrDB(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.findByUserMainIdForCacheOrDB(requestVo);
    }

    /**
     * 关联手机号
     */
    @RequestMapping(value="/connect/mobile/phone", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO connectMobilePhone(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.connectMobilePhone(requestJsonVO);
    }

    /**
     * 刷新缓存
     */
    @RequestMapping(value="/flush/cache", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO flushCache(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.flushCache(requestJsonVO);
    }
}
