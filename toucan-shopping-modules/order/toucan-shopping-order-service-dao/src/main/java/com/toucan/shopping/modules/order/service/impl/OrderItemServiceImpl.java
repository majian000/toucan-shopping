package com.toucan.shopping.modules.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.order.entity.Order;
import com.toucan.shopping.modules.order.entity.OrderItem;
import com.toucan.shopping.modules.order.mapper.OrderItemMapper;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderItemMapper orderItemMapper;


    public List<OrderItem> findByOrderNo(String orderNo, String userId){
        return orderItemMapper.findByOrderNo(orderNo,userId);
    }

    @Override
    public int deleteByOrderNo(String orderNo) {
        return orderItemMapper.deleteByOrderNo(orderNo);
    }



    @Override
    public int create(OrderItem orderItem) {
        return orderItemMapper.insert(orderItem);
    }

    @Transactional
    @Override
    public List<OrderItem> createOrderItem(List<ProductSku> productSkuList, Map<String, ProductBuy> buyMap, Order order) {
        List<OrderItem> orderItemPersistence = orderItemMapper.findByOrderNo(order.getOrderNo(),order.getUserId());
        if(CollectionUtils.isEmpty(productSkuList))
        {
            throw new IllegalArgumentException("没有选择商品");
        }
        if(buyMap==null)
        {
            throw new IllegalArgumentException("没有购买商品");
        }
        if(CollectionUtils.isEmpty(orderItemPersistence)) {
            orderItemPersistence = new ArrayList<OrderItem>();
            for(ProductSku productSku:productSkuList) {
                if(buyMap.get(String.valueOf(productSku.getId()))==null)
                {
                    continue;
                }
                ProductBuy productBuy = buyMap.get(String.valueOf(productSku.getId()));

                //商品价格
                BigDecimal productPriceBD = new BigDecimal(Double.toString(productSku.getPrice().doubleValue()));
                //购买商品数量
                BigDecimal productNumBD = new BigDecimal(Double.toString(productBuy.getBuyNum().intValue()));

                OrderItem orderItem = new OrderItem();
                orderItem.setCreateDate(new Date());
                orderItem.setUserId(order.getUserId());
                orderItem.setAppCode(order.getAppCode());
                orderItem.setOrderId(order.getId());
                orderItem.setOrderNo(order.getOrderNo());
                orderItem.setSkuId(productSku.getId());
                orderItem.setDeliveryStatus(0);
                //购买商品数量
                orderItem.setProductNum(productBuy.getBuyNum().intValue());
                //订单子项总金额
                orderItem.setOrderItemAmount(productPriceBD.multiply(productNumBD));
                orderItem.setProductPrice(productSku.getPrice());
                orderItem.setSellerStatus(0);
                orderItem.setBuyerStatus(0);
                orderItem.setDeleteStatus((short)0);

                orderItemMapper.insert(orderItem);

                if (orderItem.getId() == null) {
                    logger.info("下单失败: param:" + JSONObject.toJSONString(order));
                    throw new IllegalArgumentException("创建订单失败");
                }
                orderItemPersistence.add(orderItem);
            }
        }

//        if(new Random().nextInt(100)%2==0)
//        {
//            throw new IllegalArgumentException("手动测试异常");
//        }
        return orderItemPersistence;
    }
}
