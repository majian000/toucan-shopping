package com.toucan.shopping.cloud.apps.admin.controller.htmlPage.area;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.apps.admin.vo.htmlPage.HtmlGeneratorTab;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.HttpUtils;
import com.toucan.shopping.modules.common.util.MD5Util;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/area/html")
public class AreaGeneratorController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;


    /**
     * 生成最终版
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:area:html:generator:release"})
    @RequestMapping(value = "/generate/release",method = RequestMethod.POST)
    public ResultObjectVO generateRelease()
    {
        String previewApi = "/api/html/province/city/area/generate/release";

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
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

    /**
     * 查询地区组件静态文件选项卡
     * 对应layui版本 /htmlPage/areaGenerator/areaGeneratorPage 的界面填充逻辑
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:area:html:generator:release"})
    @RequestMapping(value = "/query/tab",method = RequestMethod.POST)
    public ResultObjectVO queryTab()
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        List<HtmlGeneratorTab> releaseHtmlGeneratorTabList = new ArrayList<HtmlGeneratorTab>();
        List<HtmlGeneratorTab> previewHtmlGeneratorTabList = new ArrayList<HtmlGeneratorTab>();

        //商城PC端的地区组件
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
                        releaseHtmlGeneratorTable.setContent("<iframe src='http://"+ip+"/htmls/release/area/province_city_area.html' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                        releaseHtmlGeneratorTabList.add(releaseHtmlGeneratorTable);

                        HtmlGeneratorTab previewHtmlGeneratorTable = new HtmlGeneratorTab();
                        previewHtmlGeneratorTable.setName(ip);
                        previewHtmlGeneratorTable.setContent("<iframe src='http://"+ip+"/htmls/release/area/province_city_area.html' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                        previewHtmlGeneratorTabList.add(previewHtmlGeneratorTable);
                    }
                }
            }else{

                HtmlGeneratorTab releaseHtmlGeneratorTable = new HtmlGeneratorTab();
                releaseHtmlGeneratorTable.setName(ipList);
                releaseHtmlGeneratorTable.setContent("<iframe src='http://"+ipList+"/htmls/release/area/province_city_area.html' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                releaseHtmlGeneratorTabList.add(releaseHtmlGeneratorTable);

                HtmlGeneratorTab previewHtmlGeneratorTable = new HtmlGeneratorTab();
                previewHtmlGeneratorTable.setName(ipList);
                previewHtmlGeneratorTable.setContent("<iframe src='http://"+ipList+"/htmls/release/area/province_city_area.html' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                previewHtmlGeneratorTabList.add(previewHtmlGeneratorTable);
            }
        }


        //卖家中心的 地区组件
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
                        releaseHtmlGeneratorTable.setContent("<iframe src='http://"+ip+"/htmls/release/area/province_city_area.html' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                        releaseHtmlGeneratorTabList.add(releaseHtmlGeneratorTable);

                        HtmlGeneratorTab previewHtmlGeneratorTable = new HtmlGeneratorTab();
                        previewHtmlGeneratorTable.setName(ip);
                        previewHtmlGeneratorTable.setContent("<iframe src='http://"+ip+"/htmls/release/area/province_city_area.html' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                        previewHtmlGeneratorTabList.add(previewHtmlGeneratorTable);
                    }
                }
            }else{

                HtmlGeneratorTab releaseHtmlGeneratorTable = new HtmlGeneratorTab();
                releaseHtmlGeneratorTable.setName(ipList);
                releaseHtmlGeneratorTable.setContent("<iframe src='http://"+ipList+"/htmls/release/area/province_city_area.html' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                releaseHtmlGeneratorTabList.add(releaseHtmlGeneratorTable);

                HtmlGeneratorTab previewHtmlGeneratorTable = new HtmlGeneratorTab();
                previewHtmlGeneratorTable.setName(ipList);
                previewHtmlGeneratorTable.setContent("<iframe src='http://"+ipList+"/htmls/release/area/province_city_area.html' style='width:100%;height:1000px;border:0px;' frameborder='0' ></iframe>");
                previewHtmlGeneratorTabList.add(previewHtmlGeneratorTable);
            }
        }

        Map<String,Object> data = new HashMap<String,Object>();
        data.put("releaseHtmlGenerators",releaseHtmlGeneratorTabList);
        data.put("previewHtmlGenerators",previewHtmlGeneratorTabList);
        resultObjectVO.setData(data);
        return resultObjectVO;
    }

}
