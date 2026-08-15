package com.toucan.shopping.modules.order.business.service;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.page.OrderPayPageInfo;
import com.toucan.shopping.modules.order.service.OrderPayService;
import com.toucan.shopping.modules.order.vo.OrderPayVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderPayBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderPayService orderPayService;

    /**
     * 查询列表页
     */
    public ResultPageInfoVO<OrderPayVO> queryListPage(RequestJsonVO requestJsonVO) {
        ResultPageInfoVO resultPageInfoVO = new ResultPageInfoVO();
        if(requestJsonVO!=null&& StringUtils.isNotEmpty(requestJsonVO.getEntityJson())) {
            try {
                OrderPayPageInfo orderPayPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), OrderPayPageInfo.class);
                resultPageInfoVO.setData(orderPayService.queryOrderPayListPage(orderPayPageInfo));
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
