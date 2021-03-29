package com.toucan.shopping.center.user.service;



import com.toucan.shopping.center.user.entity.UserEmail;

import java.util.List;

public interface UserEmailService {

    List<UserEmail> findListByEntity(UserEmail entity);

    int save(UserEmail entity);

}
