package com.toucan.shopping.modules.order.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.vo.OrderHotSellStatisticVO;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 订单热卖列表查询页对象
 */
@Data
@AllArgsConstructor
@ToString(callSuper = true)
public class OrderHotSellPageInfo extends PageInfo<OrderHotSellStatisticVO> {

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startPayDate; //开始下单时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endPayDate; //结束下单时间

}
