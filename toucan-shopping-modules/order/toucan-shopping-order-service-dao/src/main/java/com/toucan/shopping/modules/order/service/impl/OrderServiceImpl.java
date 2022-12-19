package com.toucan.shopping.modules.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.order.entity.MainOrder;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.mapper.OrderMapper;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.order.service.OrderService;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemService orderItemService;




    @Override
    public int create(Order order) {
        return orderMapper.insert(order);
    }

    @Transactional
    @Override
    public int deleteByOrderNo(String orderNo) {
        return orderMapper.deleteByOrderNo(orderNo);
    }

//    @Transactional
//    @Override
//    public Order createOrder(String userId, String orderNo, String appCode, Integer payMethod, List<ProductSku> productSkuList, Map<String, ProductBuy> buyMap) {
//
//        if(CollectionUtils.isEmpty(productSkuList))
//        {
//            throw new IllegalArgumentException("没有选择商品");
//        }
//        if(buyMap==null||buyMap.size()==0)
//        {
//            throw new IllegalArgumentException("没有选择商品");
//        }
//
//        Order orderPersistence = orderMapper.findByOrderNo(orderNo);
//
//        if(orderPersistence==null) {
//
//            BigDecimal productPriceTotal=new BigDecimal(0D);
//            for(ProductSku productSku:productSkuList)
//            {
//                if(buyMap.get(String.valueOf(productSku.getId()))==null)
//                {
//                    continue;
//                }
//                ProductBuy productBuy = buyMap.get(String.valueOf(productSku.getId()));
//                //商品价格
//                BigDecimal productPriceBD = new BigDecimal(Double.toString(productSku.getPrice().doubleValue()));
//                //购买商品数量
//                BigDecimal productNumBD = new BigDecimal(productBuy.getBuyNum());
//                //商品总价 = 单价 * 数量
//                productPriceTotal = productPriceTotal.add(productPriceBD.multiply(productNumBD));
//            }
//
//            //订单创建
//            Order order = new Order();
//            order.setCreateDate(new Date());
//            order.setUserId(userId);
//            order.setAppCode(appCode);
//            order.setPayType(-1);
//            order.setOrderNo(orderNo);
//            order.setPayStatus(0);  //支付状态
//            order.setOrderAmount(productPriceTotal); //商品总金额
//            order.setTotalAmount(productPriceTotal); //商品最终金额(折扣算完)
//            order.setTradeStatus(0); //交易状态
//            order.setPayAmount(new BigDecimal(0D));
//            order.setPayMethod(payMethod);
//            order.setDeleteStatus((short)0);
//
//            this.create(order);
//            if (order.getId() == null) {
//                logger.info("下单失败: userId:" + userId+" orderNo:"+orderNo+" productSkuList:"+JSONObject.toJSONString(productSkuList)+" buyMap:"+JSONObject.toJSONString(buyMap));
//                throw new IllegalArgumentException("创建订单失败");
//            }
//
//            orderPersistence = order;
//        }
//        return orderPersistence;
//    }



    @Override
    public Order findByOrderNo(String orderNo) {
        return orderMapper.findByOrderNo(orderNo);
    }

//    @Transactional
//    @Override
//    public int finishOrder(Order order) {
//        return orderMapper.finishOrder(order);
//    }
//

    @Override
    public List<Order> queryOrderListByPayTimeout(Order order) {
        return orderMapper.queryOrderListByPayTimeout(order);
    }

    @Override
    public PageInfo<OrderVO> queryOrderListByPayTimeoutPage(OrderPageInfo pageInfo) {
        PageInfo<OrderVO> pageResult = new PageInfo();
        pageInfo.setStart(pageInfo.getPage()*pageInfo.getLimit()-pageInfo.getLimit());
        pageResult.setList(orderMapper.queryOrderListByPayTimeoutPage(pageInfo));
        pageResult.setTotal(orderMapper.queryOrderListByPayTimeoutPageCount(pageInfo));
        return pageResult;
    }

    @Transactional
    @Override
    public int saveByVos(List<OrderVO> orders) {
        return orderMapper.insertByVoList(orders);
    }

    @Transactional
    @Override
    public int cancelNoPayOrderByMainOrderNo(String mainOrderNo, String userId) {
        return orderMapper.cancelNoPayOrderByMainOrderNo(mainOrderNo,userId);
    }
}
