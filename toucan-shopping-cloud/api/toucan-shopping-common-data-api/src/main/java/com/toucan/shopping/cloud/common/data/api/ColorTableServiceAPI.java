package com.toucan.shopping.cloud.common.data.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface ColorTableServiceAPI {

    ResultObjectVO queryListPage(RequestJsonVO requestJsonVO);

    ResultObjectVO queryList(RequestJsonVO requestJsonVO);

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListByNames(RequestJsonVO requestJsonVO);

    /**
     * 保存
     * @param requestVo
     * @return
     */
    ResultObjectVO save(RequestJsonVO requestVo);

    /**
     * 编辑
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
