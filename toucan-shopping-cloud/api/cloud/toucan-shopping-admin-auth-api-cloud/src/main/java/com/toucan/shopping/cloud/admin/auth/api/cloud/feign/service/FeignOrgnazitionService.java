package com.toucan.shopping.cloud.admin.auth.api.cloud.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.OrgnazitionServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.cloud.feign.fallback.FeignOrgnazitionServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/orgnazition",fallbackFactory = FeignOrgnazitionServiceFallbackFactory.class)
public interface FeignOrgnazitionService extends OrgnazitionServiceAPI {

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
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO queryAppOrgnazitionTreeTable(@RequestBody RequestJsonVO requestJsonVO);


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


    /**
     * 查询组织机构树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/orgnazation/tree",method = RequestMethod.POST)
    ResultObjectVO queryOrgnazationTree(@RequestBody RequestJsonVO requestJsonVO);





    @RequestMapping(value = "/query/admin/orgnazition/tree",method = RequestMethod.POST)
    ResultObjectVO queryAdminOrgnazitionTree(@RequestBody RequestJsonVO requestJsonVO);


}
