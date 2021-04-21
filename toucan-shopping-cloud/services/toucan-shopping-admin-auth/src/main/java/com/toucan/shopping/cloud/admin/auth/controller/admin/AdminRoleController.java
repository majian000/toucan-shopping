package com.toucan.shopping.cloud.admin.auth.controller.admin;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.AdminRole;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AdminRoleService;
import com.toucan.shopping.modules.admin.auth.service.AdminService;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminResultVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 管理员应用 增删改查
 */
@RestController
@RequestMapping("/adminRole")
public class AdminRoleController {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private AdminRoleService adminRoleService;



//    /**
//     * 保存管理员账户
//     * @param requestVo
//     * @return
//     */
//    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public ResultObjectVO save(@RequestBody RequestJsonVO requestVo){
//        ResultObjectVO resultObjectVO = new ResultObjectVO();
//        if(requestVo.getEntityJson()==null)
//        {
//            resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USER);
//            resultObjectVO.setMsg("请求失败,没有找到参数");
//            return resultObjectVO;
//        }
//
//        try {
//            AdminApp adminApp = JSONObject.parseObject(requestVo.getEntityJson(),AdminApp.class);
//            if(StringUtils.isEmpty(adminApp.getAppCode()))
//            {
//                resultObjectVO.setCode(AdminResultVO.FAILD);
//                resultObjectVO.setMsg("请求失败,请传入应用编码");
//                return resultObjectVO;
//            }
//            if(StringUtils.isEmpty(adminApp.getAdminId()))
//            {
//                resultObjectVO.setCode(AdminResultVO.FAILD);
//                resultObjectVO.setMsg("请求失败,请传入账号ID");
//                return resultObjectVO;
//            }
//
//            adminApp.setCreateDate(new Date());
//            adminApp.setDeleteStatus((short)0);
//            int row = adminRoleService.save(adminApp);
//            if (row < 1) {
//                resultObjectVO.setCode(ResultVO.FAILD);
//                resultObjectVO.setMsg("添加失败,创建当前账号与该应用关联失败!");
//                return resultObjectVO;
//            }
//
//        }catch(Exception e)
//        {
//            logger.warn(e.getMessage(),e);
//
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("请求失败,请稍后重试");
//        }
//        return resultObjectVO;
//    }





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
            resultObjectVO.setMsg("请求失败,没有找到参数");
            return resultObjectVO;
        }

        try {
            AdminRole queryAdminRole = JSONObject.parseObject(requestVo.getEntityJson(),AdminRole.class);
            List<AdminRole> adminRoles = adminRoleService.findListByEntity(queryAdminRole);
            resultObjectVO.setData(adminRoles);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


}
