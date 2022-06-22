package com.toucan.shopping.cloud.admin.auth.api.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignOperateServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/operateLog",fallbackFactory = FeignOperateServiceFallbackFactory.class)
public interface FeignOperateLogService {


    /**
     * 批量保存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/saves",produces = "application/json;charset=UTF-8")
    ResultObjectVO saves(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询操作日志统计表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/queryOperateChart",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryOperateChart(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/list/page",method = RequestMethod.POST)
    ResultObjectVO listPage(@RequestBody RequestJsonVO requestVo);



}
