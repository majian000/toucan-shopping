package com.toucan.shopping.modules.user.service;

import com.toucan.shopping.modules.user.entity.UserBuyCarItem;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;

import java.util.List;

/**
 * 用户购物车
 * @author majian
 */
public interface UserBuyCarItemService {

    List<UserBuyCarItem> findListByEntity(UserBuyCarItemVO query);


    List<UserBuyCarItem> findListByUserMainId(Long userMainId);

    UserBuyCarItem findById(Long id);

    int save(UserBuyCarItem entity);

    int update(UserBuyCarItem entity);

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
    int deleteByIdAndUserMainId(Long id,Long userMainId);

    /**
     * 根据ID删除
     * @return
     */
    int deleteByUserMainId(Long userMainId);
}
