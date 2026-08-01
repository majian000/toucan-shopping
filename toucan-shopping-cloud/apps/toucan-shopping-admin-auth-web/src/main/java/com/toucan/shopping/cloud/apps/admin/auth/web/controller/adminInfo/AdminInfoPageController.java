package com.toucan.shopping.cloud.apps.admin.auth.web.controller.adminInfo;

import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.admin.auth.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import jakarta.servlet.http.HttpServletRequest;
import com.toucan.shopping.modules.admin.auth.entity.*;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminInfoPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private AppServiceAPI appServiceAPI;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private AdminInfoServiceAPI adminInfoServiceAPI;



    /**
         * 完善信息页面（管理员列表-完善他人信息）
         */
        @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
        @RequestMapping(value = "/adminInfo/editPage/{adminId}", method = RequestMethod.GET)
        public String editPage(HttpServletRequest request, @PathVariable String adminId) {
            try {
                AdminInfo query = new AdminInfo();
                query.setAdminId(adminId);
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
                ResultObjectVO resultObjectVO = adminInfoServiceAPI.findByAdminId(requestJsonVO);
                if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                    AdminInfo adminInfo = JSONObject.parseObject(JSONObject.toJSONString(resultObjectVO.getData()), AdminInfo.class);
                    request.setAttribute("model", adminInfo);
                } else {
                    request.setAttribute("model", new AdminInfo());
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                request.setAttribute("model", new AdminInfo());
            }
            request.setAttribute("adminId", adminId);
            return "pages/adminInfo/edit.html";
        }

    /**
         * 完善我的信息页面（右上角-完善本人信息）
         */
        @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
        @RequestMapping(value = "/adminInfo/myInfoPage", method = RequestMethod.GET)
        public String myInfoPage(HttpServletRequest request) {
            String adminId = AdminLoginHolder.getCurrentAdminId();
            try {
                AdminInfo query = new AdminInfo();
                query.setAdminId(adminId);
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
                ResultObjectVO resultObjectVO = adminInfoServiceAPI.findByAdminId(requestJsonVO);
                if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                    AdminInfo adminInfo = JSONObject.parseObject(JSONObject.toJSONString(resultObjectVO.getData()), AdminInfo.class);
                    request.setAttribute("model", adminInfo);
                } else {
                    request.setAttribute("model", new AdminInfo());
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                request.setAttribute("model", new AdminInfo());
            }
            return "pages/adminInfo/myInfo.html";
        }

}
