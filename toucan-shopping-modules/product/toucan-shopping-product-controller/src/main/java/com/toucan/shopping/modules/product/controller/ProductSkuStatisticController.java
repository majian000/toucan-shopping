package com.toucan.shopping.modules.product.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultListVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.constant.ProductConstant;
import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.entity.ShopProductDescription;
import com.toucan.shopping.modules.product.entity.ShopProductImg;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.redis.ShopProductRedisLockKey;
import com.toucan.shopping.modules.product.service.*;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productSkuStatistic")
public class ProductSkuStatisticController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductSkuStatisticService productSkuStatisticService;




    @RequestMapping(value = "/queryTotalAndTodayAndCurrentMonthAndCurrentYear",method = RequestMethod.POST)
    @ResponseBody
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



    @RequestMapping(value = "/queryCategoryStatistic",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryCategoryStatistic(RequestJsonVO requestVo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStatisticVO query = new ProductSkuStatisticVO();
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
