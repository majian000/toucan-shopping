package com.toucan.shopping.modules.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 只允许字母、数字下划线组成
 * 长度限制为8位
 */
public class AlphabetNumberUtils {



    /**
     * 返回是否是字母、数字下划线组成最大长度8位 最小长度1位
     * @param content
     * @return
     */
    public static boolean isAlphabetNumber(String content,int minLength,int maxLength)
    {
        if (null==content || "".equals(content)){
            return false;
        }
        String regEx1 = "^[0-9a-zA-Z_]{"+minLength+","+maxLength+"}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(content);
        return m.matches();
    }
}
