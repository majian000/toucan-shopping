package com.toucan.shopping.cloud.apps.user.web.controller.user;


import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.lock.redis.RedisLock;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.PhoneUtils;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.util.UserRegistUtil;
import com.toucan.shopping.modules.common.util.UsernameUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.es.service.UserElasticSearchService;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.vo.UserRegistVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/mobile/phone")
public class UserMobilePhoneController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private UserElasticSearchService userElasticSearchService;

    @Autowired
    private FeignFunctionService feignFunctionService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {

        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/user/mobile/phone/listPage",feignFunctionService);
        return "pages/user/mobilephone/list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/registByMobilePage",method = RequestMethod.GET)
    public String addByMobilePage()
    {
        return "pages/user/db/registbymobile.html";
    }






    /**
     * 查询列表
     * @param userPageInfo
     * @return
     */
    @AdminAuth
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, UserPageInfo userPageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),userPageInfo);
            ResultObjectVO resultObjectVO = feignUserService.list(SignUtil.sign(requestVo),requestVo);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total"))));
                    if(tableVO.getCount()>0) {
                        tableVO.setData((List<Object>) resultObjectDataMap.get("list"));
                    }
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("请求失败,请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }






}

