package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface RoleFunctionServiceAPI {


    ResultObjectVO saveFunctions( String signHeader, RequestJsonVO requestJsonVO);

    ResultObjectVO queryRoleFunctionList( String signHeader, RequestJsonVO requestJsonVO);

    /**
     * 列表分页
     * @param signHeader
     * @param requestVo
     * @return
     */
    ResultObjectVO list( String signHeader, RequestJsonVO requestVo);


    /**
     * 根据角色ID和功能项父节点ID查询,并设置节点状态
     * @param requestVo
     * @return
     */
    ResultObjectVO queryFunctionTreeByRoleIdAndParentId( RequestJsonVO requestVo);



    /**
     * 刷新缓存
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO refreshCache( RequestJsonVO requestJsonVO);

}
