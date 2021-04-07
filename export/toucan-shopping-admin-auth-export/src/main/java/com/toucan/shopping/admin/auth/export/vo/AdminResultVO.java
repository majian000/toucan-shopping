package com.toucan.shopping.admin.auth.export.vo;

import com.toucan.shopping.common.vo.ResultVO;

public class AdminResultVO extends ResultVO {
    /**
     * 没有找到的用户
     */
    public static Integer NOT_FOUND_USER=-2;
    /**
     * 请输入手机号
     */
    public static Integer NOT_FOUND_MOBILE=-3;
    /**
     * 未找到所属应用
     */
    public static Integer NOT_FOUND_APP=-4;
    /**
     * 未找到验证码
     */
    public static Integer NOT_FOUND_VCODE=-5;
    /**
     * 未找到验证码类型
     */
    public static Integer NOT_FOUND_SMS_TYPE=-6;
    /**
     * 手机号码错误
     */
    public static Integer MOBILE_ERROR=-7;
    /**
     * 未输入密码
     */
    public static Integer PASSWORD_NOT_FOUND=-8;
    /**
     * 手机号码错误
     */
    public static Integer PASSWORD_ERROR=-9;
    /**
     * 登录失败,提示请先注册,可以实现注册界面跳转
     */
    public static Integer NOT_REGIST=-10;
    /**
     * 请输入手机号或邮箱
     */
    public static Integer USERNAME_NOT_FOUND=-10;




}
