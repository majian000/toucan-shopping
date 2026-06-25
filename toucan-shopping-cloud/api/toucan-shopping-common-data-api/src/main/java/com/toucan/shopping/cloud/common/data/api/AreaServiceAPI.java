package com.toucan.shopping.cloud.common.data.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AreaServiceAPI {

    ResultObjectVO save(RequestJsonVO requestJsonVO);

    ResultObjectVO queryAll(RequestJsonVO requestJsonVO);

    /**
     * 根据编码查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findByCodes(RequestJsonVO requestVo);

    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryAreaTreeTable(RequestJsonVO requestJsonVO);

    ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findById(RequestJsonVO requestVo);

    /**
     * 查询地区树
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryTree(RequestJsonVO requestJsonVO);

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
     * 编辑
     * @param requestVo
     * @return
     */
    ResultObjectVO update(RequestJsonVO requestVo);

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
    ResultObjectVO queryListByParentCode(RequestJsonVO requestJsonVO);

    /**
     * 刷新全部缓存
     * @param requestVo
     * @return
     */
    ResultObjectVO flushAllCache(RequestJsonVO requestVo);

    /**
     * 查询全量缓存
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryFullCache(RequestJsonVO requestJsonVO);

    /**
     * 查询指定节点下子节点
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryTreeChildByPid(RequestJsonVO requestJsonVO);

    /**
     * 根据所有市级名称查询出所有市级对象
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryCityListByNames(RequestJsonVO requestJsonVO);

}
