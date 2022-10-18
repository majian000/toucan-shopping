package com.toucan.shopping.cloud.common.data.api.feign.service;

import com.toucan.shopping.cloud.common.data.api.feign.fallback.FeignAreaServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-common-data-proxy/area",fallbackFactory = FeignAreaServiceFallbackFactory.class)
public interface FeignAreaService {


    @RequestMapping(value="/save",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestHeader(value = "toucan-sign-header",defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/query/all",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryAll(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader,@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 根据编码查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/codes",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findByCodes(@RequestBody RequestJsonVO requestVo);

    /**
     * 查询树表格
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryAreaTreeTable(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);



    @RequestMapping(value="/query/tree/table/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryTreeTableByPid(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",method = RequestMethod.POST)
    ResultObjectVO findById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);



    /**
     * 查询地区树
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/tree",method = RequestMethod.POST)
    ResultObjectVO queryTree(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);



    /**
     * 批量删除
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);



    /**
     * 根据ID删除
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);



    /**
     * 编辑
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",method = RequestMethod.POST)
    ResultObjectVO update(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryListByPid(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);




    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/parentCode",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryListByParentCode(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 刷新全部缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/all/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO flushAllCache(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestVo);



    /**
     * 查询全量缓存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/full/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryFullCache(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 查询指定节点下子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/child",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryTreeChildByPid(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据所有市级名称查询出所有市级对象
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/city/list/by/names",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryCityListByNames(@RequestBody RequestJsonVO requestJsonVO);

}
