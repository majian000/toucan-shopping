package com.toucan.shopping.center.user.service;




import com.toucan.shopping.center.user.entity.UserMobilePhone;

import java.util.List;

public interface UserMobilePhoneService {

    List<UserMobilePhone> findListByEntity(UserMobilePhone entity);

    List<UserMobilePhone> findListByMobilePhone(String mobilePhone);

    int save(UserMobilePhone entity);

}
