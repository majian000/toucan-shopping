package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.entity.UserCollectProduct;
import com.toucan.shopping.modules.user.page.ConsigneeAddressPageInfo;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserCollectProductMapper {

    int insert(UserCollectProduct entity);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);


}
