package com.toucan.shopping.cloud.user.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 用户头像审核服务
 */
public interface UserHeadSculptureApproveServiceAPI {


    /**
     * 保存用户实名
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO save(RequestJsonVO requestJsonVO);


    /**
     * 修改用户实名
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO update(RequestJsonVO requestJsonVO);

    /**
     * 根据用户主ID查询
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryByUserMainId(RequestJsonVO requestJsonVO);


    /**
     * 根据用户主ID查询激活的的审批
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryAliveByUserMainId(RequestJsonVO requestJsonVO);


    /**
     * 根据用户主ID查询,并且根据创建时间倒序
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryListByUserMainIdAndOrderByUpdateDateDesc(RequestJsonVO requestJsonVO);

    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    ResultObjectVO queryListPage(RequestJsonVO requestVo);


    /**
     * 通过指定审核
     * @param requestVo
     * @return
     */
    ResultObjectVO passById(RequestJsonVO requestVo);



    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds(RequestJsonVO requestVo);


    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryById(RequestJsonVO requestJsonVO);


    /**
     * 驳回指定
     * @param requestVo
     * @return
     */
    ResultObjectVO rejectById(RequestJsonVO requestVo);
}
