package com.toucan.shopping.cloud.apps.web.controller.product;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSpuService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.persistence.event.entity.EventProcess;
import com.toucan.shopping.modules.common.persistence.event.service.EventProcessService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.cloud.stock.api.feign.service.FeignProductSkuStockService;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.order.vo.CreateOrderVo;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.util.ProductRedisKeyUtil;
import com.toucan.shopping.modules.product.vo.ProductSpuVO;
import com.toucan.shopping.modules.product.vo.ShopProductDescriptionImgVO;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.product.vo.ShopProductVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.stock.entity.ProductSkuStock;
import com.toucan.shopping.modules.stock.kafka.constant.StockMessageTopicConstant;
import com.toucan.shopping.modules.stock.vo.InventoryReductionVo;
import com.toucan.shopping.modules.stock.vo.RestoreStockVo;
import com.toucan.shopping.cloud.apps.web.vo.BuyResultVo;
import com.toucan.shopping.cloud.apps.web.vo.BuyVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/product")
public class ProductApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignProductSkuService feignProductSkuService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignProductSpuService feignProductSpuService;


    @UserAuth(requestType = UserAuth.REQUEST_AJAX)
    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    public ResultObjectVO detail(@RequestBody ProductSkuVO shopProductSkuVO)
    {
        ResultObjectVO retObject = new ResultObjectVO();
        try {
            ProductSkuVO queryShopProductSku = new ProductSkuVO();
            queryShopProductSku.setId(shopProductSkuVO.getId());;
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopProductSku);
            ResultObjectVO resultObjectVO = feignProductSkuService.queryByIdForFront(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    shopProductSkuVO = resultObjectVO.formatData(ProductSkuVO.class);

                    if(shopProductSkuVO.getMainPhotoFilePath()!=null) {
                        shopProductSkuVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+shopProductSkuVO.getMainPhotoFilePath());
                    }

                    if(org.apache.commons.collections.CollectionUtils.isNotEmpty(shopProductSkuVO.getPreviewPhotoPaths())) {
                        shopProductSkuVO.setHttpPreviewPhotoPaths(new LinkedList<>());
                        for(String previewPhotoPath:shopProductSkuVO.getPreviewPhotoPaths())
                        {
                            shopProductSkuVO.getHttpPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix()+previewPhotoPath);
                        }
                    }

                    if(shopProductSkuVO.getShopProductDescriptionVO()!=null) {
                        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(shopProductSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs()))
                        {
                            for(ShopProductDescriptionImgVO shopProductDescriptionImgVO:shopProductSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs()) {
                                shopProductDescriptionImgVO.setHttpFilePath(imageUploadService.getImageHttpPrefix()+shopProductDescriptionImgVO.getFilePath());
                            }
                        }
                    }
                    if(shopProductSkuVO.getProductPreviewPath()!=null) {
                        shopProductSkuVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+shopProductSkuVO.getProductPreviewPath());
                    }


                    retObject.setData(shopProductSkuVO);
                }
            }
        }catch(Exception e)
        {
            retObject.setMsg("查询失败,请稍后重试");
            retObject.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return retObject;
    }


    /**
     * 根据商品主表ID查询1个预览的SKU
     * @param shopProductVO
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_AJAX)
    @RequestMapping(value = "/detail/pid",method = RequestMethod.POST)
    public ResultObjectVO detailByProductId(@RequestBody ShopProductVO shopProductVO)
    {
        ResultObjectVO retObject = new ResultObjectVO();
        try {
            ShopProductVO queryShopProduct = new ShopProductVO();
            queryShopProduct.setId(shopProductVO.getId());;
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopProduct);
            ResultObjectVO resultObjectVO = feignProductSkuService.queryOneByShopProductIdForFront(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    ProductSkuVO productSkuVO = resultObjectVO.formatData(ProductSkuVO.class);

                    if(productSkuVO.getMainPhotoFilePath()!=null) {
                        productSkuVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+productSkuVO.getMainPhotoFilePath());
                    }

                    if(org.apache.commons.collections.CollectionUtils.isNotEmpty(productSkuVO.getPreviewPhotoPaths())) {
                        productSkuVO.setHttpPreviewPhotoPaths(new LinkedList<>());
                        for(String previewPhotoPath:productSkuVO.getPreviewPhotoPaths())
                        {
                            productSkuVO.getHttpPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix()+previewPhotoPath);
                        }
                    }

                    if(productSkuVO.getShopProductDescriptionVO()!=null) {
                        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs()))
                        {
                            for(ShopProductDescriptionImgVO shopProductDescriptionImgVO:productSkuVO.getShopProductDescriptionVO().getProductDescriptionImgs()) {
                                shopProductDescriptionImgVO.setHttpFilePath(imageUploadService.getImageHttpPrefix()+shopProductDescriptionImgVO.getFilePath());
                            }
                        }
                    }
                    if(productSkuVO.getProductPreviewPath()!=null) {
                        productSkuVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+productSkuVO.getProductPreviewPath());
                    }


                    retObject.setData(productSkuVO);
                }
            }
        }catch(Exception e)
        {
            retObject.setMsg("查询失败,请稍后重试");
            retObject.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return retObject;
    }




    /**
     * 根据商品审核主表ID查询1个预览的SKU
     * @param shopProductVO
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_AJAX)
    @RequestMapping(value = "/spu/info",method = RequestMethod.POST)
    public ResultObjectVO querySpuAttributeList(@RequestBody ShopProductVO shopProductVO)
    {
        ResultObjectVO retObject = new ResultObjectVO();
        try {
            if(shopProductVO.getProductId()==null)
            {
                retObject.setMsg("查询失败,没有找到商品ID");
                retObject.setCode(ResultObjectVO.FAILD);
            }

            ProductSpuVO queryProductSpu = new ProductSpuVO();
            queryProductSpu.setId(shopProductVO.getProductId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryProductSpu);
            retObject = feignProductSpuService.findById(requestJsonVO);

        }catch(Exception e)
        {
            retObject.setMsg("查询失败,请稍后重试");
            retObject.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return retObject;
    }





}
