package com.toucan.shopping.cloud.apps.admin.controller.adminInfo;

import com.toucan.shopping.cloud.admin.auth.api.AdminInfoServiceAPI;
import com.toucan.shopping.modules.admin.auth.entity.AdminInfo;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 管理员信息
 */
@RestController
@RequestMapping("/adminInfo")
public class AdminInfoController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private AdminInfoServiceAPI adminInfoServiceAPI;

    /**
     * 保存/更新（完善他人信息）
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:system:admin:adminInfo:saveOrUpdate:api"})
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public ResultObjectVO saveOrUpdate(HttpServletRequest request, @RequestBody AdminInfo entity) {
        ResultObjectVO resultObjectVO;
        try {
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());
            if (entity.getCreateAdminId() == null) {
                entity.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = adminInfoServiceAPI.saveOrUpdate(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO = new ResultObjectVO();
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    /**
     * 保存/更新我的信息（强制使用当前登录用户adminId）
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:system:myinfo"})
    @RequestMapping(value = "/saveOrUpdateMyInfo", method = RequestMethod.POST)
    public ResultObjectVO saveOrUpdateMyInfo(HttpServletRequest request, @RequestBody AdminInfo entity) {
        ResultObjectVO resultObjectVO;
        try {
            String currentAdminId = AdminLoginHolder.getCurrentAdminId();
            entity.setAdminId(currentAdminId);
            entity.setUpdateAdminId(currentAdminId);
            entity.setUpdateDate(new Date());
            if (entity.getCreateAdminId() == null) {
                entity.setCreateAdminId(currentAdminId);
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = adminInfoServiceAPI.saveOrUpdate(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO = new ResultObjectVO();
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }
}
