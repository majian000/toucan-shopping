package com.toucan.shopping.cloud.apps.seller.web.controller.shop.product;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
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
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.product.vo.ShopProductApproveVO;
import com.toucan.shopping.modules.product.vo.ShopProductVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * 店铺商品SKU信息
 */
@Controller("productSkuApiController")
@RequestMapping("/api/product/sku")
public class ProductSkuApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignShopProductService feignShopProductService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignProductSkuService feignProductSkuService;








    /**
     * 查询列表
     * @param productSkuVO
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/listByShopProductId", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO listByShopProductId(HttpServletRequest httpServletRequest, @RequestBody ProductSkuVO productSkuVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if (StringUtils.isEmpty(userMainId)) {
                logger.warn("查询商品信息 没有找到用户ID {} ", userMainId);
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
                    productSkuVO.setShopId(sellerShopVO.getId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), productSkuVO);
                    resultObjectVO = feignProductSkuService.queryList(requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        List<ProductSkuVO> productSkuVOS = resultObjectVO.formatDataList(ProductSkuVO.class);
                        if(CollectionUtils.isNotEmpty(productSkuVOS)){
                            for(ProductSkuVO psv:productSkuVOS)
                            {
                                psv.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+psv.getProductPreviewPath());
                                psv.setHttpDescriptionImgPath(imageUploadService.getImageHttpPrefix()+psv.getDescriptionImgFilePath());
                            }
                        }
                        resultObjectVO.setData(productSkuVOS);
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
     * 修改库存
     * @param productSkuVO
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/update/stock", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO updateStock(HttpServletRequest httpServletRequest, @RequestBody ProductSkuVO productSkuVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if (StringUtils.isEmpty(userMainId)) {
                logger.warn("修改失败 没有找到用户ID {} ", userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("操作失败,请稍后重试");
                return resultObjectVO;
            }

            if(productSkuVO.getId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("商品ID不能为空");
                return resultObjectVO;
            }
            if(productSkuVO.getStockNum()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("库存数量不能为空");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(), requestJsonVO);
            if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if (sellerShopVO != null) {
                    productSkuVO.setShopId(sellerShopVO.getId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), productSkuVO);
                    resultObjectVO = feignProductSkuService.updateStock(requestJsonVO);
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
     * 修改单价
     * @param productSkuVO
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/update/price", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO updatePrice(HttpServletRequest httpServletRequest, @RequestBody ProductSkuVO productSkuVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if (StringUtils.isEmpty(userMainId)) {
                logger.warn("修改失败 没有找到用户ID {} ", userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("操作失败,请稍后重试");
                return resultObjectVO;
            }
            if(productSkuVO.getId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("商品ID不能为空");
                return resultObjectVO;
            }
            if(productSkuVO.getPrice()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("价格不能为空");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(), requestJsonVO);
            if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if (sellerShopVO != null) {
                    productSkuVO.setShopId(sellerShopVO.getId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), productSkuVO);
                    resultObjectVO = feignProductSkuService.updatePrice(requestJsonVO);
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
     * 上下架
     * @param productSkuVO
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/update/shelves", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO updateShelves(HttpServletRequest httpServletRequest, @RequestBody ProductSkuVO productSkuVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if (StringUtils.isEmpty(userMainId)) {
                logger.warn("修改失败 没有找到用户ID {} ", userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("操作失败,请稍后重试");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(), requestJsonVO);
            if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if (sellerShopVO != null) {
                    productSkuVO.setShopId(sellerShopVO.getId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), productSkuVO);
                    resultObjectVO = feignProductSkuService.shelves(requestJsonVO);
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
     * 上传主图
     * @param productSkuVO
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/update/preview/photo", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO updateMainPhoto(HttpServletRequest httpServletRequest,ProductSkuVO productSkuVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if (StringUtils.isEmpty(userMainId)) {
                logger.warn("修改失败 没有找到用户ID {} ", userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("操作失败,请稍后重试");
                return resultObjectVO;
            }
            if(productSkuVO.getId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("操作失败,商品ID不能为空");
                return resultObjectVO;
            }

            //校验SKU主图
            if(productSkuVO.getMainPhotoFile()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("替换失败,商品主图不能为空!");
                return resultObjectVO;
            }
            if(!ImageUtils.isImage(productSkuVO.getMainPhotoFile().getOriginalFilename(),ImageUtils.imageExtScope))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("替换失败,商品主图格式只能为:JPG、JPEG、PNG!");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(), requestJsonVO);
            if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if (sellerShopVO != null) {
                    productSkuVO.setShopId(sellerShopVO.getId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), productSkuVO);
                    //判断该用户有权限操作该商品
                    resultObjectVO = feignProductSkuService.queryById(requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        //旧的预览图
                        ProductSkuVO oldProductSkuVO = resultObjectVO.formatData(ProductSkuVO.class);
                        productSkuVO.setProductPreviewPath(imageUploadService.uploadFile(productSkuVO.getMainPhotoFile().getBytes(), ImageUtils.getImageExt(productSkuVO.getMainPhotoFile().getOriginalFilename())));
                        productSkuVO.setMainPhotoFile(null);
                        requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), productSkuVO);
                        resultObjectVO = feignProductSkuService.updatePreviewPhoto(requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            this.deleteOldProductImage(oldProductSkuVO.getProductPreviewPath());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("操作失败,请稍后重试");
        }

        return resultObjectVO;
    }



    /**
     * 上传介绍图
     * @param productSkuVO
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/update/description/photo", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO updateDescriptionPhoto(HttpServletRequest httpServletRequest,ProductSkuVO productSkuVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if (StringUtils.isEmpty(userMainId)) {
                logger.warn("修改失败 没有找到用户ID {} ", userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("操作失败,请稍后重试");
                return resultObjectVO;
            }

            if(productSkuVO.getId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("操作失败,商品ID不能为空");
                return resultObjectVO;
            }

            //校验SKU主图
            if(productSkuVO.getShopProductDescriptionVO()==null
                    ||productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs()==null
                    ||productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs().get(0).getImgFile()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("替换失败,商品介绍图不能为空!");
                return resultObjectVO;
            }
            if(!ImageUtils.isImage(productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs().get(0).getImgFile().getOriginalFilename(),ImageUtils.imageExtScope))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("替换失败,商品介绍图格式只能为:JPG、JPEG、PNG!");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(), requestJsonVO);
            if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if (sellerShopVO != null) {
                    productSkuVO.setShopId(sellerShopVO.getId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), productSkuVO);
                    //判断该用户有权限操作该商品
                    resultObjectVO = feignProductSkuService.queryById(requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        //旧的介绍图
                        ProductSkuVO oldProductSkuVO = resultObjectVO.formatData(ProductSkuVO.class);
                        productSkuVO.setDescriptionImgFilePath(imageUploadService.uploadFile(productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs().get(0).getImgFile().getBytes(), ImageUtils.getImageExt(productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs().get(0).getImgFile().getOriginalFilename())));
                        productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs().get(0).setImgFile(null);
                        requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), productSkuVO);
                        resultObjectVO = feignProductSkuService.updateDescriptionPhoto(requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            if(StringUtils.isNotEmpty(oldProductSkuVO.getDescriptionImgFilePath())) {
                                this.deleteOldProductImage(oldProductSkuVO.getDescriptionImgFilePath());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("操作失败,请稍后重试");
        }

        return resultObjectVO;
    }




    /**
     * 移除介绍图
     * @param productSkuVO
     * @return
     */
    @UserAuth
    @RequestMapping(value = "/remove/description/photo", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO removeDescriptionPhoto(HttpServletRequest httpServletRequest,@RequestBody ProductSkuVO productSkuVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(httpServletRequest.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            if (StringUtils.isEmpty(userMainId)) {
                logger.warn("修改失败 没有找到用户ID {} ", userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("操作失败,请稍后重试");
                return resultObjectVO;
            }

            if(productSkuVO.getId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("操作失败,商品ID不能为空");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(), requestJsonVO);
            if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                SellerShopVO sellerShopVO = resultObjectVO.formatData(SellerShopVO.class);
                if (sellerShopVO != null) {
                    productSkuVO.setShopId(sellerShopVO.getId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), productSkuVO);
                    //判断该用户有权限操作该商品
                    resultObjectVO = feignProductSkuService.queryById(requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        //旧的介绍图
                        ProductSkuVO oldProductSkuVO = resultObjectVO.formatData(ProductSkuVO.class);
                        requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), productSkuVO);
                        resultObjectVO = feignProductSkuService.removeDescriptionPhoto(requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            if(StringUtils.isNotEmpty(oldProductSkuVO.getDescriptionImgFilePath())) {
                                this.deleteOldProductImage(oldProductSkuVO.getDescriptionImgFilePath());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("操作失败,请稍后重试");
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

}
