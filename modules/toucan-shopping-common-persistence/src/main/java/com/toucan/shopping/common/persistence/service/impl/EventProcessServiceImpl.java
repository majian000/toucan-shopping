package com.toucan.shopping.common.persistence.service.impl;

import com.toucan.shopping.common.persistence.mapper.EventProcessMapper;
import com.toucan.shopping.common.persistence.service.EventProcessService;
import com.toucan.shopping.common.persistence.entity.EventProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class EventProcessServiceImpl implements EventProcessService {

    @Autowired
    private EventProcessMapper eventProcessMapper;

    @Override
    public List<EventProcess> queryFaildListByBefore(Date date) {
        return eventProcessMapper.queryFaildListByBefore(date);
    }

    @Override
    public List<EventProcess> queryList(EventProcess eventProcess) {
        return eventProcessMapper.queryList(eventProcess);
    }

    @Transactional
    @Override
    public int insert(EventProcess transactionMessage) {
        return eventProcessMapper.insert(transactionMessage);
    }

    @Override
    public int updateStatus(EventProcess eventProcess) {
        return eventProcessMapper.updateStatus(eventProcess);
    }


    public EventProcess findById(Long id){
        return eventProcessMapper.findById(id);
    }
}
