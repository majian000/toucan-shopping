package com.toucan.shopping.admin.auth.web.controller.app;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.admin.auth.api.feign.service.FeignAppService;
import com.toucan.shopping.admin.auth.export.entity.App;
import com.toucan.shopping.admin.auth.export.page.AdminPageInfo;
import com.toucan.shopping.admin.auth.export.page.AppPageInfo;
import com.toucan.shopping.admin.auth.export.vo.AppVO;
import com.toucan.shopping.admin.auth.web.vo.TableVO;
import com.toucan.shopping.auth.admin.Auth;
import com.toucan.shopping.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.common.properties.Toucan;
import com.toucan.shopping.common.util.AuthHeaderUtil;
import com.toucan.shopping.common.util.SignUtil;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * 应用管理
 */
@Controller
@RequestMapping("/app")
public class AppController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignAppService feignAppService;



    @Auth(verifyMethod = Auth.VERIFYMETHOD_USER_CENTER,requestType = Auth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String page()
    {
        return "pages/app/list.html";
    }



    @Auth(verifyMethod = Auth.VERIFYMETHOD_USER_CENTER,requestType = Auth.REQUEST_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage()
    {
        return "pages/app/add.html";
    }



    /**
     * 保存
     * @param app
     * @return
     */
    @Auth
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(@RequestHeader("Cookie") String cookie, @RequestBody App app)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            app.setCreateAdminId(AuthHeaderUtil.getAdminId(cookie));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, app);
            resultObjectVO = feignAppService.save(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @Auth
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO listPage(HttpServletRequest request, AppPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignAppService.listPage(SignUtil.sign(requestJsonVO),requestJsonVO);
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


    /**
     * 删除应用
     * @param request
     * @return
     */
    @Auth
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(HttpServletRequest request,@RequestHeader("Cookie") String cookie,  @PathVariable String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            App app =new App();
            app.setId(Integer.parseInt(id));
            app.setUpdateAdminId(AuthHeaderUtil.getAdminId(cookie));

            String entityJson = JSONObject.toJSONString(app);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignAppService.deleteById(SignUtil.sign(requestVo),requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 删除应用
     * @param request
     * @return
     */
    @Auth
    @RequestMapping(value = "/delete/ids",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(HttpServletRequest request,@RequestHeader("Cookie") String cookie, @RequestBody List<AppVO> appVOs)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(appVOs))
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(appVOs);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignAppService.deleteByIds(SignUtil.sign(requestVo), requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}

