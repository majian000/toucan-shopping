package com.toucan.shopping.cloud.seller.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 运费模板
 * @author majian
 * @date 2022-9-21 14:14:06
 */
public interface FreightTemplateServiceAPI {

    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    ResultObjectVO queryListPage(RequestJsonVO requestVo);

    /**
     * 保存
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO save(RequestJsonVO requestJsonVO);

    /**
     * 根据ID集合查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findByIdList(RequestJsonVO requestVo);

    /**
     * 修改
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO update(RequestJsonVO requestJsonVO);

    /**
     * 根据ID和用户ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findByIdAndUserMainId(RequestJsonVO requestVo);

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO deleteById(String signHeader,RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findById(RequestJsonVO requestVo);

}
