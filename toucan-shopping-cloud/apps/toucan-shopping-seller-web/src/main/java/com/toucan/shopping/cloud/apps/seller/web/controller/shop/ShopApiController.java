package com.toucan.shopping.cloud.apps.seller.web.controller.shop;

import com.alibaba.fastjson2.JSONArray;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.redis.ShopRegistRedisKey;
import com.toucan.shopping.cloud.apps.seller.web.util.MobilePhoneVCodeUtil;
import com.toucan.shopping.cloud.order.api.OrderServiceAPI;
import com.toucan.shopping.cloud.product.api.ProductSkuServiceAPI;
import com.toucan.shopping.cloud.product.api.ShopProductApproveServiceAPI;
import com.toucan.shopping.cloud.seller.api.SellerShopServiceAPI;
import com.toucan.shopping.cloud.user.api.SmsServiceAPI;
import com.toucan.shopping.cloud.user.api.UserServiceAPI;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.product.vo.ShopProductApproveVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopOverviewVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.sms.constant.SmsTypeConstant;
import com.toucan.shopping.modules.user.vo.UserRegistVO;
import com.toucan.shopping.modules.user.vo.UserSmsVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;


/**
 * 店铺信息
 */
@Controller("shopApiController")
@RequestMapping("/api/shop")
public class ShopApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserServiceAPI userService;

    @Autowired
    private SellerShopServiceAPI sellerShopService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ImageUploadService imageUploadService;


    @Autowired
    private SkylarkLock redisLock;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;


    @Autowired
    private SmsServiceAPI smsService;

    @Autowired
    private ProductSkuServiceAPI productSkuService;

    @Autowired
    private ShopProductApproveServiceAPI shopProductApproveService;

    @Autowired
    private OrderServiceAPI orderService;


    @UserAuth
    @RequestMapping(value="/shop/info")
    @ResponseBody
    public ResultObjectVO loginInfo(HttpServletRequest httpServletRequest){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            UserVO queryUserVO = new UserVO();
            queryUserVO.setUserMainId(Long.parseLong(UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()))));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryUserVO);
            resultObjectVO = sellerShopService.findByUser(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if(sellerShopVO==null)
                {
                    sellerShopVO = new SellerShopVO();
                }
                if(sellerShopVO.getLogo()!=null) {
                    sellerShopVO.setHttpLogo(imageUploadService.getImageHttpPrefix() + "/" + sellerShopVO.getLogo());
                }else{

                    //设置默认店铺图标
                    if(toucan.getSeller()!=null&&toucan.getSeller().getDefaultShopLogo()!=null) {
                        sellerShopVO.setHttpLogo(imageUploadService.getImageHttpPrefix() + "/" + toucan.getSeller().getDefaultShopLogo());
                    }
                }
                resultObjectVO.setData(sellerShopVO);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 发送注册验证码
     * @return
     */
    @UserAuth
    @RequestMapping("/sendRegistVerifyCode")
    @ResponseBody
    public ResultObjectVO sendRegistVerifyCode(String mobilePhone)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(StringUtils.isEmpty(mobilePhone)||!PhoneUtils.isChinaPhoneLegal(mobilePhone))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("发送失败,手机号错误");
            return resultObjectVO;
        }

        try {

            Object mobilePhoneValue = toucanStringRedisService.get(ShopRegistRedisKey.getVerifyCodeKey(mobilePhone));
            if(mobilePhoneValue!=null&&StringUtils.isNotEmpty(String.valueOf(mobilePhoneValue)))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("已发送,请在1分钟后重新获取");
                return resultObjectVO;
            }

            UserSmsVO userSmsVO = new UserSmsVO();
            userSmsVO.setMobilePhone(mobilePhone);;
            userSmsVO.setType(SmsTypeConstant.SHOP_REGIST_TYPE);
            boolean lockStatus = redisLock.lock(ShopRegistRedisKey.getRegistLockKey(mobilePhone), mobilePhone);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }


            //保存生成验证码到缓存
            String code = MobilePhoneVCodeUtil.genCode(6);
            userSmsVO.setMsg("[犀鸟电商]您于"+ DateUtils.format(DateUtils.currentDate(), DateUtils.FORMATTER_DD_CN.get())+"申请了店铺注册,验证码是"+code);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(),userSmsVO);

            resultObjectVO = smsService.send(requestJsonVO);
            if(resultObjectVO.getCode().intValue()== ResultObjectVO.SUCCESS.intValue())
            {
                //将验证码保存到缓存
                toucanStringRedisService.set(ShopRegistRedisKey.getVerifyCodeKey(userSmsVO.getMobilePhone()),code);
                //默认2分钟过期
                toucanStringRedisService.expire(ShopRegistRedisKey.getVerifyCodeKey(userSmsVO.getMobilePhone()),60*2, TimeUnit.SECONDS);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("发送失败,请稍后重试");
        }finally{
            redisLock.unLock(ShopRegistRedisKey.getRegistLockKey(mobilePhone), mobilePhone);
        }
        return resultObjectVO;


    }






    @UserAuth
    @RequestMapping(value="/shop/overview")
    @ResponseBody
    public ResultObjectVO shopOverview(HttpServletRequest httpServletRequest){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            UserVO queryUserVO = new UserVO();
            queryUserVO.setUserMainId(Long.parseLong(UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()))));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryUserVO);
            ShopOverviewVO shopOverviewVO = new ShopOverviewVO();
            //设置默认店铺图标
            if(toucan.getSeller()!=null&&toucan.getSeller().getDefaultShopLogo()!=null) {
                shopOverviewVO.setHttpShopLogo(imageUploadService.getImageHttpPrefix() + "/" + toucan.getSeller().getDefaultShopLogo());
            }
            ResultObjectVO queryShopResult = sellerShopService.findByUser(requestJsonVO);
            SellerShopVO sellerShopVO = new SellerShopVO();
            sellerShopVO.setId(-1L);
            if(queryShopResult.isSuccess())
            {
                sellerShopVO = queryShopResult.formatData(SellerShopVO.class);
                if(sellerShopVO!=null&&sellerShopVO.getLogo()!=null) {
                    shopOverviewVO.setShopLogo(sellerShopVO.getLogo());
                    shopOverviewVO.setHttpShopLogo(imageUploadService.getImageHttpPrefix() + "/" + sellerShopVO.getLogo());
                }
            }

            //已上架商品数量
            ProductSkuVO queryShelversVO=new ProductSkuVO();
            queryShelversVO.setShopId(sellerShopVO.getId());
            ResultTypeObjectVO<Long> shelvesProductCount = productSkuService.queryShelvesCountByShopId(RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShelversVO));
            if(shelvesProductCount.isSuccess()) {
                shopOverviewVO.setShelvesProductCount(shelvesProductCount.getData());
            }

            //待审核商品数量
            ShopProductApproveVO queryProductApproveVO=new ShopProductApproveVO();
            queryProductApproveVO.setShopId(sellerShopVO.getId());
            ResultTypeObjectVO<Long> queryProductApproveCount = shopProductApproveService.queryApproveCountByShopId(RequestJsonVOGenerator.generator(toucan.getAppCode(),queryProductApproveVO));
            if(queryProductApproveCount.isSuccess()) {
                shopOverviewVO.setWaitApproveProductCount(queryProductApproveCount.getData());
            }

            //查询完成订单数量
            OrderVO queryFinishCount = new OrderVO();
            queryFinishCount.setShopId(sellerShopVO.getId());
            ResultTypeObjectVO<Long> queryFinishOrderCount = orderService.queryFinishCountByShopId(RequestJsonVOGenerator.generator(toucan.getAppCode(),queryFinishCount));
            if(queryFinishOrderCount.isSuccess()) {
                shopOverviewVO.setFinishOrderCount(queryFinishOrderCount.getData());
            }


            resultObjectVO.setData(shopOverviewVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




}
