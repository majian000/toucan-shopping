package com.toucan.shopping.modules.message.redis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.message.redis.MessageTypeKey;
import com.toucan.shopping.modules.message.redis.service.MessageTypeRedisService;
import com.toucan.shopping.modules.message.vo.MessageTypeVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageTypeRedisServiceImpl implements MessageTypeRedisService {

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Override
    public void flush(List<MessageTypeVO> messageTypeVOList) {
        toucanStringRedisService.set(MessageTypeKey.getMessageTypeKey(), JSONObject.toJSONString(messageTypeVOList));
    }

    @Override
    public List<MessageTypeVO> queryAll() {
        Object messageTypesObject = toucanStringRedisService.get(MessageTypeKey.getMessageTypeKey());
        if(messageTypesObject!=null)
        {
            return JSONObject.parseArray(String.valueOf(messageTypesObject),MessageTypeVO.class);
        }
        return null;
    }

    @Override
    public MessageTypeVO queryByCode(String code) {
        List<MessageTypeVO> messageTypeVOS = this.queryAll();
        if(messageTypeVOS!=null)
        {
            for(MessageTypeVO messageTypeVO:messageTypeVOS)
            {
                if(messageTypeVO.getCode().equals(code))
                {
                    return messageTypeVO;
                }
            }
        }
        return null;
    }


}
