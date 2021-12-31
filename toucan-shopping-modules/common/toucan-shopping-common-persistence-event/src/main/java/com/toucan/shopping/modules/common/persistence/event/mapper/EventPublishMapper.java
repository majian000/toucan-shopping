package com.toucan.shopping.modules.common.persistence.event.mapper;

import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;


@Mapper
public interface EventPublishMapper {

    List<EventPublish> queryFaildListByBefore(Date date);

    List<EventPublish> queryList(EventPublish eventPublish);

    int insert(EventPublish eventPublish);

    EventPublish findById(Long id);

    int updateStatus(EventPublish eventPublish);
}
