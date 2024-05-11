package com.toucan.shopping.modules.order.vo;

import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderLog;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 订单日志对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLogVO extends OrderLog {

    /**
     * 操作类型名称
     */
    private String typeName;

    private Integer operateUserType; //1:管理员 2:商城用户

    private String operateUserName; //操作用户名称


}
