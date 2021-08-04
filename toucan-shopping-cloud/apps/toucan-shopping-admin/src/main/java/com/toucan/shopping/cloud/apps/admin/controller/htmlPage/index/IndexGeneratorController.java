package com.toucan.shopping.cloud.apps.admin.controller.htmlPage.index;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.apps.admin.vo.htmlPage.HtmlGeneratorTab;
import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.HttpUtils;
import com.toucan.shopping.modules.common.util.MD5Util;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/index/html")
public class IndexGeneratorController extends UIController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;


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
            function.setAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),function);
            ResultObjectVO resultObjectVO = feignFunctionService.queryOneChildsByAdminIdAndAppCodeAndParentUrl(SignUtil.sign(requestJsonVO),requestJsonVO);
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

        //初始化预览文件选项卡里的按钮
        this.initButtons(request,"previewTabButtons","/index/html/generate/preview");

        //初始化最终文件选项卡里的按钮
        this.initButtons(request,"releaseTabButtons","/index/html/generate/release");


        return "pages/htmlPage/index/index.html";
    }


    /**
     * 生成预览
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/generate/preview",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO generatePreview()
    {
        String previewApi = "/api/html/index/generate/preview";

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            Header header = new BasicHeader("ts_web_generator_token", MD5Util.md5("toucan_shopping_generator"));
            List<Header> headers = new ArrayList<Header>();
            headers.add(header);

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
                            String responseString = HttpUtils.get("http://"+ip+previewApi,headers);
                            if(StringUtils.isEmpty(responseString))
                            {
                                resultObjectVO.setMsg(ipList+"生成预览文件失败,请重试");
                                resultObjectVO.setCode(TableVO.FAILD);
                                return resultObjectVO;
                            }
                            resultObjectVO = JSONObject.parseObject(responseString,ResultObjectVO.class);
                            if(!resultObjectVO.isSuccess())
                            {
                                resultObjectVO.setMsg(ipList+"生成预览文件失败,请重试");
                                resultObjectVO.setCode(TableVO.FAILD);
                                return resultObjectVO;
                            }
                        }
                    }
                }else{
                    String responseString = HttpUtils.get("http://"+ipList+previewApi,headers);
                    if(StringUtils.isEmpty(responseString))
                    {
                        resultObjectVO.setMsg(ipList+"生成预览文件失败,请重试");
                        resultObjectVO.setCode(TableVO.FAILD);
                        return resultObjectVO;
                    }
                    resultObjectVO = JSONObject.parseObject(responseString,ResultObjectVO.class);
                    if(!resultObjectVO.isSuccess())
                    {
                        resultObjectVO.setMsg(ipList+"生成预览文件失败,请重试");
                        resultObjectVO.setCode(TableVO.FAILD);
                        return resultObjectVO;
                    }
                }
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

    /**
     * 生成预览
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/generate/release",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO generateRelease()
    {
        String previewApi = "/api/html/index/generate/release";

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            Header header = new BasicHeader("ts_web_generator_token", MD5Util.md5("toucan_shopping_generator"));
            List<Header> headers = new ArrayList<Header>();
            headers.add(header);

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
                            String responseString = HttpUtils.get("http://"+ip+previewApi,headers);
                            if(StringUtils.isEmpty(responseString))
                            {
                                resultObjectVO.setMsg(ipList+"生成预览文件失败,请重试");
                                resultObjectVO.setCode(TableVO.FAILD);
                                return resultObjectVO;
                            }
                            resultObjectVO = JSONObject.parseObject(responseString,ResultObjectVO.class);
                            if(!resultObjectVO.isSuccess())
                            {
                                resultObjectVO.setMsg(ipList+"生成预览文件失败,请重试");
                                resultObjectVO.setCode(TableVO.FAILD);
                                return resultObjectVO;
                            }
                        }
                    }
                }else{
                    String responseString = HttpUtils.get("http://"+ipList+previewApi,headers);
                    if(StringUtils.isEmpty(responseString))
                    {
                        resultObjectVO.setMsg(ipList+"生成预览文件失败,请重试");
                        resultObjectVO.setCode(TableVO.FAILD);
                        return resultObjectVO;
                    }
                    resultObjectVO = JSONObject.parseObject(responseString,ResultObjectVO.class);
                    if(!resultObjectVO.isSuccess())
                    {
                        resultObjectVO.setMsg(ipList+"生成预览文件失败,请重试");
                        resultObjectVO.setCode(TableVO.FAILD);
                        return resultObjectVO;
                    }
                }
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}
