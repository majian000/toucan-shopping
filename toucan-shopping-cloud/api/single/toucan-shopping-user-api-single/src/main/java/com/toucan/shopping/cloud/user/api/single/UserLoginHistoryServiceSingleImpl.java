package com.toucan.shopping.cloud.user.api.single;

import com.toucan.shopping.cloud.user.api.feign.service.FeignUserLoginHistoryService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserLoginHistoryBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginHistoryServiceSingleImpl implements FeignUserLoginHistoryService {

    @Autowired
    private UserLoginHistoryBusinessService userLoginHistoryBusinessService;

    @Override
    public ResultObjectVO queryListPage(String signHeader, RequestJsonVO requestVo) {
        return userLoginHistoryBusinessService.queryListPage(requestVo);
    }

    @Override
    public ResultObjectVO queryListByLatest10(String signHeader, RequestJsonVO requestVo) {
        return userLoginHistoryBusinessService.queryListByLatest10(requestVo);
    }
}
