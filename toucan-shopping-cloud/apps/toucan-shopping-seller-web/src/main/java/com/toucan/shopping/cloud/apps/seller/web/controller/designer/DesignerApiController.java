package com.toucan.shopping.cloud.apps.seller.web.controller.designer;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductApproveService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignFreightTemplateService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.designer.seller.constant.SellerDesignerRedisKey;
import com.toucan.shopping.modules.designer.seller.model.container.PageContainer;
import com.toucan.shopping.modules.product.constant.ProductConstant;
import com.toucan.shopping.modules.product.vo.ShopProductApproveVO;
import com.toucan.shopping.modules.product.vo.ShopProductVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.seller.constant.FreightTemplateConstant;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.page.FreightTemplatePageInfo;
import com.toucan.shopping.modules.seller.util.FreightTemplateUtils;
import com.toucan.shopping.modules.seller.vo.FreightTemplateAreaRuleVO;
import com.toucan.shopping.modules.seller.vo.FreightTemplateVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


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


    @UserAuth
    @RequestMapping("/pc/index/preview")
    @ResponseBody
    public ResultObjectVO pcIndex(HttpServletRequest request, @RequestBody PageContainer pageContainer){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{

            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if(StringUtils.isEmpty(userMainId))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("预览失败,请稍后重试");
                return resultObjectVO;
            }

            //校验模型

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
                toucanStringRedisService.set(SellerDesignerRedisKey.getPcIndexPreviewKey(shopId), JSONObject.toJSONString(pageContainer));
            }


        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        resultObjectVO.setData(null);
        return resultObjectVO;
    }


}
