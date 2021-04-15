package com.toucan.shopping.modules.user.service;

import com.toucan.shopping.modules.user.vo.UserSmsVO;

public interface SmsService {

    int send(UserSmsVO userSmsVO);
}
