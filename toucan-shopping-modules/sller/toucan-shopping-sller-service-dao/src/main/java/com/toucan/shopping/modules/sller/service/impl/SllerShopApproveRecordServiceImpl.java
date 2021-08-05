package com.toucan.shopping.modules.sller.service.impl;

import com.toucan.shopping.modules.sller.entity.SllerShopApproveRecord;
import com.toucan.shopping.modules.sller.mapper.SllerShopApproveRecordMapper;
import com.toucan.shopping.modules.sller.service.SllerShopApproveRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户店铺审核服务
 * @author majian
 * @date 2021-8-4 17:44:57
 */
@Service
public class SllerShopApproveRecordServiceImpl implements SllerShopApproveRecordService {

    @Autowired
    private SllerShopApproveRecordMapper sllerShopApproveRecordMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(SllerShopApproveRecord entity) {
        return sllerShopApproveRecordMapper.insert(entity);
    }



    @Override
    public int deleteById(Long id) {
        return sllerShopApproveRecordMapper.deleteById(id);
    }

    @Override
    public int update(SllerShopApproveRecord entity) {
        return sllerShopApproveRecordMapper.update(entity);
    }

    @Override
    public List<SllerShopApproveRecord> findListByEntity(SllerShopApproveRecord query) {
        return sllerShopApproveRecordMapper.findListByEntity(query);
    }

}
