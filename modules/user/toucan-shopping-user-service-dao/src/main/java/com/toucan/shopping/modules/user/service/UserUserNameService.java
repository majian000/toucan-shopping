package com.toucan.shopping.modules.user.service;




import com.toucan.shopping.modules.user.entity.UserUserName;

import java.util.List;

public interface UserUserNameService {

    List<UserUserName> findListByEntity(UserUserName entity);

    int save(UserUserName entity);


    List<UserUserName> queryListByUserId(Long[] userIdArray);
}
