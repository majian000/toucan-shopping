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
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO save( String signHeader,  RequestJsonVO requestVo);


    /**
     * 编辑
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO update( String signHeader,  RequestJsonVO requestVo);


    /**
     * 查询树表格
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryAppOrgnazitionTreeTable( String signHeader,  RequestJsonVO requestJsonVO);


    /**
     * 根据ID删除指定角色
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteById( String signHeader,  RequestJsonVO requestVo);




    /**
     * 根据ID查询
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO findById( String signHeader,  RequestJsonVO requestVo);


    /**
     * 批量删除
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds( String signHeader,  RequestJsonVO requestVo);


    /**
     * 查询组织机构树
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryOrgnazationTree( String signHeader, RequestJsonVO requestJsonVO);





    ResultObjectVO queryAdminOrgnazitionTree( String signHeader, RequestJsonVO requestJsonVO);


}
