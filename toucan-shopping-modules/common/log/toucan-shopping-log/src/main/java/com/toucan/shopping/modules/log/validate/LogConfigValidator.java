package com.toucan.shopping.modules.log.validate;

import com.toucan.shopping.modules.common.properties.modules.log.Log;
import org.springframework.util.CollectionUtils;

/**
 * 邮件配置校验
 * @author majian
 */
public class LogConfigValidator {

    public static void validate(Log log)
    {
        if(log==null)
        {
            throw new NullPointerException("日志没有配置参数");
        }
        if(log.getEmail()==null)
        {
            throw new NullPointerException("日志邮件没有配置参数");
        }
        if(log.getEmail().getSmtp()==null)
        {
            throw new NullPointerException("日志邮件SMTP配置没有找到");
        }
        if(CollectionUtils.isEmpty(log.getEmail().getReceiverList()))
        {
            throw new NullPointerException("日志邮件收件人列表为空");
        }
    }
}
