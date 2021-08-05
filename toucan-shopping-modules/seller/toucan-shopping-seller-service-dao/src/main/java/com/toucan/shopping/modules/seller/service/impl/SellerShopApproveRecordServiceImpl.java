package com.toucan.shopping.modules.seller.service.impl;

import com.toucan.shopping.modules.seller.entity.SellerShopApproveRecord;
import com.toucan.shopping.modules.seller.mapper.SellerShopApproveRecordMapper;
import com.toucan.shopping.modules.seller.service.SellerShopApproveRecordService;
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
public class SellerShopApproveRecordServiceImpl implements SellerShopApproveRecordService {

    @Autowired
    private SellerShopApproveRecordMapper sllerShopApproveRecordMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(SellerShopApproveRecord entity) {
        return sllerShopApproveRecordMapper.insert(entity);
    }



    @Override
    public int deleteById(Long id) {
        return sllerShopApproveRecordMapper.deleteById(id);
    }

    @Override
    public int update(SellerShopApproveRecord entity) {
        return sllerShopApproveRecordMapper.update(entity);
    }

    @Override
    public List<SellerShopApproveRecord> findListByEntity(SellerShopApproveRecord query) {
        return sllerShopApproveRecordMapper.findListByEntity(query);
    }

}
