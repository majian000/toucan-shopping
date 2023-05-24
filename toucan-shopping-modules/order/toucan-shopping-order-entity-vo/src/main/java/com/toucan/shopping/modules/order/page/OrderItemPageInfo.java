package com.toucan.shopping.modules.order.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import com.toucan.shopping.modules.order.vo.OrderVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 列表查询页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class OrderItemPageInfo extends PageInfo<OrderItemVO> {


    // ===============查询条件===================

    /**
     * 主键 雪花算法生成
     */
    private Long id;
    private String appCode; //所属应用
    private Long orderId; //订单ID
    //==============================================





}
