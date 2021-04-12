package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserUserName;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserUserNameMapper {

    int insert(UserUserName userUserName);

    List<UserUserName> findListByEntity(UserUserName userUserName);

    List<UserUserName> queryListByUserId(Long[] userIdArray);

}
