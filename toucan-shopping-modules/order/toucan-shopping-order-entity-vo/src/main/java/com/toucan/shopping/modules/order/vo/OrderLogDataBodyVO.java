package com.toucan.shopping.modules.order.vo;

import lombok.Data;


/**
 * 订单日志数据主体
 */
@Data
public class OrderLogDataBodyVO {

    private Integer type; //1:订单数据 2:收货人数据

    private Object oldData; //旧的数据

    private Object updateData; //修改的数据

}
