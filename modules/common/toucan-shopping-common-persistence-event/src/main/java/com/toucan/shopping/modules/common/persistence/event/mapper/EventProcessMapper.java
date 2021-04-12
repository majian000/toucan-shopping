package com.toucan.shopping.modules.common.persistence.event.mapper;

import com.toucan.shopping.modules.common.persistence.event.entity.EventProcess;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;


@Mapper
public interface EventProcessMapper {


    public List<EventProcess> queryFaildListByBefore(Date date);

    public List<EventProcess> queryList(EventProcess eventProcess);

    public int insert(EventProcess eventProcess);

    public EventProcess findById(Long id);

    public int updateStatus(EventProcess eventProcess);
}
