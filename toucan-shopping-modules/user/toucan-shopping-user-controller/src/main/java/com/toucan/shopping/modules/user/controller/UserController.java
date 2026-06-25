package com.toucan.shopping.modules.user.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户注册、用户登录
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserBusinessService userBusinessService;

    @RequestMapping(value="/find/mobile/phone",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findByMobilePhone(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.findByMobilePhone(requestJsonVO);
    }


    @RequestMapping(value="/regist/mobile/phone",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO registByMobilePhone(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.registByMobilePhone(requestJsonVO);
    }


    /**
     * 更新头像
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/headsculpture",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateHeadsculpture(@RequestBody RequestJsonVO requestJsonVO)
    {
        return userBusinessService.updateHeadsculpture(requestJsonVO);
    }


    @RequestMapping(value="/reset/password",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO resetPassword(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.resetPassword(requestJsonVO);
    }

    /**
     * 关联用户名
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/connect/username",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO connectUsername(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.connectUsername(requestJsonVO);
    }


    /**
     * 查询用户名列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/username/list/by/username",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findUsernameListByUsername(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.findUsernameListByUsername(requestJsonVO);
    }


    /**
     * 关联邮箱
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/connect/email",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO connectEmail(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.connectEmail(requestJsonVO);
    }


    /**
     * 更新关联邮箱
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/connect/email",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateConnectEmail(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.updateConnectEmail(requestJsonVO);
    }


    /**
     * 更新关联手机号
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/connect/mobilePhone",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateConnectMobilePhone(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.updateConnectMobilePhone(requestJsonVO);
    }


    /**
     * 查询邮箱列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/email/list/by/email",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findEmailListByEmail(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.findEmailListByEmail(requestJsonVO);
    }


    /**
     * 修改用户详情
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/detail",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateDetail(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.updateDetail(requestJsonVO);
    }


    /**
     * 修改用户信息
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/edit/info",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO editInfo(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.editInfo(requestJsonVO);
    }


    /**
     * 修改用户是否存在店铺
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update/is/shop",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateIsShop(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.updateIsShop(requestJsonVO);
    }


    /**
     * 密码登录
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/login/password",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO loginByPassword(@RequestBody RequestJsonVO requestJsonVO) {
        return userBusinessService.loginByPassword(requestJsonVO);
    }


    /**
     * 根据用户名查询用户信息
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/by/username",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findByUsername(@RequestBody RequestJsonVO requestJsonVO) {
        return userBusinessService.findByUsername(requestJsonVO);
    }


    /**
     * 判断是否在线
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/login/info",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryLoginInfo(@RequestBody RequestJsonVO requestVo) {
        return userBusinessService.queryLoginInfo(requestVo);
    }

    /**
     * 退出登录
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/logout",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO logout(@RequestBody RequestJsonVO requestVo) {
        return userBusinessService.logout(requestVo);
    }


    /**
     * 判断是否在线
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/is/online",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO isOnline(@RequestBody RequestJsonVO requestVo) {
        return userBusinessService.isOnline(requestVo);
    }


    /**
     * 判断userId和token是否一致
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/verify/login/token",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO verifyLoginToken(@RequestBody RequestJsonVO requestVo) {
        return userBusinessService.verifyLoginToken(requestVo);
    }


    /**
     * 校验用户token以及判断登录会话
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/verify/login/token/is/online",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO verifyLoginTokenAndIsOnline(@RequestBody RequestJsonVO requestVo) {
        return userBusinessService.verifyLoginTokenAndIsOnline(requestVo);
    }


    /**
     * 判断是否实名
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/verify/real/name",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO verifyRealName(@RequestBody RequestJsonVO requestVo) {
        return userBusinessService.verifyRealName(requestVo);
    }


    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO list(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.list(requestVo);
    }


    /**
     * 用户绑定手机号列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/mobile/phone/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO mobilePhoneList(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.mobilePhoneList(requestVo);
    }


    /**
     * 用户绑定邮箱列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/email/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO emailList(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.emailList(requestVo);
    }


    /**
     * 用户名列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/username/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO usernameList(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.usernameList(requestVo);
    }

    /**
     * 禁用启用指定用户
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/disabled/enabled/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledEnabledById(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.disabledEnabledById(requestVo);
    }


    /**
     * 禁用启用指定用户手机号
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/mobile/phone/disabled/enabled",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledEnabledMobilePhone(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.disabledEnabledMobilePhone(requestVo);
    }


    /**
     * 禁用启用指定用户邮箱
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/email/disabled/enabled",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledEnabledEmail(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.disabledEnabledEmail(requestVo);
    }


    /**
     * 禁用启用指定用户名
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/username/disabled/enabled",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledEnabledUsernameByUserMainIdAndUsername(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.disabledEnabledUsernameByUserMainIdAndUsername(requestVo);
    }

    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/disabled/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledByIds(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.disabledByIds(requestVo);
    }


    /**
     * 根据用户主ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/by/user/main/id",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findByUserMainId(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.findByUserMainId(requestVo);
    }


    /**
     * 根据用户主ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/by/user/main/id/for/cache/db",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findByUserMainIdForCacheOrDB(@RequestBody RequestJsonVO requestVo){
        return userBusinessService.findByUserMainIdForCacheOrDB(requestVo);
    }

    /**
     * 关联手机号
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/connect/mobile/phone",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO connectMobilePhone(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.connectMobilePhone(requestJsonVO);
    }


    /**
     * 刷新缓存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/flush/cache",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO flushCache(@RequestBody RequestJsonVO requestJsonVO){
        return userBusinessService.flushCache(requestJsonVO);
    }

}
