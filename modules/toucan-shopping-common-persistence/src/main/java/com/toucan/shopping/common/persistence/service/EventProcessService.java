package com.toucan.shopping.common.persistence.service;



import com.toucan.shopping.common.persistence.entity.EventProcess;
import com.toucan.shopping.common.persistence.entity.EventPublish;

import java.util.Date;
import java.util.List;

public interface EventProcessService {


    /**
     * 查询小于指定时间段的消息
     * @param date
     * @return
     */
    public List<EventProcess> queryFaildListByBefore(Date date);

    public List<EventProcess> queryList(EventProcess eventProcess);

    public int insert(EventProcess eventProcess);

    public int updateStatus(EventProcess eventProcess);

    public EventProcess findById(Long id);
}
