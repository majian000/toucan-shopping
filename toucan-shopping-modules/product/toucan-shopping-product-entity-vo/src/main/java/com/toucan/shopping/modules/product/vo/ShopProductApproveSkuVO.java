package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.entity.ShopProductApproveSku;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 商品审核SKU VO
 *
 * @author majian
 * @date 2022-4-5 14:55:22
 */
@Data
public class ShopProductApproveSkuVO extends ShopProductApproveSku {

    /**
     * 属性映射
     */
    private Map<String,String> attributeMap;


    /**
     * SKU商品主图
     */
    @JSONField(serialize = false)
    private MultipartFile mainPhotoFile;


    /**
     * 商品主图(HTTP访问)
     */
    private String httpMainPhoto;



    private String productAttributes; //这个商品所有属性




    /**
     * 商品预览图路径
     */
    private List<String> previewPhotoPaths;


    /**
     * 商品预览图路径
     */
    private List<String> httpPreviewPhotoPaths;


    /**
     * SKU预览图路径
     */
    private List<String> httpSkuPreviewPhotoPaths;

    /**
     * 主图文件路径
     */
    private String mainPhotoFilePath;

    /**
     * 商品主图
     */
    private String httpMainPhotoFilePath;


    /**
     * 商品介绍
     */
    private ShopProductApproveDescriptionVO shopProductApproveDescriptionVO;

    /**
     * SKU列表
     */
    private List<ShopProductApproveSkuVO> productSkuVOList;



}
