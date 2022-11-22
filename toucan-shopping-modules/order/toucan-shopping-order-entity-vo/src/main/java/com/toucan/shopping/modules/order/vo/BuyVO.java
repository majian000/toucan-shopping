package com.toucan.shopping.modules.order.vo;

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
public class BuyVO {

    private List<UserBuyCarItemVO> buyCarItems;

    private BigDecimal moneyTotal;  //总金额

}
