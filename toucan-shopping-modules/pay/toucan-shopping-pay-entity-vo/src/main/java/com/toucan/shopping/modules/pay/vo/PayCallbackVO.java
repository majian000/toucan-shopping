package com.toucan.shopping.modules.pay.vo;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 支付对象
 *
 * @author majian
 */
@Data
public class PayCallbackVO {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String orderNo;
    private String userId;

}
