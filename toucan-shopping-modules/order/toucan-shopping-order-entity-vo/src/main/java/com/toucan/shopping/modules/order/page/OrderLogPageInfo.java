package com.toucan.shopping.modules.order.page;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import com.toucan.shopping.modules.order.vo.OrderLogVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 列表查询页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class OrderLogPageInfo extends PageInfo<OrderLogVO> {


    // ===============查询条件===================

    /**
     * 主键 雪花算法生成
     */
    private Long id;
    private String appCode; //所属应用
    private Long orderNo; //订单编号
    //==============================================





}
