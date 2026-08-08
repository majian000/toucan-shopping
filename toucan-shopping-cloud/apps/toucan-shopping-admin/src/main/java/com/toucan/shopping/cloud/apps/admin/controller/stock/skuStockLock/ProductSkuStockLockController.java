package com.toucan.shopping.cloud.apps.admin.controller.stock.skuStockLock;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.DictServiceAPI;
import com.toucan.shopping.cloud.stock.api.ProductSkuStockLockServiceAPI;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.product.constant.ProductStockLockDictConstant;
import com.toucan.shopping.modules.stock.page.ProductSkuStockLockPageInfo;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 商品SKU库存锁定
 * @author majian
 */
@RestController
@RequestMapping("/stock/productSkuStockLock")
public class ProductSkuStockLockController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ProductSkuStockLockServiceAPI productSkuStockLockService;

    @Autowired
    private DictServiceAPI dictServiceAPI;



    private void setProductStockLockDictList(HttpServletRequest request) throws NoSuchAlgorithmException {
        DictVO queryDict=new DictVO();
        queryDict.setCategoryCode(ProductStockLockDictConstant.PRODUCT_STOCK_LOCK_DICT_CATEGORY_CODE);
        queryDict.setCodes(new LinkedList<>());
        queryDict.getCodes().add(ProductStockLockDictConstant.PRODUCT_STOCK_LOCK_DICT_TYPE_CODE);
        queryDict.setAppCode(toucan.getAppCode());
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryDict);
        ResultTypeObjectVO<List<DictVO>> resultObjectVO = dictServiceAPI.queryDictByCodesAndCategoryCode(requestJsonVO);
        if(resultObjectVO.isSuccess()) {
            if(!CollectionUtils.isEmpty(resultObjectVO.getData())){
                for(DictVO dictVO:resultObjectVO.getData()){
                    switch (dictVO.getCode()){
                        case ProductStockLockDictConstant.PRODUCT_STOCK_LOCK_DICT_TYPE_CODE:
                            request.setAttribute("productStockLockTypeList",dictVO.getChildren());
                            break;
                    }
                }
            }
        }

    }




    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:stock:productSkuStockLock:list:api"})
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public TableVO list(ProductSkuStockLockPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = productSkuStockLockService.queryListPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<ProductSkuStockLockVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),ProductSkuStockLockVO.class);
                    if(tableVO.getCount()>0) {
                        tableVO.setData((List)list);
                    }
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }




}
