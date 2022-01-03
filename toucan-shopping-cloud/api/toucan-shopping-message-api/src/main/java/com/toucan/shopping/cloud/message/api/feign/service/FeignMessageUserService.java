package com.toucan.shopping.cloud.message.api.feign.service;

import com.toucan.shopping.cloud.message.api.feign.fallback.FeignMessageUserServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 消息服务
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-message-proxy/message/user",fallbackFactory = FeignMessageUserServiceFallbackFactory.class)
public interface FeignMessageUserService {

    @RequestMapping(value="/send",produces = "application/json;charset=UTF-8")
    ResultObjectVO send(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);




    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/user/query/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPageByUserMianId(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询未读数量
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/user/query/unread/count",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryUnreadCountByUserMainId(@RequestBody RequestJsonVO requestJsonVO);

}
