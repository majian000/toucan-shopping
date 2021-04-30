package com.toucan.shopping.modules.admin.auth.service;




import com.toucan.shopping.modules.admin.auth.entity.OrgnazitionApp;
import com.toucan.shopping.modules.admin.auth.vo.OrgnazitionAppVO;

import java.util.List;

public interface OrgnazitionAppService {

    /**
     * 根据实体查询列表
     * @param entity
     * @return
     */
    List<OrgnazitionApp> findListByEntity(OrgnazitionApp entity);

    /**
     * 保存关联
     * @param entity
     * @return
     */
    int save(OrgnazitionApp entity);

    /**
     * 删除指定应用编码下所有关联
     * @param appCode
     * @return
     */
    int deleteByAppCode(String appCode);


    /**
     * 删除指定应用编码下所有关联
     * @param orgnazitionId
     * @return
     */
    int deleteByOrgnazitionId(String orgnazitionId);


}
