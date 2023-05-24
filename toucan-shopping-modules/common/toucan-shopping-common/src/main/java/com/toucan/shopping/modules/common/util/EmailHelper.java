package com.toucan.shopping.modules.common.util;

import com.toucan.shopping.modules.common.vo.email.Email;
import com.toucan.shopping.modules.common.vo.email.EmailConfig;
import com.toucan.shopping.modules.common.vo.email.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class EmailHelper {

    private static final Logger logger = LoggerFactory.getLogger(EmailHelper.class);

    public static void main(String[] args) throws Exception {


        Email email = new Email();
        EmailConfig emailConfig = new EmailConfig();
        emailConfig.setSmtpServer("smtp.163.com");
        emailConfig.setSender("mmdrss@163.com");
        emailConfig.setSenderAccount("IJHWJFZINFUPQIUC");

        //设置收件人
        List<Receiver> receivers = new ArrayList<Receiver>();
        Receiver receiver = new Receiver();
        receiver.setEmail("mj7612158@qq.com");
        receiver.setName("user001");
        receivers.add(receiver);
        emailConfig.setReceivers(receivers);

        email.setEmailConfig(emailConfig);
        email.setSubject("异常邮件");
        email.setContent(getEmailContent("1111", "一念之间", (600/60), DateUtils.format(new Date(), DateUtils.FORMATTER_DD.get())));
//        try{
//            int a= 1/0;
//        }catch(Exception e)
//        {
//            e.printStackTrace();
//            email.setContent(ExceptionHelper.convertExceptionStack2StringByException(e));
//        }

       EmailHelper.send(email);

    }

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

    public static void send(Email email) throws Exception{

        EmailConfig emailConfig = email.getEmailConfig();

        if(emailConfig.getReceivers()==null||emailConfig.getReceivers().size()<=0)
        {
            throw new IllegalArgumentException("收件人列表不能为空");
        }

        Session session = Session.getInstance(emailConfig.getProperties());

        session.setDebug(emailConfig.isDebug());

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailConfig.getSender(), emailConfig.getSenderName(), "UTF-8"));
        //遍历收件人
        for(Receiver receive:emailConfig.getReceivers())
        {
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receive.getEmail(), receive.getName(), "UTF-8"));
        }
        message.setSubject(email.getSubject(), "UTF-8");
        message.setContent(email.getContent(), "text/html;charset=UTF-8");
        message.setSentDate(email.getSendDate());
        message.saveChanges();

        Transport transport = session.getTransport();
        try {
            //设置发件人和发件人的授权码
            transport.connect(emailConfig.getSender(), emailConfig.getSenderAccount());
            transport.sendMessage(message, message.getAllRecipients());
        }catch(Exception e)
        {
            throw e;
        }finally {
            if (transport != null) {
                transport.close();
            }
        }

    }

}
