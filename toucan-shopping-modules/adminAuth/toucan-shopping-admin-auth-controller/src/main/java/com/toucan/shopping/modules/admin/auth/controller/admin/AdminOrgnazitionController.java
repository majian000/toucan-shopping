package com.toucan.shopping.modules.admin.auth.controller.admin;


import com.alibaba.fastjson.JSONObject;
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
import org.apache.commons.collections4.CollectionUtils;
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



    /**
     * 保存组织机构关联
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/save/orgnazition",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO saveOrgnazitions(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AdminOrgnazitionVO entity = JSONObject.parseObject(requestJsonVO.getEntityJson(), AdminOrgnazitionVO.class);
            if(StringUtils.isEmpty(entity.getAdminId()))
            {
                throw new IllegalArgumentException("adminId为空");
            }
            if(CollectionUtils.isEmpty(entity.getAdminOrgnazitions()))
            {
                String[] appCodes = {entity.getSelectAppCode()};
                //清空该应用下旧的关联
                adminOrgnazitionService.deleteByAdminIdAndAppCodes(entity.getAdminId(), appCodes);
            }else {

                AdminApp queryAdminApp = new AdminApp();
                queryAdminApp.setAdminId(entity.getCreateAdminId());

                List<AdminApp> adminApps = adminAppService.findListByEntity(queryAdminApp);

                if (CollectionUtils.isEmpty(adminApps)) {
                    throw new IllegalArgumentException("当前登录用户,关联应用列表为空");
                }

                String[] appCodes = {entity.getSelectAppCode()};
                //清空该应用下旧的关联
                adminOrgnazitionService.deleteByAdminIdAndAppCodes(entity.getAdminId(), appCodes);

                int length = 0;
                for (AdminOrgnazition adminOrgnazition : entity.getAdminOrgnazitions()) {
                    adminOrgnazition.setAdminId(entity.getAdminId());
                    adminOrgnazition.setCreateAdminId(entity.getCreateAdminId());
                    adminOrgnazition.setCreateDate(new Date());
                    adminOrgnazition.setDeleteStatus((short) 0);
                    length++;
                }
                AdminOrgnazition[] adminOrgnazitions = new AdminOrgnazition[length];
                int pos = 0;
                for (AdminOrgnazition adminOrgnazition : entity.getAdminOrgnazitions()) {
                    adminOrgnazitions[pos] = adminOrgnazition;
                    pos++;
                }
                adminOrgnazitionService.saves(adminOrgnazitions);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 根据实体查询对象
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryListByEntity",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListByEntity(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到参数");
            return resultObjectVO;
        }

        try {
            AdminOrgnazition queryAdminOrgnazition = JSONObject.parseObject(requestVo.getEntityJson(),AdminOrgnazition.class);
            List<AdminOrgnazition> adminOrgnazitions = adminOrgnazitionService.findListByEntity(queryAdminOrgnazition);
            resultObjectVO.setData(adminOrgnazitions);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


}
