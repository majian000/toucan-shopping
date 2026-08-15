package com.toucan.shopping.modules.order.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.vo.OrderRefundVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 订单退款流水 列表查询页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class OrderRefundPageInfo extends PageInfo<OrderRefundVO> {


    // ===============查询条件===================

    /**
     * 主键 雪花算法生成
     */
    private Long id;

    private String appCode; //所属应用

    private String refundNo; //内部退款流水号
    private String payNo; //关联支付流水号
    private String mainOrderNo; //主订单编号
    private String orderNo; //子订单编号
    private String userId; //用户ID
    private Integer refundStatus; //退款状态 0退款中 1退款成功 2退款失败

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startCreateDate; //开始创建时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endCreateDate; //结束创建时间

    //==============================================






}
