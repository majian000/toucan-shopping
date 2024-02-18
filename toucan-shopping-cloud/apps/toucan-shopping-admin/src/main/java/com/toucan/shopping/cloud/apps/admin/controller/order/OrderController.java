package com.toucan.shopping.cloud.apps.admin.controller.order;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignBannerAreaService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignBannerService;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.cloud.stock.api.feign.service.FeignProductSkuStockLockService;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.area.vo.AreaTreeVO;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.content.entity.Banner;
import com.toucan.shopping.modules.content.entity.BannerArea;
import com.toucan.shopping.modules.content.page.BannerPageInfo;
import com.toucan.shopping.modules.content.vo.BannerAreaVO;
import com.toucan.shopping.modules.content.vo.BannerVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.order.constant.OrderConstant;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.product.vo.InventoryReductionVO;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单列表
 */
@Controller
@RequestMapping("/order")
public class OrderController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignOrderService feignOrderService;

    @Autowired
    private FeignProductSkuService feignProductSkuService;

    @Autowired
    private FeignProductSkuStockLockService feignProductSkuStockLockService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/order/listPage",feignFunctionService);
        return "pages/order/list.html";
    }



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, OrderPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            if(pageInfo==null)
            {
                pageInfo = new OrderPageInfo();
            }

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            ResultObjectVO resultObjectVO = feignOrderService.queryListPage(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    Map<String, Object> resultObjectDataMap = (Map<String, Object>) resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total") != null ? resultObjectDataMap.get("total") : "0")));
                    List<OrderVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), OrderVO.class);
                    tableVO.setData((List)list);
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }





    /**
     * 查看
     * @param request
     * @param id
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/detailPage/{id}",method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            OrderVO query = new OrderVO();
            query.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
            ResultObjectVO resultObjectVO = feignOrderService.findById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                OrderVO orderVO = resultObjectVO.formatData(OrderVO.class);
                request.setAttribute("model",orderVO);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/order/detail.html";
    }



    /**
     * 修改
     * @param request
     * @param id
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            OrderVO query = new OrderVO();
            query.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
            ResultObjectVO resultObjectVO = feignOrderService.findById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                OrderVO orderVO = resultObjectVO.formatData(OrderVO.class);
                request.setAttribute("model",orderVO);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/order/edit.html";
    }


    /**
     * 取消
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/cancel",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO cancel(@RequestBody OrderVO orderVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockVO productSkuStockLockVO = new ProductSkuStockLockVO();
            productSkuStockLockVO.setOrderNo(orderVO.getOrderNo());
            productSkuStockLockVO.setType((short)1); //下单扣库存,付款扣库存不需要处理(因为付款扣库存是在完成订单的时候扣库存)
            resultObjectVO = feignProductSkuStockLockService.findLockStockNumByOrderNo(RequestJsonVOGenerator.generator(toucan.getAppCode(),productSkuStockLockVO));
            if(resultObjectVO.isSuccess())
            {
                List<ProductSkuStockLockVO> productSkuStockLocks = resultObjectVO.formatDataList(ProductSkuStockLockVO.class);
                if(!CollectionUtils.isEmpty(productSkuStockLocks))
                {
                    List<InventoryReductionVO> inventoryReductions= new LinkedList<>();
                    for(ProductSkuStockLockVO pssl:productSkuStockLocks)
                    {
                        InventoryReductionVO inventoryReductionVO = new InventoryReductionVO();
                        inventoryReductionVO.setProductSkuId(pssl.getProductSkuId());
                        inventoryReductionVO.setStockNum(pssl.getStockNum());
                        inventoryReductions.add(inventoryReductionVO);
                    }
                    if(!CollectionUtils.isEmpty(inventoryReductions)) {
                        //保存还原锁定库存事件
                        resultObjectVO = feignProductSkuService.restoreStock(RequestJsonVOGenerator.generator(toucan.getAppCode(), inventoryReductions));
                    }
                }
                resultObjectVO = feignProductSkuStockLockService.deleteLockStockByOrderNo(RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkuStockLockVO));
                if(resultObjectVO.isSuccess())
                {
                    resultObjectVO = feignOrderService.cancel(RequestJsonVOGenerator.generator(toucan.getAppCode(), orderVO));
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request, @RequestBody OrderVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            //已取消
            if(entity.getTradeStatus().intValue()== OrderConstant.TRADE_STATUS_CALCEL)
            {
                OrderVO query = new OrderVO();
                query.setId(entity.getId());
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
                ResultObjectVO queryOrderResultObjectVO = feignOrderService.findById(requestJsonVO);
                if(queryOrderResultObjectVO.isSuccess()) {
                    OrderVO orderVO = queryOrderResultObjectVO.formatData(OrderVO.class);
                    if(orderVO!=null) {
                        ProductSkuStockLockVO productSkuStockLockVO = new ProductSkuStockLockVO();
                        productSkuStockLockVO.setOrderNo(orderVO.getOrderNo());
                        productSkuStockLockVO.setType((short) 1); //下单扣库存,付款扣库存不需要处理(因为付款扣库存是在完成订单的时候扣库存)
                        resultObjectVO = feignProductSkuStockLockService.findLockStockNumByOrderNo(RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkuStockLockVO));
                        if (resultObjectVO.isSuccess()) {
                            List<ProductSkuStockLockVO> productSkuStockLocks = resultObjectVO.formatDataList(ProductSkuStockLockVO.class);
                            if (!CollectionUtils.isEmpty(productSkuStockLocks)) {
                                List<InventoryReductionVO> inventoryReductions = new LinkedList<>();
                                for (ProductSkuStockLockVO pssl : productSkuStockLocks) {
                                    InventoryReductionVO inventoryReductionVO = new InventoryReductionVO();
                                    inventoryReductionVO.setProductSkuId(pssl.getProductSkuId());
                                    inventoryReductionVO.setStockNum(pssl.getStockNum());
                                    inventoryReductions.add(inventoryReductionVO);
                                }
                                if (!CollectionUtils.isEmpty(inventoryReductions)) {
                                    //保存还原锁定库存事件
                                    resultObjectVO = feignProductSkuService.restoreStock(RequestJsonVOGenerator.generator(toucan.getAppCode(), inventoryReductions));
                                }
                            }
                            resultObjectVO = feignProductSkuStockLockService.deleteLockStockByOrderNo(RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkuStockLockVO));
                        }
                    }
                }
            }
            entity.setOperateUserId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignOrderService.update(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("修改失败,请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 跳转到驳回页面
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/cancel/page/{orderNo}",method = RequestMethod.GET)
    public String rejectPage(HttpServletRequest request,@PathVariable String orderNo)
    {
        request.setAttribute("orderNo",orderNo);
        return "pages/order/cancel.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/orderItemListPage/{orderId}",method = RequestMethod.GET)
    public String spuListPage(HttpServletRequest request,@PathVariable String orderId)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/order/orderItemListPage",feignFunctionService);

        request.setAttribute("orderId",orderId);
        return "pages/order/modify_order_item_list.html";
    }


}

