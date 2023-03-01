package com.toucan.shopping.modules.order.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.entity.UserApp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 列表查询页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class OrderPageInfo extends PageInfo<OrderVO> {


    // ===============查询条件===================


    /**
     * 主键 雪花算法生成
     */
    private Long id;

    private String appCode; //所属应用

    private String userId; //所属用户
    private String shopId; //所属店铺
    private String orderNo; //订单编号
    private String mainOrderNo; //主订单编号

    private Integer payStatus; //支付状态 -1全部 0未支付 1已支付 3线下支付已到账 4取消支付
    private Integer tradeStatus; //交易状态 0:待付款 1:待收货 2:已取消 3:已完成



    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startCreateDate; //开始下单时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endCreateDate; //结束下单时间

    private String keyword; //查询关键字

    //==============================================





}
