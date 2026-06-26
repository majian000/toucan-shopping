package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface OrgnazitionServiceAPI {

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
     * 查询树表格

     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryAppOrgnazitionTreeTable(RequestJsonVO requestJsonVO);


    /**
     * 根据ID删除指定角色

     * @param requestVo
     * @return
     */
    ResultObjectVO deleteById(RequestJsonVO requestVo);




    /**
     * 根据ID查询

     * @param requestVo
     * @return
     */
    ResultObjectVO findById(RequestJsonVO requestVo);


    /**
     * 批量删除

     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds(RequestJsonVO requestVo);


    /**
     * 查询组织机构树

     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryOrgnazationTree(RequestJsonVO requestJsonVO);





    ResultObjectVO queryAdminOrgnazitionTree(RequestJsonVO requestJsonVO);


}
