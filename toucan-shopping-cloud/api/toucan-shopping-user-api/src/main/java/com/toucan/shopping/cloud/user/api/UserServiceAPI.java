package com.toucan.shopping.cloud.user.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.entity.User;

/**
 * 用户服务
 */
public interface UserServiceAPI {


    /**
     * 根据手机号注册
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO registByMobilePhone(RequestJsonVO requestJsonVO);

    /**
     * 重置密码
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO resetPassword(RequestJsonVO requestJsonVO);



    /**
     * 关联到用户名
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO connectUsername(RequestJsonVO requestJsonVO);


    /**
     * 关联到邮箱
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO connectEmail(RequestJsonVO requestJsonVO);



    /**
     * 关联到手机号
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO connectMobilePhone(RequestJsonVO requestJsonVO);


    /**
     * 修改用户详情
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updateDetail(RequestJsonVO requestJsonVO);


    /**
     * 根据用户名注册
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO registByUsername(RequestJsonVO requestJsonVO);


    /**
     * 用户密码登录
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO loginByPassword(RequestJsonVO requestJsonVO);



    /**
     * 退出登录
     * @param requestVo
     * @return
     */
    ResultObjectVO logout(RequestJsonVO requestVo);

    /**
     * 用户短信验证码登录
     * @param user
     * @return
     */
    ResultObjectVO loginByVCode(User user);


    /**
     * 查询用户是否在线
     * @param requestVo
     * @return
     */
    ResultObjectVO isOnline(RequestJsonVO requestVo);


    /**
     * 校验用户token
     * @param requestVo
     * @return
     */
    ResultObjectVO verifyLoginToken(RequestJsonVO requestVo);


    /**
     * 校验用户token以及判断登录会话
     * @param requestVo
     * @return
     */
    ResultObjectVO verifyLoginTokenAndIsOnline(RequestJsonVO requestVo);


    /**
     * 获取登录信息
     * @param requestVo
     * @return
     */
    ResultObjectVO queryLoginInfo(RequestJsonVO requestVo);


    /**
     * 查询用户信息(从缓存和数据库中查询)
     * @param requestVo
     * @return
     */
    ResultObjectVO findByUserMainIdForCacheOrDB(RequestJsonVO requestVo);

    /**
     * 查询是否实名
     * @param requestVo
     * @return
     */
    ResultObjectVO verifyRealName(RequestJsonVO requestVo);


    /**
     * 根据手机号查询用户
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO findByMobilePhone(RequestJsonVO requestJsonVO);


    /**
     * 列表分页
     * @param requestVo
     * @return
     */
    ResultObjectVO list(RequestJsonVO requestVo);



    /**
     * 手机号列表分页
     * @param requestVo
     * @return
     */
    ResultObjectVO mobilePhoneList(RequestJsonVO requestVo);


    /**
     * 邮箱列表分页
     * @param requestVo
     * @return
     */
    ResultObjectVO emailList(RequestJsonVO requestVo);


    /**
     * 用户名列表分页
     * @param requestVo
     * @return
     */
    ResultObjectVO usernameList(RequestJsonVO requestVo);


    /**
     * 根据用户名查询用户名列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO findUsernameListByUsername(RequestJsonVO requestJsonVO);




    /**
     * 根据邮箱查询邮箱列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO findEmailListByEmail(RequestJsonVO requestJsonVO);


    /**
     * 禁用启用指定用户
     * @param requestVo
     * @return
     */
    ResultObjectVO disabledEnabledById(RequestJsonVO requestVo);


    /**
     * 根据用户ID和手机号 禁用手机号关联
     * @param requestVo
     * @return
     */
    ResultObjectVO disabledEnabledMobilePhone(RequestJsonVO requestVo);



    /**
     * 更新关联邮箱
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updateConnectEmail(RequestJsonVO requestJsonVO);


    /**
     * 更新关联手机号
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updateConnectMobilePhone(RequestJsonVO requestJsonVO);

    /**
     * 根据用户ID和邮箱 禁用邮箱关联
     * @param requestVo
     * @return
     */
    ResultObjectVO disabledEnabledEmail(RequestJsonVO requestVo);

    /**
     * 根据用户ID和用户名 禁用用户名关联
     * @param requestVo
     * @return
     */
    ResultObjectVO disabledEnabledUsernameByUserMainIdAndUsername(RequestJsonVO requestVo);


    /**
     * 批量禁用
     * @param requestVo
     * @return
     */
    ResultObjectVO disabledByIds(RequestJsonVO requestVo);


    /**
     * 根据用户ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findByUserMainId(RequestJsonVO requestVo);


    /**
     * 刷新用户缓存
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO flushCache(RequestJsonVO requestJsonVO);


    /**
     * 编辑用户信息
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO editInfo(RequestJsonVO requestJsonVO);



    /**
     * 修改用户是否存在店铺
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updateIsShop(RequestJsonVO requestJsonVO);


    /**
     * 更新头像
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updateHeadsculpture(RequestJsonVO requestJsonVO);



    /**
     * 根据用户名查询用户信息
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO findByUsername(RequestJsonVO requestJsonVO);



}
