package com.toucan.shopping.cloud.apps.h5.controller.shop;

import com.toucan.shopping.cloud.apps.h5.controller.BaseController;
import com.toucan.shopping.cloud.seller.api.ShopBannerServiceAPI;
import com.toucan.shopping.cloud.seller.api.ShopCategoryServiceAPI;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * H5 店铺
 */
@RestController("h5ShopController")
@RequestMapping("/h5/shop")
public class ShopApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShopBannerServiceAPI shopBannerService;

    @Autowired
    private ShopCategoryServiceAPI shopCategoryService;

    @Autowired
    private ImageUploadService imageUploadService;

    /** 店铺首页轮播图 */
    @RequestMapping("/index/banners")
    public ResultObjectVO banners(@RequestBody ShopBannerVO shopBannerVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ShopBannerVO query = new ShopBannerVO();
        query.setShopId(shopBannerVO.getShopId());
        query.setPosition("PC_INDEX");
        try {
            resultObjectVO = shopBannerService.queryIndexList(RequestJsonVOGenerator.generator(this.getAppCode(), query));
            if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                List<ShopBannerVO> banners = resultObjectVO.formatDataList(ShopBannerVO.class);
                for (ShopBannerVO banner : banners) {
                    if (StringUtils.isNotEmpty(banner.getImgPath())) {
                        banner.setHttpImgPath(imageUploadService.getImageHttpPrefix() + banner.getImgPath());
                    }
                }
                resultObjectVO.setData(banners);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    /** 店铺首页分类 */
    @RequestMapping("/index/categorys")
    public ResultObjectVO categorys(@RequestBody ShopCategoryVO shopCategoryVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ShopCategoryVO query = new ShopCategoryVO();
        query.setShopId(shopCategoryVO.getShopId());
        try {
            resultObjectVO = shopCategoryService.queryWebIndexTree(RequestJsonVOGenerator.generator(this.getAppCode(), query));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resultObjectVO;
    }
}
