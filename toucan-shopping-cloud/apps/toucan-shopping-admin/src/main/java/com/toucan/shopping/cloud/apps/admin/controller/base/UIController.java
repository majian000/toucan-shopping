package com.toucan.shopping.cloud.apps.admin.controller.base;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AppServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
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

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class UIController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 初始化选择应用控件
     * @param request
     * @param toucan
     * @param appServiceAPI
     */
    public void initSelectApp(HttpServletRequest request, Toucan toucan, AppServiceAPI appServiceAPI)
    {
        try {
            App query = new App();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), query);
            ResultObjectVO resultObjectVO = appServiceAPI.list(requestJsonVO);
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
     * @param functionServiceAPI
     */
    public void initButtons(HttpServletRequest request, Toucan toucan,String url, FunctionServiceAPI functionServiceAPI)
    {
        try {
            FunctionVO function = new FunctionVO();
            function.setUrl(url);
            function.setAppCode(toucan.getAppCode());
            function.setAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),function);
            ResultObjectVO resultObjectVO = functionServiceAPI.queryOneChildsByAdminIdAndAppCodeAndParentUrl(requestJsonVO);
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



    /**
     * 初始化更多按钮
     * @param request
     */
    public void initRowMoreButtons(HttpServletRequest request,String moreButtonText)
    {
        try{
            List<String> rowButtons = (List<String>)request.getAttribute("rowButtons");
            //行按钮只保留3个
            if(rowButtons!=null&&rowButtons.size()>3){
                List<String> newRowButtons = new LinkedList<>();
                List<String> moreRowButtons = new LinkedList<>();
                for(int i=0;i<rowButtons.size();i++){
                    if(i<3){
                        if(i!=2){
                            newRowButtons.add(rowButtons.get(i));
                        }else { //最后一个按钮显示更多按钮
                            newRowButtons.add(moreButtonText);
                            //最后一个按钮被放到更多下拉菜单里
                            moreRowButtons.add(rowButtons.get(i));
                        }
                    }else { //后面的按钮都放到更多下拉菜单里
                        moreRowButtons.add(rowButtons.get(i));
                    }
                }
                request.setAttribute("rowButtons",newRowButtons);
                request.setAttribute("moreRowButtons",moreRowButtons);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            request.setAttribute("rowButtons",new ArrayList<String>());
            request.setAttribute("moreRowButtons",new ArrayList<String>());
        }
    }

}
