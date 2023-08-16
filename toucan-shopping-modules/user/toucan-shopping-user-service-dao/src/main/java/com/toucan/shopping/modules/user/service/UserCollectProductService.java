package com.toucan.shopping.modules.user.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.entity.UserCollectProduct;
import com.toucan.shopping.modules.user.page.ConsigneeAddressPageInfo;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
import com.toucan.shopping.modules.user.vo.UserCollectProductVO;

import java.util.List;

/**
 * 用户收藏商品
 */
public interface UserCollectProductService {

    int save(UserCollectProduct entity);



    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);


    List<UserCollectProduct> findListByEntity(UserCollectProductVO query);


}
