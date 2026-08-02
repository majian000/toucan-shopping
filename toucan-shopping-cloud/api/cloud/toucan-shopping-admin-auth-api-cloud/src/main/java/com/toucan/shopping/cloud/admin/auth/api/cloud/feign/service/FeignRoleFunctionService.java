package com.toucan.shopping.cloud.admin.auth.api.cloud.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.RoleFunctionServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.cloud.feign.fallback.FeignRoleFunctionServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-admin-auth-proxy/roleFunction", fallbackFactory = FeignRoleFunctionServiceFallbackFactory.class)
public interface FeignRoleFunctionService extends RoleFunctionServiceAPI {


    @RequestMapping(value = "/save/functions", method = RequestMethod.POST)
    ResultObjectVO saveFunctions(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value = "/query/list", method = RequestMethod.POST)
    ResultObjectVO queryRoleFunctionList(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 列表分页
     *
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/list", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO list(@RequestBody RequestJsonVO requestVo);


    /**
     * 根据角色ID和功能项父节点ID查询,并设置节点状态
     *
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/query/function/tree/by/roleId/parentId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryFunctionTreeByRoleIdAndParentId(@RequestBody RequestJsonVO requestVo);

    @RequestMapping(value = "/query/role/function/full/tree", method = RequestMethod.POST)
    ResultObjectVO queryRoleFunctionFullTree(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 刷新缓存
     *
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/refresh/cache", method = RequestMethod.POST)
    ResultObjectVO refreshCache(@RequestBody RequestJsonVO requestJsonVO);

}
