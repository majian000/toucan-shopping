package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserMobilePhone;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMobilePhoneMapper {

    int insert(UserMobilePhone userMobilePhone);

    List<UserMobilePhone> findListByEntity(UserMobilePhone userMobilePhone);

    List<UserMobilePhone> findListByMobilePhone(String mobilePhone);

    List<UserMobilePhone> findListByMobilePhoneLike(String mobilePhone);

    List<UserMobilePhone> queryListByUserId(Long[] userIdArray);

}
