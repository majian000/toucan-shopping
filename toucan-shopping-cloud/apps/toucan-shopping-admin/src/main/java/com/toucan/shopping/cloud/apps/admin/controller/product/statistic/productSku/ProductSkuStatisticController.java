package com.toucan.shopping.cloud.apps.admin.controller.product.statistic.productSku;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.cloud.order.api.OrderStatisticServiceAPI;
import com.toucan.shopping.cloud.product.api.ProductSkuServiceAPI;
import com.toucan.shopping.cloud.product.api.ProductSkuStatisticServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.order.page.OrderHotSellPageInfo;
import com.toucan.shopping.modules.order.vo.OrderHotSellStatisticVO;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.vo.ProductSkuStatisticVO;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 商品SKU统计
 * @author majian
 */
@RestController
@RequestMapping("/productSkuStatistic")
public class ProductSkuStatisticController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;


    @Autowired
    private Toucan toucan;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private ProductSkuStatisticServiceAPI productSkuStatisticService;

    @Autowired
    private CategoryServiceAPI categoryService;

    @Autowired
    private OrderStatisticServiceAPI orderStatisticService;

    @Autowired
    private ProductSkuServiceAPI productSkuService;

    /**
     * 查询统计数据
     * 总数 今日新增 本月新增 本年新增
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:dashboard:productSkuStatisticPanel"})
    @RequestMapping(value = "/queryTotalAndTodayAndCurrentMonthAndCurrentYear",method = RequestMethod.POST)
    public ResultObjectVO queryTotalAndTodayAndCurrentMonthAndCurrentYear()
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),null);
            resultObjectVO = productSkuStatisticService.queryTotalAndTodayAndCurrentMonthAndCurrentYear(requestVo);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:statistic:product:statistic"})
    @RequestMapping(value = "/queryProductSkuStatistic",method = RequestMethod.POST)
    public ResultObjectVO queryProductSkuStatistic(@RequestBody ProductSkuStatisticVO productSkuStatisticVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = null;
            List<CategoryVO> categorys = null;
            List<ProductSkuStatisticVO> productCategoryStatistics = new LinkedList<>();
            List<Long> categoryIdList = new LinkedList<>();
            Long categoryId = productSkuStatisticVO.getCategoryId();
            if(productSkuStatisticVO.getCategoryId()!=null&&productSkuStatisticVO.getCategoryId().longValue()!=-1) {
                //查询分类以及子分类
                CategoryVO categoryVO = new CategoryVO();
                categoryVO.setId(productSkuStatisticVO.getCategoryId());
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categoryVO);
                resultObjectVO = categoryService.queryChildListByPid(requestJsonVO);
                if (resultObjectVO.isSuccess()) {
                    if (resultObjectVO.getData() != null) {
                        categorys = resultObjectVO.formatDataList(CategoryVO.class);
                        if (CollectionUtils.isNotEmpty(categorys)) {
                            for (CategoryVO cv : categorys) {
                                categoryIdList.add(cv.getId());
                            }
                        }
                    }
                }
                categoryIdList.add(productSkuStatisticVO.getCategoryId());
                productSkuStatisticVO.setCategoryIdList(categoryIdList);
                productSkuStatisticVO.setCategoryId(null);

                //查询当前分类
                CategoryVO queryCategoryVO = new CategoryVO();
                queryCategoryVO.setId(categoryId);
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategoryVO);
                resultObjectVO = categoryService.queryById(requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    categorys.add(resultObjectVO.formatData(CategoryVO.class));
                }

                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), productSkuStatisticVO);
                resultObjectVO = productSkuStatisticService.queryCategoryProductStatistic(requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    List<ProductSkuStatisticVO> productSkuStatistics = resultObjectVO.formatDataList(ProductSkuStatisticVO.class);

                    if(CollectionUtils.isNotEmpty(productSkuStatistics)&&CollectionUtils.isNotEmpty(categorys))
                    {
                        for (ProductSkuStatisticVO psv : productSkuStatistics) {
                            for(CategoryVO cv:categorys) {
                                if (psv.getCategoryId().longValue() == cv.getId().longValue()) {
                                    psv.setParentCategoryId(cv.getParentId());
                                    break;
                                }
                            }
                        }
                    }

                    //查询出当前节点
                    for(CategoryVO cv:categorys)
                    {
                        //查询出所有下一级节点
                        if(cv.getId().longValue()==categoryId.longValue()) {
                            ProductSkuStatisticVO productCategoryStatistic = new ProductSkuStatisticVO();
                            productCategoryStatistic.setCategoryName(cv.getName());
                            productCategoryStatistic.setCategoryId(cv.getId());
                            productCategoryStatistic.setCount(0L);
                            for(ProductSkuStatisticVO pssvo:productSkuStatistics)
                            {
                                if(productCategoryStatistic.getCategoryId().longValue()==pssvo.getCategoryId().longValue())
                                {
                                    productCategoryStatistic.setCount(pssvo.getCount());
                                    break;
                                }
                            }
                            productCategoryStatistics.add(productCategoryStatistic);
                            addProductCategoryStatistisCount(productSkuStatistics,productCategoryStatistic,productCategoryStatistic.getCategoryId());
                        }
                    }

                    //查询出所有下一级节点
                    for(CategoryVO cv:categorys)
                    {
                        if(cv.getParentId().longValue()==categoryId.longValue()) {
                            ProductSkuStatisticVO productCategoryStatistic = new ProductSkuStatisticVO();
                            productCategoryStatistic.setCategoryName(cv.getName());
                            productCategoryStatistic.setCategoryId(cv.getId());
                            productCategoryStatistic.setCount(0L);
                            for(ProductSkuStatisticVO pssvo:productSkuStatistics)
                            {
                                if(productCategoryStatistic.getCategoryId().longValue()==pssvo.getCategoryId().longValue())
                                {
                                    productCategoryStatistic.setCount(pssvo.getCount());
                                    break;
                                }
                            }
                            productCategoryStatistics.add(productCategoryStatistic);
                            addProductCategoryStatistisCount(productSkuStatistics,productCategoryStatistic,productCategoryStatistic.getCategoryId());
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
     * 合并商品分类统计
     * @param allProductCategoryStatistics
     * @param productCategoryStatistic
     * @param parentCategoryId
     */
    private void addProductCategoryStatistisCount(List<ProductSkuStatisticVO> allProductCategoryStatistics,ProductSkuStatisticVO productCategoryStatistic,Long parentCategoryId){
        if(CollectionUtils.isNotEmpty(allProductCategoryStatistics))
        {
            for(ProductSkuStatisticVO psv:allProductCategoryStatistics)
            {
                if(psv.getParentCategoryId().longValue()==parentCategoryId.longValue()) {
                    productCategoryStatistic.setCount(productCategoryStatistic.getCount().longValue()+psv.getCount().longValue());
                    this.addProductCategoryStatistisCount(allProductCategoryStatistics,productCategoryStatistic,psv.getCategoryId());
                }
            }
        }
    }




    /**
     * 商品热卖列表
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:statistic:product:hot:sell:statistic"})
    @RequestMapping(value = "/queryHotSellListPage",method = RequestMethod.POST)
    public TableVO queryHotSellListPage(OrderHotSellPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            //先查询热卖的订单类型
            ResultObjectVO resultObjectVO = orderStatisticService.queryHotSellListPage(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null)
                {

                    Map<String, Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total") != null ? resultObjectDataMap.get("total") : "0")));
                    List<OrderHotSellStatisticVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), OrderHotSellStatisticVO.class);
                    if(tableVO.getCount()>0) {
                        List<ProductSkuVO> productSkus = new LinkedList<ProductSkuVO>();
                        for (OrderHotSellStatisticVO orderHotSellStatisticVO : list) {
                            ProductSkuVO productSkuVO = new ProductSkuVO();
                            productSkuVO.setId(orderHotSellStatisticVO.getSkuId());
                            productSkus.add(productSkuVO);
                        }
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),productSkus);
                        ResultObjectVO productResultObjectVO = productSkuService.queryByIdList(requestJsonVO);
                        if(productResultObjectVO.isSuccess()) {
                            List<ProductSku> productSkuList = productResultObjectVO.formatDataList(ProductSku.class);

                            for (OrderHotSellStatisticVO orderHotSellStatisticVO : list) {
                                for(ProductSku productSku:productSkuList)
                                {
                                    if(orderHotSellStatisticVO.getSkuId()!=null
                                            &&orderHotSellStatisticVO.getSkuId().longValue()==productSku.getId().longValue())
                                    {
                                        orderHotSellStatisticVO.setProductName(productSku.getName());
                                        orderHotSellStatisticVO.setShopId(productSku.getShopId());
                                    }
                                }
                            }

                        }
                        tableVO.setData(list);
                    }
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }
}
