package com.toucan.shopping.cloud.user.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 收货地址服务
 */
public interface ConsigneeAddressServiceAPI {



    ResultObjectVO save(RequestJsonVO requestJsonVO);



    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    ResultObjectVO queryListPage(RequestJsonVO requestVo);



    ResultObjectVO update(RequestJsonVO requestJsonVO);



    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIdAndUserMainIdAndAppCode(RequestJsonVO requestVo);



    /**
     * 设置默认
     * @param requestVo
     * @return
     */
    ResultObjectVO setDefaultByIdAndUserMainId(RequestJsonVO requestVo);



    /**
     * 查询单条数据
     * @param requestVo
     * @return
     */
    ResultObjectVO findByIdAndUserMainIdAndAppcode(RequestJsonVO requestVo);



    /**
     * 查询设置为默认的收货信息,如果没有默认就查询最新一条
     * @param requestVo
     * @return
     */
    ResultObjectVO findDefaultByUserMainIdAndAppcode(RequestJsonVO requestVo);

}
