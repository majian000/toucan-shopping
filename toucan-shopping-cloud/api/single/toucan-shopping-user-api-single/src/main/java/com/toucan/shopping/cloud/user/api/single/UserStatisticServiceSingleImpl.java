package com.toucan.shopping.cloud.user.api.single;

import com.toucan.shopping.cloud.user.api.feign.service.FeignUserStatisticService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserStatisticBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStatisticServiceSingleImpl implements FeignUserStatisticService {

    @Autowired
    private UserStatisticBusinessService userStatisticBusinessService;

    @Override
    public ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo) {
        return userStatisticBusinessService.queryTotalAndTodayAndCurrentMonthAndCurrentYear(requestVo);
    }

    @Override
    public ResultObjectVO refershTotal(RequestJsonVO requestVo) {
        return userStatisticBusinessService.refershTotal(requestVo);
    }
}
