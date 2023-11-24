package com.toucan.shopping.cloud.apps.seller.web.controller.designer;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerDesignerPageModelService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.designer.core.exception.validator.ValidatorException;
import com.toucan.shopping.modules.designer.core.parser.IPageParser;
import com.toucan.shopping.modules.designer.core.validator.IPageValidator;
import com.toucan.shopping.modules.designer.seller.model.container.ShopPageContainer;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.util.ShopUtils;
import com.toucan.shopping.modules.seller.vo.SellerDesignerPageModelVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * 页面设计器
 * @author majian
 * @date 2023-10-13 14:55:40
 */
@Controller("designerApiController")
@RequestMapping("/api/designer")
public class DesignerApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Autowired
    private FeignSellerDesignerPageModelService feignSellerDesignerPageModelService;

    @Autowired
    private IPageParser pageParser;

    @Autowired
    private IPageValidator pageValidator;


    @UserAuth
    @RequestMapping("/pc/index/preview")
    @ResponseBody
    public ResultObjectVO pcIndexPreview(HttpServletRequest request,String pageJson,String position){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));

            String shopId = "-1";
            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if(sellerShopVO!=null&&sellerShopVO.getEnableStatus().intValue()==1) {
                    shopId = String.valueOf(sellerShopVO.getId());
                }
            }
            if(!"-1".equals(shopId))
            {
                try {
                    ShopPageContainer shopPageContainer = (ShopPageContainer)pageParser.convertToPageModel(pageJson);
                    //校验模型
                    pageValidator.valid(shopPageContainer);
                    SellerDesignerPageModelVO sellerDesignerPageVO=new SellerDesignerPageModelVO();
                    sellerDesignerPageVO.setType(1); //预览页
                    sellerDesignerPageVO.setPosition(Integer.parseInt(position));
                    sellerDesignerPageVO.setShopId(Long.parseLong(shopId));
                    sellerDesignerPageVO.setDesignerVersion("1.0");
                    sellerDesignerPageVO.setPageJson(JSONObject.toJSONString(shopPageContainer));
                    sellerDesignerPageVO.setUserMainId(Long.parseLong(userMainId));

                    resultObjectVO = feignSellerDesignerPageModelService.onlySaveOne(RequestJsonVOGenerator.generator(toucan.getAppCode(),sellerDesignerPageVO));
                    if(resultObjectVO.isSuccess()) {
                        resultObjectVO.setData(toucan.getShoppingPC().getBasePath() + toucan.getShoppingPC().getShopPcIndexPreviewPage() + "/" + ShopUtils.encShopId(shopId) + "/" + shopId);
                    }else{
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                    }
                }catch(Exception e)
                {
                    if(e instanceof ValidatorException)
                    {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg(e.getMessage());
                    }
                }
            }


        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("操作失败,请稍后再试");
        }
        return resultObjectVO;
    }



    @UserAuth
    @RequestMapping("/pc/index/saveAndPublish")
    @ResponseBody
    public ResultObjectVO pcIndexSaveAndPublish(HttpServletRequest request,String pageJson,String position){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));

            String shopId = "-1";
            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if(sellerShopVO!=null&&sellerShopVO.getEnableStatus().intValue()==1) {
                    shopId = String.valueOf(sellerShopVO.getId());
                }
            }
            if(!"-1".equals(shopId))
            {
                try {
                    ShopPageContainer shopPageContainer = (ShopPageContainer)pageParser.convertToPageModel(pageJson);
                    //校验模型
                    pageValidator.valid(shopPageContainer);
                    SellerDesignerPageModelVO sellerDesignerPageVO=new SellerDesignerPageModelVO();
                    sellerDesignerPageVO.setType(2); //正式页
                    sellerDesignerPageVO.setPosition(Integer.parseInt(position));
                    sellerDesignerPageVO.setShopId(Long.parseLong(shopId));
                    sellerDesignerPageVO.setDesignerVersion("1.0");
                    sellerDesignerPageVO.setPageJson(JSONObject.toJSONString(shopPageContainer));
                    sellerDesignerPageVO.setUserMainId(Long.parseLong(userMainId));

                    resultObjectVO = feignSellerDesignerPageModelService.onlySaveOne(RequestJsonVOGenerator.generator(toucan.getAppCode(),sellerDesignerPageVO));
                    resultObjectVO.setData(toucan.getShoppingPC().getBasePath()+toucan.getShoppingPC().getShopPcIndexReleasePage()+"/"+ ShopUtils.encShopId(shopId)+"/"+shopId);

                    //同步更新预览页
                    sellerDesignerPageVO.setType(1);
                    feignSellerDesignerPageModelService.onlySaveOne(RequestJsonVOGenerator.generator(toucan.getAppCode(),sellerDesignerPageVO));

                }catch(Exception e)
                {
                    if(e instanceof ValidatorException)
                    {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg(e.getMessage());
                    }
                }
            }


        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("操作失败,请稍后再试");
        }
        return resultObjectVO;
    }

}
