package com.toucan.shopping.modules.message.vo;

import com.toucan.shopping.modules.message.entity.MessageType;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 消息
 * @author majian
 */
@Data
public class MessageVO {

    /**
     * 消息主体
     */
    private MessageBodyVO messageBody;


    /**
     * 消息关联用户
     */
    private List<MessageUserVO> users;

    public MessageVO()
    {
        this.messageBody = new MessageBodyVO();
        this.users = new LinkedList<>();
    }

    public MessageVO(String title, String content, List<Long> userMainIdList)
    {
        this.messageBody = new MessageBodyVO();
        this.messageBody.setTitle(title);
        this.messageBody.setContent(content);
        this.messageBody.setDeleteStatus((short)0);

        if(CollectionUtils.isNotEmpty(userMainIdList))
        {
            this.users = new LinkedList<MessageUserVO>();
            for(Long userMainId:userMainIdList)
            {
                MessageUserVO messageUserVO = new MessageUserVO();
                messageUserVO.setUserMainId(userMainId);
                Date createDate = new Date();
                messageUserVO.setSendDate(createDate);
                messageUserVO.setStatus(0);
                messageUserVO.setDeleteStatus((short)0);
                messageUserVO.setCreateDate(createDate);

                this.users.add(messageUserVO);
            }
        }
    }


    public MessageVO(String title, String content, Long userMainId)
    {
        this.messageBody = new MessageBodyVO();
        this.messageBody.setTitle(title);
        this.messageBody.setContent(content);
        this.messageBody.setDeleteStatus((short)0);

        if(userMainId!=null)
        {
            this.users = new LinkedList<MessageUserVO>();
            MessageUserVO messageUserVO = new MessageUserVO();
            messageUserVO.setUserMainId(userMainId);
            Date createDate = new Date();
            messageUserVO.setSendDate(createDate);
            messageUserVO.setStatus(0);
            messageUserVO.setDeleteStatus((short)0);
            messageUserVO.setCreateDate(createDate);

            this.users.add(messageUserVO);
        }
    }


    public MessageVO(String title, String content,Integer contentType, Long userMainId)
    {
        this.messageBody = new MessageBodyVO();
        this.messageBody.setTitle(title);
        this.messageBody.setContent(content);
        this.messageBody.setDeleteStatus((short)0);
        this.messageBody.setContentType(contentType);

        if(userMainId!=null)
        {
            this.users = new LinkedList<MessageUserVO>();
            MessageUserVO messageUserVO = new MessageUserVO();
            messageUserVO.setUserMainId(userMainId);
            Date createDate = new Date();
            messageUserVO.setSendDate(createDate);
            messageUserVO.setStatus(0);
            messageUserVO.setDeleteStatus((short)0);
            messageUserVO.setCreateDate(createDate);

            this.users.add(messageUserVO);
        }
    }


    public String getTitle()
    {
        return this.messageBody.getTitle();
    }


    public void setSendDate(Date sendDate)
    {
        for(MessageUserVO userVO:this.users)
        {
            userVO.setSendDate(sendDate);
        }
    }


    public void setMessageType(MessageType messageType)
    {
        this.messageBody.setMessageTypeCode(messageType.getCode());
        this.messageBody.setMessageTypeName(messageType.getName());
        this.messageBody.setMessageTypeAppCode(messageType.getAppCode());

        for(MessageUserVO userVO:this.users)
        {
            userVO.setMessageTypeCode(messageType.getCode());
            userVO.setMessageTypeName(messageType.getName());
            userVO.setMessageTypeAppCode(messageType.getAppCode());
        }
    }


    public void setMessageType(String typeCode,String typeName,String typeAppCode)
    {
        this.messageBody.setMessageTypeCode(typeCode);
        this.messageBody.setMessageTypeName(typeName);
        this.messageBody.setMessageTypeAppCode(typeAppCode);

        for(MessageUserVO userVO:this.users)
        {
            userVO.setMessageTypeCode(typeCode);
            userVO.setMessageTypeName(typeName);
            userVO.setMessageTypeAppCode(typeAppCode);
        }
    }



}
