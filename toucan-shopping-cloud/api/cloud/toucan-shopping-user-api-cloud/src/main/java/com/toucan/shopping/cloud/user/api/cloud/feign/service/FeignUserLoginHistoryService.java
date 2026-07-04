package com.toucan.shopping.cloud.user.api.cloud.feign.service;

import com.toucan.shopping.cloud.user.api.UserLoginHistoryServiceAPI;
import com.toucan.shopping.cloud.user.api.cloud.feign.fallback.FeignUserLoginHistoryServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/user/loginHistory",fallbackFactory = FeignUserLoginHistoryServiceFallbackFactory.class)
public interface FeignUserLoginHistoryService extends UserLoginHistoryServiceAPI {




    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo);


    /**
     * 查询10条最近登录的记录
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/query/list/latest/10", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListByLatest10(@RequestBody RequestJsonVO requestVo);

}
