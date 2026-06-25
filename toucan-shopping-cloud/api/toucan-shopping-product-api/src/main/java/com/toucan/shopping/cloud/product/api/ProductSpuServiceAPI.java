package com.toucan.shopping.cloud.product.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface ProductSpuServiceAPI {

    /**
     * 保存
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO save(RequestJsonVO requestJsonVO);

    /**
     * 更新
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO update(RequestJsonVO requestJsonVO);

    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteById(RequestJsonVO requestVo);

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds(RequestJsonVO requestVo);

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findById(RequestJsonVO requestVo);

}
