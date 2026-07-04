package com.toucan.shopping.cloud.admin.auth.api.cloud.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.AdminAppServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.cloud.feign.fallback.FeignAdminAppServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/adminApp",fallbackFactory = FeignAdminAppServiceFallbackFactory.class)
public interface FeignAdminAppService extends AdminAppServiceAPI {

    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestVo);


    @RequestMapping(value="/queryListByEntity", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListByEntity(@RequestBody RequestJsonVO requestVo);


    @RequestMapping(value="/deleteByAppCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO deleteByAppCode(@RequestBody RequestJsonVO requestVo);


    @RequestMapping(value="/queryAppListByAdminId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryAppListByAdminId(@RequestBody RequestJsonVO requestVo);


    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO list(@RequestBody RequestJsonVO requestVo);



    /**
     * 查询在线用户列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/online/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO onlineList(@RequestBody RequestJsonVO requestVo);


    /**
     * 查询登录列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/login/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO loginList(@RequestBody RequestJsonVO requestVo);



    /**
     * 修改账号登录状态
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/batchUpdateLoginStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO batchUpdateLoginStatus(@RequestBody RequestJsonVO requestVo);



    /**
     * 查询在线用户列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/logout", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO logout(@RequestBody RequestJsonVO requestVo);


    /**
     * 查询APP登录用户信息
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryAppLoginUserCountList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryAppLoginUserCountList(@RequestBody RequestJsonVO requestVo);




}
