package com.toucan.shopping.modules.message.vo;

import com.toucan.shopping.modules.message.entity.MessageBody;
import com.toucan.shopping.modules.message.entity.MessageUser;
import lombok.Data;

/**
 * 消息用户
 * @author majian
 */
@Data
public class MessageUserVO extends MessageUser {

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 内容类型 1:纯文本
     */
    private Integer contentType;


}
