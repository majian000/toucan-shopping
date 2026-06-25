package com.toucan.shopping.cloud.user.api.single;

import com.toucan.shopping.cloud.user.api.UserLoginHistoryServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserLoginHistoryBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginHistoryServiceSingleImpl implements UserLoginHistoryServiceAPI {

    @Autowired
    private UserLoginHistoryBusinessService userLoginHistoryBusinessService;

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestVo) {
        return userLoginHistoryBusinessService.queryListPage(requestVo);
    }

    @Override
    public ResultObjectVO queryListByLatest10(RequestJsonVO requestVo) {
        return userLoginHistoryBusinessService.queryListByLatest10(requestVo);
    }
}
