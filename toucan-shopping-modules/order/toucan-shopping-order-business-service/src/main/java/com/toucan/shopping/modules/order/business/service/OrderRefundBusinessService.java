package com.toucan.shopping.modules.order.business.service;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.page.OrderRefundPageInfo;
import com.toucan.shopping.modules.order.service.OrderRefundService;
import com.toucan.shopping.modules.order.vo.OrderRefundVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderRefundBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderRefundService orderRefundService;

    /**
     * 查询列表页
     */
    public ResultPageInfoVO<OrderRefundVO> queryListPage(RequestJsonVO requestJsonVO) {
        ResultPageInfoVO resultPageInfoVO = new ResultPageInfoVO();
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {
            try {
                OrderRefundPageInfo orderRefundPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), OrderRefundPageInfo.class);
                resultPageInfoVO.setData(orderRefundService.queryOrderRefundListPage(orderRefundPageInfo));
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
