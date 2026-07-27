package com.toucan.shopping.modules.admin.auth.log.business.service;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.log.entity.OperateLog;
import com.toucan.shopping.modules.admin.auth.log.service.OperateLogService;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogChartVO;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogPageInfo;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogVO;
import com.toucan.shopping.modules.admin.auth.service.FunctionService;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志管理
 */
@Service
public class OperateLogBusinessService {

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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO saves(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
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
                    resultObjectVO = ResultObjectVO.fail(ResultObjectVO.FAILD, "部分保存失败");
                }
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryOperateChart(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try{
            OperateLogChartVO operateLogChartVO = requestJsonVO.formatEntity(OperateLogChartVO.class);
            Check.notNull(operateLogChartVO.getStartDate(), ResultVO.FAILD, "开始时间不能为空!");
            Check.notNull(operateLogChartVO.getAdvanceDay(), ResultVO.FAILD, "提前天数不能为空!");
            List<OperateLogChartVO> operateLogChartVOS = operateLogService.queryOperateLogCountList(operateLogChartVO.getStartDate(),operateLogChartVO.getEndDate(),operateLogChartVO.getAppCode());
            resultObjectVO.setData(operateLogChartVOS);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            OperateLogVO operateLogVO = JSONObject.parseObject(requestVo.getEntityJson(),OperateLogVO.class);
            Check.notNull(operateLogVO.getId(), ResultVO.FAILD, "没有找到ID");

            //查询是否存在
            OperateLogVO query=new OperateLogVO();
            query.setId(operateLogVO.getId());
            List<OperateLog> list = operateLogService.findListByEntity(query);
            if(CollectionUtils.isEmpty(list))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "记录不存在!");
            }
            resultObjectVO.setData(list);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO listPage(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

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

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

}
