package com.toucan.shopping.cloud.product.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AttributeKeyServiceAPI {

    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    /**
     * 保存
     * @param requestVo
     * @return
     */
    ResultObjectVO save(RequestJsonVO requestVo);

    /**
     * 修改
     * @param requestVo
     * @return
     */
    ResultObjectVO update(RequestJsonVO requestVo);

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
    ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO);

    /**
     * 根据ID删除指定
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteById(RequestJsonVO requestVo);

    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds(RequestJsonVO requestVo);

    /**
     * 查询树
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryTreeByCategoryId(RequestJsonVO requestJsonVO);

    /**
     * 查询所有可搜索的属性键值对
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO querySearchList(RequestJsonVO requestJsonVO);

}
