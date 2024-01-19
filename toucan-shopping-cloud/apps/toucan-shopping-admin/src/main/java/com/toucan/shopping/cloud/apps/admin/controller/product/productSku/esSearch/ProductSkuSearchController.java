package com.toucan.shopping.cloud.apps.admin.controller.product.productSku.esSearch;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.apps.admin.util.SearchUtils;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.message.api.feign.service.FeignMessageUserService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSpuService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.cloud.search.api.feign.service.FeignProductSearchService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import com.toucan.shopping.modules.common.persistence.event.enums.EventPublishTypeEnum;
import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.message.vo.MessageVO;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.vo.BrandVO;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.product.vo.ProductSpuVO;
import com.toucan.shopping.modules.product.vo.ShopProductVO;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.search.vo.ProductSearchVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 商品SKU es搜索管理
 * @author majian
 */
@Controller
@RequestMapping("/product/productSku/search")
public class ProductSkuSearchController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignCategoryService feignCategoryService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignProductSearchService feignProductSearchService;


    @Autowired
    private IdGenerator idGenerator;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/product/productSku/search/listPage",feignFunctionService);
        request.setAttribute("pcProductSkuPreviewPage",toucan.getShoppingPC().getBasePath()+toucan.getShoppingPC().getProductSkuDetailPage());
        return "pages/product/productSku/search/list.html";
    }



    /**
     * 查询列表
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, ProductSearchVO productSearchVO)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),productSearchVO);
            ResultObjectVO resultObjectVO = feignProductSearchService.search(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                PageInfo pageInfo = resultObjectVO.formatData(PageInfo.class);
                if(pageInfo.getTotal().longValue()>pageInfo.getMaxTotal().longValue())
                {
                    tableVO.setCount(pageInfo.getMaxTotal());
                }else {
                    tableVO.setCount(pageInfo.getTotal());
                }
                List<ProductSearchResultVO> list = pageInfo.formatDataList(ProductSearchResultVO.class);
                if(CollectionUtils.isNotEmpty(list))
                {
                    for(ProductSearchResultVO productSearchResultVO:list)
                    {
                        productSearchResultVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+productSearchResultVO.getProductPreviewPath());
                    }
                }
                if (tableVO.getCount() > 0) {
                    tableVO.setData((List) list);
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




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/query/category/tree/pid",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryCategoryTreeByParentId(@RequestParam Long id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(id==null)
            {
                id=-1L;
            }
            CategoryVO query = new CategoryVO();
            query.setParentId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            resultObjectVO = feignCategoryService.queryListByPid(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    List<CategoryTreeVO> categoryVOS = resultObjectVO.formatDataList(CategoryTreeVO.class);
                    for(CategoryTreeVO categoryTreeVO:categoryVOS)
                    {
                        categoryTreeVO.setOpen(false);
                        categoryTreeVO.setIcon(null);
                    }
                    resultObjectVO.setData(categoryVOS);
                }
            }
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 根据ID从缓存中删除
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/deleteById",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteById(HttpServletRequest request,@RequestBody ProductSearchResultVO productSearchResultVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(productSearchResultVO.getSkuId()==null)
            {
                resultObjectVO.setCode(TableVO.FAILD);
                resultObjectVO.setMsg("商品ID不能为空");
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),productSearchResultVO.getSkuId());
            resultObjectVO = feignProductSearchService.removeById(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("操作失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 查询类别信息
     * @param list
     * @param categoryIds
     */
    void queryCategory(List<ProductSearchResultVO> list,Long[] categoryIds)
    {
        try {
            //查询类别名称
            CategoryVO queryCategoryVO = new CategoryVO();
            queryCategoryVO.setIdArray(categoryIds);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategoryVO);
            ResultObjectVO resultObjectVO = feignCategoryService.findByIdArray(requestJsonVO.sign(), requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<CategoryVO> categoryVOS = resultObjectVO.formatDataList(CategoryVO.class);
                if (CollectionUtils.isNotEmpty(categoryVOS)) {
                    for (ProductSearchResultVO productSearchResultVO : list) {
                        for (CategoryVO categoryVO : categoryVOS) {
                            if (productSearchResultVO.getCategoryId() != null && productSearchResultVO.getCategoryId().longValue() == categoryVO.getId().longValue()) {
                                productSearchResultVO.setCategoryName(categoryVO.getName());
                                productSearchResultVO.setCategoryPath(categoryVO.getNamePath());
                                break;
                            }
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/detailPage/{id}",method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),id);
            ResultObjectVO resultObjectVO = feignProductSearchService.queryBySkuId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<ProductSearchResultVO> productSearchResultVOS = resultObjectVO.formatDataList(ProductSearchResultVO.class);
                if(CollectionUtils.isNotEmpty(productSearchResultVOS)) {
                    ProductSearchResultVO productSearchResultVO = productSearchResultVOS.get(0);
                    if (ObjectUtils.isNotEmpty(productSearchResultVO)) {
                        Long[] categoryIds = new Long[1];
                        List<Long> brandIdList = new LinkedList<>();
                        List<Long> shopIdList = new LinkedList<>();

                        categoryIds[0] = productSearchResultVO.getCategoryId();

                        //设置品牌ID
                        if (productSearchResultVO.getBrandId() != null) {
                            brandIdList.add(productSearchResultVO.getBrandId());
                        }


                        //设置店铺ID
                        if (productSearchResultVO.getShopId() != null) {
                            shopIdList.add(productSearchResultVO.getShopId());
                        }
                        productSearchResultVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix() + productSearchResultVO.getProductPreviewPath());

                        List<ProductSearchResultVO> list = new LinkedList<>();
                        list.add(productSearchResultVO);

                        //查询类别名称
                        this.queryCategory(list,categoryIds);

                        request.setAttribute("model", list.get(0));
                    } else {
                        request.setAttribute("model", new ProductSearchResultVO());
                    }
                }
            }
        }catch(Exception e)
        {
            request.setAttribute("model", new ProductSearchResultVO());
            logger.warn(e.getMessage(),e);
        }
        return "pages/product/productSku/search/detail.html";
    }




    /**
     * 删除
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/delete/ids",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<ProductSearchResultVO> productSearchResultVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(productSearchResultVOS))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            for(ProductSearchResultVO productSearchResultVO:productSearchResultVOS) {
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), productSearchResultVO.getSkuId());
                resultObjectVO = feignProductSearchService.removeById(requestJsonVO);
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 清空搜索
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/clear",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO clear(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            resultObjectVO = feignProductSearchService.clear(RequestJsonVOGenerator.generator(toucan.getAppCode(),null));
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}

