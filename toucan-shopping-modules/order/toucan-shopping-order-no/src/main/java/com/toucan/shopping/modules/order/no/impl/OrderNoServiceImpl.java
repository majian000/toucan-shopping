package com.toucan.shopping.modules.order.no.impl;

import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.NumberUtil;
import com.toucan.shopping.modules.order.no.OrderNoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * 生成订单编号
 */
@Service
public class OrderNoServiceImpl implements OrderNoService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.machine-id}")
    private String machineId;

    /**
     * 订单号生成规则
     * 总长度32位=机器ID(4位)+时间戳(13位)+备用字段填充0(10位)+随机数(5位)
     *
     * @return
     */
    @Override
    public String generateOrderNo() {
        if(StringUtils.isEmpty(machineId))
        {
            logger.warn("请设置toucan.machine-id ");
            throw new IllegalArgumentException("请设置toucan.machine-id");
        }
        if(machineId.length()>4)
        {
            logger.warn("machine-id 长度超过4位"+machineId);
            throw new IllegalArgumentException("machine-id 长度超过4位");
        }
        if(!NumberUtil.isNumber(machineId))
        {
            logger.warn("machine-id 存在非法字符"+machineId);
            throw new IllegalArgumentException("machine-id 存在非法字符");
        }
        StringBuilder builder= new StringBuilder();
        builder.append(machineId);
        builder.append(String.valueOf(DateUtils.currentDate().getTime()));
        builder.append("0000000000");
        builder.append(NumberUtil.random(5));

        if(builder.toString().length()>32)
        {
            logger.warn("订单编号生成超过32位 "+builder.toString());
            throw new IllegalArgumentException("订单编号生成超过32位");
        }
        logger.info("订单编号生成 "+builder.toString());
        return builder.toString();
    }



}
