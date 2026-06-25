package com.toucan.shopping.cloud.user.api.single;

import com.toucan.shopping.cloud.user.api.feign.service.FeignUserCollectProductService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserCollectProductBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCollectProductServiceSingleImpl implements FeignUserCollectProductService {

    @Autowired
    private UserCollectProductBusinessService userCollectProductBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return userCollectProductBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteBySkuIdAndUserMainIdAndAppCode(RequestJsonVO requestVo) {
        return userCollectProductBusinessService.deleteBySkuIdAndUserMainIdAndAppCode(requestVo);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return userCollectProductBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO queryCollectProducts(RequestJsonVO requestVo) {
        return userCollectProductBusinessService.queryCollectProducts(requestVo);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return userCollectProductBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return userCollectProductBusinessService.deleteByIds(requestVo);
    }
}
