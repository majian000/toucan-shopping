package com.toucan.shopping.cloud.apps.seller.web.controller.waitDeliveryOrder;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.common.data.api.AreaServiceAPI;
import com.toucan.shopping.cloud.order.api.OrderServiceAPI;
import com.toucan.shopping.cloud.product.api.ShopProductApproveServiceAPI;
import com.toucan.shopping.cloud.product.api.ShopProductServiceAPI;
import com.toucan.shopping.cloud.seller.api.SellerShopServiceAPI;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.order.constant.OrderConstant;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;


/**
 * 待发货订单
 */
@Controller("waitDeliveryOrderApiController")
@RequestMapping("/api/waitDeliveryOrder")
public class WaitDeliveryOrderApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private OrderServiceAPI orderService;

    @Autowired
    private SellerShopServiceAPI sellerShopService;

    @Autowired
    private ShopProductApproveServiceAPI shopProductApproveService;

    @Autowired
    private ShopProductServiceAPI shopProductService;

    @Autowired
    private AreaServiceAPI areaService;


    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO list(HttpServletRequest httpServletRequest,@RequestBody OrderPageInfo pageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            if(pageInfo==null)
            {
                pageInfo = new OrderPageInfo();
            }
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if(StringUtils.isEmpty(userMainId))
            {
                logger.warn("查询订单列表 没有找到用户ID {} ",userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,用户ID不能为空");
                return resultObjectVO;
            }
            if(StringUtils.isNotEmpty(pageInfo.getSortBy()))
            {
                String sortBy = pageInfo.getSortBy().toLowerCase();
                if(!"asc".equals(sortBy)&&!"desc".equals(sortBy)){
                    pageInfo.setSortBy(null);
                }
            }
            if(StringUtils.isNotEmpty(pageInfo.getSortColumn()))
            {
                if("orderAmount".equals(pageInfo.getSortColumn())){
                    pageInfo.setSortColumn("order_amount");
                }
                if("payAmount".equals(pageInfo.getSortColumn())){
                    pageInfo.setSortColumn("pay_amount");
                }
            }
            if(StringUtils.isNotEmpty(pageInfo.getStartCreateDateYMDHS())){
                pageInfo.setStartCreateDate(DateUtils.FORMATTER_SS.get().parse(pageInfo.getStartCreateDateYMDHS()+":00"));
            }
            if(StringUtils.isNotEmpty(pageInfo.getEndCreateDateYMDHS())){
                pageInfo.setEndCreateDate(DateUtils.FORMATTER_SS.get().parse(pageInfo.getEndCreateDateYMDHS()+":59"));
            }
            SellerShopVO  sellerShopVO = this.queryByShop(userMainId);
            pageInfo.setShopId(String.valueOf(sellerShopVO.getId()));
            pageInfo.setTradeStatus(OrderConstant.TRADE_STATUS_WAIT_DELIVERY);
            if(pageInfo.getShopId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,没有找到店铺");
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), pageInfo);
            resultObjectVO = orderService.queryListPage(requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }

        return resultObjectVO;
    }





    /**
     * 根据ID查询
     * @return
     */
    @UserAuth
    @RequestMapping(value="/findById",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findById(HttpServletRequest request,@RequestBody OrderVO orderVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try {
            if(orderVO.getId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("ID不能为空");
                return resultObjectVO;
            }
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));

            SellerShopVO  sellerShopVO = this.queryByShop(userMainId);
            OrderVO queryVO = new OrderVO();
            queryVO.setId(orderVO.getId());
            queryVO.setShopId(sellerShopVO.getId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryVO);
            resultObjectVO = orderService.findById(requestJsonVO);

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    private SellerShopVO queryByShop(String userMainId) throws Exception
    {
        SellerShop querySellerShop = new SellerShop();
        querySellerShop.setUserMainId(Long.parseLong(userMainId));
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
        ResultObjectVO resultObjectVO = sellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
        if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null) {
            SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
            return sellerShopVO;
        }
        return null;
    }


}
