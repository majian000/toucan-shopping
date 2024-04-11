package com.toucan.shopping.cloud.apps.seller.web.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderItemService;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductApproveService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.vo.OrderItemVO;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.product.constant.ProductConstant;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.product.vo.ShopProductApproveVO;
import com.toucan.shopping.modules.product.vo.ShopProductVO;
import com.toucan.shopping.modules.seller.entity.SellerShop;
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
 * 订单
 * @author majian
 * @date 2024-3-16 15:05:49
 */
@Controller("orderApiController")
@RequestMapping("/api/order")
public class OrderApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignOrderService feignOrderService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private FeignShopProductApproveService feignShopProductApproveService;

    @Autowired
    private FeignShopProductService feignShopProductService;

    @Autowired
    private FeignAreaService feignAreaService;

    @Autowired
    private FeignOrderItemService feignOrderItemService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignCategoryService feignCategoryService;

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
            if(pageInfo.getShopId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,没有找到店铺");
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), pageInfo);
            resultObjectVO = feignOrderService.queryListPage(requestJsonVO);
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
            resultObjectVO = feignOrderService.findById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                orderVO = resultObjectVO.formatData(OrderVO.class);

                OrderItemVO queryOrderItemVO = new OrderItemVO();
                queryOrderItemVO.setOrderId(orderVO.getId());

                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryOrderItemVO);
                ResultObjectVO orderItemResultObjectVO = feignOrderItemService.queryAllListByOrderId(requestJsonVO);
                if(orderItemResultObjectVO.isSuccess()) {
                    if (orderItemResultObjectVO.getData() != null) {
                        List<OrderItemVO> orderItemVOList = orderItemResultObjectVO.formatDataList(OrderItemVO.class);
                        if(CollectionUtils.isNotEmpty(orderItemVOList)){
                            List<ProductSkuVO> orderItemProductSkuList = new LinkedList<>();
                            for(OrderItemVO orderItemVO:orderItemVOList){
                                if(StringUtils.isNotEmpty(orderItemVO.getProductPreviewPath())){
                                    orderItemVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+orderItemVO.getProductPreviewPath());
                                }
                                String productSkuJson = orderItemVO.getProductSkuJson();
                                if(StringUtils.isNotEmpty(productSkuJson)) {
                                    ProductSkuVO productSkuVO = JSONObject.parseObject(productSkuJson, ProductSkuVO.class);
                                    orderItemProductSkuList.add(productSkuVO);
                                }
                            }
                            Set<Long> categoryIdSet = new HashSet();
                            for(int i = 0; i < orderItemProductSkuList.size(); i++) {
                                ProductSkuVO productSkuVO = orderItemProductSkuList.get(i);

                                if(StringUtils.isNotEmpty(productSkuVO.getMainPhotoFilePath()))
                                {
                                    productSkuVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix() + productSkuVO.getProductPreviewPath());
                                }

                                //设置店铺分类ID
                                if (productSkuVO.getCategoryId() != null) {
                                    categoryIdSet.add(productSkuVO.getCategoryId());
                                }
                            }

                            this.queryCategory(orderItemProductSkuList,categoryIdSet.stream().toArray(Long[]::new));

                            orderVO.setOrderItemProductSkus(orderItemProductSkuList);
                        }
                        orderVO.setOrderItems(orderItemVOList);
                    }
                }

                resultObjectVO.setData(orderVO);

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
     * 查询类别信息
     *
     * @param list
     * @param categoryIds
     */
    void queryCategory(List<ProductSkuVO> list, Long[] categoryIds) {
        try {
            //查询类别名称
            CategoryVO queryCategoryVO = new CategoryVO();
            queryCategoryVO.setIdArray(categoryIds);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategoryVO);
            ResultObjectVO resultObjectVO = feignCategoryService.findByIdArray(requestJsonVO.sign(), requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<CategoryVO> categoryVOS = resultObjectVO.formatDataList(CategoryVO.class);
                if (CollectionUtils.isNotEmpty(categoryVOS)) {
                    for (ProductSkuVO productSkuVO : list) {
                        for (CategoryVO categoryVO : categoryVOS) {
                            if (productSkuVO.getCategoryId() != null && productSkuVO.getCategoryId().longValue() == categoryVO.getId().longValue()) {
                                productSkuVO.setCategoryName(categoryVO.getName());
                                productSkuVO.setCategoryPath(categoryVO.getNamePath());
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    private SellerShopVO queryByShop(String userMainId) throws Exception
    {
        SellerShop querySellerShop = new SellerShop();
        querySellerShop.setUserMainId(Long.parseLong(userMainId));
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
        ResultObjectVO resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
        if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null) {
            SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
            return sellerShopVO;
        }
        return null;
    }


}
