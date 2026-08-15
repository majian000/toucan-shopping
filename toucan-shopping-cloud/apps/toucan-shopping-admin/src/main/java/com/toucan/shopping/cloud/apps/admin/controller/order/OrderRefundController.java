package com.toucan.shopping.cloud.apps.admin.controller.order;


import com.toucan.shopping.cloud.order.api.OrderRefundServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.order.page.OrderRefundPageInfo;
import com.toucan.shopping.modules.order.vo.OrderRefundVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 订单退款流水
 */
@RestController
@RequestMapping("/order/orderRefund")
public class OrderRefundController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private OrderRefundServiceAPI orderRefundService;


    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:order:refund:list"})
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public TableVO list(@RequestBody OrderRefundPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            if(pageInfo==null)
            {
                pageInfo = new OrderRefundPageInfo();
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            ResultPageInfoVO<OrderRefundVO> resultPageInfoVO = orderRefundService.queryListPage(requestJsonVO);
            if(resultPageInfoVO.isSuccess()) {
                PageInfo orderRefundPageInfo = resultPageInfoVO.getData();
                tableVO.setCount(orderRefundPageInfo.getTotal()!=null?orderRefundPageInfo.getTotal():0);
                List<OrderRefundVO> orderRefunds = orderRefundPageInfo.getList();
                tableVO.setData(orderRefunds);
            }

        }catch(Exception e)
        {
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }



}
