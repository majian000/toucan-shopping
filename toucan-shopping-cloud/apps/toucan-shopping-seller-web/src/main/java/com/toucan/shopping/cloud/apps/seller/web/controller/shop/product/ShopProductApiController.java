package com.toucan.shopping.cloud.apps.seller.web.controller.shop.product;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.service.CategoryService;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignColorTableService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyValueService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.color.table.vo.ColorTableVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.product.vo.PublishProductVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * 店铺商品信息
 */
@Controller("shopProductApiController")
@RequestMapping("/api/shop/product")
public class ShopProductApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String[] imageExtScope = new String[]{".JPG",".JPEG",".PNG"};
    @Autowired
    private Toucan toucan;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FeignShopCategoryService feignShopCategoryService;

    @Autowired
    private FeignAttributeKeyValueService feignAttributeKeyValueService;

    @Autowired
    private FeignColorTableService feignColorTableService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignShopProductService feignShopProductService;



    @UserAuth(requestType = UserAuth.REQUEST_FORM)
    @RequestMapping(value = "/publish",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO release(HttpServletRequest request, @RequestParam List<MultipartFile> previewPhotoFiles, PublishProductVO publishProductVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            publishProductVO.setCreateUserId(Long.parseLong(userMainId));


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

            if(!CollectionUtils.isEmpty(publishProductVO.getProductSkuVOList())) {
                for (ProductSkuVO productSkuVO : publishProductVO.getProductSkuVOList()) {
                    if(StringUtils.isEmpty(productSkuVO.getAttributes()))
                    {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("发布失败,SKU列表属性值不能为空!");
                        return resultObjectVO;
                    }
                }
            }


            //SKU属性表格式化成Map
            if(!CollectionUtils.isEmpty(publishProductVO.getProductSkuVOList())) {
                for (ProductSkuVO productSkuVO : publishProductVO.getProductSkuVOList()) {
                    productSkuVO.setAttributeMap(JSONObject.parseObject(productSkuVO.getAttributes(), HashMap.class));
                    String name = publishProductVO.getName();
                    Set<String> keys = productSkuVO.getAttributeMap().keySet();
                    Iterator keyIt = keys.iterator();
                    while(keyIt.hasNext())
                    {
                        name+=" " +productSkuVO.getAttributeMap().get(keyIt.next());
                    }

                    productSkuVO.setName(name);
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
                for(ProductSkuVO productSkuVO:publishProductVO.getProductSkuVOList())
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

            //上传SKU表商品主图
            for(ProductSkuVO productSkuVO:publishProductVO.getProductSkuVOList())
            {
                productSkuVO.setMainPhotoFilePath(imageUploadService.uploadFile(productSkuVO.getMainPhotoFile().getBytes(),ImageUtils.getImageExt(productSkuVO.getMainPhotoFile().getOriginalFilename())));
                productSkuVO.setMainPhotoFilePath(null);
            }

            publishProductVO.setAppCode("10001001");
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),publishProductVO);
            resultObjectVO = feignShopProductService.publish(requestJsonVO);

        }catch (Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("发布失败!");
        }
        return resultObjectVO;
    }


    /**
     * 设置颜色表
     * @param attributeKeyVOS
     */
    void setColorTable(List<AttributeKeyVO> attributeKeyVOS)
    {
        try {
            if(CollectionUtils.isNotEmpty(attributeKeyVOS))
            {
                List<String> colorNameList = new LinkedList<>();
                for(AttributeKeyVO attributeKeyVO:attributeKeyVOS) {
                    //如果是颜色属性,遍历出所有颜色值,再查询颜色表
                    if(attributeKeyVO.getAttributeType()!=null&&attributeKeyVO.getAttributeType().intValue()==2)
                    {
                        List<AttributeValueVO> attributeValueVOS = attributeKeyVO.getValues();
                        if(CollectionUtils.isNotEmpty(attributeValueVOS)) {

                            for(AttributeValueVO attributeValueVO:attributeValueVOS) {
                                colorNameList.add(attributeValueVO.getAttributeValue());
                            }
                        }
                    }
                }

                //查询颜色表
                if (CollectionUtils.isNotEmpty(colorNameList)) {
                    ColorTableVO queryColorTableVO = new ColorTableVO();
                    queryColorTableVO.setNameList(colorNameList);
                    RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryColorTableVO);
                    ResultObjectVO queryColorTableResultObject = feignColorTableService.queryListByNames(requestJsonVO);
                    if (queryColorTableResultObject.isSuccess()) {
                        List<ColorTableVO> colorTableVOS = queryColorTableResultObject.formatDataList(ColorTableVO.class);
                        if (CollectionUtils.isNotEmpty(colorTableVOS)) {
                            for (AttributeKeyVO attributeKeyVO : attributeKeyVOS) {
                                //如果是颜色属性,遍历出所有颜色值,再查询颜色表
                                if (attributeKeyVO.getAttributeType() != null && attributeKeyVO.getAttributeType().intValue() == 2) {
                                    List<AttributeValueVO> attributeValueVOS = attributeKeyVO.getValues();
                                    if (CollectionUtils.isNotEmpty(attributeValueVOS)) {
                                        for (AttributeValueVO attributeValueVO : attributeValueVOS) {
                                            for (ColorTableVO colorTableVO : colorTableVOS) {
                                                if (StringUtils.isNotEmpty(colorTableVO.getName()) && colorTableVO.getName().equals(attributeValueVO.getAttributeValue())) {
                                                    //设置颜色值
                                                    attributeValueVO.setRgbColor(colorTableVO.getRgbColor());
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
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
            queryAttributeKeyVO.setAttributeScope((short)2); //查询SKU属性
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAttributeKeyVO);
            resultObjectVO = feignAttributeKeyValueService.findByCategoryId(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<AttributeKeyVO> attributeKeyVOS = resultObjectVO.formatDataList(AttributeKeyVO.class);
                setColorTable(attributeKeyVOS);

                resultObjectVO.setData(attributeKeyVOS);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}
