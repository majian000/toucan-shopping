package com.toucan.shopping.modules.message.vo;

import com.toucan.shopping.modules.message.entity.MessageBody;
import com.toucan.shopping.modules.message.entity.MessageType;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 消息内容
 * @author majian
 */
@Data
public class MessageBodyVO extends MessageBody {

    /**
     * 消息体ID
     */
    private Set<Long> idSet;

}
