package com.toucan.shopping.modules.product.business.service;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.service.ProductSkuStatisticService;
import com.toucan.shopping.modules.product.vo.ProductSkuStatisticVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductSkuStatisticBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductSkuStatisticService productSkuStatisticService;


    public ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear(RequestJsonVO requestVo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            resultObjectVO.setData(productSkuStatisticService.queryTotalAndTodayAndCurrentMonthAndCurrentYear());
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 分类商品统计
     * @param requestVo
     * @return
     */
    public ResultObjectVO queryCategoryProductStatistic(RequestJsonVO requestVo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStatisticVO query = requestVo.formatEntity(ProductSkuStatisticVO.class);
            resultObjectVO.setData(productSkuStatisticService.queryCategoryStatistic(query));
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}
