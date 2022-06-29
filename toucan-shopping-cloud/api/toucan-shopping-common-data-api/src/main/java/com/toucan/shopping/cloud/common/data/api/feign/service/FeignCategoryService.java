package com.toucan.shopping.cloud.common.data.api.feign.service;

import com.toucan.shopping.cloud.common.data.api.feign.fallback.FeignCategoryServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-common-data-proxy/category",fallbackFactory = FeignCategoryServiceFallbackFactory.class)
public interface FeignCategoryService {


    @RequestMapping(value = "/save",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value = "/query/id",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO);


    @RequestMapping(value = "/query/ids",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByIdList(@RequestBody RequestJsonVO requestJsonVO);


    @RequestMapping(value = "/find/idArray",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    ResultObjectVO findByIdArray(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据ID查询返回分类ID路径
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/path/by/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findIdPathById(@RequestBody RequestJsonVO requestVo);

    /**
     * 根据ID查询
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",method = RequestMethod.POST)
    ResultObjectVO findById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    /**
     * 查询树表格
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryTreeTable(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);




    /**
     * 编辑
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",method = RequestMethod.POST)
    ResultObjectVO update(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);



    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryTreeTableByPid(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询树
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/tree",method = RequestMethod.POST)
    ResultObjectVO queryTree(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);



    /**
     * 查询商城首页类别树
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/web/index/tree",method = RequestMethod.POST)
    ResultObjectVO queryWebIndexTree(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);




    /**
     * 刷新首页缓存
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/index/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO flushWebIndexCache(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader,@RequestBody RequestJsonVO requestVo);


    /**
     * 刷新全部缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/all/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    public ResultObjectVO flushAllCache(@RequestBody RequestJsonVO requestVo);


    /**
     * 刷新预览树缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/wmini/tree",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO flushWMiniTreeCache(@RequestBody RequestJsonVO requestVo);


    /**
     * 导航分类树
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/navigation/mini/tree",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    public ResultObjectVO flushNavigationMiniTreeCache(@RequestBody RequestJsonVO requestVo);

    /**
     * 清空首页缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/clear/index/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO clearWebIndexCache(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader,@RequestBody RequestJsonVO requestVo);



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
    @RequestMapping(value="/query/child/list/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    public ResultObjectVO queryChildListByPid(@RequestBody RequestJsonVO requestJsonVO);

}
