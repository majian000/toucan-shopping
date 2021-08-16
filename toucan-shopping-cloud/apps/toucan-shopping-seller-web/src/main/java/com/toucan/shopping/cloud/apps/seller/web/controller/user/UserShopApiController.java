package com.toucan.shopping.cloud.apps.seller.web.controller.user;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.redis.ShopRegistRedisKey;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 个人店铺
 */
@Controller("userShopApiController")
@RequestMapping("/api/user/shop")
public class UserShopApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private SkylarkLock redisLock;


    /**
     * 个人店铺申请
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/shop_regist")
    public ResultObjectVO center(HttpServletRequest httpServletRequest,@RequestBody SellerShopVO sellerShopVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try {
            userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            boolean lockStatus = redisLock.lock(ShopRegistRedisKey.getRegistLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }
            UserVO userVO = new UserVO();
            userVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), userVO);
            resultObjectVO = feignUserService.verifyRealName(requestJsonVO.sign(), requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                boolean result = Boolean.valueOf(String.valueOf(resultObjectVO.getData()));
                if(result)
                {
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), sellerShopVO);
                    resultObjectVO = feignSellerShopService.save(requestJsonVO.sign(),requestJsonVO);
                }else{
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请先实名认证");
                }
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
            logger.warn(e.getMessage(),e);
        }finally{
            redisLock.unLock(ShopRegistRedisKey.getRegistLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }

}
