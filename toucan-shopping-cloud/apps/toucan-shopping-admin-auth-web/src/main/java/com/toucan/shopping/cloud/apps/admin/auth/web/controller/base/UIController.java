package com.toucan.shopping.cloud.apps.admin.auth.web.controller.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminAppService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
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
     * @param feignAdminAppService
     */
    public void initSelectApp(HttpServletRequest request, Toucan toucan, FeignAdminAppService feignAdminAppService)
    {
        try {
            AdminApp query = new AdminApp();
            query.setAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), query);
            ResultObjectVO resultObjectVO = feignAdminAppService.queryAppListByAdminId(SignUtil.sign(requestJsonVO), requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                List<AdminAppVO> adminAppVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), AdminAppVO.class);
                request.setAttribute("adminAppVOS",adminAppVOS);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("adminAppVOS",new ArrayList<AdminAppVO>());
        }
    }

    /**
     * 初始化界面按钮
     * @param request
     * @param toucan
     * @param functionId
     * @param feignFunctionService
     */
    public void initButtons(HttpServletRequest request, Toucan toucan,String functionId, FeignFunctionService feignFunctionService)
    {
        try {
            Function function = new Function();
            function.setPid(Long.parseLong(functionId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),function);
            ResultObjectVO resultObjectVO = feignFunctionService.queryChildren(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode().longValue()==ResultObjectVO.SUCCESS.longValue())
            {
                List<Function> functions = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Function.class);
                if(!CollectionUtils.isEmpty(functions))
                {
                    List<Function> toolbarButtons = new ArrayList<Function>();
                    List<Function> rowButtons = new ArrayList<Function>();

                    for(Function buttonFunction:functions)
                    {
                        if(buttonFunction.getType().shortValue()==2)
                        {
                            rowButtons.add(buttonFunction);
                        }else if(buttonFunction.getType().shortValue()==3)
                        {
                            toolbarButtons.add(buttonFunction);
                        }
                    }

                    request.setAttribute("toolbarButtons",toolbarButtons);
                    request.setAttribute("rowButtons",rowButtons);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("toolbarButtons",new ArrayList<Function>());
            request.setAttribute("rowButtons",new ArrayList<Function>());
        }
    }


}
