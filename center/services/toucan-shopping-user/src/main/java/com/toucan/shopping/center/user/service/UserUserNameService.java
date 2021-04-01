package com.toucan.shopping.center.user.service;




import com.toucan.shopping.center.user.entity.UserMobilePhone;
import com.toucan.shopping.center.user.entity.UserUserName;

import java.util.List;

public interface UserUserNameService {

    List<UserUserName> findListByEntity(UserUserName entity);

    int save(UserUserName entity);


    List<UserUserName> queryListByUserId(Long[] userIdArray);
}
