package com.toucan.shopping.cloud.admin.auth.api.cloud.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.cloud.feign.fallback.FeignAdminServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/admin",fallbackFactory = FeignAdminServiceFallbackFactory.class)
public interface FeignAdminService extends AdminServiceAPI {

    /**
     * 登录账号
     * @param requestVo
     * @return
     */
    @PostMapping("/login")
    ResultObjectVO login(@RequestBody RequestJsonVO requestVo);

    @PostMapping("/query/login/token")
    ResultObjectVO queryLoginToken(@RequestBody RequestJsonVO requestVo);

    @PostMapping("/is/online")
    ResultObjectVO isOnline(@RequestBody RequestJsonVO requestVo);



    @RequestMapping(value="/queryListByEntity",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryListByEntity(@RequestBody RequestJsonVO requestVo);

    /**
     * 保存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",method = RequestMethod.POST)
    ResultObjectVO save( @RequestBody RequestJsonVO requestVo);



    /**
     * 编辑
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",method = RequestMethod.POST)
    ResultObjectVO update( @RequestBody RequestJsonVO requestVo);



    /**
     * 列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO list(@RequestBody RequestJsonVO requestVo);


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findById(@RequestBody RequestJsonVO requestVo);


    /**
     * 退出登录
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/logout",produces = "application/json;charset=UTF-8")
    ResultObjectVO logout(@RequestBody RequestJsonVO requestVo);


    /**
     * 修改密码
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update/password",produces = "application/json;charset=UTF-8")
    ResultObjectVO updatePassword(@RequestBody RequestJsonVO requestVo);





    /**
     * 根据ID删除指定角色
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",method = RequestMethod.DELETE)
    ResultObjectVO deleteById( @RequestBody RequestJsonVO requestVo);


    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds( @RequestBody RequestJsonVO requestVo);




}
