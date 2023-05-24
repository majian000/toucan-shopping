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
        builder.append("<p>\n" +
                "\t<strong>亲爱的"+nickName+"：<br />\n" +
                "</strong>\n" +
                "</p>\n" +
                "<blockquote>\n" +
                "\t<p>\n" +
                "\t\t欢迎使用犀鸟商城修改密码功能。\n" +
                "\t</p>\n" +
                "\t<p>\n" +
                "\t\t您此次修改密码的验证码是："+vode+"，请在"+maxAge+"分钟内在修改密码页填入此验证码。\n" +
                "\t</p>\n" +
                "\t<p>\n" +
                "\t\t如果不是本人操作请忽略该邮件。\n" +
                "\t</p>\n" +
                "</blockquote>\n" +
                "<blockquote>\n" +
                "\t<p style=\"text-align:center;\">\n" +
                "\t\t犀鸟商城敬上&nbsp;\n" +
                "\t</p>\n" +
                "</blockquote>\n" +
                "<blockquote>\n" +
                "\t<p style=\"text-align:center;\">\n" +
                "\t\t"+senderDate+"\n" +
                "\t</p>\n" +
                "</blockquote>\n" +
                "<p>\n" +
                "\t<br />\n" +
                "</p>\n" +
                "<p style=\"color:#333333;font-family:&quot;background-color:#FFFFFF;text-indent:24px;\">\n" +
                "\t<span></span> \n" +
                "</p>");
        return builder.toString();
    }
}
