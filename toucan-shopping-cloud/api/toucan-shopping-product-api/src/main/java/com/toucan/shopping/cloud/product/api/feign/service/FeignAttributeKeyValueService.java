package com.toucan.shopping.cloud.product.api.feign.service;

import com.toucan.shopping.cloud.product.api.feign.fallback.FeignAttributeKeyServiceFallbackFactory;
import com.toucan.shopping.cloud.product.api.feign.fallback.FeignAttributeKeyValueServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-product-proxy/attributeKeyValue",fallbackFactory = FeignAttributeKeyValueServiceFallbackFactory.class)
public interface FeignAttributeKeyValueService {


    /**
     * 根据分类ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/category/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findByCategoryId(@RequestBody RequestJsonVO requestVo);


    /**
     * 根据分类ID查询属性树
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/attribute/tree/page",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryAttributeTreePage(@RequestBody RequestJsonVO requestVo);




    /**
     * 根据SPUID和SKU中的分类ID以及属性 查询可被搜索的属性列表
     * 注:同一级分类下属性名称不允许重复
     * 例如：手机分类下有颜色属性，游戏手机分类下也允许有颜色属性
     * 结构:
     *      手机 颜色属性
     *      手机》游戏手机 颜色属性
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/search/attribute/list/spuId/categoryId/attributeName/AttributeValue",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO querySearchAttributeList(@RequestBody RequestJsonVO requestVo);

}
