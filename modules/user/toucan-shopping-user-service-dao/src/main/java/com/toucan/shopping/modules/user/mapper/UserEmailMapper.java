package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserEmail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserEmailMapper {

    int insert(UserEmail userEmail);

    List<UserEmail> findListByEntity(UserEmail userEmail);


}
