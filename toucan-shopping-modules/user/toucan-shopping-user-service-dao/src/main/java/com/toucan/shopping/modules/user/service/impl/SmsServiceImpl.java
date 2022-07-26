package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.mapper.UserMapper;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.service.SmsService;
import com.toucan.shopping.modules.user.service.UserService;
import com.toucan.shopping.modules.user.vo.UserSmsVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SmsServiceImpl implements SmsService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public int send(UserSmsVO userSmsVO) {
        logger.info("调用第三方短信接口 {}  {}",userSmsVO.getMobilePhone(),userSmsVO.getMsg());

        return 1;
    }
}
