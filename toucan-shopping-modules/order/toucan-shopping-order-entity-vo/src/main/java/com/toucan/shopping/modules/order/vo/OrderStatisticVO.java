package com.toucan.shopping.modules.order.vo;


import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单统计
 * @author majian
 */
@Data
public class OrderStatisticVO {


    private BigDecimal totalMoney = new BigDecimal(0); ; //总金额

    private BigDecimal todayMoney = new BigDecimal(0); //今日金额

    private BigDecimal curMonthMoney = new BigDecimal(0); //本月金额

    private BigDecimal curYearMoney = new BigDecimal(0); //本年金额



}
