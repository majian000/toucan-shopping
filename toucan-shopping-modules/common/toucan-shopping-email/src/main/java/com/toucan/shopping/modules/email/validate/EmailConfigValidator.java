package com.toucan.shopping.modules.email.validate;

import com.toucan.shopping.modules.common.properties.modules.email.Email;
import com.toucan.shopping.modules.common.properties.modules.log.Log;
import org.springframework.util.CollectionUtils;

/**
 * 邮件配置校验
 * @author majian
 */
public class EmailConfigValidator {

    public static void validate(Email email)
    {
        if(email==null)
        {
            throw new NullPointerException("邮件没有配置参数");
        }
        if(email.getSmtp()==null)
        {
            throw new NullPointerException("邮件SMTP配置没有找到");
        }
    }
}
