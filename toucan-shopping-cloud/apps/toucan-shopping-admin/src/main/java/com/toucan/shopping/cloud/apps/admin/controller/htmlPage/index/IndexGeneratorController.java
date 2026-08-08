package com.toucan.shopping.cloud.apps.admin.controller.htmlPage.index;


import com.alibaba.fastjson2.JSONObject;
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
import java.util.List;

@RestController
@RequestMapping("/index/html")
public class IndexGeneratorController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;


    /**
     * 生成预览
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:index:html:generator:preview:api"})
    @RequestMapping(value = "/generate/preview",method = RequestMethod.POST)
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
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

    /**
     * 生成最终版
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:index:html:generator:release:api"})
    @RequestMapping(value = "/generate/release",method = RequestMethod.POST)
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
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}
