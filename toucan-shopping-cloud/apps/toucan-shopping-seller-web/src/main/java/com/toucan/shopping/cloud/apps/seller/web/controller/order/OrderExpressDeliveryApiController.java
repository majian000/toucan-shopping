package com.toucan.shopping.cloud.apps.seller.web.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.service.ShopService;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderExpressDeliveryService;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderItemService;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderStatisticService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductApproveService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.order.enums.ExpressCompanyEnum;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.vo.ExpressCompanyVO;
import com.toucan.shopping.modules.order.vo.OrderExpressDeliveryVO;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * 订单快递信息
 * @author majian
 * @date 2024-4-18 11:03:31
 */
@Controller("orderExpressDeliveryApiController")
@RequestMapping("/api/orderExpressDelivery")
public class OrderExpressDeliveryApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignOrderExpressDeliveryService feignOrderExpressDeliveryService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private FeignOrderService feignOrderService;


    /**
     * 保存或更新
     * @param request
     * @param orderExpressDeliveryVO
     * @return
     */
    @UserAuth
    @RequestMapping(value="/saveOrUpdate",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO saveOrUpdate(HttpServletRequest request,@RequestBody OrderExpressDeliveryVO orderExpressDeliveryVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try {
            if(orderExpressDeliveryVO.getOrderId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("订单ID不能为空");
                return resultObjectVO;
            }
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            SellerShopVO sellerShopVO = shopService.queryByShop(userMainId);
            OrderVO queryVO = new OrderVO();
            queryVO.setId(orderExpressDeliveryVO.getOrderId());
            queryVO.setShopId(sellerShopVO.getId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryVO);
            resultObjectVO = feignOrderService.findById(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                OrderVO orderVO = resultObjectVO.formatData(OrderVO.class);
                orderExpressDeliveryVO.setBuyerUserMainId(Long.parseLong(orderVO.getUserId()));
                orderExpressDeliveryVO.setSellerUserMainId(sellerShopVO.getUserMainId());
                orderExpressDeliveryVO.setAppCode(toucan.getAppCode());
                resultObjectVO = feignOrderExpressDeliveryService.saveOrUpdate(RequestJsonVOGenerator.generator(toucan.getAppCode(), orderExpressDeliveryVO));
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 查询快递公司列表
     * @param request
     * @return
     */
    @UserAuth
    @RequestMapping(value="/queryCompanyList",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryCompanyList(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            List<ExpressCompanyVO> expressCompanyVOS = new LinkedList<>();
            ExpressCompanyEnum[] expressCompanyEnums = ExpressCompanyEnum.values();
            for(ExpressCompanyEnum expressCompanyEnum:expressCompanyEnums){
                ExpressCompanyVO expressCompanyVO=new ExpressCompanyVO();
                expressCompanyVO.setCode(expressCompanyEnum.getCode());
                expressCompanyVO.setName(expressCompanyEnum.getName());
                expressCompanyVOS.add(expressCompanyVO);
            }
            resultObjectVO.setData(expressCompanyVOS);
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}
