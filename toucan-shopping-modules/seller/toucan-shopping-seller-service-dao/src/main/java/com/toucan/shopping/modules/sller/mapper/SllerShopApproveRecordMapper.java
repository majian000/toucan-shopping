package com.toucan.shopping.modules.sller.mapper;

import com.toucan.shopping.modules.sller.entity.SllerShopApproveRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SllerShopApproveRecordMapper {

    int insert(SllerShopApproveRecord entity);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    int update(SllerShopApproveRecord entity);

    List<SllerShopApproveRecord> findListByEntity(SllerShopApproveRecord query);

}
