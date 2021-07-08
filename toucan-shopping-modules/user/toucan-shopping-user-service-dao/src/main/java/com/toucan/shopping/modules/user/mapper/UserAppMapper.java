package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserApp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserAppMapper {

    int insert(UserApp entity);

    List<UserApp> findListByEntity(UserApp entity);


}
