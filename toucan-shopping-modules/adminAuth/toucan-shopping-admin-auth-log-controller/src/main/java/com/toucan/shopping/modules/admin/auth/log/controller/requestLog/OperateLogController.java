package com.toucan.shopping.modules.admin.auth.log.controller.requestLog;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.log.entity.OperateLog;
import com.toucan.shopping.modules.admin.auth.log.service.OperateLogService;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogChartVO;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogPageInfo;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogVO;
import com.toucan.shopping.modules.admin.auth.service.FunctionService;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private FunctionService functionService;


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
                List<String> urls = new LinkedList<>();
                List<String> appCodes = new LinkedList<>();
                for(OperateLogVO requestLogVO:requestLogVOS)
                {
                    if(requestLogVO!=null)
                    {
                        requestLogVO.setId(idGenerator.id());
                        requestLogVO.setCreateDate(new Date());
                        requestLogVO.setDeleteStatus((short)0);

                        if(StringUtils.isNotEmpty(requestLogVO.getUri())) {
                            urls.add(requestLogVO.getUri());
                        }

                        if(StringUtils.isNotEmpty(requestLogVO.getAppCode())) {
                            appCodes.add(requestLogVO.getAppCode());
                        }
                    }
                }

                urls = urls.stream().distinct().collect(Collectors.toList());
                appCodes = appCodes.stream().distinct().collect(Collectors.toList());
                List<FunctionVO> functionVOS = functionService.queryListByUrlsAndAppCodes(urls,appCodes);
                for(OperateLogVO operateLogVO:requestLogVOS)
                {
                    if(StringUtils.isNotEmpty(operateLogVO.getUri())&&StringUtils.isNotEmpty(operateLogVO.getAppCode()))
                    {
                        for(FunctionVO functionVO:functionVOS)
                        {
                            if(functionVO!=null&&operateLogVO.getUri().equals(functionVO.getUrl())&&operateLogVO.getAppCode().equals(functionVO.getAppCode())){
                                operateLogVO.setFunctionId(functionVO.getFunctionId());
                                operateLogVO.setFunctionName(functionVO.getName());
                                break;
                            }
                        }
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
            List<OperateLogChartVO> operateLogChartVOS = operateLogService.queryOperateLogCountList(operateLogChartVO.getStartDate(),operateLogChartVO.getEndDate(),operateLogChartVO.getAppCode());
            resultObjectVO.setData(operateLogChartVOS);
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            OperateLogVO operateLogVO = JSONObject.parseObject(requestVo.getEntityJson(),OperateLogVO.class);
            if(operateLogVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在
            OperateLogVO query=new OperateLogVO();
            query.setId(operateLogVO.getId());
            List<OperateLog> list = operateLogService.findListByEntity(query);
            if(CollectionUtils.isEmpty(list))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("记录不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(list);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO listPage(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            OperateLogPageInfo pageInfo = JSONObject.parseObject(requestVo.getEntityJson(), OperateLogPageInfo.class);
            PageInfo<OperateLogVO> page =  operateLogService.queryListPage(pageInfo);
            if(!CollectionUtils.isEmpty(page.getList()))
            {
                for(OperateLogVO operateLogVO:page.getList())
                {
                    if(operateLogVO!=null&&operateLogVO.getParams().length()>200)
                    {
                        operateLogVO.setParams(operateLogVO.getParams().substring(0,200)+"...");
                    }
                }
            }
            resultObjectVO.setData(page);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

}
