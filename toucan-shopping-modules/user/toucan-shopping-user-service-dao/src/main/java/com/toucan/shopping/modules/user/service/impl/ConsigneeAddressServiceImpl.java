package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.mapper.ConsigneeAddressMapper;
import com.toucan.shopping.modules.user.service.ConsigneeAddressService;
import com.toucan.shopping.modules.user.service.SmsService;
import com.toucan.shopping.modules.user.vo.UserSmsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货地址服务
 * @author majian
 * @date 2021-8-4 17:44:57
 */
@Service
public class ConsigneeAddressServiceImpl implements ConsigneeAddressService {

    @Autowired
    private ConsigneeAddressMapper consigneeAddressMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(ConsigneeAddress entity) {
        return consigneeAddressMapper.insert(entity);
    }



    @Override
    public int deleteById(Long id) {
        return consigneeAddressMapper.deleteById(id);
    }

    @Override
    public int update(ConsigneeAddress entity) {
        return consigneeAddressMapper.update(entity);
    }

    @Override
    public List<ConsigneeAddress> findListByEntity(ConsigneeAddress query) {
        return consigneeAddressMapper.findListByEntity(query);
    }

}
