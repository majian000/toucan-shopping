package com.toucan.shopping.modules.common.vo.email;


import com.toucan.shopping.modules.common.util.EmailHelper;
import lombok.Data;

import java.util.Date;

/**
 * 邮件对象
 */
@Data
public class Email {

    /**
     * 邮件配置
     */
    private EmailConfig emailConfig;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 发送时间
     */
    private Date sendDate;



    public void send() throws Exception {
        EmailHelper.send(this);
    }

}
