package com.toucan.shopping.modules.user.constant;

public final class UserLoginConstant {

    /**
     * 登录会话UID 最大存活时间 1天
     */
    public static Integer LOGIN_UID_MAX_AGE=86400*1;

    /**
     * 登录会话Token 最大存活时间 1天
     */
    public static Integer LOGIN_TOKEN_MAX_AGE=86400*1;

    /**
     * 登录会话超级Token 最大存活时间 30分钟
     */
    public static Integer LOGIN_SUPER_TOKEN_MAX_AGE=1800;

    /**
     * 没有找到的用户
     */
    public static Integer NOT_FOUND_USER=-1;

    /**
     * 未找到所属应用
     */
    public static Integer NOT_FOUND_APP=-2;


    /**
     * 登录失败,提示请先注册,可以实现注册界面跳转
     */
    public static Integer NOT_REGIST=-3;

    /**
     * 请输入手机号或邮箱
     */
    public static Integer USERNAME_NOT_FOUND=-4;

    /**
     * 请输入密码
     */
    public static Integer PASSWORD_NOT_FOUND=-5;

}
