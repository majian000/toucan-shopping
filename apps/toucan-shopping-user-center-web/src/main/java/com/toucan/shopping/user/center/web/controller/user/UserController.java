package com.toucan.shopping.user.center.web.controller.user;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.auth.admin.Auth;
import com.toucan.shopping.center.app.export.entity.AppPageInfo;
import com.toucan.shopping.center.user.api.feign.service.FeignUserService;
import com.toucan.shopping.center.user.export.page.UserPageInfo;
import com.toucan.shopping.center.user.export.vo.UserElasticSearchVO;
import com.toucan.shopping.center.user.service.UserElasticSearchService;
import com.toucan.shopping.center.user.vo.SearchAfterPage;
import com.toucan.shopping.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.common.properties.Toucan;
import com.toucan.shopping.common.util.AuthHeaderUtil;
import com.toucan.shopping.common.util.SignUtil;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.user.center.web.vo.TableVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private UserElasticSearchService userElasticSearchService;


    @Auth(verifyMethod = Auth.VERIFYMETHOD_USER_CENTER,requestType = Auth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String page()
    {
        return "pages/user/list.html";
    }




    /**
     * 查询列表
     * @param request
     * @param userPageInfo
     * @return
     */
    @Auth
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO listPage(HttpServletRequest request, UserPageInfo userPageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            String authHeader = request.getHeader(toucan.getUserCenter().getHttpBbsAuthHeader());
            //设置创建人
//            appPageInfo.setAdminId(AuthHeaderUtil.getAdminId(authHeader));


            RequestJsonVO requestVo = RequestJsonVOGenerator.generatorByAdmin(toucan.getAppCode(),"",userPageInfo);
            UserElasticSearchVO userElasticSearchVO = new UserElasticSearchVO();
            SearchAfterPage searchAfterPage = userElasticSearchService.queryListForSearchAfter(userElasticSearchVO,userPageInfo.getLimit(),userPageInfo.getSortValues());
//            ResultObjectVO resultObjectVO = feignUserService.list(SignUtil.sign(requestVo),requestVo);
//            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
//            {
//                if(resultObjectVO.getData()!=null)
//                {
//                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
//                    tableVO.setCount(Integer.parseInt(String.valueOf(resultObjectDataMap.get("total"))));
//                    if(tableVO.getCount()>0) {
//                        tableVO.setData((List<Object>) resultObjectDataMap.get("list"));
//                    }
//                }
//            }
        }catch(Exception e)
        {
            tableVO.setMsg("请求失败,请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }



}

