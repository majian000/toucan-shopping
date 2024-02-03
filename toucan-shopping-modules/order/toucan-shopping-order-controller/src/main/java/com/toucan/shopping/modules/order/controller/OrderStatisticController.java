package com.toucan.shopping.modules.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.order.page.OrderHotSellPageInfo;
import com.toucan.shopping.modules.order.service.OrderStatisticService;
import com.toucan.shopping.modules.order.vo.OrderHotSellStatisticVO;
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




    /**
     * 查询热销列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/hot/sell/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryHotSellListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到对象: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到对象!");
            return resultObjectVO;
        }
        try {
            OrderHotSellPageInfo queryPageInfo = requestJsonVO.formatEntity(OrderHotSellPageInfo.class);
            PageInfo<OrderHotSellStatisticVO> pageInfo =  orderStatisticService.queryHotSellListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

}
