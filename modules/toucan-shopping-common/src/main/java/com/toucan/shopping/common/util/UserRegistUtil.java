package com.toucan.shopping.common.util;

public class UserRegistUtil {

    /**
     * 密码验证
     * @param pwd
     * @return
     */
    public static boolean checkPwd(String pwd) {
        String regExp = "^[\\w_]{6,15}$";
        if(pwd.matches(regExp)) {
            return true;
        }
        return false;
    }
}
