package com.toucan.shopping.cloud.apps.admin.controller.product.statistic.productSku;


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
    @RequestMapping(value = "/queryProductSkuStatistic",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryProductSkuStatistic(@RequestBody ProductSkuStatisticVO productSkuStatisticVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = null;
            List<CategoryVO> categorys = null;
            List<ProductSkuStatisticVO> productCategoryStatistics = new LinkedList<>();
            if(productSkuStatisticVO.getCategoryId()!=null&&productSkuStatisticVO.getCategoryId().longValue()!=-1) {
                //查询分类以及子分类
                CategoryVO categoryVO = new CategoryVO();
                categoryVO.setId(productSkuStatisticVO.getCategoryId());
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categoryVO);
                resultObjectVO = feignCategoryService.queryChildListByPid(requestJsonVO);
                if (resultObjectVO.isSuccess()) {
                    if (resultObjectVO.getData() != null) {
                        categorys = resultObjectVO.formatDataList(CategoryVO.class);
                        if (CollectionUtils.isNotEmpty(categorys)) {
                            List<Long> categoryIdList = new LinkedList<>();
                            for (CategoryVO cv : categorys) {
                                categoryIdList.add(cv.getId());
                            }
                            categoryIdList.add(productSkuStatisticVO.getCategoryId());
                            productSkuStatisticVO.setCategoryIdList(categoryIdList);
                            productSkuStatisticVO.setCategoryId(null);
                        }
                    }
                }

                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkuStatisticVO);
                resultObjectVO = feignProductSkuStatisticService.queryCategoryProductStatistic(requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    List<ProductSkuStatisticVO> productSkuStatistics = resultObjectVO.formatDataList(ProductSkuStatisticVO.class);
                    if(CollectionUtils.isNotEmpty(productSkuStatistics))
                    {
                        for(CategoryVO cv:categorys)
                        {
                            ProductSkuStatisticVO productCategoryStatistic = new ProductSkuStatisticVO();
                            productCategoryStatistic.setCategoryName(cv.getName());
                            productCategoryStatistic.setCount(0L);
                            for(ProductSkuStatisticVO pss:productSkuStatistics)
                            {
                                if(cv.getId().equals(pss.getCategoryId())) {
                                    productCategoryStatistic.setCount(pss.getCount());
                                    break;
                                }
                            }
                            productCategoryStatistics.add(productCategoryStatistic);
                        }
                    }
                }
            }
            resultObjectVO.setData(productCategoryStatistics);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请稍后重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 分类SKU统计列表
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/categoryStatisticPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/productSkuStatistic/categoryStatisticPage",feignFunctionService);
        return "pages/product/statistic/productSku/statistic_list.html";
    }





}

