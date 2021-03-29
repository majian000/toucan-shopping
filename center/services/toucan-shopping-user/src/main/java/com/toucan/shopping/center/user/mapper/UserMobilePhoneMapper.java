package com.toucan.shopping.center.user.mapper;

import com.toucan.shopping.center.user.entity.User;
import com.toucan.shopping.center.user.entity.UserMobilePhone;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMobilePhoneMapper {

    int insert(UserMobilePhone userMobilePhone);

    List<UserMobilePhone> findListByEntity(UserMobilePhone userMobilePhone);


    List<UserMobilePhone> findListByMobilePhone(String mobilePhone);


}
