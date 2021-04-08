package com.toucan.shopping.user.service;




import com.toucan.shopping.user.entity.UserMobilePhone;

import java.util.List;

public interface UserMobilePhoneService {

    List<UserMobilePhone> findListByEntity(UserMobilePhone entity);

    List<UserMobilePhone> findListByMobilePhone(String mobilePhone);

    List<UserMobilePhone> findListByMobilePhoneLike(String mobilePhone);

    int save(UserMobilePhone entity);


    List<UserMobilePhone> queryListByUserId(Long[] userIdArray);

}
