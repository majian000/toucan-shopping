package com.toucan.shopping.cloud.apps.admin.auth.web.controller.operateLog;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AppServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.OperateLogServiceAPI;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.log.entity.OperateLog;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogDetailVO;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogPageInfo;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogVO;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;



/**
 * 操作日志
 */
@Controller
@RequestMapping("/operateLog")
public class OperateLogController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private OperateLogServiceAPI operateLogServiceAPI;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private AppServiceAPI appServiceAPI;






    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_JSON,responseType=AdminAuth.RESPONSE_JSON)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO listPage(HttpServletRequest request, @RequestBody OperateLogPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = operateLogServiceAPI.listPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    fillTableVOPageData(tableVO, resultObjectVO.getData());
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }



    /**
     * 查询详情
     * @param request
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"pms:log:operate:detail"})
    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryDetail(HttpServletRequest request, @RequestBody OperateLog entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(entity.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = operateLogServiceAPI.findById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<OperateLogVO> operateLogVOS = resultObjectVO.formatDataList(OperateLogVO.class);
                if(!CollectionUtils.isEmpty(operateLogVOS))
                {
                    OperateLogVO operateLogVO = operateLogVOS.get(0);
                    OperateLogDetailVO detailVO = new OperateLogDetailVO();
                    BeanUtils.copyProperties(operateLogVO, detailVO);
                    // 解析应用名称
                    if(StringUtils.isNotEmpty(detailVO.getAppCode()))
                    {
                        Set<String> appCodes = new HashSet<>();
                        appCodes.add(detailVO.getAppCode());
                        AppVO appQuery = new AppVO();
                        appQuery.setCodes(new ArrayList<>(appCodes));
                        RequestJsonVO appRequestJsonVO = RequestJsonVOGenerator.generator(appCode, appQuery);
                        ResultObjectVO appResult = appServiceAPI.queryListByCodes(appRequestJsonVO);
                        if(appResult.isSuccess())
                        {
                            List<AppVO> apps = appResult.formatDataList(AppVO.class);
                            if(!CollectionUtils.isEmpty(apps))
                            {
                                detailVO.setAppName(apps.get(0).getName());
                            }
                        }
                    }
                    resultObjectVO.setData(detailVO);
                }
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}

