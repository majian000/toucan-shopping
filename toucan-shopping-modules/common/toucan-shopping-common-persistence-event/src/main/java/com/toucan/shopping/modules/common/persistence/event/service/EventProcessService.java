package com.toucan.shopping.modules.common.persistence.event.service;




import com.toucan.shopping.modules.common.persistence.event.entity.EventProcess;

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
