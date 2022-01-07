package com.toucan.shopping.cloud.apps.admin.auth.web.controller.base;

import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

public abstract class BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignAdminService feignAdminService;

    @Autowired
    private Toucan toucan;

    public List<AdminVO> queryAdminListByAdminId(List<String> adminIdList) throws Exception
    {
        if(!CollectionUtils.isEmpty(adminIdList)) {
            String[] createOrUpdateAdminIds = new String[adminIdList.size()];
            adminIdList.toArray(createOrUpdateAdminIds);
            AdminVO queryAdminVO = new AdminVO();
            queryAdminVO.setAdminIds(createOrUpdateAdminIds);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAdminVO);
            ResultObjectVO resultObjectVO = feignAdminService.queryListByEntity(requestJsonVO.sign(), requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<AdminVO> adminVOS = (List<AdminVO>) resultObjectVO.formatDataList(AdminVO.class);
                if (!CollectionUtils.isEmpty(adminVOS)) {
                    return adminVOS;
                }
            }
        }
        return null;
    }

}
