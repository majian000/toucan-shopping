package com.toucan.shopping.cloud.product.api.cloud.feign.service;

import com.toucan.shopping.cloud.product.api.AttributeKeyServiceAPI;
import com.toucan.shopping.cloud.product.api.cloud.feign.fallback.FeignAttributeKeyServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/attributeKey",fallbackFactory = FeignAttributeKeyServiceFallbackFactory.class)
public interface FeignAttributeKeyService extends AttributeKeyServiceAPI {


    @Override
    @RequestMapping(value="/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 保存
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/save",method = RequestMethod.POST)
    ResultObjectVO save(@RequestBody RequestJsonVO requestVo);


    /**
     * 修改
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/update",method = RequestMethod.POST)
    ResultObjectVO update(@RequestBody RequestJsonVO requestVo);

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO findById(@RequestBody RequestJsonVO requestVo);


    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/query/tree/table/by/pid", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据ID删除指定
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/delete/id",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo);


    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/delete/ids",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo);


    /**
     * 查询树
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value = "/query/tree/category/id",method = RequestMethod.POST)
    ResultObjectVO queryTreeByCategoryId(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询所有可搜索的属性键值对
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/query/search/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO querySearchList(@RequestBody RequestJsonVO requestJsonVO);

}
