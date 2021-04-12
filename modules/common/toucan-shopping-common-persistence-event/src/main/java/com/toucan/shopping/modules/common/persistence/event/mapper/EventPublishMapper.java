package com.toucan.shopping.modules.common.persistence.event.mapper;

import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;


@Mapper
public interface EventPublishMapper {

    public List<EventPublish> queryFaildListByBefore(Date date);

    public List<EventPublish> queryList(EventPublish eventPublish);

    public int insert(EventPublish eventPublish);

    public EventPublish findById(Long id);

    public int updateStatus(EventPublish eventPublish);
}
