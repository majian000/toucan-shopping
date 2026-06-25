package com.toucan.shopping.cloud.product.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface BrandServiceAPI {

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
     * 根据ID集合查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findByIdList(RequestJsonVO requestVo);

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
     * 根据名称以及分类ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findListByNameAndCategoryIdAndEnabled(RequestJsonVO requestVo);

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListByCategoryId(RequestJsonVO requestJsonVO);

}
