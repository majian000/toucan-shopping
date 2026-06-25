package com.toucan.shopping.cloud.product.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AttributeValueServiceAPI {

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

}
