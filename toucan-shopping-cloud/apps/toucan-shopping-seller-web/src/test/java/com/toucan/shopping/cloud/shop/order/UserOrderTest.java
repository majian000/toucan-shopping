package com.toucan.shopping.cloud.shop.order;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductApproveService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.vo.PublishProductApproveVO;
import com.toucan.shopping.modules.product.vo.ShopProductApproveSkuVO;
import com.toucan.shopping.modules.product.vo.ShopProductApproveVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;


//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = CloudSellerWebApplication.class)
public class UserOrderTest {

    @Autowired
    private FeignShopProductApproveService feignShopProductApproveService;

    @Autowired
    private Toucan toucan;


    /**
     * 批量插入订单
     */
//    @Test
    public void batchInsertOrder() throws NoSuchAlgorithmException {
        Random rand = new Random(10);
        double buyCount = rand.nextInt();


//        this.batchApproveList();

    }



}
