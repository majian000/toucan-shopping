package com.toucan.shopping.modules.admin.auth.log.controller.requestLog;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.log.service.OperateLogService;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogChartVO;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 操作日志管理
 */
@RestController
@RequestMapping("/operateLog")
public class OperateLogController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private OperateLogService operateLogService;

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
            List<OperateLogVO> requestLogVOS = requestJsonVO.formatEntityList(OperateLogVO.class);
            if(!CollectionUtils.isEmpty(requestLogVOS))
            {
                for(OperateLogVO requestLogVO:requestLogVOS)
                {
                    if(requestLogVO!=null)
                    {
                        requestLogVO.setId(idGenerator.id());
                        requestLogVO.setCreateDate(new Date());
                        requestLogVO.setDeleteStatus((short)0);
                    }
                }
                int ret = operateLogService.saves(requestLogVOS);
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



    /**
     * 查询操作日志统计表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/queryOperateChart",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryOperateChart(@RequestBody RequestJsonVO requestJsonVO) {
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
            OperateLogChartVO operateLogChartVO = requestJsonVO.formatEntity(OperateLogChartVO.class);
            if (operateLogChartVO.getStartDate() == null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("开始时间不能为空!");
                return resultObjectVO;
            }
            if (operateLogChartVO.getAdvanceDay() == null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("提前天数不能为空!");
                return resultObjectVO;
            }
            List<OperateLogChartVO> operateLogChartVOS = operateLogService.queryOperateLogCountList(operateLogChartVO.getStartDate(),operateLogChartVO.getEndDate(),null);
            resultObjectVO.setData(operateLogChartVOS);
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }
}
