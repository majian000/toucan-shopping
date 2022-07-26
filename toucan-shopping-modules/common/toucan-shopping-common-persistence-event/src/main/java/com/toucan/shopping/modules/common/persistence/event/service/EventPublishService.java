package com.toucan.shopping.modules.common.persistence.event.service;



import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;

import java.util.Date;
import java.util.List;

public interface EventPublishService {

    /**
     * 查询小于指定时间段的消息
     * @param date
     * @return
     */
    public List<EventPublish> queryFaildListByBefore(Date date);

    public List<EventPublish> queryList(EventPublish eventPublish);

    public int insert(EventPublish eventPublish);

    public int updateStatus(EventPublish eventPublish);

    public EventPublish findById(Long id);
}
