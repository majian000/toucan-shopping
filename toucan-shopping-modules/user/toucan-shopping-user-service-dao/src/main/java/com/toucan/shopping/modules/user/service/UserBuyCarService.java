package com.toucan.shopping.modules.user.service;

import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.entity.UserBuyCar;

import java.util.List;

/**
 * 用户购物车
 * @author majian
 */
public interface UserBuyCarService {

    List<UserBuyCar> findListByEntity(UserBuyCar query);


    List<UserBuyCar> findListByUserMainId(Long userMainId);



    int save(UserBuyCar entity);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

}
