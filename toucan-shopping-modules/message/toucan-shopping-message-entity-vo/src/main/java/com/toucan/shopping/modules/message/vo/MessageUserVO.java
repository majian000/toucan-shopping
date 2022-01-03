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
     * 发送时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date sendDateYearMonthDay;



}
