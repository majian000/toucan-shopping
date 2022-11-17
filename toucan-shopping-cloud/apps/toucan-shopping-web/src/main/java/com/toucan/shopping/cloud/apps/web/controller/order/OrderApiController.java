package com.toucan.shopping.cloud.apps.web.controller.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.vo.BuyResultVo;
import com.toucan.shopping.cloud.apps.web.vo.BuyVo;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSpuService;
import com.toucan.shopping.cloud.stock.api.feign.service.FeignProductSkuStockService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserBuyCarService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.persistence.event.entity.EventProcess;
import com.toucan.shopping.modules.common.persistence.event.service.EventProcessService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.order.vo.CreateOrderVo;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.util.ProductRedisKeyUtil;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.product.vo.ProductSpuVO;
import com.toucan.shopping.modules.product.vo.ShopProductDescriptionImgVO;
import com.toucan.shopping.modules.product.vo.ShopProductVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.stock.entity.ProductSkuStock;
import com.toucan.shopping.modules.stock.kafka.constant.StockMessageTopicConstant;
import com.toucan.shopping.modules.stock.vo.InventoryReductionVo;
import com.toucan.shopping.modules.stock.vo.RestoreStockVo;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * 订单服务
 * @author majian
 * @date 2022-11-17 09:41:58
 */
@RestController
@RequestMapping("/api/order")
public class OrderApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignOrderService feignOrderService;

    @Autowired
    private FeignProductSkuStockService feignProductSkuStockService;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private EventProcessService eventProcessService;

    @Autowired
    private OrderNoService orderNoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private FeignProductSkuService feignProductSkuService;

    @Autowired
    private FeignUserBuyCarService feignUserBuyCarService;

    /**
     * 创建订单
     * TODO:订单30分钟未支付 恢复预扣库存数量,支付宝回调刷新订单状态 如果失败创建本地事件以便事务补偿回滚
     * @param buyVo
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_AJAX)
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO buy(HttpServletRequest request, @RequestBody BuyVo buyVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        logger.info("购买商品: param:"+ JSONObject.toJSONString(buyVo));
        if(buyVo==null)
        {
            logger.info("没有找到要购买的商品: param:"+ JSONObject.toJSONString(buyVo));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到要购买的商品!");
            return resultObjectVO;
        }
        //锁住当前购买所有商品
        List<String> lockKeys= new ArrayList<String>();
        String userId = "-1";
        try {
            userId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if("-1".equals(userId))
            {
                throw new IllegalArgumentException("登录认证失败");
            }
            //订单号
            String orderNo= orderNoService.generateOrderNo();
            String globalTransactionId = UUID.randomUUID().toString().replace("-","");
            String appCode = toucan.getAppCode();
            for(UserBuyCarItemVO buyCarItemVO : buyVo.getBuyCarItems())
            {
                if(buyCarItemVO!=null) {
                    if (buyCarItemVO.getShopProductSkuId()==null) {
                        logger.warn("sku id is null params: {} ", JSONObject.toJSONString(buyCarItemVO));
                        throw new IllegalArgumentException("没有找到商品");
                    }
                    String productBuyKey = ProductRedisKeyUtil.getProductBuyKey(appCode, String.valueOf(buyCarItemVO.getShopProductSkuId()));
                    boolean lockStatus = skylarkLock.lock(productBuyKey, userId);
                    if (!lockStatus) {
                        //释放加的所有锁
                        if(!CollectionUtils.isEmpty(lockKeys))
                        {
                            for(String lockKey:lockKeys)
                            {
                                skylarkLock.unLock(lockKey,userId);
                            }
                        }
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("请稍后重试");
                        return resultObjectVO;
                    }
                    lockKeys.add(productBuyKey);
                }
            }


            //===================================查询当前库中的所有购物车项
            resultObjectVO = this.queryBuyCarItems(buyVo,Long.parseLong(userId));
            if(!resultObjectVO.isSuccess())
            {
                return resultObjectVO;
            }

            //可购买商品列表
            List<ProductSku> releaseProductSkuList = new ArrayList<ProductSku>();
            //库存不够商品列表
            List<ProductSku> faildProductSkuList = new ArrayList<ProductSku>();


            //==============================查询商品
            List<ProductSkuVO> productSkuVOS = new LinkedList<>();
            for(UserBuyCarItemVO userBuyCarItemVO:buyVo.getBuyCarItems())
            {
                ProductSkuVO productSkuVO = new ProductSkuVO();
                productSkuVO.setId(userBuyCarItemVO.getShopProductSkuId());
                productSkuVOS.add(productSkuVO);
            }
            if(CollectionUtils.isEmpty(productSkuVOS))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByUser(appCode,userId,productSkuVOS);
            resultObjectVO = feignProductSkuService.queryByIdList(SignUtil.sign(appCode,requestJsonVO.getEntityJson()),requestJsonVO);
            if(!resultObjectVO.isSuccess())
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }

            List<ProductSkuVO> queryProductSkuList = resultObjectVO.formatDataList(ProductSkuVO.class);
            if(CollectionUtils.isEmpty(queryProductSkuList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }

            //判断商品数量
            boolean buyStatus=true; //是否可以购买
            for (ProductSku productSku : queryProductSkuList) {
                if (productSku.getStockNum().intValue() <= 0) {
                    faildProductSkuList.add(productSku);
                    continue;
                }
                for(UserBuyCarItemVO userBuyCarItemVO:buyVo.getBuyCarItems())
                {
                    if(productSku.getId().longValue()==userBuyCarItemVO.getShopProductSkuId().longValue())
                    {
                        //购买数量大于库存数量
                        if(userBuyCarItemVO.getBuyCount().intValue()>productSku.getStockNum().intValue()) {
                            buyStatus = false;
                            faildProductSkuList.add(productSku);
                        }
                        break;
                    }
                }
                if(buyStatus) {
                    releaseProductSkuList.add(productSku);
                }
            }

            this.recalculateProductPrice(buyVo);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("购买失败,请重试!");
        }finally{   //上面做return 不会影响到这个锁的释放
            //释放所有商品锁
            for(String lockKey:lockKeys) {
                skylarkLock.unLock(lockKey, userId);
            }
        }

        return resultObjectVO;
    }

    /**
     * 查询库中所有购物车项
     * @param buyVo
     * @return
     */
    private ResultObjectVO queryBuyCarItems(BuyVo buyVo,Long userId)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            UserBuyCarItemVO userBuyCarVO = new UserBuyCarItemVO();
            userBuyCarVO.setUserMainId(userId);
            resultObjectVO = feignUserBuyCarService.listByUserMainId(RequestJsonVOGenerator.generator(toucan.getAppCode(),userBuyCarVO));
            if(!resultObjectVO.isSuccess())
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }
            //当前购物车的所有项
            List<UserBuyCarItemVO> userBuyCarVOList = resultObjectVO.formatDataList(UserBuyCarItemVO.class);
            if(CollectionUtils.isEmpty(userBuyCarVOList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试!");
                return resultObjectVO;
            }

            //以前端的购买数量为准
            for(UserBuyCarItemVO userBuyCarItemVO:userBuyCarVOList)
            {
                for(UserBuyCarItemVO frontBuyCarItem : buyVo.getBuyCarItems())
                {
                    if(userBuyCarItemVO.getId().longValue()==frontBuyCarItem.getId().longValue())
                    {
                        userBuyCarItemVO.setBuyCount(frontBuyCarItem.getBuyCount());
                    }
                }
            }
            //将数据库中的购物车项设置进去
            buyVo.setBuyCarItems(userBuyCarVOList);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请稍后重试!");
            resultObjectVO.setCode(ResultVO.FAILD);
        }
        return resultObjectVO;
    }

    /**
     * 重新计算商品价格
     */
    private void recalculateProductPrice(BuyVo buyVo)
    {

    }

}
