package com.toucan.shopping.cloud.apps.admin.controller.sellerWebHtmlPage.freeShop;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.apps.admin.vo.htmlPage.HtmlGeneratorTab;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FreeShopGeneratorPageController extends UIController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;


    /**
     * 初始化界面按钮
     * @param request
     * @param url
     */
    public void initButtons(HttpServletRequest request,String key,String url)
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
                    List<String> rowControls = new ArrayList<String>();

                    for(Function buttonFunction:functions)
                    {
                        if(buttonFunction.getType().intValue()==5)
                        {
                            rowControls.add(buttonFunction.getFunctionText());
                        }
                    }

                    request.setAttribute(key,rowControls);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute(key,new ArrayList<Function>());
        }
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/sellerWebHtmlPage/freeShopGenerator/freeShopGeneratorPage",method = RequestMethod.GET)
    public String indexGeneratorPage(HttpServletRequest request)
    {
        List<HtmlGeneratorTab> releaseHtmlGeneratorTabList = new ArrayList<HtmlGeneratorTab>();
        List<HtmlGeneratorTab> previewHtmlGeneratorTabList = new ArrayList<HtmlGeneratorTab>();
        if(toucan.getShoppingSellerWebPC()!=null&& StringUtils.isNotEmpty(toucan.getShoppingSellerWebPC().getIpList()))
        {
            String ipList = toucan.getShoppingSellerWebPC().getIpList();
            if(ipList.indexOf(",")!=-1)
            {
                String[] ips = ipList.split(",");
                if(ips!=null&&ips.length>0)
                {
                    for(String ip:ips)
                    {
                        HtmlGeneratorTab releaseHtmlGeneratorTable = new HtmlGeneratorTab();
                        releaseHtmlGeneratorTable.setName(ip);
                        releaseHtmlGeneratorTable.setContent("<iframe src='http://"+ip+"/freeShop' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                        releaseHtmlGeneratorTabList.add(releaseHtmlGeneratorTable);

                        HtmlGeneratorTab previewHtmlGeneratorTable = new HtmlGeneratorTab();
                        previewHtmlGeneratorTable.setName(ip);
                        previewHtmlGeneratorTable.setContent("<iframe src='http://"+ip+"/htmls/preview/freeShop.html' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                        previewHtmlGeneratorTabList.add(previewHtmlGeneratorTable);
                    }
                }
            }else{

                HtmlGeneratorTab releaseHtmlGeneratorTable = new HtmlGeneratorTab();
                releaseHtmlGeneratorTable.setName(ipList);
                releaseHtmlGeneratorTable.setContent("<iframe src='http://"+ipList+"/freeShop' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                releaseHtmlGeneratorTabList.add(releaseHtmlGeneratorTable);

                HtmlGeneratorTab previewHtmlGeneratorTable = new HtmlGeneratorTab();
                previewHtmlGeneratorTable.setName(ipList);
                previewHtmlGeneratorTable.setContent("<iframe src='http://"+ipList+"/htmls/preview/freeShop.html' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                previewHtmlGeneratorTabList.add(previewHtmlGeneratorTable);
            }
        }

        request.setAttribute("releaseHtmlGenerators",releaseHtmlGeneratorTabList);
        request.setAttribute("previewHtmlGenerators",previewHtmlGeneratorTabList);

        //初始化预览文件选项卡里的按钮
        this.initButtons(request,"previewTabButtons","/seller/web/freeShop/html/generate/preview");

        //初始化最终文件选项卡里的按钮
        this.initButtons(request,"releaseTabButtons","/seller/web/freeShop/html/generate/release");


        return "pages/sellerWebHtmlPage/freeShop/freeShop.html";
    }

}
