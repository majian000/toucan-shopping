package com.toucan.shopping.cloud.common.data.api.cloud.feign.service;

import com.toucan.shopping.cloud.common.data.api.ColorTableServiceAPI;
import com.toucan.shopping.cloud.common.data.api.cloud.feign.fallback.FeignColorTableServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-common-data-proxy/colorTable",fallbackFactory = FeignColorTableServiceFallbackFactory.class)
public interface FeignColorTableService extends ColorTableServiceAPI {


    @RequestMapping(value="/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);


    @RequestMapping(value="/query/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryList(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/names", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListByNames(@RequestBody RequestJsonVO requestJsonVO);


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
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO findById(@RequestBody RequestJsonVO requestVo);




    /**
     * 根据ID删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo);


    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo);



}
