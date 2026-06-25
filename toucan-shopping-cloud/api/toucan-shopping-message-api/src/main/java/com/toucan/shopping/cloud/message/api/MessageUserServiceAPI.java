package com.toucan.shopping.cloud.message.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface MessageUserServiceAPI {


    ResultObjectVO send( RequestJsonVO requestJsonVO);


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListPage( RequestJsonVO requestJsonVO);



    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListPageByUserMianId( RequestJsonVO requestJsonVO);



    /**
     * 编辑
     * @param requestVo
     * @return
     */
    ResultObjectVO update( RequestJsonVO requestVo);

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO findById( RequestJsonVO requestJsonVO);


    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO deleteById( RequestJsonVO requestJsonVO);



    /**
     * 查询未读数量
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryUnreadCountByUserMainId( RequestJsonVO requestJsonVO);


    /**
     * 更新为已读
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updateReadStatus( RequestJsonVO requestJsonVO);


    /**
     * 更新全部为已读
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updateAllReadStatus( RequestJsonVO requestJsonVO);
}
