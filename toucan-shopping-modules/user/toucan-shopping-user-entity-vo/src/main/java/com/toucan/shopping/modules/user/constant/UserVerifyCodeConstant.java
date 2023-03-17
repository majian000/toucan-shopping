package com.toucan.shopping.modules.user.constant;

public final class UserVerifyCodeConstant {


    /**
     * 默认验证码超时时间(单位秒)
     */
    public static Integer DEFAULT_VERIFY_CODE_MAX_AGE=3*60;

    /**
     * 实名认证页面验证码时间(单位秒)
     */
    public static Integer TRUE_NAME_VERIFY_CODE_MAX_AGE=3*60;

    /**
     * 注册手机验证码超时时间3分钟(单位秒)
     */
    public static Integer REGIST_PHONE_VERIFY_CODE_MAX_AGE=3*60;

    /**
     * 登录输入错误多次 验证码时间(单位秒)
     */
    public static Integer LOGIN_FAILD_VERIFY_CODE_MAX_AGE=2*60;

}
