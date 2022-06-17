package com.toucan.shopping.modules.common.util;

public class AdminRegistUtil {

    /**
     * 密码验证
     * @param pwd
     * @return
     */
    public static boolean checkPwd(String pwd) {
        String regExp = "^[\\w_]{6,25}$";
        if(pwd.matches(regExp)) {
            return true;
        }
        return false;
    }
}
