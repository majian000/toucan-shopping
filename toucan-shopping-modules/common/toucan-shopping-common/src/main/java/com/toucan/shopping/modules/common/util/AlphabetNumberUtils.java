package com.toucan.shopping.modules.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 只允许字母、数字下划线组成
 * 长度限制为8位
 */
public class AlphabetNumberUtils {


    private static final String regEx1 = "^[0-9a-zA-Z_]{1,8}$";
    private static Pattern p = Pattern.compile(regEx1);


    /**
     * 返回是否是字母、数字下划线组成最大长度8位 最小长度1位
     * @param content
     * @return
     */
    public static boolean isAlphabetNumber(String content)
    {
        if (null==content || "".equals(content)){
            return false;
        }
        Matcher m = p.matcher(content);
        return m.matches();
    }
}
