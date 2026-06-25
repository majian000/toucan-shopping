package com.toucan.shopping.cloud.seller.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 店铺分类
 * @author majian
 */
public interface ShopCategoryServiceAPI {

    /**
     * 保存
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO save(RequestJsonVO requestJsonVO);

    /**
     * 保存分类(后台管理端)
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO saveForAdmin(RequestJsonVO requestJsonVO);

    /**
     * 更新
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO update(RequestJsonVO requestJsonVO);

    /**
     * 更新分类(后台管理端)
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updateForAdmin(RequestJsonVO requestJsonVO);

    /**
     * 查询全部类别
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryAllList(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryById(RequestJsonVO requestJsonVO);

    /**
     * 根据ID数组查询
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryByIdList(RequestJsonVO requestJsonVO);

    /**
     * 批量刷新缓存
     * @param requestVo
     * @return
     */
    ResultObjectVO flushCache(RequestJsonVO requestVo);

    /**
     * 清空缓存
     * @param requestVo
     * @return
     */
    ResultObjectVO clearCache(RequestJsonVO requestVo);

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findById(RequestJsonVO requestVo);

    /**
     * 置顶
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO moveTop(RequestJsonVO requestJsonVO);

    /**
     * 置顶(后台管理端)
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO moveTopForAdmin(RequestJsonVO requestJsonVO);

    /**
     * 置底
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO moveBottom(RequestJsonVO requestJsonVO);

    /**
     * 置底(后台管理端)
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO moveBottomForAdmin(RequestJsonVO requestJsonVO);

    /**
     * 向上
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO moveUp(RequestJsonVO requestJsonVO);

    /**
     * 向上(后台管理端)
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO moveUpForAdmin(RequestJsonVO requestJsonVO);

    /**
     * 向下
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO moveDown(RequestJsonVO requestJsonVO);

    /**
     * 向下(后台管理端)
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO moveDownForAdmin(RequestJsonVO requestJsonVO);

    /**
     * 根据ID数组查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findByIdArray(RequestJsonVO requestVo);

    /**
     * 查询树
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryTree(RequestJsonVO requestJsonVO);

    /**
     * 查询PC端首页类别树
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryWebIndexTree(RequestJsonVO requestJsonVO);

    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryTreeTable(RequestJsonVO requestJsonVO);

    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListByPid(RequestJsonVO requestJsonVO);

    /**
     * 查询指定节点树表格
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO);

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO deleteById(RequestJsonVO requestJsonVO);

    /**
     * 根据ID删除(后台管理)
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO deleteByIdForAdmin(RequestJsonVO requestJsonVO);

    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds(RequestJsonVO requestVo);

    /**
     * 根据ID查询返回分类ID路径
     * @param requestVo
     * @return
     */
    ResultObjectVO findIdPathById(RequestJsonVO requestVo);

    /**
     * 根据店铺ID查询所有分类
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListByShopId(RequestJsonVO requestJsonVO);

}
