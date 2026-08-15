package com.toucan.shopping.modules.order.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.vo.OrderPayVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 订单支付流水 列表查询页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class OrderPayPageInfo extends PageInfo<OrderPayVO> {


    // ===============查询条件===================

    /**
     * 主键 雪花算法生成
     */
    private Long id;

    private String appCode; //所属应用

    private String payNo; //内部支付流水号
    private String mainOrderNo; //主订单编号
    private String userId; //用户ID
    private Integer payType; //交易类型 -1未确定 0微信 1支付宝
    private Integer payStatus; //支付状态 0未支付 1已支付 4取消支付 5已退款
    private String outerTradeNo; //第三方交易流水号

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startCreateDate; //开始创建时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endCreateDate; //结束创建时间

    //==============================================






}
