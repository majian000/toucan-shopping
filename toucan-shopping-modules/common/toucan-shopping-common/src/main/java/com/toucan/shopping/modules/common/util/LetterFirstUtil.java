package com.toucan.shopping.modules.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 判断是否是字母开头
 */
public class LetterFirstUtil {

    private static final String regEx1 = "^[a-zA-Z]+$";
    private static Pattern p = Pattern.compile(regEx1);


    /**
     * 返回是否是字母开头
     * @param content
     * @return
     */
    public static boolean isLetterFirst(String content)
    {
        if (null==content || "".equals(content)){
            return false;
        }
        Matcher m = p.matcher(content);
        return m.matches();
    }
}
