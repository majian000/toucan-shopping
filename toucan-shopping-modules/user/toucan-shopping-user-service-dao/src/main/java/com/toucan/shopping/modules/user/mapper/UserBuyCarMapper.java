package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.entity.UserBuyCar;
import com.toucan.shopping.modules.user.vo.UserBuyCarVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserBuyCarMapper {


    List<UserBuyCar> findListByEntity(UserBuyCarVO query);

    List<UserBuyCar> findListByUserMainId(Long userMainId);


    int insert(UserBuyCar entity);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    int update(UserBuyCar entity);
}
