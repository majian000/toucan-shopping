package com.toucan.shopping.cloud.seller.api.feign.service;

import com.toucan.shopping.cloud.seller.api.feign.fallback.FeignSellerShopServiceFallbackFactory;
import com.toucan.shopping.cloud.seller.api.feign.fallback.FeignShopCategoryServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-seller-proxy/shop/category",fallbackFactory = FeignShopCategoryServiceFallbackFactory.class)
public interface FeignShopCategoryService {

    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestHeader(value = "toucan-sign-header",defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO);


    /**
     * 保存分类(后台管理端)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/save",produces = "application/json;charset=UTF-8")
    ResultObjectVO saveForAdmin(@RequestBody RequestJsonVO requestJsonVO);

    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 更新分类(后台管理端)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/update",produces = "application/json;charset=UTF-8")
    ResultObjectVO updateForAdmin(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询全部类别
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/all/list",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    public ResultObjectVO queryAllList(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/ids",produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByIdList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 批量刷新缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO flushCache(@RequestBody RequestJsonVO requestVo);



    /**
     * 清空缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/clear/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO clearCache(@RequestBody RequestJsonVO requestVo);


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findById(@RequestBody RequestJsonVO requestVo);



    /**
     * 置顶
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/move/top",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO moveTop(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 置顶
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/move/top",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO moveTopForAdmin(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 置底
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/move/bottom",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO moveBottom(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 置底
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/move/bottom",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO moveBottomForAdmin(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 向上
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/move/up",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO moveUp(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 向上(后台管理端)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/move/up",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    public ResultObjectVO moveUpForAdmin(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 向下
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/move/down",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO moveDown(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 向下(后台管理端)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/move/down",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO moveDownForAdmin(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID数组查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/idArray",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findByIdArray(@RequestBody RequestJsonVO requestVo);

    /**
     * 查询树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/tree",method = RequestMethod.POST)
    ResultObjectVO queryTree(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询PC端首页类别树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/web/index/tree",method = RequestMethod.POST)
    ResultObjectVO queryWebIndexTree(@RequestBody RequestJsonVO requestJsonVO);
    
    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryTreeTable(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryListByPid(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据ID删除(后台管理)
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    public ResultObjectVO deleteByIdForAdmin(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 批量删除功能项
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo);


    /**
     * 根据ID查询返回分类ID路径
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/path/by/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO findIdPathById(@RequestBody RequestJsonVO requestVo);



    /**
     * 根据店铺ID查询所有分类
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/shopId",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    ResultObjectVO queryListByShopId(@RequestBody RequestJsonVO requestJsonVO);
    
}
