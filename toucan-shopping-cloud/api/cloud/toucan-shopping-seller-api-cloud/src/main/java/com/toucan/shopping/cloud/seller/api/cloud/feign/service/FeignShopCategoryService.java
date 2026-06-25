package com.toucan.shopping.cloud.seller.api.cloud.feign.service;

import com.toucan.shopping.cloud.seller.api.cloud.feign.fallback.FeignShopCategoryServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺分类
 * @author majian
 */
@FeignClient(value = "toucan-shopping-gateway", path = "/toucan-shopping-seller-proxy/shop/category", fallbackFactory = FeignShopCategoryServiceFallbackFactory.class)
public interface FeignShopCategoryService {

    /**
     * 保存
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/save")
    ResultObjectVO save(@RequestHeader(value = "toucan-sign-header", defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    /**
     * 管理员保存
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/admin/save")
    ResultObjectVO saveForAdmin(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 修改
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/update")
    ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 管理员修改
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/admin/update")
    ResultObjectVO updateForAdmin(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询所有列表
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/query/all/list")
    ResultObjectVO queryAllList(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/query/id")
    ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID列表查询
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/query/ids")
    ResultObjectVO queryByIdList(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO);

    /**
     * 刷新缓存
     * @param requestVo
     * @return
     */
    @PostMapping("/flush/cache")
    ResultObjectVO flushCache(@RequestBody RequestJsonVO requestVo);

    /**
     * 清除缓存
     * @param requestVo
     * @return
     */
    @PostMapping("/clear/cache")
    ResultObjectVO clearCache(@RequestBody RequestJsonVO requestVo);

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @PostMapping("/find/id")
    ResultObjectVO findById(@RequestBody RequestJsonVO requestVo);

    /**
     * 置顶
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/move/top")
    ResultObjectVO moveTop(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 管理员置顶
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/admin/move/top")
    ResultObjectVO moveTopForAdmin(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 置底
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/move/bottom")
    ResultObjectVO moveBottom(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 管理员置底
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/admin/move/bottom")
    ResultObjectVO moveBottomForAdmin(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 上移
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/move/up")
    ResultObjectVO moveUp(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 管理员上移
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/admin/move/up")
    ResultObjectVO moveUpForAdmin(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 下移
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/move/down")
    ResultObjectVO moveDown(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 管理员下移
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/admin/move/down")
    ResultObjectVO moveDownForAdmin(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID数组查询
     * @param requestVo
     * @return
     */
    @PostMapping("/find/idArray")
    ResultObjectVO findByIdArray(@RequestBody RequestJsonVO requestVo);

    /**
     * 查询树
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/query/tree")
    ResultObjectVO queryTree(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询Web首页树
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/query/web/index/tree")
    ResultObjectVO queryWebIndexTree(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询树形表格
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/query/tree/table")
    ResultObjectVO queryTreeTable(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据父ID查询列表
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/query/list/by/pid")
    ResultObjectVO queryListByPid(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据父ID查询树形表格
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/query/tree/table/by/pid")
    ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @DeleteMapping("/delete/id")
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 管理员根据ID删除
     * @param requestJsonVO
     * @return
     */
    @DeleteMapping("/admin/delete/id")
    ResultObjectVO deleteByIdForAdmin(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据ID列表删除
     * @param requestVo
     * @return
     */
    @DeleteMapping("/delete/ids")
    ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo);

    /**
     * 根据ID查询路径
     * @param requestVo
     * @return
     */
    @PostMapping("/find/path/by/id")
    ResultObjectVO findIdPathById(@RequestBody RequestJsonVO requestVo);

    /**
     * 根据店铺ID查询列表
     * @param requestJsonVO
     * @return
     */
    @PostMapping("/query/list/by/shopId")
    ResultObjectVO queryListByShopId(@RequestBody RequestJsonVO requestJsonVO);

}
