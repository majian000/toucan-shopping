package com.toucan.shopping.modules.user.service;


import com.toucan.shopping.modules.user.entity.UserApp;

import java.util.List;

public interface UserAppService {

    int save(UserApp userApp);

    List<UserApp> findListByEntity(UserApp userApp);
}
