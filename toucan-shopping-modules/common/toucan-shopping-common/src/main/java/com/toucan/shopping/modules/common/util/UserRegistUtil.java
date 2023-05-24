package com.toucan.shopping.modules.common.util;

public class UserRegistUtil {

    /**
     * 密码验证
     * 包含任意字母、数字、_或*的6,15位
     * @param pwd
     * @return
     */
    public static boolean checkPwd(String pwd) {
        String regExp = "^[a-zA-Z0-9_*]{6,15}$";

        if(pwd.matches(regExp)) {
            return true;
        }
        return false;
    }

    public static String checkPwdFailText()
    {
        return "密码只能由6-15位的组成,可以包含字母、数字、下划线(_)或星号(*)";
    }
}
