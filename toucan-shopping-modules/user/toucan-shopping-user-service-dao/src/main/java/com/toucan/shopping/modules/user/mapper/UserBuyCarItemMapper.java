package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserBuyCarItem;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserBuyCarItemMapper {


    List<UserBuyCarItem> findListByEntity(UserBuyCarItemVO query);

    List<UserBuyCarItem> findListByUserMainId(Long userMainId);


    int insert(UserBuyCarItem entity);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    int update(UserBuyCarItem entity);

    int deleteByIdAndUserMainId(Long id, Long userMainId);

    int deleteByUserMainId(Long userMainId);

    UserBuyCarItem findById(Long id);
}
