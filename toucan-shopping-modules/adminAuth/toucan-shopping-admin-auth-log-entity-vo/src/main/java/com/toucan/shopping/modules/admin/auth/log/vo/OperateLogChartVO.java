package com.toucan.shopping.modules.admin.auth.log.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 操作日志图表
 */
@Data
public class OperateLogChartVO extends OperateLogVO{

    private Long operateCount;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date operateDateYMD;

    private String operateDateYMDString; //操作时间字符串

    private Integer advanceDay = 0 ; //提前的天数

    private Date startDate; //开始时间

    private Date endDate; //结束时间

    private List<String> appNames; //应用名称

    private List<String> categorys; //分类列表

    private List<String> values; //数据值

    private List<OperateLogChartVO> datas; //每条数据

}
