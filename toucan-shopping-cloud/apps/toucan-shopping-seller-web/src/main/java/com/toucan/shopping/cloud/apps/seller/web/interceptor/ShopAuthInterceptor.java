package com.toucan.shopping.cloud.apps.seller.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.shop.ShopAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.spring.context.SpringContextHolder;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.nio.charset.Charset;


/**
 * 店铺权限校验
 * 1.用户是否实名
 * 2.是否有店铺
 */
@Component
public class ShopAuthInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private SpringContextHolder springContextHolder;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ResultObjectVO resultVO = new ResultObjectVO();
        resultVO.setCode(ResultVO.SUCCESS);
        if (handler!=null&&handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            ShopAuth authAnnotation = method.getAnnotation(ShopAuth.class);
            try {
                response.setCharacterEncoding(Charset.defaultCharset().name());

                if (authAnnotation != null) {

                    //用户实名校验
                    if(authAnnotation.userRealName())
                    {
                        //拿到用户中心账号服务
                        FeignUserService feignUserService = springContextHolder.getBean(FeignUserService.class);

                        UserVO userVO = new UserVO();
                        String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
                        userVO.setUserMainId(Long.parseLong(userMainId));
                        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userVO);
                        ResultObjectVO resultObjectVO = feignUserService.verifyRealName(requestJsonVO.sign(), requestJsonVO);
                        if(resultObjectVO.isSuccess()) {
                            boolean result = Boolean.valueOf(String.valueOf(resultObjectVO.getData()));
                            if (!result) {
                                if(authAnnotation.requestType()==ShopAuth.REQUEST_FORM) {
                                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                            + request.getContextPath() + "/page/user/true/name/approve/page");
                                }else if(authAnnotation.requestType()==ShopAuth.REQUEST_JSON)
                                {
                                    resultVO.setCode(ResultVO.FAILD);
                                    resultVO.setMsg("该账号未实名");
                                    response.setContentType("application/json");
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    response.getWriter().write(JSONObject.toJSONString(resultVO));
                                }
                            }
                        }
                    }

                    if(authAnnotation.existsShop())
                    {
                        //拿到店铺服务
                        FeignSellerShopService feignSellerShopService = springContextHolder.getBean(FeignSellerShopService.class);

                        SellerShop querySellerShop = new SellerShop();
                        String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
                        querySellerShop.setUserMainId(Long.parseLong(userMainId));
                        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), querySellerShop);
                        //判断是个人店铺还是企业店铺
                        ResultObjectVO resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
                        if(!resultObjectVO.isSuccess()||resultObjectVO.getData()==null) {
                            if(authAnnotation.requestType()==ShopAuth.REQUEST_FORM) {
                                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                                        + request.getContextPath() + "/page/shop/select_remodel_type");
                            }else if(authAnnotation.requestType()==ShopAuth.REQUEST_JSON)
                            {
                                resultVO.setCode(ResultVO.FAILD);
                                resultVO.setMsg("没有找到店铺");
                                response.setContentType("application/json");
                                response.setStatus(HttpStatus.FORBIDDEN.value());
                                response.getWriter().write(JSONObject.toJSONString(resultVO));
                            }
                        }

                    }

                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                if (authAnnotation.requestType() == ShopAuth.REQUEST_JSON ) {
                    resultVO.setCode(ResultVO.FAILD);
                    resultVO.setMsg("请求失败");
                    response.setContentType("application/json");
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write(JSONObject.toJSONString(resultVO));
                }
                if (authAnnotation.requestType() == ShopAuth.REQUEST_FORM) {
                    response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                            + request.getContextPath() + "/page/user/login");
                }

                return false;
            }
        }
        return true;
    }


}