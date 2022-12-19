package com.toucan.shopping.modules.order.page;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.MainOrder;
import com.toucan.shopping.modules.order.entity.Order;
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
public class MainOrderPageInfo extends PageInfo<MainOrder> {


    // ===============查询条件===================


    /**
     * 主键 雪花算法生成
     */
    private Long id;

    private String appCode; //所属应用

    private String userId; //所属用户


    //==============================================





}
