package com.toucan.shopping.cloud.apps.admin.controller.user;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.message.api.feign.service.FeignMessageUserService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserTrueNameApproveService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import com.toucan.shopping.modules.common.persistence.event.enums.EventPublishTypeEnum;
import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.message.constant.MessageContentTypeConstant;
import com.toucan.shopping.modules.message.vo.MessageVO;
import com.toucan.shopping.modules.user.page.UserTrueNameApprovePageInfo;
import com.toucan.shopping.modules.user.vo.UserTrueNameApproveVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户收藏商品管理
 */
@Controller
@RequestMapping("/user/collect/product")
public class UserCollectProductController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;


}

