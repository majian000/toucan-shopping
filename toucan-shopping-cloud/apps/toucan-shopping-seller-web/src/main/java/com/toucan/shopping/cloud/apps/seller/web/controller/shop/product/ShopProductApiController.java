package com.toucan.shopping.cloud.apps.seller.web.controller.shop.product;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.redis.ShopProductRedisKey;
import com.toucan.shopping.cloud.apps.seller.web.util.VCodeUtil;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyValueService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.util.VerifyCodeUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
 * 店铺商品信息
 */
@Controller("shopProductApiController")
@RequestMapping("/api/shop/product")
public class ShopProductApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignShopProductService feignShopProductService;

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

    private String[] imageExtScope = new String[]{".JPG", ".JPEG", ".PNG"};


    /**
     * 查询类别信息
     *
     * @param list
     * @param categoryIds
     */
    void queryCategory(List<ShopProductVO> list, Long[] categoryIds) {
        try {
            //查询类别名称
            CategoryVO queryCategoryVO = new CategoryVO();
            queryCategoryVO.setIdArray(categoryIds);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategoryVO);
            ResultObjectVO resultObjectVO = feignCategoryService.findByIdArray(requestJsonVO.sign(), requestJsonVO);
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
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }


    /**
     * 查询列表
     *
     * @param pageInfo
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO list(HttpServletRequest httpServletRequest, @RequestBody ShopProductPageInfo pageInfo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (pageInfo == null) {
                pageInfo = new ShopProductPageInfo();
            }
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if (StringUtils.isEmpty(userMainId)) {
                logger.warn("查询商品审核 没有找到用户ID {} ", userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("查询失败,请稍后重试");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(), requestJsonVO);
            if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if (sellerShopVO != null) {
                    pageInfo.setShopId(sellerShopVO.getId());
                    pageInfo.setOrderColumn("update_date");
                    pageInfo.setOrderSort("desc");
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), pageInfo);
                    resultObjectVO = feignShopProductService.queryListPage(requestJsonVO);
                    if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                        Map<String, Object> resultObjectDataMap = (Map<String, Object>) resultObjectVO.getData();
                        List<ShopProductVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), ShopProductVO.class);
                        if (CollectionUtils.isNotEmpty(list)) {

                            Long[] categoryIds = new Long[list.size()];
                            boolean categoryExists = false;
                            for (int i = 0; i < list.size(); i++) {
                                ShopProductVO shopProductVO = list.get(i);

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

                            resultObjectDataMap.put("list", list);

                            resultObjectVO.setData(resultObjectDataMap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }

        return resultObjectVO;
    }


    /**
     * 店铺商品 上架/下架
     *
     * @param shopProductVO
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/shelves", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO shelves(HttpServletRequest request,@RequestBody ShopProductVO shopProductVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            if(shopProductVO==null||shopProductVO.getId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("商品ID不能为空");
                return resultObjectVO;
            }
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
                    //设置店铺ID
                    shopProductVO.setShopId(sellerShopVORet.getId());

                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopProductVO);
                    resultObjectVO = feignShopProductService.shelves(requestJsonVO);
                }
            }
        }catch(Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试");
        }

        return resultObjectVO;
    }


    @RequestMapping(value = "/queryProductByShopProductUUID",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryProductByShopProductUUID(HttpServletRequest request,@RequestBody  String shopProductUUID)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),shopProductUUID);
            requestJsonVO.setEntityJson(shopProductUUID);
            resultObjectVO = feignShopProductService.queryListByShopProductUuid(requestJsonVO);
            if (resultObjectVO.getCode() == ResultObjectVO.SUCCESS) {

            }
        }catch(Exception e)
        {

        }
        return resultObjectVO;
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
                    ShopProductVO shopProductVO = new ShopProductVO();
                    shopProductVO.setId(queryShopProductApproveVO.getId());
                    shopProductVO.setShopId(sellerShopVORet.getId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), shopProductVO);
                    resultObjectVO = feignShopProductService.deleteById(requestJsonVO);

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


}
