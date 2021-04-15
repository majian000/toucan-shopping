package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserApp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserAppMapper {

    public int insert(UserApp userApp);

    public List<UserApp> findListByEntity(UserApp userApp);


}
