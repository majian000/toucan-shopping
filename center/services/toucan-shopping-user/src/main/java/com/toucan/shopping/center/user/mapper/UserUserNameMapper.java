package com.toucan.shopping.center.user.mapper;

import com.toucan.shopping.center.user.entity.UserUserName;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserUserNameMapper {

    int insert(UserUserName userUserName);

    List<UserUserName> findListByEntity(UserUserName userUserName);


}
