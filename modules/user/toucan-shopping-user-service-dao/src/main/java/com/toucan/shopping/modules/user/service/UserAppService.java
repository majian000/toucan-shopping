package com.toucan.shopping.modules.user.service;


import com.toucan.shopping.modules.user.entity.UserApp;

import java.util.List;

public interface UserAppService {

    public int save(UserApp userApp);

    List<UserApp> findListByEntity(UserApp userApp);
}
