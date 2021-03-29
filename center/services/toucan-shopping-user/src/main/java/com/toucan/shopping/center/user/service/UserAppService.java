package com.toucan.shopping.center.user.service;


import com.toucan.shopping.center.user.entity.UserApp;

import java.util.List;

public interface UserAppService {

    public int save(UserApp userApp);

    List<UserApp> findListByEntity(UserApp userApp);
}
