package com.toucan.shopping.cloud.message.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface MessageTypeServiceAPI {


    ResultObjectVO save( RequestJsonVO requestJsonVO);


    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO deleteById( RequestJsonVO requestJsonVO);


    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds( RequestJsonVO requestVo);


    /**
     * 编辑
     * @param requestVo
     * @return
     */
    ResultObjectVO update( RequestJsonVO requestVo);

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListPage( RequestJsonVO requestJsonVO);



    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO findById( RequestJsonVO requestJsonVO);


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryList( RequestJsonVO requestJsonVO);


    /**
     * 根据code查询(从缓存拿数据)
     * @param requestVo
     * @return
     */
    ResultObjectVO findCacheByCode( RequestJsonVO requestVo);




    ResultObjectVO flushCache( RequestJsonVO requestJsonVO);

}
