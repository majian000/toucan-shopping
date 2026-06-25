package com.toucan.shopping.cloud.common.data.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface CategoryServiceAPI {

    ResultObjectVO save(RequestJsonVO requestJsonVO);

    ResultObjectVO queryById(RequestJsonVO requestJsonVO);

    ResultObjectVO queryByIdList(RequestJsonVO requestJsonVO);

    ResultObjectVO findByIdArray(RequestJsonVO requestJsonVO);

    /**
     * 查询指定节点下子节点
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryTreeChildByPid(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询返回分类ID路径
     * @param requestVo
     * @return
     */
    ResultObjectVO findIdPathById(RequestJsonVO requestVo);

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findById(RequestJsonVO requestVo);

    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryTreeTable(RequestJsonVO requestJsonVO);

    /**
     * 编辑
     * @param requestVo
     * @return
     */
    ResultObjectVO update(RequestJsonVO requestVo);

    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO);

    /**
     * 查询树
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryTree(RequestJsonVO requestJsonVO);

    /**
     * 查询树
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryMiniTree(RequestJsonVO requestJsonVO);

    /**
     * 查询商城首页类别树
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryWebIndexTree(RequestJsonVO requestJsonVO);

    /**
     * 刷新首页缓存
     * @param requestVo
     * @return
     */
    ResultObjectVO flushWebIndexCache(RequestJsonVO requestVo);

    /**
     * 刷新全部缓存
     * @param requestVo
     * @return
     */
    ResultObjectVO flushAllCache(RequestJsonVO requestVo);

    /**
     * 刷新预览树缓存
     * @param requestVo
     * @return
     */
    ResultObjectVO flushWMiniTreeCache(RequestJsonVO requestVo);

    /**
     * 导航分类树
     * @param requestVo
     * @return
     */
    ResultObjectVO flushNavigationMiniTreeCache(RequestJsonVO requestVo);

    /**
     * 清空首页缓存
     * @param requestVo
     * @return
     */
    ResultObjectVO clearWebIndexCache(RequestJsonVO requestVo);

    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds(RequestJsonVO requestVo);

    /**
     * 根据ID删除
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteById(RequestJsonVO requestVo);

    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListByPid(RequestJsonVO requestJsonVO);

    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryChildListByPid(RequestJsonVO requestJsonVO);

    /**
     * 查询指定节点下一级子节点
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryNextOneLevelChildListByPid(RequestJsonVO requestJsonVO);

    /**
     * 查询全部
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryAllList(RequestJsonVO requestJsonVO);

}
