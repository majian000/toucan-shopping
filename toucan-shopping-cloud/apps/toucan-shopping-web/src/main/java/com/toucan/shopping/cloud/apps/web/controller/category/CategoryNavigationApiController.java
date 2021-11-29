package com.toucan.shopping.cloud.apps.web.controller.category;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.UserEditInfoRedisKey;
import com.toucan.shopping.cloud.apps.web.redis.UserLoginRedisKey;
import com.toucan.shopping.cloud.apps.web.redis.UserRegistRedisKey;
import com.toucan.shopping.cloud.apps.web.redis.VerifyCodeRedisKey;
import com.toucan.shopping.cloud.apps.web.service.IndexService;
import com.toucan.shopping.cloud.apps.web.util.VCodeUtil;
import com.toucan.shopping.cloud.user.api.feign.service.FeignSmsService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.sms.constant.SmsTypeConstant;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.UserMobilePhone;
import com.toucan.shopping.modules.user.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 分类导航条
 */
@RestController("categoryNavigationApiController")
@RequestMapping("/api/category/navigation")
public class CategoryNavigationApiController extends BaseController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IndexService indexService;


    /**
     * 查询分类导航条
     * @return
     */
    @RequestMapping("/list")
    public ResultObjectVO queryList()
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            //查询类别列表
            resultObjectVO.setData(indexService.queryCategorys());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





}
