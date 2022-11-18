package com.toucan.shopping.cloud.apps.web.controller.product.approve;

import com.toucan.shopping.cloud.product.api.feign.service.*;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private FeignProductSpuService feignProductSpuService;

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
                    if(shopProductApproveSkuVO.getProductPreviewPath()!=null) {
                        shopProductApproveSkuVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+shopProductApproveSkuVO.getProductPreviewPath());
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


    /**
     * 根据商品审核主表ID查询1个预览的SKU
     * @param shopProductApproveVO
     * @return
     */
    @RequestMapping(value = "/detail/paid",method = RequestMethod.POST)
    public ResultObjectVO detailByProductApproveId(@RequestBody ShopProductApproveVO shopProductApproveVO)
    {
        ResultObjectVO retObject = new ResultObjectVO();
        try {
            ShopProductApproveVO queryShopProductApprove = new ShopProductApproveVO();
            queryShopProductApprove.setId(shopProductApproveVO.getId());;
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopProductApprove);
            ResultObjectVO resultObjectVO = feignShopProductApproveSkuService.queryOneByProductApproveIdForFront(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    ShopProductApproveSkuVO shopProductApproveSkuVO = resultObjectVO.formatData(ShopProductApproveSkuVO.class);

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
                    if(shopProductApproveSkuVO.getProductPreviewPath()!=null) {
                        shopProductApproveSkuVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+shopProductApproveSkuVO.getProductPreviewPath());
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




    /**
     * 根据商品审核主表ID查询1个预览的SKU
     * @param shopProductApproveVO
     * @return
     */
    @RequestMapping(value = "/spu/info",method = RequestMethod.POST)
    public ResultObjectVO querySpuAttributeList(@RequestBody ShopProductApproveVO shopProductApproveVO)
    {
        ResultObjectVO retObject = new ResultObjectVO();
        try {
            if(shopProductApproveVO.getProductId()==null)
            {
                retObject.setMsg("查询失败,没有找到商品ID");
                retObject.setCode(ResultObjectVO.FAILD);
            }

            ProductSpuVO queryProductSpu = new ProductSpuVO();
            queryProductSpu.setId(shopProductApproveVO.getProductId());
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
