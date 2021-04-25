package com.toucan.shopping.cloud.apps.admin.auth.web.controller.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminAppService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAppService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public abstract class UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 初始化选择应用控件
     * @param request
     * @param toucan
     * @param feignAppService
     */
    public void initSelectApp(HttpServletRequest request, Toucan toucan, FeignAppService feignAppService)
    {
        try {
            App query = new App();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), query);
            ResultObjectVO resultObjectVO = feignAppService.list(SignUtil.sign(requestJsonVO), requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                List<AppVO> apps = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), AppVO.class);
                request.setAttribute("apps",apps);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("apps",new ArrayList<AdminAppVO>());
        }
    }

    /**
     * 初始化界面按钮
     * @param request
     * @param toucan
     * @param url
     * @param feignFunctionService
     */
    public void initButtons(HttpServletRequest request, Toucan toucan,String url, FeignFunctionService feignFunctionService)
    {
        try {
            FunctionVO function = new FunctionVO();
            function.setUrl(url);
            function.setAppCode(toucan.getAppCode());
            function.setAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),function);
            ResultObjectVO resultObjectVO = feignFunctionService.queryOneChildsByAdminIdAndAppCodeAndParentUrl(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<Function> functions = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Function.class);
                if(!CollectionUtils.isEmpty(functions))
                {
                    List<String> toolbarButtons = new ArrayList<String>();
                    List<String> rowButtons = new ArrayList<String>();

                    for(Function buttonFunction:functions)
                    {
                        if(buttonFunction.getType().shortValue()==2)
                        {
                            rowButtons.add(buttonFunction.getFunctionText());
                        }else if(buttonFunction.getType().shortValue()==3)
                        {
                            toolbarButtons.add(buttonFunction.getFunctionText());
                        }
                    }

                    request.setAttribute("toolbarButtons",toolbarButtons);
                    request.setAttribute("rowButtons",rowButtons);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("toolbarButtons",new ArrayList<String>());
            request.setAttribute("rowButtons",new ArrayList<String>());
        }
    }


}
