package com.toucan.shopping.cloud.apps.admin.controller.product.productSpu;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyValueService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSpuService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.product.page.AttributeKeyPageInfo;
import com.toucan.shopping.modules.product.page.BrandPageInfo;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedList;
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

    @Autowired
    private FeignProductSpuService feignProductSpuService;

    @Autowired
    private FeignBrandService feignBrandService;

    @Autowired
    private FeignAttributeKeyValueService feignAttributeKeyValueService;



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/productSpu/listPage",feignFunctionService);
        return "pages/product/productSpu/list.html";
    }





    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/addPage/{categoryId}",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request,@PathVariable Long categoryId)
    {

        if(categoryId!=null&&categoryId!=-1)
        {
            try {
                CategoryVO queryCategoryVO = new CategoryVO();
                queryCategoryVO.setId(categoryId);
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategoryVO);
                ResultObjectVO resultObjectVO = feignCategoryService.queryById(requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    CategoryTreeVO categoryTreeVO = resultObjectVO.formatData(CategoryTreeVO.class);
                    request.setAttribute("categoryId",categoryTreeVO.getId());
                    request.setAttribute("categoryName",categoryTreeVO.getPath());
                }else{
                    request.setAttribute("categoryId","");
                    request.setAttribute("categoryName","");
                }
                return "pages/product/productSpu/add.html";
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
            }
        }
        request.setAttribute("categoryId","");
        request.setAttribute("categoryName","");
        return "pages/product/productSpu/add.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/selectBrandPage/{categoryId}",method = RequestMethod.GET)
    public String selectBrandPage(HttpServletRequest request, @PathVariable Long categoryId)
    {
        request.setAttribute("categoryId",categoryId);
        return "pages/product/productSpu/brand_list.html";
    }


    /**
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(@RequestBody ProductSpuVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignProductSpuService.save(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("保存失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(@RequestBody ProductSpuVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignProductSpuService.update(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("修改失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 查询品牌
     * @param list
     * @param brandIdList
     */
    void queryBrand(List<ProductSpuVO> list,List<Long> brandIdList)
    {
        try {
            BrandVO queryBrandVO = new BrandVO();
            queryBrandVO.setIdList(brandIdList);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryBrandVO);
            ResultObjectVO resultObjectVO = feignBrandService.findByIdList(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<BrandVO> brandVOS = resultObjectVO.formatDataList(BrandVO.class);
                if(CollectionUtils.isNotEmpty(brandVOS))
                {
                    for(ProductSpuVO productSpuVO:list)
                    {
                        for(BrandVO brandVO:brandVOS)
                        {
                            if(productSpuVO.getBrandId()!=null&&productSpuVO.getBrandId().longValue()==brandVO.getId().longValue())
                            {
                                productSpuVO.setBrandChineseName(brandVO.getChineseName());
                                productSpuVO.setBrandEnglishName(brandVO.getEnglishName());
                                productSpuVO.setBrandLogo(brandVO.getLogoPath());
                                if(brandVO.getLogoPath()!=null) {
                                    productSpuVO.setBrandHttpLogo(imageUploadService.getImageHttpPrefix() +brandVO.getLogoPath());
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
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
            ResultObjectVO resultObjectVO = feignProductSpuService.queryListPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<ProductSpuVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),ProductSpuVO.class);
                    if(tableVO.getCount()>0) {
                        boolean brandExists=false;
                        List<Long> brandIdList = new LinkedList<>();
                        for(int i=0;i<list.size();i++)
                        {
                            ProductSpuVO productSpuVO = list.get(i);

                            //设置品牌ID
                            brandExists=false;
                            for(Long brandId:brandIdList)
                            {
                                if(productSpuVO.getBrandId()!=null&&brandId!=null
                                        &&brandId.longValue()==productSpuVO.getBrandId().longValue())
                                {
                                    brandExists=true;
                                    break;
                                }

                            }
                            if(!brandExists) {
                                if(productSpuVO.getBrandId()!=null) {
                                    brandIdList.add(productSpuVO.getBrandId());
                                }
                            }
                        }

                        //查询品牌名称
                        this.queryBrand(list, brandIdList);

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



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/query/category/tree/pid",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryCategoryTreeByParentId(@RequestParam Long id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(id==null)
            {
                id=-1L;
            }
            CategoryVO query = new CategoryVO();
            query.setParentId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            resultObjectVO = feignCategoryService.queryListByPid(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    List<CategoryTreeVO> categoryVOS = resultObjectVO.formatDataList(CategoryTreeVO.class);
                    for(CategoryTreeVO categoryTreeVO:categoryVOS)
                    {
                        categoryTreeVO.setOpen(false);
                        categoryTreeVO.setIcon(null);
                    }
                    resultObjectVO.setData(categoryVOS);
                }
            }
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }






    /**
     * 查询品牌列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/brand/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, BrandPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignBrandService.queryListPage(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<BrandVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),BrandVO.class);
                    if(tableVO.getCount()>0) {
                        List<Category> categories = new ArrayList<Category>();
                        for(BrandVO brandVO:list)
                        {
                            brandVO.setCategoryNamePathList(new LinkedList<>());
                            String[] categoryIdArray = brandVO.getCategoryIdCacheArray();
                            if(categoryIdArray!=null&&categoryIdArray.length>0) {
                                for(String categoryId:categoryIdArray) {
                                    if(categoryId!=null) {
                                        Category category = new Category();
                                        category.setId(Long.parseLong(categoryId));
                                        categories.add(category);
                                    }
                                }
                            }

                            if(StringUtils.isNotEmpty(brandVO.getLogoPath()))
                            {
                                brandVO.setHttpLogoPath(imageUploadService.getImageHttpPrefix()+brandVO.getLogoPath());
                            }

                        }
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),categories);
                        resultObjectVO = feignCategoryService.queryByIdList(requestJsonVO);
                        if(resultObjectVO.isSuccess()) {
                            List<CategoryVO> categoryList = resultObjectVO.formatDataList(CategoryVO.class);
                            if(CollectionUtils.isNotEmpty(categoryList))
                            {
                                for(CategoryVO categoryVO:categoryList)
                                {
                                    for(BrandVO brandVO:list)
                                    {
                                        String[] categoryIdArray = brandVO.getCategoryIdCacheArray();
                                        if(categoryIdArray!=null&&categoryIdArray.length>0) {
                                            for(String categoryId:categoryIdArray)
                                            {
                                                if(String.valueOf(categoryVO.getId()).equals(categoryId))
                                                {
                                                    brandVO.getCategoryNamePathList().add(categoryVO.getNamePath());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
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






    @RequestMapping(value = "/query/attribute/tree/page",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAttributeTreePage(@RequestBody AttributeKeyPageInfo attributeKeyPageInfo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(attributeKeyPageInfo.getCategoryId()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到分类ID");
            return resultObjectVO;
        }
        try {

            attributeKeyPageInfo.setAttributeType((short)1); //查询全局属性
            attributeKeyPageInfo.setParentId(-1L); //从根节点开始查询
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), attributeKeyPageInfo);
            resultObjectVO = feignAttributeKeyValueService.queryAttributeTreePage(requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 删除
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(HttpServletRequest request,  @PathVariable String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            ProductSpuVO productSpuVO =new ProductSpuVO();
            productSpuVO.setId(Long.parseLong(id));

            String entityJson = JSONObject.toJSONString(productSpuVO);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignProductSpuService.deleteById(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 删除
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/delete/ids",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds( @RequestBody List<ProductSpuVO> productSpuVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(org.springframework.util.CollectionUtils.isEmpty(productSpuVOS))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(productSpuVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignProductSpuService.deleteByIds(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            ProductSpuVO queryProductSpu = new ProductSpuVO();
            queryProductSpu.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryProductSpu);
            ResultObjectVO resultObjectVO = feignProductSpuService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    ProductSpuVO productSpuVO = resultObjectVO.formatData(ProductSpuVO.class);

                    //查询分类
                    CategoryVO queryCategory = new CategoryVO();
                    queryCategory.setId(productSpuVO.getCategoryId());
                    requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryCategory);
                    resultObjectVO = feignCategoryService.queryById(requestJsonVO);
                    if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null)
                    {
                        CategoryTreeVO categoryTreeVO = resultObjectVO.formatData(CategoryTreeVO.class);
                        if(categoryTreeVO!=null) {
                            productSpuVO.setCategoryName(categoryTreeVO.getPath());
                        }
                    }

                    //查询品牌
                    BrandVO queryBrand = new BrandVO();
                    queryBrand.setId(productSpuVO.getBrandId());
                    requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryBrand);
                    resultObjectVO = feignBrandService.findById(requestJsonVO.sign(),requestJsonVO);
                    if(resultObjectVO.isSuccess()&&resultObjectVO.getData()!=null)
                    {
                        List<BrandVO> brandVOS = resultObjectVO.formatDataList(BrandVO.class);
                        BrandVO brandVO = brandVOS.get(0);
                        String brandName = "";
                        if(StringUtils.isNotEmpty(brandVO.getChineseName()))
                        {
                            brandName+=brandVO.getChineseName();
                        }
                        if(StringUtils.isNotEmpty(brandVO.getEnglishName()))
                        {
                            brandName+=" "+brandVO.getEnglishName();
                        }
                        productSpuVO.setBrandName(brandName);

                    }

                    //将属性名和属性值转换成字符串
                    productSpuVO.setAttributeKeyValuesJson(JSONArray.toJSONString(productSpuVO.getAttributeKeyValues()));

                    request.setAttribute("model",productSpuVO);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/product/productSpu/edit.html";
    }




}

