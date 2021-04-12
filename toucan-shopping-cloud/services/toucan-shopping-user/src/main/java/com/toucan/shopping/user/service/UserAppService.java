package com.toucan.shopping.user.service;


import com.toucan.shopping.user.entity.UserApp;

import java.util.List;

public interface UserAppService {

    public int save(UserApp userApp);

    List<UserApp> findListByEntity(UserApp userApp);
}
