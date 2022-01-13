package com.toucan.shopping.modules.message.redis.service;

import com.toucan.shopping.modules.message.vo.MessageTypeVO;

import java.util.List;

/**
 * 消息分类缓存服务
 * @auth majian
 * @date 2022-1-13 14:22:16
 */
public interface MessageTypeRedisService {


    /**
     * 刷新到缓存
     * @param messageTypeVOList
     */
    void flush(List<MessageTypeVO> messageTypeVOList);

    /**
     * 查询缓存中全部消息分类
     * @return
     */
    List<MessageTypeVO> queryAll();

    /**
     * 根据消息编码查询消息分类
     * @param code
     * @return
     */
    MessageTypeVO queryByCode(String code);

}
