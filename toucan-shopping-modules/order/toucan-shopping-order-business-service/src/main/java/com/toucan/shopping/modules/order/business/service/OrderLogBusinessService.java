package com.toucan.shopping.modules.order.business.service;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.page.OrderLogPageInfo;
import com.toucan.shopping.modules.order.service.OrderItemService;
import com.toucan.shopping.modules.order.service.OrderLogService;
import com.toucan.shopping.modules.order.vo.OrderLogVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderLogBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderLogService orderLogService;

    /**
     * 查询列表页
     */
    public ResultPageInfoVO<OrderLogVO> queryListPage(RequestJsonVO requestJsonVO) {
        ResultPageInfoVO resultPageInfoVO = new ResultPageInfoVO();
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {
            try {
                OrderLogPageInfo orderLogPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), OrderLogPageInfo.class);
                if(StringUtils.isEmpty(orderLogPageInfo.getOrderNo())){
                    resultPageInfoVO.setCode(ResultObjectVO.FAILD);
                    resultPageInfoVO.setMsg("订单编号不能为空");
                    return resultPageInfoVO;
                }
                resultPageInfoVO.setData(orderLogService.queryOrderListPage(orderLogPageInfo));
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
                resultPageInfoVO.setCode(ResultObjectVO.FAILD);
                resultPageInfoVO.setMsg("请求失败");
            }
        }
        return resultPageInfoVO;
    }
}
