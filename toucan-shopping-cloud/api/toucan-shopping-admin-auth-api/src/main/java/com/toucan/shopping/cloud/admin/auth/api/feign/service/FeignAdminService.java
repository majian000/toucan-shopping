package com.toucan.shopping.cloud.admin.auth.api.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.feign.fallback.FeignAdminServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/admin",fallbackFactory = FeignAdminServiceFallbackFactory.class)
public interface FeignAdminService {

    /**
     * 登录账号
     * @param signHeader
     * @param requestVo
     * @return
     */
    @PostMapping("/login")
    ResultObjectVO login(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);

    @PostMapping("/query/login/token")
    ResultObjectVO queryLoginToken(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);

    @PostMapping("/is/online")
    ResultObjectVO isOnline(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);



    @RequestMapping(value="/queryListByEntity",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListByEntity(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);

    /**
     * 保存
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",method = RequestMethod.POST)
    ResultObjectVO save(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);



    /**
     * 编辑
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",method = RequestMethod.POST)
    ResultObjectVO update(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);



    /**
     * 列表分页
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO list(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findById(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 退出登录
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/logout",produces = "application/json;charset=UTF-8")
    ResultObjectVO logout(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 修改密码
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update/password",produces = "application/json;charset=UTF-8")
    ResultObjectVO updatePassword(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);





    /**
     * 根据ID删除指定角色
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    /**
     * 批量删除
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);




}
