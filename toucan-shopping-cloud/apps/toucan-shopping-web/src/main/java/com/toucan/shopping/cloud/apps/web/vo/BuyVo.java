package com.toucan.shopping.cloud.apps.web.vo;

import com.toucan.shopping.modules.product.entity.ProductBuy;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 购买对象
 *
 * @author majian
 */
@Data
public class BuyVo {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private List<UserBuyCarItemVO> buyCarItems;

    private BigDecimal moneyTotal;  //总金额

}
