package com.toucan.shopping.modules.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class UsernameUtils {

    // ^ 匹配输入字符串开始的位置
    // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
    // $ 匹配输入字符串结尾的位置
    private static String regExp = "^[^0-9][\\w_]{5,15}$";
    private static Pattern p = Pattern.compile(regExp);

    /**
     * 判断是否是合法用户名
     * 长度5-12位
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isUsername(String str) throws PatternSyntaxException {
        Matcher m = p.matcher(str);
        return m.matches();
    }

}
