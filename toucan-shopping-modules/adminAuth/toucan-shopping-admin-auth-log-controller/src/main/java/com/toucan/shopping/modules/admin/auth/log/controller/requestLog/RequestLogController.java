package com.toucan.shopping.modules.admin.auth.log.controller.requestLog;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.log.entity.RequestLog;
import com.toucan.shopping.modules.admin.auth.log.service.RequestLogService;
import com.toucan.shopping.modules.admin.auth.log.vo.RequestLogVO;
import com.toucan.shopping.modules.admin.auth.page.AppPageInfo;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 请求日志管理
 */
@RestController
@RequestMapping("/requestLog")
public class RequestLogController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private RequestLogService requestLogService;

    @Autowired
    private IdGenerator idGenerator;


    /**
     * 批量保存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/saves",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO saves(@RequestBody RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null) {
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if (requestJsonVO.getAppCode() == null) {
            logger.warn("没有找到应用编码: param:" + JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }
        try{
            List<RequestLogVO> requestLogVOS = requestJsonVO.formatEntityList(RequestLogVO.class);
            if(!CollectionUtils.isEmpty(requestLogVOS))
            {
                for(RequestLogVO requestLogVO:requestLogVOS)
                {
                    if(requestLogVO!=null)
                    {
                        requestLogVO.setId(idGenerator.id());
                        requestLogVO.setCreateDate(new Date());
                    }
                }
                int ret = requestLogService.saves(requestLogVOS);
                if(ret!=requestLogVOS.size())
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("部分保存失败");
                }
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}
