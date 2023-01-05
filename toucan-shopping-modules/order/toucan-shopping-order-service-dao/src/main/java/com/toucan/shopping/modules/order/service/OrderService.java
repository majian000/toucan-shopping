package com.toucan.shopping.modules.order.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.MainOrder;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;

import java.util.List;
import java.util.Map;

public interface OrderService {

    int create(Order order);

    int deleteByOrderNo(String orderNo);

//    Order createOrder(String userId, String orderNo, String appCode, Integer payMethod, List<ProductSku> productSkuList, Map<String, ProductBuy> buyMap);

    Order findByOrderNo(String orderNo);

    List<Order> findListByMainOrderNo(String mainOrderNo);

//    int finishOrder(Order order);
//

    List<Order> queryOrderListByPayTimeout(Order order);


    /**
     * 查询支付超时列表
     * @param pageInfo
     * @return
     */
    PageInfo<OrderVO> queryOrderListByPayTimeoutPage(OrderPageInfo pageInfo);



    /**
     * 查询订单列表页
     * @param pageInfo
     * @return
     */
    PageInfo<OrderVO> queryOrderListPage(OrderPageInfo pageInfo);

    int saveByVos(List<OrderVO> orders);

    /**
     * 取消所有未支付的订单
     * @param mainOrderNo
     * @param userId
     * @return
     */
    int cancelNoPayOrderByMainOrderNo(String mainOrderNo,String userId);


    /**
     * 取消所有未支付的订单
     * @param mainOrderNo
     * @param userId
     * @return
     */
    int cancelNoPayOrderByMainOrderNo(String mainOrderNo,String userId,String cancelRemark);

    /**
     * 根据主订单编号取消订单
     * @param mainOrderNo
     * @param appCode
     * @param cancelRemark
     * @return
     */
    int cancelByMainOrderNo(String mainOrderNo,String appCode,String cancelRemark);


    /**
     * 查询一个子订单对象
     * @param orderVO
     * @return
     */
    Order queryOneByVO(OrderVO orderVO);

}
