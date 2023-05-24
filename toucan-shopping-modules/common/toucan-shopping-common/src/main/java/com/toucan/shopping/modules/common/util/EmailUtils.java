package com.toucan.shopping.modules.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 邮件处理工具类
 */
public class EmailUtils {


    /**
     * 长度限制2-50位
     */
    private static String regEx1 = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,50}";
    private static Pattern p = Pattern.compile(regEx1);

    /**
     * 返回是否是邮件地址
     * @param email
     * @return true是邮件地址 false不是邮件地址
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)){
            return false;
        }
        Matcher m = p.matcher(email);
        return m.matches();
    }

}
