package com.toucan.shopping.cloud.admin.auth.api.cloud.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.RoleServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.cloud.feign.fallback.FeignRoleServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/role",fallbackFactory = FeignRoleServiceFallbackFactory.class)
public interface FeignRoleService extends RoleServiceAPI {

    /**
     * 保存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",method = RequestMethod.POST)
    ResultObjectVO save(@RequestBody RequestJsonVO requestVo);


    /**
     * 编辑
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",method = RequestMethod.POST)
    ResultObjectVO update(@RequestBody RequestJsonVO requestVo);


    /**
     * 查询列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/list/page",method = RequestMethod.POST)
    ResultObjectVO listPage(@RequestBody RequestJsonVO requestVo);



    /**
     * 查询指定用户的角色树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/admin/role/tree",method = RequestMethod.POST)
    public ResultObjectVO queryAdminRoleTree(@RequestBody RequestJsonVO requestJsonVO);




    /**
     * 根据ID删除指定角色
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",method = RequestMethod.POST)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo);




    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",method = RequestMethod.POST)
    ResultObjectVO findById(@RequestBody RequestJsonVO requestVo);


    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",method = RequestMethod.POST)
    ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo);



}
