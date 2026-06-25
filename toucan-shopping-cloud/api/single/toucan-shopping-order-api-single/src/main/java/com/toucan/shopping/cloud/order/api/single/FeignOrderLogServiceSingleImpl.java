package com.toucan.shopping.cloud.order.api.single;

import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderLogService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
import com.toucan.shopping.modules.order.vo.OrderLogVO;
import org.springframework.stereotype.Service;

@Service
public class FeignOrderLogServiceSingleImpl implements FeignOrderLogService {

    @Override
    public ResultPageInfoVO<OrderLogVO> queryListPage(RequestJsonVO requestJsonVO) {
        ResultPageInfoVO<OrderLogVO> resultObjectVO = new ResultPageInfoVO<OrderLogVO>();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("请稍后重试!");
        return resultObjectVO;
    }
}
