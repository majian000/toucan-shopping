package com.toucan.shopping.modules.log.message;

import com.toucan.shopping.modules.common.vo.email.Email;
import lombok.Data;

/**
 * 日志邮件消息
 */
@Data
public class LogEmailMessage {
    private Email email;
}
