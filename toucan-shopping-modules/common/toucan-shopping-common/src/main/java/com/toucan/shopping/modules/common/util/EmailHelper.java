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

        try{
            int a= 1/0;
        }catch(Exception e)
        {
            e.printStackTrace();
            email.setContent(ExceptionHelper.convertExceptionStack2StringByException(e));
        }

       EmailHelper.send(email);

    }



    public static void send(Email email) throws Exception{

        EmailConfig emailConfig = email.getEmailConfig();

        if(emailConfig.getReceivers()==null||emailConfig.getReceivers().size()<=0)
        {
            throw new IllegalArgumentException("收件人列表不能为空");
        }

        Session session = Session.getInstance(emailConfig.getProperties());
        session.setDebug(true);

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
