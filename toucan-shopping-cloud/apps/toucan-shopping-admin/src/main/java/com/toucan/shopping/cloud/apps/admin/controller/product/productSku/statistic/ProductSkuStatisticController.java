package com.toucan.shopping.cloud.apps.admin.controller.product.productSku.statistic;


import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuStatisticService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserStatisticService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.vo.CategoryProductSkuStatisticVO;
import com.toucan.shopping.modules.product.vo.ProductSkuStatisticVO;
import com.toucan.shopping.modules.product.vo.ProductSkuStatusVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * 商品SKU统计
 * @author majian
 */
@Controller
@RequestMapping("/productSkuStatistic")
public class ProductSkuStatisticController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;


    @Autowired
    private Toucan toucan;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignProductSkuStatisticService feignProductSkuStatisticService;

    @Autowired
    private FeignCategoryService feignCategoryService;

    /**
     * 查询统计数据
     * 总数 今日新增 本月新增 本年新增
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/queryTotalAndTodayAndCurrentMonthAndCurrentYear",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear()
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),null);
            resultObjectVO = feignProductSkuStatisticService.queryTotalAndTodayAndCurrentMonthAndCurrentYear(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 分类下商品统计
     * 总数 今日新增 本月新增 本年新增
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/queryCategoryProductStatistic",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryCategoryProductStatistic(@RequestBody ProductSkuStatisticVO query)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),query);
            resultObjectVO = feignProductSkuStatisticService.queryCategoryProductStatistic(requestVo);
            if(resultObjectVO.isSuccess())
            {
                List<ProductSkuStatisticVO> productSkuStatusVOList = resultObjectVO.formatDataList(ProductSkuStatisticVO.class);
                List<CategoryProductSkuStatisticVO> categoryProductSkuStatistics = new LinkedList();
                requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),null);
                resultObjectVO = feignCategoryService.queryAllList(requestVo);
                if(resultObjectVO.isSuccess())
                {
                    List<CategoryVO> categorys = resultObjectVO.formatDataList(CategoryVO.class);
                    if(CollectionUtils.isNotEmpty(categorys))
                    {
                        boolean productSkuStatisticIsEmpty=CollectionUtils.isEmpty(productSkuStatusVOList);
                        for(CategoryVO categoryVO:categorys)
                        {
                            CategoryProductSkuStatisticVO categoryProductSkuStatisticVO = new CategoryProductSkuStatisticVO();
                            categoryProductSkuStatisticVO.setCategoryId(categoryVO.getId());
                            categoryProductSkuStatisticVO.setParentCategoryId(categoryVO.getParentId());
                            categoryProductSkuStatisticVO.setCategoryName(categoryVO.getName());
                            if(!productSkuStatisticIsEmpty) {
                                for (ProductSkuStatisticVO productSkuStatisticVO : productSkuStatusVOList) {
                                    if(categoryProductSkuStatisticVO.getCategoryId().longValue()==productSkuStatisticVO.getCategoryId().longValue())
                                    {
                                        categoryProductSkuStatisticVO.setCount(productSkuStatisticVO.getTotal());
                                    }
                                }
                            }
                        }
                    }
                }
                resultObjectVO.setData(categoryProductSkuStatistics);
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * SKU统计列表
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/productSkuStatistic/listPage",feignFunctionService);
        return "pages/product/productSku/statistic/statistic_list.html";
    }

}

