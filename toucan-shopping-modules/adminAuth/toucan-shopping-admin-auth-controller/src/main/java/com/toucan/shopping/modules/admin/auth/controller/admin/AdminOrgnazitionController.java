package com.toucan.shopping.modules.admin.auth.controller.admin;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.business.service.AdminOrgnazitionBusinessService;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.AdminOrgnazition;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AdminOrgnazitionService;
import com.toucan.shopping.modules.admin.auth.service.AdminRoleService;
import com.toucan.shopping.modules.admin.auth.vo.AdminOrgnazitionVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminResultVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 账号组织机构 增删改查
 */
@RestController
@RequestMapping("/adminOrgnazition")
public class AdminOrgnazitionController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AdminAppService adminAppService;

    @Autowired
    private AdminOrgnazitionService adminOrgnazitionService;

    @Autowired
    private AdminOrgnazitionBusinessService adminOrgnazitionBusinessService;


    /**
     * 保存组织机构关联
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/save/orgnazition",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO saveOrgnazitions(@RequestBody RequestJsonVO requestJsonVO)
    {
        return adminOrgnazitionBusinessService.saveOrgnazitions(requestJsonVO);
    }





    /**
     * 根据实体查询对象
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryListByEntity",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListByEntity(@RequestBody RequestJsonVO requestVo){
        return adminOrgnazitionBusinessService.queryListByEntity(requestVo);
    }


}
