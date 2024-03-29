package com.toucan.shopping.cloud.apps.admin.controller.product.productSku;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.apps.admin.util.SearchUtils;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.message.api.feign.service.FeignMessageUserService;
import com.toucan.shopping.cloud.product.api.feign.service.*;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import com.toucan.shopping.modules.common.persistence.event.enums.EventPublishTypeEnum;
import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.message.vo.MessageVO;
import com.toucan.shopping.modules.product.constant.ProductConstant;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.page.ProductSpuPageInfo;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品SKU管理
 * @author majian
 */
@Controller
@RequestMapping("/product/productSku")
public class ProductSkuController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignShopProductService feignShopProductService;

    @Autowired
    private FeignCategoryService feignCategoryService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignBrandService feignBrandService;

    @Autowired
    private FeignShopCategoryService feignShopCategoryService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private FeignProductSkuService feignProductSkuService;

    @Autowired
    private FeignProductSpuService feignProductSpuService;


    @Autowired
    private EventPublishService eventPublishService;

    @Autowired
    private FeignMessageUserService feignMessageUserService;

    @Autowired
    private IdGenerator idGenerator;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/product/productSku/listPage",feignFunctionService);
        request.setAttribute("pcProductSkuPreviewPage",toucan.getShoppingPC().getBasePath()+toucan.getShoppingPC().getProductSkuPreviewPage());
        return "pages/product/productSku/list.html";
    }





    /**
     * 查询类别信息
     * @param list
     * @param categoryIds
     */
    void queryCategory(List<ProductSkuVO> list,Long[] categoryIds)
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
                    for (ProductSkuVO productSkuVO : list) {
                        for (CategoryVO categoryVO : categoryVOS) {
                            if (productSkuVO.getCategoryId() != null && productSkuVO.getCategoryId().longValue() == categoryVO.getId().longValue()) {
                                productSkuVO.setCategoryName(categoryVO.getName());
                                productSkuVO.setCategoryPath(categoryVO.getNamePath());
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


    /**
     * 查询店铺类别
     * @param list
     * @param shopCategoryIds
     */
    void queryShopCategory(List<ProductSkuVO> list,Long[] shopCategoryIds)
    {
        try {
            ShopCategoryVO queryShopCategoryVO = new ShopCategoryVO();
            queryShopCategoryVO.setIdArray(shopCategoryIds);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopCategoryVO);
            ResultObjectVO resultObjectVO = feignShopCategoryService.findByIdArray(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<ShopCategoryVO> shopCategoryVOS = resultObjectVO.formatDataList(ShopCategoryVO.class);
                if(CollectionUtils.isNotEmpty(shopCategoryVOS))
                {
                    for(ProductSkuVO productSkuVO:list)
                    {
                        for(ShopCategoryVO shopCategoryVO:shopCategoryVOS)
                        {
                            if(productSkuVO.getShopCategoryId()!=null&&productSkuVO.getShopCategoryId().longValue()==shopCategoryVO.getId().longValue())
                            {
                                productSkuVO.setShopCategoryName(shopCategoryVO.getName());
                                productSkuVO.setShopCategoryPath(shopCategoryVO.getNamePath());
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

    /**
     * 查询品牌
     * @param list
     * @param brandIdList
     */
    void queryBrand(List<ProductSkuVO> list,List<Long> brandIdList)
    {
        try {
            BrandVO queryBrandVO = new BrandVO();
            queryBrandVO.setIdList(brandIdList);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryBrandVO);
            ResultObjectVO resultObjectVO = feignBrandService.findByIdList(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<BrandVO> brandVOS = resultObjectVO.formatDataList(BrandVO.class);
                if(CollectionUtils.isNotEmpty(brandVOS))
                {
                    for(ProductSkuVO productSkuVO:list)
                    {
                        for(BrandVO brandVO:brandVOS)
                        {
                            if(productSkuVO.getBrandId()!=null&&productSkuVO.getBrandId().longValue()==brandVO.getId().longValue())
                            {
                                productSkuVO.setBrandChineseName(brandVO.getChineseName());
                                productSkuVO.setBrandEnglishName(brandVO.getEnglishName());
                                productSkuVO.setBrandLogo(brandVO.getLogoPath());
                                if(brandVO.getLogoPath()!=null) {
                                    productSkuVO.setBrandHttpLogo(imageUploadService.getImageHttpPrefix() +brandVO.getLogoPath());
                                }
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


    /**
     * 查询店铺
     * @param list
     * @param shopIdList
     */
    void queryShop(List<ProductSkuVO> list,List<Long> shopIdList)
    {
        try {
            SellerShopVO queryShopVO = new SellerShopVO();
            queryShopVO.setIdList(shopIdList);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopVO);
            ResultObjectVO resultObjectVO = feignSellerShopService.findByIdList(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<SellerShopVO> sellerShopVOS = resultObjectVO.formatDataList(SellerShopVO.class);
                if(CollectionUtils.isNotEmpty(sellerShopVOS))
                {
                    for(ProductSkuVO productSkuVO:list)
                    {
                        for(SellerShopVO sellerShopVO:sellerShopVOS)
                        {
                            if(productSkuVO.getShopId()!=null&&productSkuVO.getShopId().longValue()==sellerShopVO.getId().longValue())
                            {
                                productSkuVO.setShopName(sellerShopVO.getName());
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





    /**
     * 查询品牌
     * @param list
     * @param brandIdList
     */
    void queryBrandForProductSpu(List<ProductSpuVO> list, List<Long> brandIdList)
    {
        try {
            BrandVO queryBrandVO = new BrandVO();
            queryBrandVO.setIdList(brandIdList);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryBrandVO);
            ResultObjectVO resultObjectVO = feignBrandService.findByIdList(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<BrandVO> brandVOS = resultObjectVO.formatDataList(BrandVO.class);
                if(CollectionUtils.isNotEmpty(brandVOS))
                {
                    for(ProductSpuVO productSpuVO:list)
                    {
                        for(BrandVO brandVO:brandVOS)
                        {
                            if(productSpuVO.getBrandId()!=null&&productSpuVO.getBrandId().longValue()==brandVO.getId().longValue())
                            {
                                productSpuVO.setBrandChineseName(brandVO.getChineseName());
                                productSpuVO.setBrandEnglishName(brandVO.getEnglishName());
                                productSpuVO.setBrandLogo(brandVO.getLogoPath());
                                if(brandVO.getLogoPath()!=null) {
                                    productSpuVO.setBrandHttpLogo(imageUploadService.getImageHttpPrefix() +brandVO.getLogoPath());
                                }
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




    /**
     * 查询类别信息
     * @param list
     * @param categoryIds
     */
    void queryCategoryForProductSpu(List<ProductSpuVO> list,Long[] categoryIds)
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
                    for (ProductSpuVO productSpuVO : list) {
                        for (CategoryVO categoryVO : categoryVOS) {
                            if (productSpuVO.getCategoryId() != null && productSpuVO.getCategoryId().longValue() == categoryVO.getId().longValue()) {
                                productSpuVO.setCategoryName(categoryVO.getName());
                                productSpuVO.setCategoryPath(categoryVO.getNamePath());
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




    EventPublish saveEventPublish(MessageVO messageVO)
    {
        String globalTransactionId = UUID.randomUUID().toString().replace("-","");

        EventPublish eventPublish = new EventPublish();
        eventPublish.setCreateDate(new Date());
        eventPublish.setId(idGenerator.id());
        eventPublish.setRemark(messageVO.getTitle());
        eventPublish.setTransactionId(globalTransactionId);
        eventPublish.setPayload(JSONObject.toJSONString(messageVO));
        eventPublish.setStatus((short)0); //待发送
        eventPublish.setType(EventPublishTypeEnum.USER_TRUESCULPTURE_MESSAGE.getCode());
        if(eventPublishService.insert(eventPublish)>0) {
            return eventPublish;
        }
        return null;
    }



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, ProductSkuPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = null;
            ResultObjectVO resultObjectVO = null;
            if(pageInfo.getCategoryId()!=null&&pageInfo.getCategoryId().longValue()!=-1) {
                //查询分类以及子分类
                CategoryVO categoryVO = new CategoryVO();
                categoryVO.setId(pageInfo.getCategoryId());
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categoryVO);
                resultObjectVO = feignCategoryService.queryChildListByPid(requestJsonVO);
                if (resultObjectVO.isSuccess()) {
                    if (resultObjectVO.getData() != null) {
                        List<CategoryVO> categoryVOS = resultObjectVO.formatDataList(CategoryVO.class);
                        if (CollectionUtils.isNotEmpty(categoryVOS)) {
                            List<Long> categoryIdList = new LinkedList<>();
                            for (CategoryVO cv : categoryVOS) {
                                categoryIdList.add(cv.getId());
                            }
                            categoryIdList.add(pageInfo.getCategoryId());
                            pageInfo.setCategoryIdList(categoryIdList);
                            pageInfo.setCategoryId(null);
                        }
                    }
                }
            }
            pageInfo.setOrderColumns(new LinkedList<>());
            pageInfo.getOrderColumns().add("update_date");
            pageInfo.getOrderColumns().add("create_date");
            pageInfo.setOrderSort("desc");
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            resultObjectVO = feignProductSkuService.queryListPage(requestJsonVO);
            if (resultObjectVO.getCode() == ResultObjectVO.SUCCESS) {
                if (resultObjectVO.getData() != null) {
                    Map<String, Object> resultObjectDataMap = (Map<String, Object>) resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total") != null ? resultObjectDataMap.get("total") : "0")));
                    List<ProductSkuVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), ProductSkuVO.class);
                    if (CollectionUtils.isNotEmpty(list)) {
                        Long[] categoryIds = new Long[list.size()];
                        Long[] shopCategoryIds = new Long[list.size()];
                        List<Long> brandIdList = new LinkedList<>();
                        List<Long> shopIdList = new LinkedList<>();

                        boolean brandExists = false;
                        boolean shopCategoryExists = false;
                        boolean shopExists = false;
                        for (int i = 0; i < list.size(); i++) {
                            ProductSkuVO shopProductVO = list.get(i);
                            categoryIds[i] = shopProductVO.getCategoryId();

                            //设置品牌ID
                            brandExists = false;
//                            for (Long brandId : brandIdList) {
//                                if (shopProductVO.getBrandId() != null && brandId != null
//                                        && brandId.longValue() == shopProductVO.getBrandId().longValue()) {
//                                    brandExists = true;
//                                    break;
//                                }
//
//                            }
//                            if (!brandExists) {
//                                if (shopProductVO.getBrandId() != null) {
//                                    brandIdList.add(shopProductVO.getBrandId());
//                                }
//                            }


                            //设置店铺分类ID
                            shopCategoryExists = false;
                            for (int sci = 0; sci < shopCategoryIds.length; sci++) {
                                Long shopCategoryId = shopCategoryIds[sci];
                                if (shopProductVO.getShopCategoryId() != null && shopCategoryId != null
                                        && shopCategoryId.longValue() == shopProductVO.getShopCategoryId().longValue()) {
                                    shopCategoryExists = true;
                                    break;
                                }

                            }
                            if (!shopCategoryExists) {
                                if (shopProductVO.getShopCategoryId() != null) {
                                    shopCategoryIds[i] = shopProductVO.getShopCategoryId();
                                }
                            }


                            //设置店铺ID
                            shopExists = false;
                            for (Long shopId : shopIdList) {
                                if (shopProductVO.getShopId() != null && shopId != null
                                        && shopId.longValue() == shopProductVO.getShopId().longValue()) {
                                    shopExists = true;
                                    break;
                                }

                            }
                            if (!shopExists) {
                                if (shopProductVO.getShopId() != null) {
                                    shopIdList.add(shopProductVO.getShopId());
                                }
                            }

                        }


                        //查询类别名称
                        this.queryCategory(list, categoryIds);


                        //查询店铺类别名称
                        this.queryShopCategory(list, shopCategoryIds);

                        //查询品牌名称
                        this.queryBrand(list, brandIdList);

                        //查询店铺名称
                        this.queryShop(list, shopIdList);


                        for (ProductSkuVO productSkuVO : list) {
                            if (productSkuVO.getProductPreviewPath() != null) {
                                productSkuVO.setHttpMainPhoto(imageUploadService.getImageHttpPrefix() + productSkuVO.getProductPreviewPath());
                            }
                        }

                    }
                    if (tableVO.getCount() > 0) {
                        tableVO.setData((List) list);
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






    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/query/category/tree",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO queryCategoryTree(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryVO query = new CategoryVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            resultObjectVO = feignCategoryService.queryTree(SignUtil.sign(requestJsonVO),requestJsonVO);
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
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






    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/detailPage/{id}",method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            ProductSkuVO queryProductSkuVO = new ProductSkuVO();
            queryProductSkuVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryProductSkuVO);
            ResultObjectVO resultObjectVO = feignProductSkuService.queryById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                ProductSkuVO productSkuVO = resultObjectVO.formatData(ProductSkuVO.class);
                if(ObjectUtils.isNotEmpty(productSkuVO)) {
                    Long[] categoryIds = new Long[1];
                    Long[] shopCategoryIds = new Long[1];
                    List<Long> brandIdList = new LinkedList<>();
                    List<Long> shopIdList =new LinkedList<>();

                    categoryIds[0] = productSkuVO.getCategoryId();

                    //设置品牌ID
                    if(productSkuVO.getBrandId()!=null) {
                        brandIdList.add(productSkuVO.getBrandId());
                    }


                    //设置店铺分类ID
                    if(productSkuVO.getShopCategoryId()!=null) {
                        shopCategoryIds[0] = productSkuVO.getShopCategoryId();
                    }



                    //设置店铺ID
                    if(productSkuVO.getShopId()!=null) {
                        shopIdList.add(productSkuVO.getShopId());
                    }


                    productSkuVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+productSkuVO.getProductPreviewPath());

                    List<ProductSkuVO> list = new LinkedList<>();
                    list.add(productSkuVO);

                    //查询类别名称
                    this.queryCategory(list,categoryIds);


                    //查询店铺类别名称
                    this.queryShopCategory(list,shopCategoryIds);

                    //查询品牌名称
                    this.queryBrand(list,brandIdList);

                    //查询店铺名称
                    this.queryShop(list,shopIdList);


                    request.setAttribute("model", list.get(0));
                }else{
                    request.setAttribute("model", new ProductSkuVO());
                }
            }
        }catch(Exception e)
        {
            request.setAttribute("model", new ProductSkuVO());
            logger.warn(e.getMessage(),e);
        }
        return "pages/product/productSku/detail.html";
    }




    /**
     * 店铺商品 上架/下架
     * @param shopProductVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/shelves",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO shelves(HttpServletRequest request,@RequestBody ShopProductVO shopProductVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(shopProductVO.getId()==null)
            {
                resultObjectVO.setCode(TableVO.FAILD);
                resultObjectVO.setMsg("商品ID不能为空");
                return resultObjectVO;
            }
            if(shopProductVO.getShopId()==null)
            {
                resultObjectVO.setCode(TableVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空");
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopProductVO);
            resultObjectVO = feignProductSkuService.shelves(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("操作失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





    /**
     * SKU同步搜索缓存
     * @param shopProductVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/flush/search",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO flushSearch(HttpServletRequest request,@RequestBody ShopProductVO shopProductVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(shopProductVO.getProductSkuVOList()))
            {
                resultObjectVO.setCode(TableVO.FAILD);
                resultObjectVO.setMsg("要同步的商品ID不能为空");
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopProductVO.getProductSkuVOList());
            resultObjectVO = feignProductSkuService.queryByIdList(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<ProductSkuVO> productSkuVOS = resultObjectVO.formatDataList(ProductSkuVO.class);
                if(CollectionUtils.isNotEmpty(productSkuVOS))
                {
                    for(ProductSkuVO productSkuVO:productSkuVOS)
                    {
                        SearchUtils.flushToSearch(productSkuVO);
                    }
                }

            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("操作失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}

