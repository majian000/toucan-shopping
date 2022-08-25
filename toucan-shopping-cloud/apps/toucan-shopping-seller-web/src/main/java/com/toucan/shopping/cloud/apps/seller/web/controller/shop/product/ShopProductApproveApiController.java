package com.toucan.shopping.cloud.apps.seller.web.controller.shop.product;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.redis.ShopProductRedisKey;
import com.toucan.shopping.cloud.apps.seller.web.util.VCodeUtil;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyValueService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductApproveService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.constant.ProductConstant;
import com.toucan.shopping.modules.product.entity.ShopProductApproveDescriptionImg;
import com.toucan.shopping.modules.product.page.ShopProductApprovePageInfo;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 店铺商品审核信息
 */
@Controller("shopProductApproveApiController")
@RequestMapping("/api/shop/product/approve")
public class ShopProductApproveApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignShopProductApproveService feignShopProductApproveService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private FeignCategoryService feignCategoryService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignShopCategoryService feignShopCategoryService;


    @Autowired
    private FeignAttributeKeyValueService feignAttributeKeyValueService;


    @Autowired
    private ToucanStringRedisService toucanStringRedisService;


    private String[] imageExtScope = new String[]{".JPG",".JPEG",".PNG"};




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
     * 查询详情
     * @param queryShopProductApproveVO
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteByApproveId(HttpServletRequest request,@RequestBody ShopProductApproveVO queryShopProductApproveVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            String userMainId="-1";
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            //查询店铺
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    SellerShopVO sellerShopVORet = resultObjectVO.formatData(SellerShopVO.class);
                    ShopProductApproveVO shopProductApproveVO = new ShopProductApproveVO();
                    shopProductApproveVO.setId(queryShopProductApproveVO.getId());
                    shopProductApproveVO.setShopId(sellerShopVORet.getId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), shopProductApproveVO);
                    resultObjectVO = feignShopProductApproveService.deleteByProductApproveIdAndShopId(requestJsonVO);

                }
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("删除失败,请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 查询详情
     * @param queryShopProductApproveVO
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO detail(HttpServletRequest request,@RequestBody ShopProductApproveVO queryShopProductApproveVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            String userMainId="-1";
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            //查询店铺
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null)
                {
                    SellerShopVO sellerShopVORet = resultObjectVO.formatData(SellerShopVO.class);
                    ShopProductApproveVO shopProductApproveVO = new ShopProductApproveVO();
                    shopProductApproveVO.setId(queryShopProductApproveVO.getId());
                    shopProductApproveVO.setShopId(sellerShopVORet.getId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), shopProductApproveVO);
                    resultObjectVO = feignShopProductApproveService.queryByProductApproveIdAndShopId(requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        shopProductApproveVO = resultObjectVO.formatData(ShopProductApproveVO.class);
                        CategoryVO queryCateogry = new CategoryVO();
                        queryCateogry.setId(shopProductApproveVO.getCategoryId());
                        requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), queryCateogry);

                        //查询分类
                        ResultObjectVO resultCategoryObjectVO = feignCategoryService.findIdPathById(requestJsonVO);
                        if(resultCategoryObjectVO.isSuccess()&&resultCategoryObjectVO.getData()!=null)
                        {
                            CategoryVO categoryVO = resultCategoryObjectVO.formatData(CategoryVO.class);
                            List<String> categoryIdPath = new LinkedList<>();
                            List<String> categoryNamePath = new LinkedList<>();
                            if(CollectionUtils.isNotEmpty(categoryVO.getIdPath()))
                            {
                                for(Long categoryId:categoryVO.getIdPath())
                                {
                                    categoryIdPath.add(String.valueOf(categoryId));
                                }
                            }
                            if(CollectionUtils.isNotEmpty(categoryVO.getNamePaths()))
                            {
                                for(String categoryName:categoryVO.getNamePaths())
                                {
                                    categoryNamePath.add(String.valueOf(categoryName));
                                }
                            }
                            shopProductApproveVO.setCategoryIdPath(categoryIdPath);
                            shopProductApproveVO.setCategoryNamePath(categoryNamePath);

                        }


                        //查询店铺分类
                        if(shopProductApproveVO.getShopCategoryId()!=null) {
                            ShopCategoryVO queryShopCateogry = new ShopCategoryVO();
                            queryShopCateogry.setId(shopProductApproveVO.getShopCategoryId());
                            requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), queryShopCateogry);
                            ResultObjectVO resultShopCategoryObjectVO = feignShopCategoryService.findIdPathById(requestJsonVO);
                            if (resultShopCategoryObjectVO.isSuccess() && resultShopCategoryObjectVO.getData() != null) {
                                ShopCategoryVO shopCategoryVO = resultShopCategoryObjectVO.formatData(ShopCategoryVO.class);
                                List<String> shopCategoryIdPath = new LinkedList<>();
                                List<String> shopCategoryNamePath = new LinkedList<>();
                                if (CollectionUtils.isNotEmpty(shopCategoryVO.getIdPath())) {
                                    for (Long shopCategoryId : shopCategoryVO.getIdPath()) {
                                        shopCategoryIdPath.add(String.valueOf(shopCategoryId));
                                    }
                                }
                                if (CollectionUtils.isNotEmpty(shopCategoryVO.getNamePaths())) {
                                    for (String shopCategoryName : shopCategoryVO.getNamePaths()) {
                                        shopCategoryNamePath.add(String.valueOf(shopCategoryName));
                                    }
                                }
                                shopProductApproveVO.setShopCategoryIdPath(shopCategoryIdPath);
                                shopProductApproveVO.setShopCategoryNamePath(shopCategoryNamePath);

                            }
                        }

                    }
                    if(StringUtils.isNotEmpty(shopProductApproveVO.getMainPhotoFilePath()))
                    {
                        shopProductApproveVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+shopProductApproveVO.getMainPhotoFilePath());
                    }
                    if(CollectionUtils.isNotEmpty(shopProductApproveVO.getPreviewPhotoPaths()))
                    {
                        List<String> httpPreviewPhotos = new LinkedList<>();
                        for(String previewPhoto:shopProductApproveVO.getPreviewPhotoPaths())
                        {
                            httpPreviewPhotos.add(imageUploadService.getImageHttpPrefix()+previewPhoto);
                        }
                        shopProductApproveVO.setHttpPreviewPhotoPaths(httpPreviewPhotos);
                    }

                    if(CollectionUtils.isNotEmpty(shopProductApproveVO.getProductSkuVOList())) {
                        List<ShopProductApproveSkuAttribute> skuAttributes = new LinkedList<>();
                        for(ShopProductApproveSkuVO shopProductApproveSkuVO:shopProductApproveVO.getProductSkuVOList())
                        {
                            //设置商品预览图
                            if(StringUtils.isNotEmpty(shopProductApproveSkuVO.getProductPreviewPath()))
                            {
                                shopProductApproveSkuVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+shopProductApproveSkuVO.getProductPreviewPath());
                            }

                            Map<String,String> attributeKeyValue = JSONObject.parseObject(shopProductApproveSkuVO.getAttributes(),Map.class);
                            Set<String> keysSet = attributeKeyValue.keySet();
                            for(String key:keysSet)
                            {
                                ShopProductApproveSkuAttribute shopProductApproveSkuAttribute = null;
                                Optional<ShopProductApproveSkuAttribute> shopProductApproveSkuAttributeOptional = skuAttributes.stream().filter(item -> item.getKey().equals(key)).findFirst();
                                //如果不存在属性名对象就创建
                                if(!shopProductApproveSkuAttributeOptional.isPresent())
                                {
                                    shopProductApproveSkuAttribute = new ShopProductApproveSkuAttribute();
                                    shopProductApproveSkuAttribute.setKey(key);
                                    shopProductApproveSkuAttribute.setValues(new LinkedList<>());
                                    skuAttributes.add(shopProductApproveSkuAttribute);
                                }else{
                                    shopProductApproveSkuAttribute = shopProductApproveSkuAttributeOptional.get();
                                }

                                //如果这个属性名没有任何属性值就直接添加
                                if(CollectionUtils.isEmpty(shopProductApproveSkuAttribute.getValues()))
                                {
                                    shopProductApproveSkuAttribute.getValues().add(attributeKeyValue.get(key));
                                }else{
                                    //判断是否有重复
                                    Optional<String> shopProductApproveSkuAttributeValueOptional = shopProductApproveSkuAttribute.getValues().stream().filter(item -> item.equals(attributeKeyValue.get(key))).findFirst();
                                    if(!shopProductApproveSkuAttributeValueOptional.isPresent())
                                    {
                                        shopProductApproveSkuAttribute.getValues().add(attributeKeyValue.get(key));
                                    }
                                }

                            }
                        }

                        shopProductApproveVO.setSkuAttributes(skuAttributes);
                    }
                    if(shopProductApproveVO.getShopProductApproveDescriptionVO()!=null
                            &&CollectionUtils.isNotEmpty(shopProductApproveVO.getShopProductApproveDescriptionVO().getProductDescriptionImgs()))
                    {
                        for(ShopProductApproveDescriptionImgVO shopProductApproveDescriptionImgVO:shopProductApproveVO.getShopProductApproveDescriptionVO().getProductDescriptionImgs())
                        {
                            shopProductApproveDescriptionImgVO.setHttpFilePath(imageUploadService.getImageHttpPrefix()+shopProductApproveDescriptionImgVO.getFilePath());
                        }
                    }
                    resultObjectVO.setData(shopProductApproveVO);
                }
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO list(HttpServletRequest httpServletRequest,@RequestBody ShopProductApprovePageInfo pageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            if(pageInfo==null)
            {
                pageInfo = new ShopProductApprovePageInfo();
            }
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if(StringUtils.isEmpty(userMainId))
            {
                logger.warn("查询商品审核 没有找到用户ID {} ",userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,请稍后重试");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if(sellerShopVO!=null) {
                    pageInfo.setShopId(sellerShopVO.getId());
                    pageInfo.setOrderColumn("update_date");
                    pageInfo.setOrderSort("desc");
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), pageInfo);
                    resultObjectVO = feignShopProductApproveService.queryListPage(requestJsonVO);
                    if (resultObjectVO.isSuccess()&&resultObjectVO.getData() != null) {
                        Map<String, Object> resultObjectDataMap = (Map<String, Object>) resultObjectVO.getData();
                        List<ShopProductApproveVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), ShopProductApproveVO.class);
                        if (CollectionUtils.isNotEmpty(list)) {

                            Long[] categoryIds = new Long[list.size()];
                            boolean categoryExists = false;
                            for (int i = 0; i < list.size(); i++) {
                                ShopProductApproveVO shopProductVO = list.get(i);


                                if(StringUtils.isNotEmpty(shopProductVO.getMainPhotoFilePath()))
                                {
                                    shopProductVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+shopProductVO.getMainPhotoFilePath());
                                }

                                //设置店铺分类ID
                                categoryExists = false;
                                for (int sci = 0; sci < categoryIds.length; sci++) {
                                    Long categoryId = categoryIds[sci];
                                    if (shopProductVO.getCategoryId() != null && categoryId != null
                                            && categoryId.longValue() == shopProductVO.getCategoryId().longValue()) {
                                        categoryExists = true;
                                        break;
                                    }

                                }
                                if (!categoryExists) {
                                    if (shopProductVO.getCategoryId() != null) {
                                        categoryIds[i] = shopProductVO.getCategoryId();
                                    }
                                }

                            }

                            //查询类别名称
                            this.queryCategory(list, categoryIds);

                            resultObjectDataMap.put("list",list);

                            resultObjectVO.setData(resultObjectDataMap);
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }

        return resultObjectVO;
    }


    /**
     * 上传商品介绍中的图片
     * @param publishProductVO
     */
    void uploadProductDescriptionImgForPublish(PublishProductApproveVO publishProductVO) throws Exception {
        //校验商品介绍
        if(publishProductVO.getProductDescription()!=null&&CollectionUtils.isNotEmpty(publishProductVO.getProductDescription().getProductDescriptionImgs()))
        {
            int sort = publishProductVO.getProductDescription().getProductDescriptionImgs().size();
            for(ShopProductApproveDescriptionImgVO shopProductApproveDescriptionImgVO:publishProductVO.getProductDescription().getProductDescriptionImgs()) {
                if(shopProductApproveDescriptionImgVO!=null&&shopProductApproveDescriptionImgVO.getImgFile()!=null) {
                    shopProductApproveDescriptionImgVO.setFilePath(imageUploadService.uploadFile(shopProductApproveDescriptionImgVO.getImgFile().getBytes(), ImageUtils.getImageExt(shopProductApproveDescriptionImgVO.getImgFile().getOriginalFilename())));
                    shopProductApproveDescriptionImgVO.setImgFile(null);
                }
                shopProductApproveDescriptionImgVO.setImgSort(sort);
                sort--;
            }
        }
    }



    /**
     * 上传商品介绍中的图片
     * @param rePublishProductApproveVO
     */
    void uploadProductDescriptionImgForRePublish(RePublishProductApproveVO rePublishProductApproveVO,ShopProductApproveVO republishProductVOPersistent) throws Exception {
        //如果有旧的商品介绍图片,判断是否删除了,如果删除了 就把那个商品介绍图片资源删除
        if(republishProductVOPersistent.getShopProductApproveDescriptionVO()!=null)
        {
            if(CollectionUtils.isNotEmpty(republishProductVOPersistent.getShopProductApproveDescriptionVO().getProductDescriptionImgs()))
            {
                ShopProductApproveDescriptionVO shopProductApproveDescriptionVOPersistent = republishProductVOPersistent.getShopProductApproveDescriptionVO();
                ShopProductApproveDescriptionVO  shopProductApproveDescriptionVO = rePublishProductApproveVO.getProductDescription();
                boolean isFind; //是否在这次提交中找到了旧的图片路径
                for(int i=0;i<shopProductApproveDescriptionVOPersistent.getProductDescriptionImgs().size();i++)
                {
                    isFind = false;
                    ShopProductApproveDescriptionImgVO shopProductApproveDescriptionImgVOPersistent = shopProductApproveDescriptionVOPersistent.getProductDescriptionImgs().get(i);
                    if(shopProductApproveDescriptionVO!=null&&shopProductApproveDescriptionVO.getProductDescriptionImgs()!=null) {
                        for (int j = 0; j < shopProductApproveDescriptionVO.getProductDescriptionImgs().size(); j++) {
                            ShopProductApproveDescriptionImgVO shopProductApproveDescriptionImgVO = shopProductApproveDescriptionVO.getProductDescriptionImgs().get(j);
                            if (StringUtils.isNotEmpty(shopProductApproveDescriptionImgVOPersistent.getFilePath())
                                    && StringUtils.isNotEmpty(shopProductApproveDescriptionImgVO.getFilePath())) {
                                if (shopProductApproveDescriptionImgVOPersistent.getFilePath().equals(shopProductApproveDescriptionImgVO.getFilePath())) {
                                    isFind = true;
                                    break;
                                }
                            }
                        }
                    }
                    //如果没找到这个图片,就删除掉
                    if(!isFind)
                    {
                        imageUploadService.deleteFile(shopProductApproveDescriptionImgVOPersistent.getFilePath());
                    }
                }
            }
        }
        //校验商品介绍
        if (rePublishProductApproveVO.getProductDescription() != null && CollectionUtils.isNotEmpty(rePublishProductApproveVO.getProductDescription().getProductDescriptionImgs())) {
            int sort = rePublishProductApproveVO.getProductDescription().getProductDescriptionImgs().size();
            for (ShopProductApproveDescriptionImgVO shopProductApproveDescriptionImgVO : rePublishProductApproveVO.getProductDescription().getProductDescriptionImgs()) {
                if (shopProductApproveDescriptionImgVO != null && shopProductApproveDescriptionImgVO.getImgFile() != null) {
                    shopProductApproveDescriptionImgVO.setFilePath(imageUploadService.uploadFile(shopProductApproveDescriptionImgVO.getImgFile().getBytes(), ImageUtils.getImageExt(shopProductApproveDescriptionImgVO.getImgFile().getOriginalFilename())));
                    shopProductApproveDescriptionImgVO.setImgFile(null);
                }
                shopProductApproveDescriptionImgVO.setImgSort(sort);
                sort--;
            }
        }
    }



    /**
     * 发布商品
     * @param request
     * @param previewPhotoFiles
     * @param publishProductVO
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping(value = "/publish",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO publish(HttpServletRequest request, @RequestParam List<MultipartFile> previewPhotoFiles, PublishProductApproveVO publishProductVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{

            if(StringUtils.isEmpty(publishProductVO.getVcode()))
            {
                resultObjectVO.setMsg("请输入验证码");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            String cookie = request.getHeader("Cookie");
            if(StringUtils.isEmpty(cookie))
            {
                resultObjectVO.setMsg("请重新刷新验证码");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String clientVCodeId = VCodeUtil.getClientVCodeId(cookie);
            if(StringUtils.isEmpty(clientVCodeId))
            {
                resultObjectVO.setMsg("验证码输入有误");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String vcodeRedisKey = ShopProductRedisKey.getVerifyCodeKey(this.getAppCode(),clientVCodeId);
            Object vCodeObject = toucanStringRedisService.get(vcodeRedisKey);
            if(vCodeObject==null)
            {
                resultObjectVO.setMsg("验证码过期请刷新");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            if(!StringUtils.equals(publishProductVO.getVcode().toUpperCase(),String.valueOf(vCodeObject).toUpperCase()))
            {
                resultObjectVO.setMsg("验证码输入有误");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            //删除缓存中验证码
            toucanStringRedisService.delete(vcodeRedisKey);



            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            publishProductVO.setCreateUserId(Long.parseLong(userMainId));

            //查询这个用户下的店铺
            UserVO queryUserVO = new UserVO();
            queryUserVO.setUserMainId(Long.parseLong(UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()))));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryUserVO);
            resultObjectVO = feignSellerShopService.findByUser(toucan.getAppCode(),requestJsonVO);
            if(!resultObjectVO.isSuccess()) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("发布失败,请稍后重试!");
                return resultObjectVO;
            }
            SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
            if (sellerShopVO == null||(sellerShopVO.getEnableStatus()!=null&&sellerShopVO.getEnableStatus().intValue()==0)) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("发布失败,当前店铺被禁用!");
                return resultObjectVO;
            }


            //校验商品主图
            if(publishProductVO.getMainPhotoFile()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("发布失败,请上传商品主图!");
                return resultObjectVO;
            }

            //校验SKU列表
            if(CollectionUtils.isEmpty(publishProductVO.getProductSkuVOList()))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("发布失败,SKU列表不能为空!");
                return resultObjectVO;
            }

            //判断商品预览图数量
            if(CollectionUtils.isNotEmpty(previewPhotoFiles))
            {
                if(previewPhotoFiles.size()>6)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("发布失败,商品预览图最大数量为6个!");
                    return resultObjectVO;
                }
            }

            if(!CollectionUtils.isEmpty(publishProductVO.getProductSkuVOList())) {
                for (ShopProductApproveSkuVO productSkuVO : publishProductVO.getProductSkuVOList()) {
                    if(StringUtils.isEmpty(productSkuVO.getAttributes()))
                    {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("发布失败,SKU列表属性值不能为空!");
                        return resultObjectVO;
                    }
                }
            }

            publishProductVO.setAppCode("10001001");

            //上传商品介绍图片
            this.uploadProductDescriptionImgForPublish(publishProductVO);

            //SKU属性表格式化成Map
            if(!CollectionUtils.isEmpty(publishProductVO.getProductSkuVOList())) {
                for (ShopProductApproveSkuVO productSkuVO : publishProductVO.getProductSkuVOList()) {
                    productSkuVO.setOnlyName(publishProductVO.getName());
                    productSkuVO.setAttributeMap(JSONObject.parseObject(productSkuVO.getAttributes(), HashMap.class));
                    String name = publishProductVO.getName();
                    Set<String> keys = productSkuVO.getAttributeMap().keySet();
                    Iterator keyIt = keys.iterator();
                    int pos = 0;
                    StringBuilder attributeValueGroup = new StringBuilder();
                    while(keyIt.hasNext())
                    {
                        if(pos!=0)
                        {
                            attributeValueGroup.append("_");
                        }
                        String value = productSkuVO.getAttributeMap().get(keyIt.next());
                        name+=" " +value;
                        attributeValueGroup.append(value);
                        pos++;
                    }
                    productSkuVO.setAttributeValueGroup(attributeValueGroup.toString());
                    productSkuVO.setName(name);
                    productSkuVO.setAppCode(publishProductVO.getAppCode());
                }
            }

            if(!ImageUtils.isImage(publishProductVO.getMainPhotoFile().getOriginalFilename(),imageExtScope))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("发布失败,商品主图的格式只能为:JPG、JPEG、PNG!");
                return resultObjectVO;
            }

            //校验商品预览图
            if(CollectionUtils.isNotEmpty(previewPhotoFiles)) {

                for(MultipartFile multipartFile:previewPhotoFiles)
                {
                    if(!ImageUtils.isImage(multipartFile.getOriginalFilename(),imageExtScope))
                    {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("发布失败,商品预览图格式只能为:JPG、JPEG、PNG!");
                        return resultObjectVO;
                    }
                }
            }

            //校验SKU主图
            if(!CollectionUtils.isEmpty(publishProductVO.getProductSkuVOList())){
                for(ShopProductApproveSkuVO productSkuVO: publishProductVO.getProductSkuVOList())
                {
                    if(productSkuVO.getMainPhotoFile()==null)
                    {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("发布失败,的商品主图不能为空!");
                        return resultObjectVO;
                    }
                    if(!ImageUtils.isImage(productSkuVO.getMainPhotoFile().getOriginalFilename(),imageExtScope))
                    {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("发布失败,SKU中的商品主图格式只能为:JPG、JPEG、PNG!");
                        return resultObjectVO;
                    }
                }
            }

            //上传商品主图
            publishProductVO.setMainPhotoFilePath(imageUploadService.uploadFile(publishProductVO.getMainPhotoFile().getBytes(),ImageUtils.getImageExt(publishProductVO.getMainPhotoFile().getOriginalFilename())));
            publishProductVO.setMainPhotoFile(null);
            if(StringUtils.isEmpty(publishProductVO.getMainPhotoFilePath()))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("发布失败,商品主图上传失败!");
                return resultObjectVO;
            }

            //上传SKU表商品主图
            for(ShopProductApproveSkuVO productSkuVO: publishProductVO.getProductSkuVOList())
            {
                productSkuVO.setProductPreviewPath(imageUploadService.uploadFile(productSkuVO.getMainPhotoFile().getBytes(),ImageUtils.getImageExt(productSkuVO.getMainPhotoFile().getOriginalFilename())));
                productSkuVO.setMainPhotoFile(null);
                if(StringUtils.isEmpty(productSkuVO.getProductPreviewPath()))
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("发布失败,商品主图上传失败!");
                    return resultObjectVO;
                }
            }

            //上传商品预览图
            if(CollectionUtils.isNotEmpty(previewPhotoFiles))
            {
                publishProductVO.setPreviewPhotoPaths(new LinkedList<>());
                for(MultipartFile multipartFile:previewPhotoFiles)
                {
                    String productPreviewPath = imageUploadService.uploadFile(multipartFile.getBytes(),ImageUtils.getImageExt(multipartFile.getOriginalFilename()));
                    if(StringUtils.isEmpty(productPreviewPath))
                    {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("发布失败,商品预览图上传失败!");
                        return resultObjectVO;
                    }
                    publishProductVO.getPreviewPhotoPaths().add(productPreviewPath);
                }
            }

            publishProductVO.setShopId(sellerShopVO.getId());
            publishProductVO.setCreateUserId(queryUserVO.getUserMainId());
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), publishProductVO);
            resultObjectVO = feignShopProductApproveService.publish(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                resultObjectVO.setMsg("发布成功");
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("发布失败!");
        }
        return resultObjectVO;
    }


    /**
     * 删除旧的商品图片
     * @param imagePath
     */
    void deleteOldProductImage(String imagePath)
    {
        try{
            imageUploadService.deleteFile(imagePath);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }

    /**
     * 重新发布
     * @param request
     * @param previewPhotoFiles
     * @param republishProductVO
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping(value = "/republish",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO republish(HttpServletRequest request, @RequestParam List<MultipartFile> previewPhotoFiles, RePublishProductApproveVO republishProductVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{

            if(StringUtils.isEmpty(republishProductVO.getVcode()))
            {
                resultObjectVO.setMsg("请输入验证码");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            String cookie = request.getHeader("Cookie");
            if(StringUtils.isEmpty(cookie))
            {
                resultObjectVO.setMsg("请重新刷新验证码");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String clientVCodeId = VCodeUtil.getClientVCodeId(cookie);
            if(StringUtils.isEmpty(clientVCodeId))
            {
                resultObjectVO.setMsg("验证码输入有误");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String vcodeRedisKey = ShopProductRedisKey.getVerifyCodeKey(this.getAppCode(),clientVCodeId);
            Object vCodeObject = toucanStringRedisService.get(vcodeRedisKey);
            if(vCodeObject==null)
            {
                resultObjectVO.setMsg("验证码过期请刷新");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            if(!StringUtils.equals(republishProductVO.getVcode().toUpperCase(),String.valueOf(vCodeObject).toUpperCase()))
            {
                resultObjectVO.setMsg("验证码输入有误");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            //删除缓存中验证码
            toucanStringRedisService.delete(vcodeRedisKey);


            if(republishProductVO.getId()==null)
            {
                resultObjectVO.setMsg("ID不能为空");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            republishProductVO.setCreateUserId(Long.parseLong(userMainId));

            //查询这个用户下的店铺
            UserVO queryUserVO = new UserVO();
            queryUserVO.setUserMainId(Long.parseLong(UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()))));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryUserVO);
            resultObjectVO = feignSellerShopService.findByUser(toucan.getAppCode(),requestJsonVO);
            if(!resultObjectVO.isSuccess()) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("发布失败,请稍后重试!");
                return resultObjectVO;
            }
            SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
            if (sellerShopVO == null||(sellerShopVO.getEnableStatus()!=null&&sellerShopVO.getEnableStatus().intValue()==0)) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("发布失败,当前店铺被禁用!");
                return resultObjectVO;
            }

            //查询库中的商品信息
            SellerShopVO sellerShopVORet = resultObjectVO.formatData(SellerShopVO.class);
            ShopProductApproveVO shopProductApproveVO = new ShopProductApproveVO();
            shopProductApproveVO.setId(republishProductVO.getId());
            shopProductApproveVO.setShopId(sellerShopVORet.getId());
            requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), shopProductApproveVO);
            resultObjectVO = feignShopProductApproveService.queryByProductApproveIdAndShopId(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                shopProductApproveVO = resultObjectVO.formatData(ShopProductApproveVO.class);
            }
            if(shopProductApproveVO.getApproveStatus().intValue()== ProductConstant.PASS.intValue())
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("该商品已审核通过,如要修改请在商品列表中操作!");
                return resultObjectVO;
            }

            //校验SKU列表
            if(CollectionUtils.isEmpty(republishProductVO.getProductSkuVOList()))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("发布失败,SKU列表不能为空!");
                return resultObjectVO;
            }

            //判断商品预览图数量
            if(CollectionUtils.isNotEmpty(previewPhotoFiles))
            {
                if(previewPhotoFiles.size()>6)
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("发布失败,商品预览图最大数量为6个!");
                    return resultObjectVO;
                }
            }

            if(!CollectionUtils.isEmpty(republishProductVO.getProductSkuVOList())) {
                for (ShopProductApproveSkuVO productSkuVO : republishProductVO.getProductSkuVOList()) {
                    if(StringUtils.isEmpty(productSkuVO.getAttributes()))
                    {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("发布失败,SKU列表属性值不能为空!");
                        return resultObjectVO;
                    }
                }
            }

            republishProductVO.setAppCode("10001001");

            //SKU属性表格式化成Map
            if(!CollectionUtils.isEmpty(republishProductVO.getProductSkuVOList())) {
                for (ShopProductApproveSkuVO productSkuVO : republishProductVO.getProductSkuVOList()) {
                    productSkuVO.setOnlyName(republishProductVO.getName());
                    productSkuVO.setAttributeMap(JSONObject.parseObject(productSkuVO.getAttributes(), HashMap.class));
                    String name = republishProductVO.getName();
                    Set<String> keys = productSkuVO.getAttributeMap().keySet();
                    Iterator keyIt = keys.iterator();
                    int pos = 0;
                    StringBuilder attributeValueGroup = new StringBuilder();
                    while(keyIt.hasNext())
                    {
                        if(pos!=0)
                        {
                            attributeValueGroup.append("_");
                        }
                        String value = productSkuVO.getAttributeMap().get(keyIt.next());
                        name+=" " +value;
                        attributeValueGroup.append(value);
                        pos++;
                    }
                    productSkuVO.setAttributeValueGroup(attributeValueGroup.toString());
                    productSkuVO.setName(name);
                    productSkuVO.setAppCode(republishProductVO.getAppCode());
                }
            }

            if(republishProductVO.getMainPhotoFile()!=null&&!ImageUtils.isImage(republishProductVO.getMainPhotoFile().getOriginalFilename(),imageExtScope))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("发布失败,商品主图的格式只能为:JPG、JPEG、PNG!");
                return resultObjectVO;
            }

            //校验商品预览图
            if(CollectionUtils.isNotEmpty(previewPhotoFiles)) {

                for(MultipartFile multipartFile:previewPhotoFiles)
                {
                    if(!ImageUtils.isImage(multipartFile.getOriginalFilename(),imageExtScope))
                    {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("发布失败,商品预览图格式只能为:JPG、JPEG、PNG!");
                        return resultObjectVO;
                    }
                }
            }

            //校验SKU主图
            if(!CollectionUtils.isEmpty(republishProductVO.getProductSkuVOList())){
                for(ShopProductApproveSkuVO productSkuVO: republishProductVO.getProductSkuVOList())
                {
                    if(productSkuVO.getMainPhotoFile()!=null&&!ImageUtils.isImage(productSkuVO.getMainPhotoFile().getOriginalFilename(),imageExtScope))
                    {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("发布失败,SKU中的商品主图格式只能为:JPG、JPEG、PNG!");
                        return resultObjectVO;
                    }
                }
            }

            //上传商品主图
            if(republishProductVO.getMainPhotoFile()!=null) {
                republishProductVO.setMainPhotoFilePath(imageUploadService.uploadFile(republishProductVO.getMainPhotoFile().getBytes(), ImageUtils.getImageExt(republishProductVO.getMainPhotoFile().getOriginalFilename())));
                republishProductVO.setMainPhotoFile(null);
                if (StringUtils.isEmpty(republishProductVO.getMainPhotoFilePath())) {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("发布失败,商品主图上传失败!");
                    return resultObjectVO;
                }
                //删除旧的商品主图
                this.deleteOldProductImage(shopProductApproveVO.getMainPhotoFilePath());
            }else{
                republishProductVO.setMainPhotoFilePath(shopProductApproveVO.getMainPhotoFilePath());
            }


            //上传SKU表商品主图
            for(ShopProductApproveSkuVO productSkuVO: republishProductVO.getProductSkuVOList())
            {
                if(productSkuVO.getMainPhotoFile()!=null) {
                    productSkuVO.setProductPreviewPath(imageUploadService.uploadFile(productSkuVO.getMainPhotoFile().getBytes(), ImageUtils.getImageExt(productSkuVO.getMainPhotoFile().getOriginalFilename())));
                    productSkuVO.setMainPhotoFile(null);
                    if (StringUtils.isEmpty(productSkuVO.getProductPreviewPath())) {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("发布失败,商品主图上传失败!");
                        return resultObjectVO;
                    }
                }else{ //找到库中保存的商品主图路径设置进去
                    for(ShopProductApproveSkuVO oldProductSkuVO:shopProductApproveVO.getProductSkuVOList())
                    {
                        if(productSkuVO.getId()!=null&&productSkuVO.getId().longValue()==oldProductSkuVO.getId().longValue())
                        {
                            productSkuVO.setProductPreviewPath(oldProductSkuVO.getProductPreviewPath());
                        }
                    }

                }
            }

            //删除旧的SKU商品图片
            if(CollectionUtils.isNotEmpty(shopProductApproveVO.getProductSkuVOList()))
            {
                boolean previewIsChange=false;
                for(ShopProductApproveSkuVO oldProductSkuVO:shopProductApproveVO.getProductSkuVOList())
                {
                    previewIsChange = false;
                    for(ShopProductApproveSkuVO productSkuVO: republishProductVO.getProductSkuVOList())
                    {
                        if(productSkuVO.getId()!=null&&oldProductSkuVO.getId().longValue()==productSkuVO.getId().longValue())
                        {
                            if(productSkuVO.getMainPhotoFile()!=null) {
                                previewIsChange = true;
                            }
                        }
                    }
                    //图片修改了,那么就删除旧的图片
                    if(previewIsChange)
                    {
                        this.deleteOldProductImage(oldProductSkuVO.getProductPreviewPath());
                    }
                }
            }

            List<String> reuploadPreviewPhotoPaths = new LinkedList<>();
            //上传商品预览图
            if(CollectionUtils.isNotEmpty(previewPhotoFiles))
            {
                for(MultipartFile multipartFile:previewPhotoFiles)
                {
                    String productPreviewPath = imageUploadService.uploadFile(multipartFile.getBytes(),ImageUtils.getImageExt(multipartFile.getOriginalFilename()));
                    if(StringUtils.isEmpty(productPreviewPath))
                    {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("发布失败,商品预览图上传失败!");
                        return resultObjectVO;
                    }
                    reuploadPreviewPhotoPaths.add(productPreviewPath);
                }
            }

            //如果进行了删除操作
            if(StringUtils.isNotEmpty(republishProductVO.getPreviewPhotoDelPosArray())) {
                int reuploadPos=0;
                //拿到删除的图片下标
                String[] deletePreviewPhotoPosArray = republishProductVO.getPreviewPhotoDelPosArray().split(",");
                if (deletePreviewPhotoPosArray != null && deletePreviewPhotoPosArray.length > 0) {
                    Integer[] deletePreviewPhotoPosIntArray = new Integer[deletePreviewPhotoPosArray.length];
                    for(int p=0;p<deletePreviewPhotoPosArray.length;p++)
                    {
                        deletePreviewPhotoPosIntArray[p] = Integer.parseInt(deletePreviewPhotoPosArray[p]);
                    }
                    //将下标排序
                    Arrays.sort(deletePreviewPhotoPosIntArray);
                    //过滤删除的商品预览图和替换上传的商品预览图
                    List<String> releasePreviewPhotoPaths = new LinkedList<>();

                    for (Integer deletePreviewPhotoPos : deletePreviewPhotoPosIntArray) {
                        for (int i = 0; i < shopProductApproveVO.getPreviewPhotoPaths().size(); i++) {
                            if (deletePreviewPhotoPos.intValue()==i) {
                                this.deleteOldProductImage(shopProductApproveVO.getPreviewPhotoPaths().get(i));
                                //将删除的设置成-1
                                shopProductApproveVO.getPreviewPhotoPaths().set(i,"-1");
                                break;
                            }
                        }
                    }

                    //过滤掉删除的那些预览图
                    for(String previewPhotoPath:shopProductApproveVO.getPreviewPhotoPaths())
                    {
                        if(!"-1".equals(previewPhotoPath))
                        {
                            releasePreviewPhotoPaths.add(previewPhotoPath);
                        }else{
                            if (CollectionUtils.isNotEmpty(reuploadPreviewPhotoPaths)) {
                                //将重新上传的图片 设置到原来的商品预览列表中
                                if (reuploadPos < reuploadPreviewPhotoPaths.size()) {
                                    releasePreviewPhotoPaths.add(reuploadPreviewPhotoPaths.get(reuploadPos));
                                    reuploadPos++;
                                }
                            }
                        }
                    }
                    //将剩余的追加到列表中
                    for(int p=reuploadPos;p<reuploadPreviewPhotoPaths.size();p++)
                    {
                        releasePreviewPhotoPaths.add(reuploadPreviewPhotoPaths.get(p));
                    }
                    shopProductApproveVO.setPreviewPhotoPaths(releasePreviewPhotoPaths);
                }
            }else{ //没有进行删除操作 直接追加上传的图片
                if(CollectionUtils.isNotEmpty(reuploadPreviewPhotoPaths))
                {
                    shopProductApproveVO.getPreviewPhotoPaths().addAll(reuploadPreviewPhotoPaths);
                }
            }
            republishProductVO.setPreviewPhotoPaths(shopProductApproveVO.getPreviewPhotoPaths());

            //上传商品介绍图片
            uploadProductDescriptionImgForRePublish(republishProductVO,shopProductApproveVO);

            republishProductVO.setShopId(sellerShopVO.getId());
            republishProductVO.setUpdateUserId(queryUserVO.getUserMainId());
            republishProductVO.setUuid(shopProductApproveVO.getUuid());
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), republishProductVO);
            resultObjectVO = feignShopProductApproveService.republish(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                resultObjectVO.setMsg("发布成功");
            }
        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("发布失败!");
        }
        return resultObjectVO;
    }



    @RequestMapping(value="/vcode", method = RequestMethod.GET)
    public void verifyCode(HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            int w = 200, h = 80;
            //生成4位验证码
            String code = VerifyCodeUtil.generateVerifyCode(4);


            //生成客户端验证码ID
            String vcodeUuid = GlobalUUID.uuid();
            String vcodeRedisKey = ShopProductRedisKey.getVerifyCodeKey(this.getAppCode(),vcodeUuid);
            toucanStringRedisService.set(vcodeRedisKey,code);
            toucanStringRedisService.expire(vcodeRedisKey,60, TimeUnit.SECONDS);

            Cookie clientVCodeId = new Cookie("clientVCodeId",vcodeUuid);
            clientVCodeId.setPath("/");
            //60秒过期
            clientVCodeId.setMaxAge(60);
            response.addCookie(clientVCodeId);

            VerifyCodeUtil.outputImage(w, h, outputStream, code);
        } catch (IOException e) {
            logger.warn(e.getMessage(),e);
        }finally{
            if(outputStream!=null)
            {
                try {
                    outputStream.flush();
                    outputStream.close();
                }catch(Exception e)
                {
                    logger.warn(e.getMessage(),e);
                }
            }
        }
    }


    @UserAuth
    @RequestMapping(value = "/{categoryId}/attributes",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO queryAttributesByCategoryId(@PathVariable Long categoryId){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(categoryId==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到分类ID");
            return resultObjectVO;
        }
        try {
            AttributeKeyVO queryAttributeKeyVO = new AttributeKeyVO();
            queryAttributeKeyVO.setCategoryId(categoryId);
            queryAttributeKeyVO.setShowStatus((short)1);
            queryAttributeKeyVO.setAttributeType((short)2); //查询SKU属性
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAttributeKeyVO);
            resultObjectVO = feignAttributeKeyValueService.findByCategoryId(requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 根据用户ID查询所属的店铺ID
     * @param userMainId
     * @return
     */
    private Long queryShopIdByUserMainId(Long userMainId)
    {
        try{
            //查询这个用户下的店铺
            UserVO queryUserVO = new UserVO();
            queryUserVO.setUserMainId(userMainId);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryUserVO);
            ResultObjectVO resultObjectVO = feignSellerShopService.findByUser(toucan.getAppCode(),requestJsonVO);
            if(!resultObjectVO.isSuccess()) {
                return -1L;
            }
            SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
            if (sellerShopVO == null||(sellerShopVO.getEnableStatus()!=null&&sellerShopVO.getEnableStatus().intValue()==0)) {
                return -1L;
            }
            return sellerShopVO.getId();
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return -1L;
    }



    /**
     * 查询最新4个商品审核
     * @param request
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/queryProductApproveListTop4",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryProductApproveListTop4(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ShopProductApprovePageInfo shopProductApprovePageInfo = new ShopProductApprovePageInfo();
            shopProductApprovePageInfo.setLimit(4);
            shopProductApprovePageInfo.setShopId(queryShopIdByUserMainId(Long.parseLong(UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader())))));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopProductApprovePageInfo);
            resultObjectVO = feignShopProductApproveService.queryNewestListByShopId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<ShopProductApproveVO> shopProductApproveVOS = resultObjectVO.formatDataList(ShopProductApproveVO.class);
                if(CollectionUtils.isNotEmpty(shopProductApproveVOS))
                {
                    for(ShopProductApproveVO shopProductApproveVO:shopProductApproveVOS)
                    {
                        if(StringUtils.isNotEmpty(shopProductApproveVO.getMainPhotoFilePath()))
                        {
                            shopProductApproveVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+shopProductApproveVO.getMainPhotoFilePath());
                        }
                    }
                }
                resultObjectVO.setData(shopProductApproveVOS);
            }
        }catch(Exception e){
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }
        return resultObjectVO;
    }


}
