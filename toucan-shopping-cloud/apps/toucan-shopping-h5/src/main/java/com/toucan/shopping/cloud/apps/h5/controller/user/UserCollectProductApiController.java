package com.toucan.shopping.cloud.apps.h5.controller.user;

import com.toucan.shopping.cloud.apps.h5.controller.BaseController;
import com.toucan.shopping.cloud.product.api.ProductSkuServiceAPI;
import com.toucan.shopping.cloud.user.api.UserCollectProductServiceAPI;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.user.page.UserCollectProductPageInfo;
import com.toucan.shopping.modules.user.vo.UserCollectProductVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * H5 用户收藏商品
 */
@RestController("h5UserCollectProductController")
@RequestMapping("/h5/user/collect/product")
public class UserCollectProductApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserCollectProductServiceAPI userCollectProductService;

    @Autowired
    private ProductSkuServiceAPI productSkuService;

    @Autowired
    private ImageUploadService imageUploadService;

    @UserAuth
    @RequestMapping(value = "/collect")
    public ResultObjectVO collect(HttpServletRequest request, @RequestBody UserCollectProductVO userCollectProductVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (userCollectProductVO == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,没有找到收藏商品");
            return resultObjectVO;
        }
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            userCollectProductVO.setUserMainId(Long.parseLong(userMainId));
            userCollectProductVO.setAppCode(this.getAppCode());

            if (userCollectProductVO.getUserMainId() == null) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空");
                return resultObjectVO;
            }
            if (userCollectProductVO.getProductSkuId() == null) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到收藏商品");
                return resultObjectVO;
            }

            if (userCollectProductVO.getType() == 1) {
                resultObjectVO = userCollectProductService.save(RequestJsonVOGenerator.generator(this.getAppCode(), userCollectProductVO));
            } else {
                resultObjectVO = userCollectProductService.deleteBySkuIdAndUserMainIdAndAppCode(RequestJsonVOGenerator.generator(this.getAppCode(), userCollectProductVO));
            }
            resultObjectVO.setData(null);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    @UserAuth
    @RequestMapping(value = "/isCollect")
    public ResultObjectVO isCollect(HttpServletRequest request, @RequestBody UserCollectProductVO vo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (vo == null || vo.getProductSkuIds() == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败,ID不能为空");
            return resultObjectVO;
        }
        try {
            vo.setUserMainId(Long.parseLong(UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()))));
            vo.setAppCode(this.getAppCode());
            resultObjectVO = userCollectProductService.queryCollectProducts(RequestJsonVOGenerator.generator(this.getAppCode(), vo));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    @UserAuth
    @RequestMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultObjectVO list(HttpServletRequest request, @RequestBody UserCollectProductPageInfo pageInfo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            pageInfo.setUserMainId(Long.parseLong(UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()))));
            pageInfo.setAppCode(this.getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), pageInfo);
            resultObjectVO = userCollectProductService.queryListPage(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                pageInfo = resultObjectVO.formatData(UserCollectProductPageInfo.class);
                if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
                    List<ProductSkuVO> productSkus = new LinkedList<>();
                    for (UserCollectProductVO collectVO : pageInfo.getList()) {
                        ProductSkuVO sku = new ProductSkuVO();
                        sku.setId(collectVO.getProductSkuId());
                        productSkus.add(sku);
                    }
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), productSkus);
                    ResultObjectVO productResult = productSkuService.queryByIdList(requestJsonVO);
                    if (productResult.isSuccess()) {
                        List<ProductSku> skuList = productResult.formatDataList(ProductSku.class);
                        if (CollectionUtils.isNotEmpty(skuList)) {
                            for (ProductSku sku : skuList) {
                                for (UserCollectProductVO collectVO : pageInfo.getList()) {
                                    if (sku.getId().longValue() == collectVO.getProductSkuId().longValue()) {
                                        collectVO.setProductSkuName(sku.getName());
                                        if (sku.getStatus().intValue() == 0) {
                                            collectVO.setProductSkuName(collectVO.getProductSkuName() + " 已下架");
                                        }
                                        if (sku.getStockNum().longValue() <= 0) {
                                            collectVO.setProductSkuName(collectVO.getProductSkuName() + " 已售罄");
                                        }
                                        collectVO.setProductPrice(sku.getPrice());
                                        collectVO.setHttpProductImgPath(imageUploadService.getImageHttpPrefix() + sku.getProductPreviewPath());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                resultObjectVO.setData(pageInfo);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }
}
