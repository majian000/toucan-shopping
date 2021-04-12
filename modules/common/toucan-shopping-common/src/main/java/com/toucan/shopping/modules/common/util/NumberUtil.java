package com.toucan.shopping.modules.common.util;

import java.util.Random;

public class NumberUtil {

    /**
     * 随机生成N位小数
     * @param n
     * @return
     */
    public static String random(int n)
    {
        StringBuilder builder=new StringBuilder();
        Random random = new Random();
        for(int i=0;i<n;i++)
        {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    /**
     * 判断一个字符串是否内容是数字
     * @param str
     * @return true内容是数字 false内容有字符不是
     */
    public static boolean isNumber(String str)
    {
        if(str ==null||str.replace(" ","").equals(""))
        {
            return false;
        }
        boolean ret=true;
        for(int i=0;i<str.length();i++)
        {
            int ascii = (int)str.charAt(i);
            //0-9的ASCII 对应十进制48-57
            if(ascii<48||ascii>57)
            {
                ret=false;
                break;
            }
        }
        return ret;
    }
}
