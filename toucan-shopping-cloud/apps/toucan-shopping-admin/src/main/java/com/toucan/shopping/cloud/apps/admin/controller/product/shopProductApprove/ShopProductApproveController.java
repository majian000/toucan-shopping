package com.toucan.shopping.cloud.apps.admin.controller.product.shopProductApprove;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
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
import com.toucan.shopping.modules.product.entity.ShopProductApprove;
import com.toucan.shopping.modules.product.page.ShopProductApproveSkuPageInfo;
import com.toucan.shopping.modules.product.page.ProductSpuPageInfo;
import com.toucan.shopping.modules.product.page.ShopProductApprovePageInfo;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 店铺商品管理
 * @author majian
 */
@Controller
@RequestMapping("/product/shopProductApprove")
public class ShopProductApproveController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignShopProductApproveService feignShopProductApproveService;

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
    private FeignShopProductApproveSkuService feignShopProductApproveSkuService;

    @Autowired
    private FeignProductSpuService feignProductSpuService;


    @Autowired
    private EventPublishService eventPublishService;

    @Autowired
    private FeignMessageUserService feignMessageUserService;

    @Autowired
    private IdGenerator idGenerator;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/product/shopProductApprove/listPage",feignFunctionService);
        return "pages/product/shopProductApprove/list.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/spuListPage/{categoryId}",method = RequestMethod.GET)
    public String spuListPage(HttpServletRequest request,@PathVariable Long categoryId)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/product/shopProductApprove/spuListPage",feignFunctionService);

        request.setAttribute("categoryId",categoryId);
        return "pages/product/shopProductApprove/spu_list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/spuDetailPage/{id}",method = RequestMethod.GET)
    public String spuDetailPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            ProductSpuVO queryProductSpu = new ProductSpuVO();
            queryProductSpu.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryProductSpu);
            ResultObjectVO resultObjectVO = feignProductSpuService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    ProductSpuVO productSpuVO = resultObjectVO.formatData(ProductSpuVO.class);

                    //查询分类
                    CategoryVO queryCategory = new CategoryVO();
                    queryCategory.setId(productSpuVO.getCategoryId());
                    requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryCategory);
                    resultObjectVO = feignCategoryService.queryById(requestJsonVO);
                    if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null)
                    {
                        CategoryTreeVO categoryTreeVO = resultObjectVO.formatData(CategoryTreeVO.class);
                        if(categoryTreeVO!=null) {
                            productSpuVO.setCategoryName(categoryTreeVO.getPath());
                        }
                    }

                    //查询品牌
                    BrandVO queryBrand = new BrandVO();
                    queryBrand.setId(productSpuVO.getBrandId());
                    requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryBrand);
                    resultObjectVO = feignBrandService.findById(requestJsonVO.sign(),requestJsonVO);
                    if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null)
                    {
                        List<BrandVO> brandVOS = resultObjectVO.formatDataList(BrandVO.class);
                        BrandVO brandVO = brandVOS.get(0);
                        String brandName = "";
                        if(StringUtils.isNotEmpty(brandVO.getChineseName()))
                        {
                            brandName+=brandVO.getChineseName();
                        }
                        if(StringUtils.isNotEmpty(brandVO.getEnglishName()))
                        {
                            brandName+=" "+brandVO.getEnglishName();
                        }
                        productSpuVO.setBrandName(brandName);

                    }

                    //将属性名和属性值转换成字符串
                    productSpuVO.setAttributeKeyValuesJson(JSONArray.toJSONString(productSpuVO.getAttributeKeyValues()));

                    request.setAttribute("model",productSpuVO);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/product/shopProductApprove/spu_detail.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/approvePage/{id}",method = RequestMethod.GET)
    public String approvePage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            ShopProductApproveVO shopProductVO = new ShopProductApproveVO();
            shopProductVO.setId(id);;
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopProductVO);
            ResultObjectVO resultObjectVO = feignShopProductApproveService.queryByProductApproveId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<ShopProductApproveVO> list = resultObjectVO.formatDataList(ShopProductApproveVO.class);
                if(CollectionUtils.isNotEmpty(list)) {
                    Long[] categoryIds = new Long[list.size()];
                    Long[] shopCategoryIds = new Long[list.size()];
                    List<Long> brandIdList = new LinkedList<>();
                    List<Long> shopIdList =new LinkedList<>();

                    boolean brandExists=false;
                    boolean shopCategoryExists=false;
                    boolean shopExists=false;
                    for(int i=0;i<list.size();i++)
                    {
                        ShopProductApproveVO shopProductVOTmp = list.get(i);
                        categoryIds[i] = shopProductVOTmp.getCategoryId();

                        //设置品牌ID
                        brandExists=false;
                        for(Long brandId:brandIdList)
                        {
                            if(shopProductVOTmp.getBrandId()!=null&&brandId!=null
                                    &&brandId.longValue()==shopProductVOTmp.getBrandId().longValue())
                            {
                                brandExists=true;
                                break;
                            }

                        }
                        if(!brandExists) {
                            if(shopProductVOTmp.getBrandId()!=null) {
                                brandIdList.add(shopProductVOTmp.getBrandId());
                            }
                        }


                        //设置店铺分类ID
                        shopCategoryExists=false;
                        for(int sci=0;sci<shopCategoryIds.length;sci++)
                        {
                            Long shopCategoryId = shopCategoryIds[sci];
                            if(shopProductVOTmp.getShopCategoryId()!=null&&shopCategoryId!=null
                                    &&shopCategoryId.longValue()==shopProductVOTmp.getShopCategoryId().longValue())
                            {
                                shopCategoryExists=true;
                                break;
                            }

                        }
                        if(!shopCategoryExists) {
                            if(shopProductVOTmp.getShopCategoryId()!=null) {
                                shopCategoryIds[i] = shopProductVOTmp.getShopCategoryId();
                            }
                        }



                        //设置店铺ID
                        shopExists=false;
                        for(Long shopId:shopIdList)
                        {
                            if(shopProductVOTmp.getShopId()!=null&&shopId!=null
                                    &&shopId.longValue()==shopProductVOTmp.getShopId().longValue())
                            {
                                shopExists=true;
                                break;
                            }

                        }
                        if(!shopExists) {
                            if(shopProductVOTmp.getShopId()!=null) {
                                shopIdList.add(shopProductVOTmp.getShopId());
                            }
                        }

                    }


                    //查询类别名称
                    this.queryCategory(list,categoryIds);


                    //查询店铺类别名称
                    this.queryShopCategory(list,shopCategoryIds);

                    //查询品牌名称
                    this.queryBrand(list,brandIdList);

                    //查询店铺名称
                    this.queryShop(list,shopIdList);



                    for(ShopProductApproveVO shopProductVOTmp:list)
                    {
                        if(shopProductVOTmp.getMainPhotoFilePath()!=null) {
                            shopProductVOTmp.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+shopProductVOTmp.getMainPhotoFilePath());
                        }

                        if(CollectionUtils.isNotEmpty(shopProductVOTmp.getPreviewPhotoPaths())) {
                            shopProductVOTmp.setHttpPreviewPhotoPaths(new LinkedList<>());
                            for(String previewPhotoPath:shopProductVOTmp.getPreviewPhotoPaths())
                            {
                                shopProductVOTmp.getHttpPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix()+previewPhotoPath);
                            }
                        }

                        if(CollectionUtils.isNotEmpty(shopProductVOTmp.getProductSkuVOList()))
                        {
                            shopProductVOTmp.setHttpSkuPreviewPhotoPaths(new LinkedList<>());
                            for(ShopProductApproveSkuVO productSkuVO:shopProductVOTmp.getProductSkuVOList())
                            {
                                if(StringUtils.isNotEmpty(productSkuVO.getProductPreviewPath())) {
                                    shopProductVOTmp.getHttpSkuPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix() + productSkuVO.getProductPreviewPath());
                                }
                            }
                        }
                    }

                    request.setAttribute("model", list.get(0));
                }else{
                    request.setAttribute("model", new ShopProductApproveVO());
                }
            }
        }catch(Exception e)
        {
            request.setAttribute("model", new ShopProductApproveVO());
            logger.warn(e.getMessage(),e);
        }
        return "pages/product/shopProductApprove/approve.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/descriptionPage/{id}",method = RequestMethod.GET)
    public String descriptionPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            ShopProductApproveVO shopProductVO = new ShopProductApproveVO();
            shopProductVO.setId(id);;
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopProductVO);
            ResultObjectVO resultObjectVO = feignShopProductApproveService.queryByProductApproveId(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                List<ShopProductApproveVO> list = resultObjectVO.formatDataList(ShopProductApproveVO.class);
                if(CollectionUtils.isNotEmpty(list)) {
                    request.setAttribute("description", list.get(0).getProductDescription());
                }
            }
        }catch(Exception e)
        {
            request.setAttribute("description","" );
            logger.warn(e.getMessage(),e);
        }
        return "pages/product/shopProductApprove/description.html";
    }


    /**
     * 查询类别信息
     * @param list
     * @param categoryIds
     */
    void queryCategory(List<ShopProductApproveVO> list,Long[] categoryIds)
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
                    for (ShopProductApproveVO shopProductVO : list) {
                        for (CategoryVO categoryVO : categoryVOS) {
                            if (shopProductVO.getCategoryId() != null && shopProductVO.getCategoryId().longValue() == categoryVO.getId().longValue()) {
                                shopProductVO.setCategoryName(categoryVO.getName());
                                shopProductVO.setCategoryPath(categoryVO.getNamePath());
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
    void queryShopCategory(List<ShopProductApproveVO> list,Long[] shopCategoryIds)
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
                    for(ShopProductApproveVO shopProductVO:list)
                    {
                        for(ShopCategoryVO shopCategoryVO:shopCategoryVOS)
                        {
                            if(shopProductVO.getShopCategoryId()!=null&&shopProductVO.getShopCategoryId().longValue()==shopCategoryVO.getId().longValue())
                            {
                                shopProductVO.setShopCategoryName(shopCategoryVO.getName());
                                shopProductVO.setShopCategoryPath(shopCategoryVO.getNamePath());
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
    void queryBrand(List<ShopProductApproveVO> list,List<Long> brandIdList)
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
                    for(ShopProductApproveVO shopProductVO:list)
                    {
                        for(BrandVO brandVO:brandVOS)
                        {
                            if(shopProductVO.getBrandId()!=null&&shopProductVO.getBrandId().longValue()==brandVO.getId().longValue())
                            {
                                shopProductVO.setBrandChineseName(brandVO.getChineseName());
                                shopProductVO.setBrandEnglishName(brandVO.getEnglishName());
                                shopProductVO.setBrandLogo(brandVO.getLogoPath());
                                if(brandVO.getLogoPath()!=null) {
                                    shopProductVO.setBrandHttpLogo(imageUploadService.getImageHttpPrefix() +brandVO.getLogoPath());
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
    void queryShop(List<ShopProductApproveVO> list,List<Long> shopIdList)
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
                    for(ShopProductApproveVO shopProductVO:list)
                    {
                        for(SellerShopVO sellerShopVO:sellerShopVOS)
                        {
                            if(shopProductVO.getShopId()!=null&&shopProductVO.getShopId().longValue()==sellerShopVO.getId().longValue())
                            {
                                shopProductVO.setShopName(sellerShopVO.getName());
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
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/query/product/spu/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO queryProductSpuList(HttpServletRequest request, ProductSpuPageInfo pageInfo)
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
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            resultObjectVO = feignProductSpuService.queryListPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<ProductSpuVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),ProductSpuVO.class);
                    if(tableVO.getCount()>0) {
                        boolean brandExists=false;
                        boolean shopCategoryExists = false;
                        List<Long> brandIdList = new LinkedList<>();
                        Long[] categoryIds = new Long[list.size()];
                        for(int i=0;i<list.size();i++)
                        {
                            ProductSpuVO productSpuVO = list.get(i);

                            //设置品牌ID
                            brandExists=false;
                            for(Long brandId:brandIdList)
                            {
                                if(productSpuVO.getBrandId()!=null&&brandId!=null
                                        &&brandId.longValue()==productSpuVO.getBrandId().longValue())
                                {
                                    brandExists=true;
                                    break;
                                }

                            }
                            if(!brandExists) {
                                if(productSpuVO.getBrandId()!=null) {
                                    brandIdList.add(productSpuVO.getBrandId());
                                }
                            }

                            //设置店铺分类ID
                            shopCategoryExists = false;
                            for (int sci = 0; sci < categoryIds.length; sci++) {
                                Long shopCategoryId = categoryIds[sci];
                                if (productSpuVO.getCategoryId() != null && shopCategoryId != null
                                        && shopCategoryId.longValue() == productSpuVO.getCategoryId().longValue()) {
                                    shopCategoryExists = true;
                                    break;
                                }

                            }
                            if (!shopCategoryExists) {
                                if (productSpuVO.getCategoryId() != null) {
                                    categoryIds[i] = productSpuVO.getCategoryId();
                                }
                            }

                        }

                        //查询品牌名称
                        this.queryBrandForProductSpu(list, brandIdList);


                        //查询类别名称
                        this.queryCategoryForProductSpu(list, categoryIds);

                        tableVO.setData((List)list);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, ShopProductApprovePageInfo pageInfo)
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
            pageInfo.setOrderColumn("update_date");
            pageInfo.setOrderSort("desc");
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            resultObjectVO = feignShopProductApproveService.queryListPage(requestJsonVO);
            if (resultObjectVO.getCode() == ResultObjectVO.SUCCESS) {
                if (resultObjectVO.getData() != null) {
                    Map<String, Object> resultObjectDataMap = (Map<String, Object>) resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total") != null ? resultObjectDataMap.get("total") : "0")));
                    List<ShopProductApproveVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), ShopProductApproveVO.class);
                    if (CollectionUtils.isNotEmpty(list)) {
                        Long[] categoryIds = new Long[list.size()];
                        Long[] shopCategoryIds = new Long[list.size()];
                        List<Long> brandIdList = new LinkedList<>();
                        List<Long> shopIdList = new LinkedList<>();

                        boolean brandExists = false;
                        boolean shopCategoryExists = false;
                        boolean shopExists = false;
                        for (int i = 0; i < list.size(); i++) {
                            ShopProductApproveVO shopProductVO = list.get(i);
                            categoryIds[i] = shopProductVO.getCategoryId();

                            //设置品牌ID
                            brandExists = false;
                            for (Long brandId : brandIdList) {
                                if (shopProductVO.getBrandId() != null && brandId != null
                                        && brandId.longValue() == shopProductVO.getBrandId().longValue()) {
                                    brandExists = true;
                                    break;
                                }

                            }
                            if (!brandExists) {
                                if (shopProductVO.getBrandId() != null) {
                                    brandIdList.add(shopProductVO.getBrandId());
                                }
                            }


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


                        for (ShopProductApproveVO shopProductVO : list) {
                            if (shopProductVO.getMainPhotoFilePath() != null) {
                                shopProductVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix() + shopProductVO.getMainPhotoFilePath());
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



    /**
     * 查询SKU列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/query/product/sku/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO queryShopProductApproveSkuList(HttpServletRequest request, ShopProductApproveSkuPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            if(pageInfo.getProductApproveId()==null)
            {
                tableVO.setCode(TableVO.FAILD);
                tableVO.setMsg("商品ID不能为空");
                return tableVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignShopProductApproveSkuService.queryListPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<ShopProductApproveSkuVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), ShopProductApproveSkuVO.class);
                    if(CollectionUtils.isNotEmpty(list))
                    {
                        for(ShopProductApproveSkuVO productSkuVO:list)
                        {
                            if(productSkuVO.getProductPreviewPath()!=null) {
                                productSkuVO.setHttpMainPhoto(imageUploadService.getImageHttpPrefix()+productSkuVO.getProductPreviewPath());
                            }
                        }

                    }
                    if(tableVO.getCount()>0) {
                        tableVO.setData((List)list);
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



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
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


    /**
     * 删除
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(HttpServletRequest request,  @PathVariable String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            ShopProductApprove shopProductApprove =new ShopProductApprove();
            shopProductApprove.setId(Long.parseLong(id));

            String entityJson = JSONObject.toJSONString(shopProductApprove);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignShopProductApproveService.deleteById(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("删除失败,请稍后重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 审核驳回
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/reject",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO reject(HttpServletRequest request, @RequestBody ShopProductApproveRecordVO shopProductApproveRecordVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(shopProductApproveRecordVO.getApproveId()==null)
            {
                resultObjectVO.setMsg("审核ID不能为空");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            shopProductApproveRecordVO.setCreateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), shopProductApproveRecordVO);
            resultObjectVO = feignShopProductApproveService.reject(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                //发送商品审核驳回消息
                if(resultObjectVO.isSuccess())
                {
                    SellerShopVO querySellerShopVO = new SellerShopVO();
                    querySellerShopVO.setId(shopProductApproveRecordVO.getShopId());
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), querySellerShopVO);
                    resultObjectVO = feignSellerShopService.findById(requestJsonVO.sign(),requestJsonVO);
                    if(resultObjectVO.isSuccess()) {
                        List<SellerShopVO> sellerShopVOS = resultObjectVO.formatDataList(SellerShopVO.class);
                        SellerShopVO sellerShopVO = null;
                        if(CollectionUtils.isNotEmpty(sellerShopVOS))
                        {
                            sellerShopVO = sellerShopVOS.get(0);
                        }
                        if(sellerShopVO!=null&&sellerShopVO.getUserMainId()!=null) {
                            //发送消息
                            MessageVO messageVO = new MessageVO("商品审核消息", shopProductApproveRecordVO.getApproveText(), ProductConstant.MESSAGE_CONTENT_TYPE_1, sellerShopVO.getUserMainId());
                            messageVO.setMessageTypeCode(ProductConstant.PRODUCT_APPROVE_MESSAGE_CODE);

                            //保存消息发布事件
                            EventPublish eventPublish = saveEventPublish(messageVO);
                            if (eventPublish == null) {
                                logger.warn("消息发布事件保存失败 payload {} ", JSONObject.toJSONString(messageVO));
                            }

                            requestJsonVO = RequestJsonVOGenerator.generator(appCode, messageVO);
                            resultObjectVO = feignMessageUserService.send(requestJsonVO);
                            if (resultObjectVO.isSuccess()) {
                                //设置消息为已发送
                                eventPublish.setStatus((short) 1);
                                eventPublishService.updateStatus(eventPublish);
                            }else{
                                logger.warn("商品审核已被驳回,但是消息发送失败 payload {} ", JSONObject.toJSONString(messageVO));
                                resultObjectVO.setCode(ResultObjectVO.FAILD);
                                resultObjectVO.setMsg("商品审核已被驳回,但是消息发送失败");
                            }
                        }
                    }
                }

            }
            return resultObjectVO;
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setMsg("审核失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }





    /**
     * 审核通过
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/pass",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO pass(HttpServletRequest request, @RequestBody ShopProductApproveVO shopProductApproveVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(shopProductApproveVO.getId()==null)
            {
                resultObjectVO.setMsg("店铺商品ID不能为空");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), shopProductApproveVO);
            resultObjectVO = feignShopProductApproveService.pass(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                //发送商品审核消息
                if(resultObjectVO.isSuccess())
                {
                    resultObjectVO = feignShopProductApproveService.queryByProductApproveId(requestJsonVO);
                    if(resultObjectVO.isSuccess()) {
                        List<ShopProductApproveVO> shopProductApproveVOS = resultObjectVO.formatDataList(ShopProductApproveVO.class);
                        if(CollectionUtils.isNotEmpty(shopProductApproveVOS)) {

                            shopProductApproveVO = shopProductApproveVOS.get(0);

                            SellerShopVO querySellerShopVO = new SellerShopVO();
                            querySellerShopVO.setId(shopProductApproveVO.getShopId());
                            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), querySellerShopVO);
                            resultObjectVO = feignSellerShopService.findById(requestJsonVO.sign(), requestJsonVO);
                            if (resultObjectVO.isSuccess()) {
                                List<SellerShopVO> sellerShopVOS = resultObjectVO.formatDataList(SellerShopVO.class);
                                if(CollectionUtils.isNotEmpty(sellerShopVOS)) {
                                    SellerShopVO sellerShopVO = sellerShopVOS.get(0);
                                    if (sellerShopVO != null && sellerShopVO.getUserMainId() != null && shopProductApproveVO.getName() != null) {
                                        //发送消息
                                        MessageVO messageVO = new MessageVO("商品审核消息", "您发布的商品," + shopProductApproveVO.getName() + "已经审核通过!", ProductConstant.MESSAGE_CONTENT_TYPE_1, sellerShopVO.getUserMainId());
                                        messageVO.setMessageTypeCode(ProductConstant.PRODUCT_APPROVE_MESSAGE_CODE);

                                        //保存消息发布事件
                                        EventPublish eventPublish = saveEventPublish(messageVO);
                                        if (eventPublish == null) {
                                            logger.warn("消息发布事件保存失败 payload {} ", JSONObject.toJSONString(messageVO));
                                        }

                                        requestJsonVO = RequestJsonVOGenerator.generator(appCode, messageVO);
                                        resultObjectVO = feignMessageUserService.send(requestJsonVO);
                                        if (resultObjectVO.isSuccess()) {
                                            //设置消息为已发送
                                            eventPublish.setStatus((short) 1);
                                            eventPublishService.updateStatus(eventPublish);
                                        }else{
                                            logger.warn("商品审核已被通过,但是消息发送失败 payload {} ", JSONObject.toJSONString(messageVO));
                                            resultObjectVO.setCode(ResultObjectVO.SUCCESS);
                                            resultObjectVO.setMsg("商品审核已被通过,但是消息发送失败");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
            return resultObjectVO;
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setMsg("审核失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }

}

