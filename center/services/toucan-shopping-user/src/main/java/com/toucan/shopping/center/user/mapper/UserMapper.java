package com.toucan.shopping.center.user.mapper;

import com.toucan.shopping.center.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMapper {

    int insert(User user);

    List<User> findListByEntity(User user);

    int deleteById(Long id);

    List<User> findById(Long id);

}
