package com.toucan.shopping.common.persistence.mapper;

import com.toucan.shopping.common.persistence.entity.EventProcess;
import com.toucan.shopping.common.persistence.entity.EventPublish;
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
