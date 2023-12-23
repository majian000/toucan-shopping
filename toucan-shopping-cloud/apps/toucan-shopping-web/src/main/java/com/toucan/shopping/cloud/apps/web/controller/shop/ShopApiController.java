package com.toucan.shopping.cloud.apps.web.controller.shop;

import com.toucan.shopping.cloud.apps.web.service.IndexService;
import com.toucan.shopping.cloud.apps.web.vo.index.LikeProductVo;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopBannerService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryTreeVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 店铺控制器
 */
@RestController("apiShopController")
@RequestMapping("/api/shop")
public class ShopApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IndexService indexService;


    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignShopBannerService feignShopBannerService;

    @Autowired
    private FeignShopCategoryService feignShopCategoryService;


    @Autowired
    private ImageUploadService imageUploadService;

    /**
     * 店铺首页轮播图
     * @param request
     * @param shopBannerVO
     * @return
     */
    @RequestMapping("/index/banners")
    @ResponseBody
    public ResultObjectVO banners(HttpServletRequest request,@RequestBody ShopBannerVO shopBannerVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ShopBannerVO queryShopBanner=new ShopBannerVO();
        queryShopBanner.setShopId(shopBannerVO.getShopId());
        queryShopBanner.setPosition("PC_INDEX");
        try {
            resultObjectVO = feignShopBannerService.queryIndexList(RequestJsonVOGenerator.generator(toucan.getAppCode(), queryShopBanner));
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    List<ShopBannerVO> shopBanners = resultObjectVO.formatDataList(ShopBannerVO.class);
                    for(ShopBannerVO bannerVO:shopBanners)
                    {
                        if(StringUtils.isNotEmpty(bannerVO.getImgPath())) {
                            bannerVO.setHttpImgPath(imageUploadService.getImageHttpPrefix() + bannerVO.getImgPath());
                        }
                    }
                    resultObjectVO.setData(shopBanners);
                }

            }
        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 店铺首页分类
     * @param request
     * @param shopCategoryVO
     * @return
     */
    @RequestMapping("/index/categorys")
    @ResponseBody
    public ResultObjectVO categorys(HttpServletRequest request,@RequestBody ShopCategoryVO shopCategoryVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ShopCategoryVO queryShopCategory=new ShopCategoryVO();
        queryShopCategory.setShopId(shopCategoryVO.getShopId());
        try {
            resultObjectVO = feignShopCategoryService.queryWebIndexTree(RequestJsonVOGenerator.generator(toucan.getAppCode(), queryShopCategory));
        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}
