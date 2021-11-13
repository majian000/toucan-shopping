package com.toucan.shopping.cloud.apps.seller.web.controller.shop;

import com.alibaba.fastjson.JSONArray;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.area.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 店铺信息
 */
@Controller("shopPageController")
@RequestMapping("/page/shop")
public class ShopPageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignAreaService feignAreaService;

    @Autowired
    private ImageUploadService imageUploadService;


    /**
     * 店铺信息
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/info")
    public String info(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {

        try {
            UserVO userVO = new UserVO();
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            userVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), userVO);
            ResultObjectVO resultObjectVO = feignUserService.verifyRealName(requestJsonVO.sign(), requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                boolean result = Boolean.valueOf(String.valueOf(resultObjectVO.getData()));
                if(result)
                {
                    SellerShop querySellerShop = new SellerShop();
                    querySellerShop.setUserMainId(userVO.getUserMainId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
                    //判断是个人店铺还是企业店铺
                    resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        //该账号存在店铺
                        SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                        if(sellerShopVO==null)
                        {
                            return "shop/select_regist_type";
                        }

                        //设置店铺logo
                        if(sellerShopVO.getLogo()!=null) {
                            sellerShopVO.setHttpLogo(imageUploadService.getImageHttpPrefix() + "/" + sellerShopVO.getLogo());
                        }
                        //个人店铺
                        if(sellerShopVO.getType().intValue()==1)
                        {
                            httpServletRequest.setAttribute("sellerShop",sellerShopVO);
                            return "index";
                        }

                    }

                }else{
                    return "shop/please_true_name";
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "index";
    }


    /**
     * 修改店铺信息
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/edit")
    public String update(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {

        try {
            UserVO userVO = new UserVO();
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            userVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), userVO);
            ResultObjectVO resultObjectVO = feignUserService.verifyRealName(requestJsonVO.sign(), requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                boolean result = Boolean.valueOf(String.valueOf(resultObjectVO.getData()));
                if(result)
                {
                    SellerShop querySellerShop = new SellerShop();
                    querySellerShop.setUserMainId(userVO.getUserMainId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
                    //判断是个人店铺还是企业店铺
                    resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        //该账号存在店铺
                        SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                        if(sellerShopVO==null)
                        {
                            return "shop/select_regist_type";
                        }

                        //个人店铺
                        if(sellerShopVO.getType().intValue()==1) {
                            if (sellerShopVO.getEnableStatus().intValue() != 1)
                            {
                                httpServletRequest.setAttribute("rejectText","店铺已被禁用");
                                return "shop/faild";
                            }

                            if(sellerShopVO.getChangeNameCount()!=null)
                            {
                                sellerShopVO.setSurplusChangeNameCount(3-sellerShopVO.getChangeNameCount().intValue());
                            }else{
                                sellerShopVO.setSurplusChangeNameCount(3);
                            }

                            String selectProvinceCityAreaName="";
                            if(StringUtils.isNotEmpty(sellerShopVO.getProvince()))
                            {
                                selectProvinceCityAreaName = sellerShopVO.getProvince();
                            }
                            if(StringUtils.isNotEmpty(sellerShopVO.getCity()))
                            {
                                selectProvinceCityAreaName +="/"+ sellerShopVO.getCity();
                            }
                            if(StringUtils.isNotEmpty(sellerShopVO.getArea()))
                            {
                                selectProvinceCityAreaName +="/"+ sellerShopVO.getArea();
                            }

                            httpServletRequest.setAttribute("selectProvinceCityAreaName",selectProvinceCityAreaName);
                            httpServletRequest.setAttribute("sellerShop",sellerShopVO);
                            try {
                                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), new AreaVO());
                                resultObjectVO = feignAreaService.queryFullCache(requestJsonVO.sign(), requestJsonVO);
                                if(resultObjectVO.isSuccess())
                                {
                                    httpServletRequest.setAttribute("areaList", JSONArray.toJSONString(resultObjectVO.getData()));
                                }else{
                                    httpServletRequest.setAttribute("areaList","[]");
                                }
                            }catch(Exception e)
                            {
                                httpServletRequest.setAttribute("areaList","[]");
                                logger.warn("查询地区缓存失败 {} ",e.getMessage());
                                logger.warn(e.getMessage(),e);
                            }
                            return "shop/userShop/edit";
                        }

                    }

                }else{
                    return "shop/please_true_name";
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "index";
    }

    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/regist_success")
    public String submitSuccess(HttpServletRequest request){
        return "shop/regist_success";
    }

    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/update_success")
    public String updateSuccess(HttpServletRequest request){
        return "shop/update_success";
    }

    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping("/select_remodel_type")
    public String selectRemodelType(HttpServletRequest request){
        return "shop/select_remodel_type";
    }

}
