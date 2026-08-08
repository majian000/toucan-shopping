package com.toucan.shopping.cloud.apps.admin.controller.order;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.order.api.OrderServiceAPI;
import com.toucan.shopping.cloud.product.api.ProductSkuServiceAPI;
import com.toucan.shopping.cloud.stock.api.ProductSkuStockLockServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.order.constant.OrderConstant;
import com.toucan.shopping.modules.order.page.OrderPageInfo;
import com.toucan.shopping.modules.order.vo.OrderVO;
import com.toucan.shopping.modules.product.vo.InventoryReductionVO;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 订单列表
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private OrderServiceAPI orderService;

    @Autowired
    private ProductSkuServiceAPI productSkuService;

    @Autowired
    private ProductSkuStockLockServiceAPI productSkuStockLockService;



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:order:list:api"})
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public TableVO list(@RequestBody OrderPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            if(pageInfo==null)
            {
                pageInfo = new OrderPageInfo();
            }

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            ResultObjectVO resultObjectVO = orderService.queryListPage(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    Map<String, Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
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
     * 取消
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:order:cancel:api"})
    @RequestMapping(value = "/cancel",method = RequestMethod.POST)
    public ResultObjectVO cancel(@RequestBody OrderVO orderVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockVO productSkuStockLockVO = new ProductSkuStockLockVO();
            productSkuStockLockVO.setOrderNo(orderVO.getOrderNo());
            productSkuStockLockVO.setType((short)1); //下单扣库存,付款扣库存不需要处理(因为付款扣库存是在完成订单的时候扣库存)
            resultObjectVO = productSkuStockLockService.findLockStockNumByOrderNo(RequestJsonVOGenerator.generator(toucan.getAppCode(),productSkuStockLockVO));
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
                        resultObjectVO = productSkuService.restoreStock(RequestJsonVOGenerator.generator(toucan.getAppCode(), inventoryReductions));
                    }
                }
                resultObjectVO = productSkuStockLockService.deleteLockStockByOrderNo(RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkuStockLockVO));
                if(resultObjectVO.isSuccess())
                {
                    resultObjectVO = orderService.cancel(RequestJsonVOGenerator.generator(toucan.getAppCode(), orderVO));
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:order:update:api"})
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResultObjectVO update(@RequestBody OrderVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            //已取消
            if(entity.getTradeStatus().intValue()== OrderConstant.TRADE_STATUS_CALCEL)
            {
                OrderVO query = new OrderVO();
                query.setId(entity.getId());
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
                ResultObjectVO queryOrderResultObjectVO = orderService.findById(requestJsonVO);
                if(queryOrderResultObjectVO.isSuccess()) {
                    OrderVO orderVO = queryOrderResultObjectVO.formatData(OrderVO.class);
                    if(orderVO!=null) {
                        ProductSkuStockLockVO productSkuStockLockVO = new ProductSkuStockLockVO();
                        productSkuStockLockVO.setOrderNo(orderVO.getOrderNo());
                        productSkuStockLockVO.setType((short) 1); //下单扣库存,付款扣库存不需要处理(因为付款扣库存是在完成订单的时候扣库存)
                        resultObjectVO = productSkuStockLockService.findLockStockNumByOrderNo(RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkuStockLockVO));
                        if (resultObjectVO.isSuccess()) {
                            List<ProductSkuStockLockVO> productSkuStockLocks = resultObjectVO.formatDataList(ProductSkuStockLockVO.class);
                            if (!CollectionUtils.isEmpty(productSkuStockLocks)) {
                                List<InventoryReductionVO> inventoryReductions = new LinkedList<>();
                                for (ProductSkuStockLockVO pssl : productSkuStockLocks) {
                                    //如果当前库存锁定是未还原状态
                                    if(pssl.getRestoreStatus().intValue()==0) {
                                        InventoryReductionVO inventoryReductionVO = new InventoryReductionVO();
                                        inventoryReductionVO.setProductSkuId(pssl.getProductSkuId());
                                        inventoryReductionVO.setStockNum(pssl.getStockNum());
                                        inventoryReductions.add(inventoryReductionVO);
                                    }
                                }
                                if (!CollectionUtils.isEmpty(inventoryReductions)) {
                                    resultObjectVO = productSkuService.restoreStock(RequestJsonVOGenerator.generator(toucan.getAppCode(), inventoryReductions));
                                }
                            }
                            resultObjectVO = productSkuStockLockService.deleteLockStockByOrderNo(RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkuStockLockVO));
                        }
                    }
                }
            }
            entity.setOperateUserId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = orderService.update(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("修改失败,请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}
