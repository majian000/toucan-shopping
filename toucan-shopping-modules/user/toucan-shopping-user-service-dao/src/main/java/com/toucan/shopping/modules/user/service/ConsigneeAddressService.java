package com.toucan.shopping.modules.user.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.page.ConsigneeAddressPageInfo;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
import com.toucan.shopping.modules.user.vo.UserSmsVO;

import java.util.List;

/**
 * 收货地址服务
 * @author majian
 * @date 2021-8-4 17:44:57
 */
public interface ConsigneeAddressService {

    int save(ConsigneeAddress entity);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteByIdAndUserMainIdAndAppCode(Long id,Long uerMainId,String appCode);

    /**
     * 修改
     * @param entity
     * @return
     */
    int update(ConsigneeAddress entity);


    List<ConsigneeAddress> findListByEntity(ConsigneeAddressVO query);


    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<ConsigneeAddress> queryListPage(ConsigneeAddressPageInfo queryPageInfo);



}
