package com.toucan.shopping.modules.message.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.message.entity.MessageBody;
import com.toucan.shopping.modules.message.entity.MessageUser;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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

    /**
     * 消息类型编码
     */
    private String messageTypeCode;

    /**
     * 消息类型名称
     */
    private String messageTypeName;

    /**
     * 消息类型应用编码
     */
    private String messageTypeAppCode;


    /**
     * 用户范围 -1:全部用户 1:指定用户
     */
    private Integer userScope;

    /**
     * 用户ID列表字符串
     */
    private String userMainIdListString;


}
