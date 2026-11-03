package com.toucan.shopping.cloud.apps.h5.controller.user;

import com.toucan.shopping.cloud.apps.h5.controller.BaseController;
import com.toucan.shopping.cloud.product.api.ProductSkuServiceAPI;
import com.toucan.shopping.cloud.user.api.UserBuyCarServiceAPI;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.user.vo.UserBuyCarItemVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * H5 购物车
 */
@RestController("h5UserBuyCarController")
@RequestMapping("/h5/user/buyCar")
public class UserBuyCarApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserBuyCarServiceAPI userBuyCarService;

    @Autowired
    private ProductSkuServiceAPI productSkuService;

    @Autowired
    private ImageUploadService imageUploadService;

    /** 购物车列表（含商品信息） */
    @UserAuth
    @RequestMapping("/list")
    public ResultObjectVO list(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            UserBuyCarItemVO query = new UserBuyCarItemVO();
            query.setUserMainId(Long.parseLong(userMainId));
            ResultObjectVO buyCarResult = userBuyCarService.listByUserMainId(RequestJsonVOGenerator.generator(this.getAppCode(), query));
            if (buyCarResult.isSuccess()) {
                List<UserBuyCarItemVO> cartList = buyCarResult.formatDataList(UserBuyCarItemVO.class);
                if (CollectionUtils.isNotEmpty(cartList)) {
                    List<ProductSkuVO> skus = new LinkedList<>();
                    for (UserBuyCarItemVO item : cartList) {
                        ProductSkuVO sku = new ProductSkuVO();
                        sku.setId(item.getShopProductSkuId());
                        skus.add(sku);
                    }
                    ResultObjectVO skuResult = productSkuService.queryByIdList(RequestJsonVOGenerator.generator(this.getAppCode(), skus));
                    if (skuResult.isSuccess()) {
                        List<ProductSku> skuList = skuResult.formatDataList(ProductSku.class);
                        if (CollectionUtils.isNotEmpty(skuList)) {
                            for (UserBuyCarItemVO item : cartList) {
                                for (ProductSku sku : skuList) {
                                    if (sku.getId().longValue() == item.getShopProductSkuId().longValue()) {
                                        item.setProductSkuName(sku.getName());
                                        item.setProductPrice(sku.getPrice());
                                        item.setHttpProductImgPath(imageUploadService.getImageHttpPrefix() + sku.getProductPreviewPath());
                                        item.setShopId(sku.getShopId());
                                        if (sku.getStatus().intValue() == 0) {
                                            item.setAllowedBuy(false);
                                            item.setNoAllowedBuyStatus((short) 1);
                                            item.setNoAllowedBuyDesc("已下架");
                                        } else if (sku.getStockNum() != null && sku.getStockNum().intValue() <= 0) {
                                            item.setAllowedBuy(false);
                                            item.setNoAllowedBuyStatus((short) 2);
                                            item.setNoAllowedBuyDesc("已售罄");
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                resultObjectVO.setData(cartList);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /** 加入购物车 */
    @UserAuth
    @RequestMapping("/save")
    public ResultObjectVO save(HttpServletRequest request, @RequestBody UserBuyCarItemVO vo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            vo.setUserMainId(Long.parseLong(userMainId));
            resultObjectVO = userBuyCarService.save(RequestJsonVOGenerator.generator(this.getAppCode(), vo));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /** 删除购物车项 */
    @UserAuth
    @RequestMapping("/remove")
    public ResultObjectVO remove(HttpServletRequest request, @RequestBody UserBuyCarItemVO vo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            vo.setUserMainId(Long.parseLong(userMainId));
            resultObjectVO = userBuyCarService.removeBuyCar(RequestJsonVOGenerator.generator(this.getAppCode(), vo));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    /** 清空购物车 */
    @UserAuth
    @RequestMapping("/clear")
    public ResultObjectVO clear(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            UserBuyCarItemVO vo = new UserBuyCarItemVO();
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            vo.setUserMainId(Long.parseLong(userMainId));
            resultObjectVO = userBuyCarService.clearByUserMainId(RequestJsonVOGenerator.generator(this.getAppCode(), vo));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    /** 修改购物车项（数量等） */
    @UserAuth
    @RequestMapping("/update")
    public ResultObjectVO update(HttpServletRequest request, @RequestBody UserBuyCarItemVO vo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            vo.setUserMainId(Long.parseLong(userMainId));
            resultObjectVO = userBuyCarService.update(RequestJsonVOGenerator.generator(this.getAppCode(), vo));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }
}
