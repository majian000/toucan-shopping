package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.entity.UserCollectProduct;
import com.toucan.shopping.modules.user.page.ConsigneeAddressPageInfo;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
import com.toucan.shopping.modules.user.vo.UserCollectProductVO;
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

    List<UserCollectProduct> findListByEntity(UserCollectProductVO query);

    int deleteByIdAndUserMainIdAndAppCode(Long id, Long userMainId, String appCode);

    int deleteBySkuIdAndUserMainIdAndAppCode(Long skuId, Long userMainId, String appCode);


}
