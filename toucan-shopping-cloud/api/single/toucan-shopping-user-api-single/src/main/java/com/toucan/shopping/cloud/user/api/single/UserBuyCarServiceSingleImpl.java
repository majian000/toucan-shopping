package com.toucan.shopping.cloud.user.api.single;

import com.toucan.shopping.cloud.user.api.UserBuyCarServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserBuyCarBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBuyCarServiceSingleImpl implements UserBuyCarServiceAPI {

    @Autowired
    private UserBuyCarBusinessService userBuyCarBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return userBuyCarBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO removeBuyCar(RequestJsonVO requestVo) {
        return userBuyCarBusinessService.removeBuyCar(requestVo);
    }

    @Override
    public ResultObjectVO listByUserMainId(RequestJsonVO requestJsonVO) {
        return userBuyCarBusinessService.listByUserMainId(requestJsonVO);
    }

    @Override
    public ResultObjectVO clearByUserMainId(RequestJsonVO requestVo) {
        return userBuyCarBusinessService.clearByUserMainId(requestVo);
    }

    @Override
    public ResultObjectVO updates(RequestJsonVO requestJsonVO) {
        return userBuyCarBusinessService.updates(requestJsonVO);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return userBuyCarBusinessService.update(requestJsonVO);
    }
}
