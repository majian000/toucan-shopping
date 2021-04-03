package com.toucan.shopping.user.service;



import com.toucan.shopping.user.entity.UserEmail;

import java.util.List;

public interface UserEmailService {

    List<UserEmail> findListByEntity(UserEmail entity);

    int save(UserEmail entity);

}
