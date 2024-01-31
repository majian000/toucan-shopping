package com.toucan.shopping.modules.order.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.order.service.OrderStatisticService;
import com.toucan.shopping.modules.product.vo.ProductSkuStatisticVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderStatistic")
public class OrderStatisticController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderStatisticService orderStatisticService;


    /**
     * 总金额
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/queryTotalAndTodayAndCurrentMonthAndCurrentYear",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(@RequestBody RequestJsonVO requestVo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            resultObjectVO.setData(orderStatisticService.queryTotalAndTodayAndCurrentMonthAndCurrentYear());
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




}
