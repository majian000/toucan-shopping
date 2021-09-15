package com.toucan.shopping.cloud.user.api.feign.service;

import com.toucan.shopping.cloud.user.api.feign.fallback.FeignUserLoginHistoryServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/user/loginHistory",fallbackFactory = FeignUserLoginHistoryServiceFallbackFactory.class)
public interface FeignUserLoginHistoryService {




    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    /**
     * 查询10条最近登录的记录
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/list/latest/10",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListByLatest10(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);

}
