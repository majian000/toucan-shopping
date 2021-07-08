package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserTrueNameApproveMapper {

    int insert(UserTrueNameApprove entity);

    List<UserTrueNameApprove> findListByEntity(UserTrueNameApprove entity);


}
