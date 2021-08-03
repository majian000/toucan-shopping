package com.toucan.shopping.cloud.apps.admin.controller.htmlPage.index;


import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.apps.admin.vo.htmlPage.HtmlGeneratorTab;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/index/html")
public class IndexGeneratorController extends UIController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/indexGeneratorPage",method = RequestMethod.GET)
    public String indexGeneratorPage(HttpServletRequest request)
    {
        List<HtmlGeneratorTab> releaseHtmlGeneratorTabList = new ArrayList<HtmlGeneratorTab>();
        List<HtmlGeneratorTab> previewHtmlGeneratorTabList = new ArrayList<HtmlGeneratorTab>();
        if(toucan.getShoppingPC()!=null&& StringUtils.isNotEmpty(toucan.getShoppingPC().getIpList()))
        {
            String ipList = toucan.getShoppingPC().getIpList();
            if(ipList.indexOf(",")!=-1)
            {
                String[] ips = ipList.split(",");
                if(ips!=null&&ips.length>0)
                {
                    for(String ip:ips)
                    {
                        HtmlGeneratorTab releaseHtmlGeneratorTable = new HtmlGeneratorTab();
                        releaseHtmlGeneratorTable.setName(ip);
                        releaseHtmlGeneratorTable.setContent("<iframe src='http://"+ip+"/htmls/release/index.html' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                        releaseHtmlGeneratorTabList.add(releaseHtmlGeneratorTable);

                        HtmlGeneratorTab previewHtmlGeneratorTable = new HtmlGeneratorTab();
                        previewHtmlGeneratorTable.setName(ip);
                        previewHtmlGeneratorTable.setContent("<iframe src='http://"+ip+"/htmls/preview/index.html' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                        previewHtmlGeneratorTabList.add(previewHtmlGeneratorTable);
                    }
                }
            }else{

                HtmlGeneratorTab releaseHtmlGeneratorTable = new HtmlGeneratorTab();
                releaseHtmlGeneratorTable.setName(ipList);
                releaseHtmlGeneratorTable.setContent("<iframe src='http://"+ipList+"/htmls/release/index.html' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                releaseHtmlGeneratorTabList.add(releaseHtmlGeneratorTable);

                HtmlGeneratorTab previewHtmlGeneratorTable = new HtmlGeneratorTab();
                previewHtmlGeneratorTable.setName(ipList);
                previewHtmlGeneratorTable.setContent("<iframe src='http://"+ipList+"/htmls/preview/index.html' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                previewHtmlGeneratorTabList.add(previewHtmlGeneratorTable);
            }
        }

        request.setAttribute("releaseHtmlGenerators",releaseHtmlGeneratorTabList);
        request.setAttribute("previewHtmlGenerators",previewHtmlGeneratorTabList);

        return "pages/htmlPage/index/index.html";
    }


    /**
     * 生成预览
     * @return
     */
    public ResultObjectVO generatePreview()
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}
