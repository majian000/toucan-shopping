package com.toucan.shopping.modules.user.service;



import com.toucan.shopping.modules.user.entity.UserEmail;

import java.util.List;

public interface UserEmailService {

    List<UserEmail> findListByEntity(UserEmail entity);

    int save(UserEmail entity);

}
