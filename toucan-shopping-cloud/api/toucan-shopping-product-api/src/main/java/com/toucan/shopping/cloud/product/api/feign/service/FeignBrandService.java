package com.toucan.shopping.cloud.product.api.feign.service;

import com.toucan.shopping.cloud.product.api.feign.fallback.FeignAttributeKeyServiceFallbackFactory;
import com.toucan.shopping.cloud.product.api.feign.fallback.FeignBrandServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/brand",fallbackFactory = FeignBrandServiceFallbackFactory.class)
public interface FeignBrandService {


    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO);


    /**
     * 保存
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",method = RequestMethod.POST)
    ResultObjectVO save(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);


    /**
     * 修改
     * @param signHeader
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",method = RequestMethod.POST)
    ResultObjectVO update(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestVo);




    /**
     * 根据ID集合查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/idList",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findByIdList(@RequestBody RequestJsonVO requestVo);


    /**
     * 根据ID删除指定
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





    /**
     * 根据名称以及分类ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/name/categoryId/enabled",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findListByNameAndCategoryIdAndEnabled(@RequestBody RequestJsonVO requestVo);




    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/categoryId",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListByCategoryId(@RequestBody RequestJsonVO requestJsonVO);
}
