package com.toucan.shopping.cloud.apps.admin.controller.product.shopProductApprove;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
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
import com.toucan.shopping.modules.product.vo.BrandVO;
import com.toucan.shopping.modules.product.vo.ShopProductVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 店铺商品管理
 * @author majian
 */
@Controller
@RequestMapping("/product/shopProductApprove")
public class ShopProductApproveController extends UIController {

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

    @Autowired
    private FeignBrandService feignBrandService;

    @Autowired
    private FeignShopCategoryService feignShopCategoryService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/product/shopProductApprove/listPage",feignFunctionService);
        return "pages/product/shopProductApprove/list.html";
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
                        Long[] shopCategoryIds = new Long[list.size()];
                        List<Long> brandIdList = new LinkedList<>();

                        for(int i=0;i<list.size();i++)
                        {
                            ShopProductVO shopProductVO = list.get(i);
                            categoryIds[i] = shopProductVO.getCategoryId();

                            //设置品牌ID
                            boolean brandExists=false;
                            for(Long brandId:brandIdList)
                            {
                                if(shopProductVO.getBrandId()!=null&&brandId!=null
                                        &&brandId.longValue()==shopProductVO.getBrandId().longValue())
                                {
                                    brandExists=true;
                                    break;
                                }

                            }
                            if(!brandExists) {
                                if(shopProductVO.getBrandId()!=null) {
                                    brandIdList.add(shopProductVO.getBrandId());
                                }
                            }


                            //设置店铺分类ID
                            boolean shopCategoryExists=false;
                            for(int sci=0;sci<shopCategoryIds.length;sci++)
                            {
                                Long shopCategoryId = shopCategoryIds[sci];
                                if(shopProductVO.getShopCategoryId()!=null&&shopCategoryId!=null
                                        &&shopCategoryId.longValue()==shopProductVO.getShopCategoryId().longValue())
                                {
                                    shopCategoryExists=true;
                                    break;
                                }

                            }
                            if(!shopCategoryExists) {
                                if(shopProductVO.getShopCategoryId()!=null) {
                                    shopCategoryIds[i] = shopProductVO.getShopCategoryId();
                                }
                            }


                        }

                        //查询类别名称
                        CategoryVO queryCategoryVO = new CategoryVO();
                        queryCategoryVO.setIdArray(categoryIds);
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryCategoryVO);
                        resultObjectVO = feignCategoryService.findByIdArray(requestJsonVO.sign(),requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            List<CategoryVO> categoryVOS =resultObjectVO.formatDataList(CategoryVO.class);
                            if(CollectionUtils.isNotEmpty(categoryVOS))
                            {
                                for(ShopProductVO shopProductVO:list)
                                {
                                    for(CategoryVO categoryVO:categoryVOS)
                                    {
                                        if(shopProductVO.getCategoryId()!=null&&shopProductVO.getCategoryId().longValue()==categoryVO.getId().longValue())
                                        {
                                            shopProductVO.setCategoryName(categoryVO.getName());
                                            shopProductVO.setCategoryPath(categoryVO.getNamePath());
                                            break;
                                        }
                                    }
                                }
                            }
                        }


                        //查询店铺类别名称
                        ShopCategoryVO queryShopCategoryVO = new ShopCategoryVO();
                        queryShopCategoryVO.setIdArray(shopCategoryIds);
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopCategoryVO);
                        resultObjectVO = feignShopCategoryService.findByIdArray(requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            List<ShopCategoryVO> shopCategoryVOS = resultObjectVO.formatDataList(ShopCategoryVO.class);
                            if(CollectionUtils.isNotEmpty(shopCategoryVOS))
                            {
                                for(ShopProductVO shopProductVO:list)
                                {
                                    for(ShopCategoryVO shopCategoryVO:shopCategoryVOS)
                                    {
                                        if(shopProductVO.getShopCategoryId()!=null&&shopProductVO.getShopCategoryId().longValue()==shopCategoryVO.getId().longValue())
                                        {
                                            shopProductVO.setShopCategoryName(shopCategoryVO.getName());
                                            shopProductVO.setShopCategoryPath(shopCategoryVO.getNamePath());
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        //查询品牌名称
                        BrandVO queryBrandVO = new BrandVO();
                        queryBrandVO.setIdList(brandIdList);
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryBrandVO);
                        resultObjectVO = feignBrandService.findByIdList(requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            List<BrandVO> brandVOS = (List<BrandVO>)resultObjectVO.formatDataList(BrandVO.class);
                            if(CollectionUtils.isNotEmpty(brandVOS))
                            {
                                for(ShopProductVO shopProductVO:list)
                                {
                                    for(BrandVO brandVO:brandVOS)
                                    {
                                        if(shopProductVO.getBrandId()!=null&&shopProductVO.getBrandId().longValue()==brandVO.getId().longValue())
                                        {
                                            shopProductVO.setBrandChineseName(brandVO.getChineseName());
                                            shopProductVO.setBrandEnglishName(brandVO.getEnglishName());
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






    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/query/category/tree",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO queryCategoryTree(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryVO query = new CategoryVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            resultObjectVO = feignCategoryService.queryTree(SignUtil.sign(requestJsonVO),requestJsonVO);
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




}

