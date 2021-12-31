package com.toucan.shopping.modules.common.persistence.event.mapper;

import com.toucan.shopping.modules.common.persistence.event.entity.EventProcess;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;


@Mapper
public interface EventProcessMapper {


    List<EventProcess> queryFaildListByBefore(Date date);

    List<EventProcess> queryList(EventProcess eventProcess);

    int insert(EventProcess eventProcess);

    EventProcess findById(Long id);

    int updateStatus(EventProcess eventProcess);
}
