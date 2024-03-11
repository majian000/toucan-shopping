package com.toucan.shopping.modules.common.util;

/**
 * 机构编码工具类
 */
public class CodeUtils {

    /**
     * 生成最小指定位数的编码
     * @param codeVal
     * @return
     */
    public static String genMinCode(int codeVal,int minDigit){
        StringBuilder builder = new StringBuilder();
        for(int i=1;i<minDigit;i++)
        {
            builder.append("0");
        }
        int minDigitVal = 10;
        for(int i=1;i<=minDigit;i++)
        {
            if(i>1) {
                minDigitVal *= 10;
            }
            if(codeVal<minDigitVal)
            {
                return builder.substring(0,(minDigit-i))+codeVal;
            }
        }
        return String.valueOf(codeVal);
    }


    public static void main(String[] args)
    {
        System.out.println(genMinCode(1,3));
        System.out.println(genMinCode(10,3));
        System.out.println(genMinCode(100,3));
        System.out.println(genMinCode(1000,3));


        System.out.println(genMinCode(2,1));
        System.out.println(genMinCode(20,1));
        System.out.println(genMinCode(200,1));
        System.out.println(genMinCode(2000,1));
    }


}
