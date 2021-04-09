package com.toucan.shopping.common.persistence.event.service;



import com.toucan.shopping.common.persistence.event.entity.EventPublish;

import java.util.Date;
import java.util.List;

public interface EventPublishService {

    /**
     * 查询小于指定时间段的消息
     * @param date
     * @return
     */
    public List<EventPublish> queryFaildListByBefore(Date date);

    public List<EventPublish> queryList(EventPublish mqTransaction);

    public int insert(EventPublish mqTransaction);

    public int updateStatus(EventPublish eventPublish);

    public EventPublish findById(Long id);
}
