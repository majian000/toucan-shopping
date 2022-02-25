package com.toucan.shopping.cloud.apps.admin.controller.product.productSpu;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.vo.ShopProductVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * SPU管理
 * @author majian
 */
@Controller
@RequestMapping("/productSpu")
public class ProductSpuController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignShopProductService feignShopProductService;

    @Autowired
    private FeignCategoryService feignCategoryService;

    @Autowired
    private ImageUploadService imageUploadService;



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/product/shopProduct/listPage",feignFunctionService);
        return "pages/product/shopProduct/list.html";
    }



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, ShopProductPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignShopProductService.queryListPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<ShopProductVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),ShopProductVO.class);
                    if(CollectionUtils.isNotEmpty(list))
                    {
                        Long[] categoryIds = new Long[list.size()];
                        //查询类别名称
                        CategoryVO queryCategoryVO = new CategoryVO();
                        queryCategoryVO.setIdArray(categoryIds);
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryCategoryVO);
                        resultObjectVO = feignCategoryService.findByIdArray(requestJsonVO.sign(),requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            List<CategoryVO> categoryVOS = (List<CategoryVO>)resultObjectVO.formatDataList(CategoryVO.class);
                            if(CollectionUtils.isNotEmpty(categoryVOS))
                            {
                                for(ShopProductVO shopProductVO:list)
                                {
                                    for(CategoryVO categoryVO:categoryVOS)
                                    {
                                        if(shopProductVO.getCategoryId().longValue()==categoryVO.getId().longValue())
                                        {
                                            shopProductVO.setCategoryName(categoryVO.getName());
                                            shopProductVO.setCategoryPath(categoryVO.getNamePath());
                                            break;
                                        }
                                    }
                                }
                            }
                        }


                        for(ShopProductVO shopProductVO:list)
                        {

                            if(shopProductVO.getMainPhotoFilePath()!=null) {
                                shopProductVO.setHttpMainPhotoFilePath(imageUploadService.getImageHttpPrefix()+shopProductVO.getMainPhotoFilePath());
                            }
                        }

                    }
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

