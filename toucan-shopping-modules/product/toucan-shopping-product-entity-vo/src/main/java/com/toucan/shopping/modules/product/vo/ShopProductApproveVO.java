package com.toucan.shopping.modules.product.vo;

import com.toucan.shopping.modules.product.entity.ShopProduct;
import com.toucan.shopping.modules.product.entity.ShopProductApprove;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 店铺维度的商品审核 (关联到平台维度的SPU)
 *
 * @author majian
 */
@Data
public class ShopProductApproveVO extends ShopProductApprove {

    private String categoryName; //类别名称
    private String categoryPath; //类别路径

    private String shopCategoryName; //类别名称
    private String shopCategoryPath; //类别路径

    private String brandChineseName; //品牌中文名称
    private String brandEnglishName; //品牌英文名称
    private String brandLogo; //品牌logo
    private String brandHttpLogo; //品牌LOGO地址

    private String shopName; //店铺名称

//    private String productDescription; //商品介绍


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
     * SKU列表
     */
    private List<ShopProductApproveSkuVO> productSkuVOList;

    /**
     * 验证码
     */
    private String vcode;

    /**
     * 审核人
     */
    private String createAdminId;


    /**
     * 分类上级ID路径(包含当前ID)
     * 格式:当前分类ID,上级分类节点ID,上上级分类节点ID
     */
    private List<String> categoryIdPath;

    /**
     * 分类路径
     * 格式:当前分类ID,上级分类节点ID,上上级分类节点ID
     */
    private List<String> categoryNamePath;


    /**
     * 分类上级ID路径(包含当前ID)
     * 格式:当前分类ID,上级分类节点ID,上上级分类节点ID
     */
    private List<String> shopCategoryIdPath;

    /**
     * 分类路径
     * 格式:当前分类ID,上级分类节点ID,上上级分类节点ID
     */
    private List<String> shopCategoryNamePath;

    /**
     * SKU的所有属性对应
     */
    private List<ShopProductApproveSkuAttribute> skuAttributes;

    /**
     * 商品介绍
     */
    private ShopProductApproveDescriptionVO shopProductApproveDescriptionVO;

    /**
     * 商品介绍JSON字符串
     */
    private String shopProductApproveDescriptionJson="{}";


    /**
     * 运费模板名称
     */
    private String freightTemplateName;


}
