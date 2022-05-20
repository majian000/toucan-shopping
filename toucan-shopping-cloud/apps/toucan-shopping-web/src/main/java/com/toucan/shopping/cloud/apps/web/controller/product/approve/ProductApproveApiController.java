package com.toucan.shopping.cloud.apps.web.controller.product.approve;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.vo.BuyResultVo;
import com.toucan.shopping.cloud.apps.web.vo.BuyVo;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderService;
import com.toucan.shopping.cloud.product.api.feign.service.*;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.cloud.stock.api.feign.service.FeignProductSkuStockService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.persistence.event.entity.EventProcess;
import com.toucan.shopping.modules.common.persistence.event.service.EventProcessService;
import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.order.no.OrderNoService;
import com.toucan.shopping.modules.order.vo.CreateOrderVo;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.util.ProductRedisKeyUtil;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.stock.entity.ProductSkuStock;
import com.toucan.shopping.modules.stock.kafka.constant.StockMessageTopicConstant;
import com.toucan.shopping.modules.stock.vo.InventoryReductionVo;
import com.toucan.shopping.modules.stock.vo.RestoreStockVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/product/approve")
public class ProductApproveApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;


    @Autowired
    private FeignShopProductApproveSkuService feignShopProductApproveSkuService;

    @Autowired
    private ImageUploadService imageUploadService;


    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    public ResultObjectVO detail(@RequestBody ShopProductApproveSkuVO shopProductApproveSkuVO)
    {
        ResultObjectVO retObject = new ResultObjectVO();
        try {
            ShopProductApproveSkuVO queryShopProductApproveSku = new ShopProductApproveSkuVO();
            queryShopProductApproveSku.setId(shopProductApproveSkuVO.getId());;
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopProductApproveSku);
            ResultObjectVO resultObjectVO = feignShopProductApproveSkuService.queryByIdForFront(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    shopProductApproveSkuVO = resultObjectVO.formatData(ShopProductApproveSkuVO.class);



                    if(shopProductApproveSkuVO.getMainPhotoFilePath()!=null) {
                        shopProductApproveSkuVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+shopProductApproveSkuVO.getMainPhotoFilePath());
                    }

                    if(CollectionUtils.isNotEmpty(shopProductApproveSkuVO.getPreviewPhotoPaths())) {
                        shopProductApproveSkuVO.setHttpPreviewPhotoPaths(new LinkedList<>());
                        for(String previewPhotoPath:shopProductApproveSkuVO.getPreviewPhotoPaths())
                        {
                            shopProductApproveSkuVO.getHttpPreviewPhotoPaths().add(imageUploadService.getImageHttpPrefix()+previewPhotoPath);
                        }
                    }

                    if(shopProductApproveSkuVO.getShopProductApproveDescriptionVO()!=null) {
                        if(CollectionUtils.isNotEmpty(shopProductApproveSkuVO.getShopProductApproveDescriptionVO().getProductDescriptionImgs()))
                        {
                            for(ShopProductApproveDescriptionImgVO shopProductApproveDescriptionImgVO:shopProductApproveSkuVO.getShopProductApproveDescriptionVO().getProductDescriptionImgs()) {
                                shopProductApproveDescriptionImgVO.setHttpFilePath(imageUploadService.getImageHttpPrefix()+shopProductApproveDescriptionImgVO.getFilePath());
                            }
                        }
                    }

                    retObject.setData(shopProductApproveSkuVO);
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



}
