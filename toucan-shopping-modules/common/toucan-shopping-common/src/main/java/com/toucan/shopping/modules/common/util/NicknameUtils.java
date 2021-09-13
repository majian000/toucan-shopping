package com.toucan.shopping.modules.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 昵称工具类
 */
public class NicknameUtils {

    /**
     * 不能以数字开头,可以是字母汉字下划线
     */
    private static String regExp = "^[^0-9][\\w\\u4E00-\\u9FA5]{1,15}$";
    private static Pattern p = Pattern.compile(regExp);

    /**
     * 判断是否是合法昵称
     * 长度2-15位
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isNickname(String str) throws PatternSyntaxException {
        Matcher m = p.matcher(str);
        return m.matches();
    }


    public static void main(String[] args)
    {
        System.out.println(NicknameUtils.isNickname("一"));
        System.out.println(NicknameUtils.isNickname("一念之间a0"));
    }
}
