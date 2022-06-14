package com.toucan.shopping.cloud.apps.admin.auth.web.controller.chart;

import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignOperateLogService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogChartVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作统计图标
 */

@Controller
@RequestMapping("/operate/log/chart")
public class OperateChartController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private FeignOperateLogService feignOperateLogService;

    /**
     * 查询操作数
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/queryOperateChart",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAppLoginUserCountList()
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            int adviceDay = 5;
            OperateLogChartVO operateLogChartVO = new OperateLogChartVO();
            operateLogChartVO.setStartDate(new Date());
            operateLogChartVO.setAppCode(appCode);
            Date endDate = DateUtils.currentDate();
            Date startDate = DateUtils.parse(DateUtils.format(DateUtils.advanceDay(endDate,5),DateUtils.FORMATTER_DD.get())+" 00:00:00",DateUtils.FORMATTER_SS.get());
            operateLogChartVO.setStartDate(startDate);
            operateLogChartVO.setEndDate(endDate);
            operateLogChartVO.setAdvanceDay(adviceDay); //查询前5天数据
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, operateLogChartVO);
            resultObjectVO = feignOperateLogService.queryOperateChart(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                operateLogChartVO = new OperateLogChartVO();
                List<OperateLogChartVO> datas = resultObjectVO.formatDataList(OperateLogChartVO.class);
                if (CollectionUtils.isNotEmpty(datas)) {
                    //设置应用名称
                    List<String> appNames = new LinkedList<>();
                    for (OperateLogChartVO olc : datas) {
                        if (olc != null) {
                            appNames.add(olc.getAppName());
                        }
                        olc.setOperateDateYMDString(DateUtils.format(olc.getOperateDateYMD(),DateUtils.FORMATTER_DD.get()));
                    }
                    appNames = appNames.stream().distinct().collect(Collectors.toList());
                    operateLogChartVO.setAppNames(appNames);
                }

                //设置分类
                operateLogChartVO.setCategorys(new LinkedList<>());
                for (int i = adviceDay; i >= 1; i--)
                {
                    operateLogChartVO.getCategorys().add(DateUtils.format(DateUtils.advanceDay(endDate,i),DateUtils.FORMATTER_DD.get()));
                }
                operateLogChartVO.getCategorys().add(DateUtils.format(endDate,DateUtils.FORMATTER_DD.get()));

                boolean isFind=false;
                operateLogChartVO.setDatas(new LinkedList<>());
                if(CollectionUtils.isNotEmpty(operateLogChartVO.getAppNames()))
                {
                    //遍历应用
                    for(String appName:operateLogChartVO.getAppNames())
                    {
                        OperateLogChartVO appOperateLogChartVO = new OperateLogChartVO();
                        appOperateLogChartVO.setValues(new LinkedList<>());
                        appOperateLogChartVO.setAppName(appName);
                        if(StringUtils.isNotEmpty(appName))
                        {
                            //遍历分类
                            for(String category:operateLogChartVO.getCategorys()) {
                                isFind = false;
                                for (OperateLogChartVO olc : datas) {
                                    //这个应用的这个日期 有操作记录
                                    if (olc.getAppName() != null && olc.getAppName().equals(appName)) {
                                        if(olc.getOperateDateYMDString()!=null&&olc.getOperateDateYMDString().equals(category))
                                        {
                                            isFind=true;
                                            appOperateLogChartVO.getValues().add(String.valueOf(olc.getOperateCount()));
                                            break;
                                        }
                                    }
                                }
                                if(!isFind)
                                {
                                    appOperateLogChartVO.getValues().add("0");
                                }
                            }
                        }
                        operateLogChartVO.getDatas().add(appOperateLogChartVO);
                    }
                }

                resultObjectVO.setData(operateLogChartVO);
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
