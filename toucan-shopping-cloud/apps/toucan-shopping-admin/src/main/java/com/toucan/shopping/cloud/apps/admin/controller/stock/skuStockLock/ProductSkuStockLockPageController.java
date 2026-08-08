package com.toucan.shopping.cloud.apps.admin.controller.stock.skuStockLock;

import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.admin.auth.api.DictServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.stock.api.ProductSkuStockLockServiceAPI;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.product.constant.ProductStockLockDictConstant;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

/**
 * 商品SKU库存锁定 - 页面控制器
 */
@Controller
public class ProductSkuStockLockPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private ProductSkuStockLockServiceAPI productSkuStockLockService;

    @Autowired
    private DictServiceAPI dictServiceAPI;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/productSkuStockLock/listPage", method = RequestMethod.GET)
    public String listPage(HttpServletRequest request) throws NoSuchAlgorithmException {
        super.initButtons(request, toucan, "/productSkuStockLock/listPage", functionServiceAPI);
        this.setProductStockLockDictList(request);
        return "pages/stock/skuStockLock/list.html";
    }

    private void setProductStockLockDictList(HttpServletRequest request) throws NoSuchAlgorithmException {
        DictVO queryDict = new DictVO();
        queryDict.setCategoryCode(ProductStockLockDictConstant.PRODUCT_STOCK_LOCK_DICT_CATEGORY_CODE);
        queryDict.setCodes(new LinkedList<>());
        queryDict.getCodes().add(ProductStockLockDictConstant.PRODUCT_STOCK_LOCK_DICT_TYPE_CODE);
        queryDict.setAppCode(toucan.getAppCode());
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryDict);
        ResultTypeObjectVO<List<DictVO>> resultObjectVO = dictServiceAPI.queryDictByCodesAndCategoryCode(requestJsonVO);
        if (resultObjectVO.isSuccess()) {
            if (!CollectionUtils.isEmpty(resultObjectVO.getData())) {
                for (DictVO dictVO : resultObjectVO.getData()) {
                    switch (dictVO.getCode()) {
                        case ProductStockLockDictConstant.PRODUCT_STOCK_LOCK_DICT_TYPE_CODE:
                            request.setAttribute("productStockLockTypeList", dictVO.getChildren());
                            break;
                    }
                }
            }
        }
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/productSkuStockLock/detailPage/{id}", method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request, @PathVariable Long id) {
        try {
            ProductSkuStockLockVO query = new ProductSkuStockLockVO();
            query.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
            ResultObjectVO resultObjectVO = productSkuStockLockService.findById(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                ProductSkuStockLockVO productSkuStockLockVO = resultObjectVO.formatData(ProductSkuStockLockVO.class);
                request.setAttribute("model", productSkuStockLockVO);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return "pages/stock/skuStockLock/detail.html";
    }

}
