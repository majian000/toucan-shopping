package com.toucan.shopping.cloud.apps.admin.controller.product.shopProduct;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.apps.admin.util.SearchUtils;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.cloud.product.api.*;
import com.toucan.shopping.cloud.seller.api.SellerShopServiceAPI;
import com.toucan.shopping.cloud.seller.api.ShopCategoryServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import com.toucan.shopping.modules.common.persistence.event.enums.EventPublishTypeEnum;
import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.message.vo.MessageVO;
import com.toucan.shopping.modules.product.page.*;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 店铺商品管理
 * @author majian
 */
@RestController
@RequestMapping("/product/shopProduct")
public class ShopProductController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private CategoryServiceAPI categoryService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private BrandServiceAPI brandService;

    @Autowired
    private ShopCategoryServiceAPI shopCategoryService;

    @Autowired
    private SellerShopServiceAPI sellerShopService;

    @Autowired
    private ShopProductServiceAPI shopProductService;

    @Autowired
    private EventPublishService eventPublishService;

    @Autowired
    private ProductSkuServiceAPI productSkuService;

    @Autowired
    private IdGenerator idGenerator;




    /**
     * 查询类别信息
     * @param list
     * @param categoryIds
     */
    void queryCategory(List<ShopProductVO> list,Long[] categoryIds)
    {
        try {
            //查询类别名称
            CategoryVO queryCategoryVO = new CategoryVO();
            queryCategoryVO.setIdArray(categoryIds);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategoryVO);
            ResultObjectVO resultObjectVO = categoryService.findByIdArray(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<CategoryVO> categoryVOS = resultObjectVO.formatDataList(CategoryVO.class);
                if (CollectionUtils.isNotEmpty(categoryVOS)) {
                    for (ShopProductVO shopProductVO : list) {
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
    void queryShopCategory(List<ShopProductVO> list,Long[] shopCategoryIds)
    {
        try {
            ShopCategoryVO queryShopCategoryVO = new ShopCategoryVO();
            queryShopCategoryVO.setIdArray(shopCategoryIds);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopCategoryVO);
            ResultObjectVO resultObjectVO = shopCategoryService.findByIdArray(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<ShopCategoryVO> shopCategoryVOS = resultObjectVO.formatDataList(ShopCategoryVO.class);
                if(CollectionUtils.isNotEmpty(shopCategoryVOS))
                {
                    for(ShopProductVO shopProductVO:list)
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
    void queryBrand(List<ShopProductVO> list,List<Long> brandIdList)
    {
        try {
            BrandVO queryBrandVO = new BrandVO();
            queryBrandVO.setIdList(brandIdList);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryBrandVO);
            ResultObjectVO resultObjectVO = brandService.findByIdList(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<BrandVO> brandVOS = resultObjectVO.formatDataList(BrandVO.class);
                if(CollectionUtils.isNotEmpty(brandVOS))
                {
                    for(ShopProductVO shopProductVO:list)
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
    void queryShop(List<ShopProductVO> list,List<Long> shopIdList)
    {
        try {
            SellerShopVO queryShopVO = new SellerShopVO();
            queryShopVO.setIdList(shopIdList);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopVO);
            ResultObjectVO resultObjectVO = sellerShopService.findByIdList(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<SellerShopVO> sellerShopVOS = resultObjectVO.formatDataList(SellerShopVO.class);
                if(CollectionUtils.isNotEmpty(sellerShopVOS))
                {
                    for(ShopProductVO shopProductVO:list)
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
            ResultObjectVO resultObjectVO = brandService.findByIdList(requestJsonVO);
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
            ResultObjectVO resultObjectVO = categoryService.findByIdArray(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:sku:list"})
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public TableVO list(@RequestBody ShopProductPageInfo pageInfo)
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
                resultObjectVO = categoryService.queryChildListByPid(requestJsonVO);
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
            resultObjectVO = shopProductService.queryListPage(requestJsonVO);
            if (resultObjectVO.getCode() == ResultObjectVO.SUCCESS) {
                if (resultObjectVO.getData() != null) {
                    Map<String, Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total") != null ? resultObjectDataMap.get("total") : "0")));
                    List<ShopProductVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), ShopProductVO.class);
                    if (CollectionUtils.isNotEmpty(list)) {
                        Long[] categoryIds = new Long[list.size()];
                        Long[] shopCategoryIds = new Long[list.size()];
                        List<Long> brandIdList = new LinkedList<>();
                        List<Long> shopIdList = new LinkedList<>();

                        boolean brandExists = false;
                        boolean shopCategoryExists = false;
                        boolean shopExists = false;
                        for (int i = 0; i < list.size(); i++) {
                            ShopProductVO shopProductVO = list.get(i);
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


                        for (ShopProductVO shopProductVO : list) {
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





    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:sku:tree"})
    @RequestMapping(value = "/query/category/tree",method = RequestMethod.POST)
    public ResultObjectVO queryCategoryTree(@RequestBody CategoryVO categoryVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryVO query = new CategoryVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            resultObjectVO = categoryService.queryTree(requestJsonVO);
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:sku:tree:pid"})
    @RequestMapping(value = "/query/category/tree/pid",method = RequestMethod.POST)
    public ResultObjectVO queryCategoryTreeByParentId(@RequestBody CategoryVO categoryVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryVO query = new CategoryVO();
            query.setParentId(categoryVO.getParentId() != null ? categoryVO.getParentId() : -1L);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            resultObjectVO = categoryService.queryListByPid(requestJsonVO);
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
     * 查询SKU列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:shopProduct:query:sku:list"})
    @RequestMapping(value = "/query/product/sku/list",method = RequestMethod.POST)
    public TableVO queryShopProductSkuList(@RequestBody ProductSkuPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            if(pageInfo.getShopProductId()==null)
            {
                tableVO.setCode(TableVO.FAILD);
                tableVO.setMsg("商品ID不能为空");
                return tableVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = productSkuService.queryListPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<ProductSkuVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), ProductSkuVO.class);
                    if(CollectionUtils.isNotEmpty(list))
                    {
                        for(ProductSkuVO productSkuVO:list)
                        {
                            if(productSkuVO.getProductPreviewPath()!=null) {
                                productSkuVO.setHttpMainPhoto(imageUploadService.getImageHttpPrefix()+productSkuVO.getProductPreviewPath());
                            }
                            if(productSkuVO.getDescriptionImgFilePath()!=null) {
                                productSkuVO.setHttpDescriptionImgPath(imageUploadService.getImageHttpPrefix()+productSkuVO.getDescriptionImgFilePath());
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




    /**
     * 店铺商品 上架/下架
     * @param shopProductVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:shelves"})
    @RequestMapping(value = "/shelves",method = RequestMethod.POST)
    public ResultObjectVO shelves(@RequestBody ShopProductVO shopProductVO)
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
            resultObjectVO = shopProductService.shelves(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:shopProduct:flushSearch"})
    @RequestMapping(value = "/flush/search",method = RequestMethod.POST)
    public ResultObjectVO flushSearch(@RequestBody ShopProductVO shopProductVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(shopProductVO.getIds()))
            {
                resultObjectVO.setCode(TableVO.FAILD);
                resultObjectVO.setMsg("要同步的店铺商品ID不能为空");
                return resultObjectVO;
            }
            ProductSkuVO queryProductSkuVO = new ProductSkuVO();
            queryProductSkuVO.setShopProductIdList(shopProductVO.getIds());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryProductSkuVO);
            resultObjectVO = productSkuService.queryListByShopProductIdList(requestJsonVO);
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


    /**
     * 查看商品详情
     * @param shopProductVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:shopProduct:detailPage"})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultObjectVO detail(@RequestBody ShopProductVO shopProductVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(shopProductVO == null || shopProductVO.getId() == null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到商品ID");
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), shopProductVO);
            ResultObjectVO detailResult = shopProductService.queryByShopProductId(requestJsonVO);
            if(detailResult.isSuccess())
            {
                List<ShopProductVO> list = detailResult.formatDataList(ShopProductVO.class);
                if(CollectionUtils.isNotEmpty(list))
                {
                    ShopProductVO vo = list.get(0);
                    // 填充主图HTTP路径
                    if(vo.getMainPhotoFilePath() != null)
                    {
                        vo.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix() + vo.getMainPhotoFilePath());
                    }
                    // 填充分类/店铺分类/品牌/店铺名称
                    List<ShopProductVO> tmpList = new LinkedList<>();
                    tmpList.add(vo);
                    Long[] categoryIds = new Long[1];
                    Long[] shopCategoryIds = new Long[1];
                    List<Long> brandIdList = new LinkedList<>();
                    List<Long> shopIdList = new LinkedList<>();
                    categoryIds[0] = vo.getCategoryId();
                    if(vo.getShopCategoryId() != null) {
                        shopCategoryIds[0] = vo.getShopCategoryId();
                    }
                    if(vo.getBrandId() != null) {
                        brandIdList.add(vo.getBrandId());
                    }
                    if(vo.getShopId() != null) {
                        shopIdList.add(vo.getShopId());
                    }
                    this.queryCategory(tmpList, categoryIds);
                    this.queryShopCategory(tmpList, shopCategoryIds);
                    this.queryBrand(tmpList, brandIdList);
                    this.queryShop(tmpList, shopIdList);
                    // 填充预览图HTTP路径
                    if(CollectionUtils.isNotEmpty(vo.getPreviewPhotoPaths())) {
                        vo.setHttpPreviewPhotoPaths(new LinkedList<>());
                        for(String previewPhotoPath : vo.getPreviewPhotoPaths()) {
                            vo.getHttpPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix() + previewPhotoPath);
                        }
                    }
                    // 填充SKU预览图HTTP路径
                    if(CollectionUtils.isNotEmpty(vo.getProductSkuVOList())) {
                        vo.setHttpSkuPreviewPhotoPaths(new LinkedList<>());
                        for(ProductSkuVO productSkuVO : vo.getProductSkuVOList()) {
                            if(StringUtils.isNotEmpty(productSkuVO.getProductPreviewPath())) {
                                productSkuVO.setHttpMainPhoto(imageUploadService.getImageHttpPrefix() + productSkuVO.getProductPreviewPath());
                                vo.getHttpSkuPreviewPhotoPaths().add(productSkuVO.getHttpMainPhoto());
                            }
                            if(StringUtils.isNotEmpty(productSkuVO.getDescriptionImgFilePath())) {
                                productSkuVO.setHttpDescriptionImgPath(imageUploadService.getImageHttpPrefix() + productSkuVO.getDescriptionImgFilePath());
                            }
                        }
                    }
                    // 填充商品介绍图片HTTP路径
                    if(vo.getShopProductDescriptionVO() != null) {
                        if(CollectionUtils.isNotEmpty(vo.getShopProductDescriptionVO().getProductDescriptionImgs())) {
                            for(ShopProductDescriptionImgVO imgVO : vo.getShopProductDescriptionVO().getProductDescriptionImgs()) {
                                imgVO.setHttpFilePath(imageUploadService.getImageHttpPrefix() + imgVO.getFilePath());
                            }
                            vo.setShopProductDescriptionJson(JSONObject.toJSONString(vo.getShopProductDescriptionVO()));
                        }
                    }
                    resultObjectVO.setData(vo);
                }else
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("商品不存在");
                }
            }else
            {
                resultObjectVO.setMsg(detailResult.getMsg());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }



}
