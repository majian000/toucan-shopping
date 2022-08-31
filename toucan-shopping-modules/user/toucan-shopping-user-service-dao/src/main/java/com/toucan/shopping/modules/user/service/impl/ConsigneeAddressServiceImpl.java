package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.mapper.ConsigneeAddressMapper;
import com.toucan.shopping.modules.user.page.ConsigneeAddressPageInfo;
import com.toucan.shopping.modules.user.service.ConsigneeAddressService;
import com.toucan.shopping.modules.user.service.SmsService;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
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
    public int deleteByIdAndUserMainIdAndAppCode(Long id, Long userMainId,String appCode) {
        return consigneeAddressMapper.deleteByIdAndUserMainIdAndAppCode(id,userMainId,appCode);
    }

    @Override
    public ConsigneeAddressVO findByIdAndUserMainIdAndAppCode(Long id, Long userMainId, String appCode) {
        return consigneeAddressMapper.findByIdAndUserMainIdAndAppCode(id,userMainId,appCode);
    }


    @Override
    public ConsigneeAddressVO findDefaultByUserMainIdAndAppCode(Long userMainId,String appCode){
        return consigneeAddressMapper.findDefaultByUserMainIdAndAppCode(userMainId,appCode);
    }


    @Override
    public ConsigneeAddressVO findNewestOneByUserMainIdAndAppCode(Long userMainId,String appCode){
        return consigneeAddressMapper.findNewestOneByUserMainIdAndAppCode(userMainId,appCode);
    }


    @Override
    public int setDefaultByIdAndUserMainIdAndAppCode(Long id, Long userMainId,String appCode) {
        return consigneeAddressMapper.setDefaultByIdAndUserMainIdAndAppCode(id,userMainId,appCode);
    }

    @Override
    public int setCancelDefaultByUserMainIdAndAppCode(Long userMainId, String appCode) {
        return consigneeAddressMapper.setCancelDefaultByUserMainIdAndAppCode(userMainId,appCode);
    }

    @Override
    public int update(ConsigneeAddress entity) {
        return consigneeAddressMapper.update(entity);
    }

    @Override
    public List<ConsigneeAddress> findListByEntity(ConsigneeAddressVO query) {
        return consigneeAddressMapper.findListByEntity(query);
    }



    @Override
    public PageInfo<ConsigneeAddress> queryListPage(ConsigneeAddressPageInfo queryPageInfo) {
        PageInfo<ConsigneeAddress> pageInfo = new PageInfo();
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        pageInfo.setList(consigneeAddressMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(consigneeAddressMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }
}
