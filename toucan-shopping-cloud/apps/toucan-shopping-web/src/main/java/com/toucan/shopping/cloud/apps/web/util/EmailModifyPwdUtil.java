package com.toucan.shopping.cloud.apps.web.util;

public class EmailModifyPwdUtil {

    /**
     * 修改密码邮件内容
     * @param vode
     * @param nickName
     * @param maxAge
     * @param senderDate
     * @return
     */
    public static String getEmailContent(String vode,String nickName,Integer maxAge,String senderDate)
    {
        StringBuilder builder=new StringBuilder();
        builder.append("亲爱的"+nickName+"：\n" +
                "欢迎使用犀鸟商城修改密码功能。\n" +
                "\n" +
                "您此次修改密码的验证码是："+vode+"，请在"+String.valueOf(maxAge)+"分钟内在修改密码页填入此验证码。\n" +
                "\n" +
                "如果您并未发过此请求，则可能是因为其他用户在尝试重设密码时误输入了您的电子邮件地址而使您收到这封邮件，那么您可以放心的忽略此邮件，无需进一步采取任何操作。\n" +
                "\n" +
                "犀鸟商城敬上\n" +
                "\n" +
                senderDate);
        return builder.toString();
    }
}
