package com.toucan.shopping.modules.order.page;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.entity.UserApp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 列表查询页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class OrderPageInfo extends PageInfo<Order> {


    // ===============查询条件===================


    /**
     * 主键 雪花算法生成
     */
    private Long id;

    private String appCode; //所属应用


    //==============================================





}
