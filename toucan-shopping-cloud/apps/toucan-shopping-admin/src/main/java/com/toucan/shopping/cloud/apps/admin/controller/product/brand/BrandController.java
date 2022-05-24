package com.toucan.shopping.cloud.apps.admin.controller.product.brand;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.product.entity.AttributeKey;
import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.BrandCategory;
import com.toucan.shopping.modules.product.page.BrandPageInfo;
import com.toucan.shopping.modules.product.vo.BrandCategoryVO;
import com.toucan.shopping.modules.product.vo.BrandVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 品牌管理
 */
@Controller
@RequestMapping("/product/brand")
public class BrandController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignBrandService feignBrandService;

    @Autowired
    private FeignCategoryService feignCategoryService;

    @Autowired
    private FeignBrandCategoryService feignBrandCategoryService;

    @Autowired
    private ImageUploadService imageUploadService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/product/brand/listPage",feignFunctionService);
        return "pages/product/brand/list.html";
    }



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, BrandPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {

            RequestJsonVO requestJsonVO = null;
            ResultObjectVO resultObjectVO = null;
            if(pageInfo.getCategoryId()!=null&&pageInfo.getCategoryId().longValue()!=-1) {
                //查询分类以及子分类
                CategoryVO categoryVO = new CategoryVO();
                categoryVO.setId(pageInfo.getCategoryId());
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categoryVO);
                resultObjectVO = feignCategoryService.queryChildListByPid(requestJsonVO);
                if (resultObjectVO.isSuccess()) {
                    if (resultObjectVO.getData() != null) {
                        List<CategoryVO> categoryVOS = resultObjectVO.formatDataList(CategoryVO.class);
                        if (CollectionUtils.isNotEmpty(categoryVOS)) {
                            List<Long> categoryIdList = new LinkedList<>();
                            for (CategoryVO cv : categoryVOS) {
                                categoryIdList.add(cv.getId());
                            }
                            categoryIdList.add(pageInfo.getCategoryId());
                            pageInfo.setCategoryIdList(categoryIdList);
                            pageInfo.setCategoryId(null);
                        }
                    }
                }
            }
            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            resultObjectVO = feignBrandService.queryListPage(SignUtil.sign(requestJsonVO),requestJsonVO);
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



    /**
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(@RequestBody BrandVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignBrandService.save(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {
        return "pages/product/brand/add.html";
    }





    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping("/upload/logo")
    @ResponseBody
    public ResultObjectVO  uploadLogo(@RequestParam("file") MultipartFile file, @RequestParam("id")Long brandId)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(0);
        try{
            String fileName = file.getOriginalFilename();
            if(!ImageUtils.isStaticImage(fileName))
            {
                throw new RuntimeException("请上传图片格式(.jpg|.jpeg|.png)");
            }
            String fileExt = ".jpg";
            if(StringUtils.isNotEmpty(fileName)&&fileName.indexOf(".")!=-1)
            {
                fileExt = fileName.substring(fileName.lastIndexOf(".")+1);

            }
            String groupPath = imageUploadService.uploadFile(file.getBytes(),fileExt);

            if(StringUtils.isEmpty(groupPath))
            {
                throw new RuntimeException("LOGO上传失败");
            }
            BrandVO brandVO = new BrandVO();
            if(brandId!=null&&brandId.longValue()!=-1) { //给修改功能和注册功能使用,注册功能没有用户ID
                brandVO.setId(brandId);
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), brandVO);
                ResultObjectVO brandResultVO = feignBrandService.findById(requestJsonVO.sign(), requestJsonVO);
                if (brandResultVO.isSuccess()) {
                    brandVO = brandResultVO.formatData(BrandVO.class);
                    brandVO.setLogoPath(groupPath);

                    //设置预览头像
                    if (brandVO.getLogoPath() != null) {
                        brandVO.setHttpLogoPath(imageUploadService.getImageHttpPrefix() + brandVO.getLogoPath());
                    }
                    resultObjectVO.setData(brandVO);
                }
            }else{
                brandVO.setLogoPath(groupPath);

                //设置预览头像
                if (brandVO.getLogoPath() != null) {
                    brandVO.setHttpLogoPath(imageUploadService.getImageHttpPrefix() + brandVO.getLogoPath());
                }
                resultObjectVO.setData(brandVO);
            }
        }catch (Exception e)
        {
            resultObjectVO.setCode(1);
            resultObjectVO.setMsg("LOGO上传失败");
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
    public ResultObjectVO update(@RequestBody BrandVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignBrandService.update(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            BrandVO brandVO = new BrandVO();
            brandVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, brandVO);
            ResultObjectVO resultObjectVO = feignBrandService.findById(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<BrandVO> brandVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),BrandVO.class);
                    if(!CollectionUtils.isEmpty(brandVOS))
                    {
                        brandVO = brandVOS.get(0);

                        List<Category> categories = new LinkedList<Category>();
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

                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),categories);
                        resultObjectVO = feignCategoryService.queryByIdList(requestJsonVO);
                        if(resultObjectVO.isSuccess()) {
                            List<CategoryVO> categoryList = resultObjectVO.formatDataList(CategoryVO.class);
                            if(CollectionUtils.isNotEmpty(categoryList))
                            {
                                StringBuilder categoryNamePath = new StringBuilder();
                                int categoryListSize=categoryList.size();
                                for(int i=0;i<categoryListSize;i++)
                                {
                                    CategoryVO categoryVO = categoryList.get(i);
                                    if(categoryIdArray!=null&&categoryIdArray.length>0) {
                                        for(String categoryId:categoryIdArray)
                                        {
                                            if(String.valueOf(categoryVO.getId()).equals(categoryId))
                                            {
                                                brandVO.getCategoryNamePathList().add(categoryVO.getNamePath());
                                                categoryNamePath.append(categoryVO.getNamePath());
                                            }
                                        }
                                    }
                                    if(i+1<categoryListSize)
                                    {
                                        categoryNamePath.append("、");
                                    }
                                }
                                brandVO.setCategoryNamePath(categoryNamePath.toString());
                            }
                        }

                        if(StringUtils.isNotEmpty(brandVO.getLogoPath()))
                        {
                            brandVO.setHttpLogoPath(imageUploadService.getImageHttpPrefix()+brandVO.getLogoPath());
                        }

                        request.setAttribute("model",brandVO);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/product/brand/edit.html";
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
            Brand brand =new Brand();
            brand.setId(Long.parseLong(id));

            String entityJson = JSONObject.toJSONString(brand);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignBrandService.deleteById(requestVo.sign(), requestVo);
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
    public ResultObjectVO deleteByIds( @RequestBody List<BrandVO> brandVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(org.springframework.util.CollectionUtils.isEmpty(brandVOS))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(brandVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignBrandService.deleteByIds(SignUtil.sign(requestVo), requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 列表页 查询分类树
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/list/page/query/category/tree",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO queryCategoryTreeForListPage(HttpServletRequest request)
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




    public void setTreeNodeSelect(AtomicLong id,CategoryTreeVO parentTreeVO,List<CategoryTreeVO> categoryTreeVOList,List<BrandCategoryVO> brandCategories)
    {
        for(CategoryTreeVO categoryTreeVO:categoryTreeVOList)
        {
            //保留数据库ID
            categoryTreeVO.setNodeId(categoryTreeVO.getId());
            //将ID替换成自增
            categoryTreeVO.setId(id.incrementAndGet());
            categoryTreeVO.setParentId(parentTreeVO.getId());
            categoryTreeVO.setPid(parentTreeVO.getId());
            if(CollectionUtils.isNotEmpty(brandCategories)) {
                for (BrandCategory brandCategory : brandCategories) {
                    if (categoryTreeVO.getNodeId().longValue() == brandCategory.getCategoryId().longValue()) {
                        //设置节点被选中
                        categoryTreeVO.getState().setChecked(true);
                    }
                }
            }
            if(!CollectionUtils.isEmpty(categoryTreeVO.getChildren()))
            {
                setTreeNodeSelect(id,categoryTreeVO,categoryTreeVO.getChildren(),brandCategories);
            }
        }
    }


    /**
     * 返回类别树
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/query/category/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryCategoryTree(Long brandId)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            //查询类别树
            CategoryVO query = new CategoryVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),query);
            resultObjectVO = feignCategoryService.queryTree(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<CategoryTreeVO> categoryTreeVOList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), CategoryTreeVO.class);
                List<BrandCategoryVO> brandCategoryVOS = null;
                if(brandId!=null&&brandId.longValue()!=-1L) {
                    BrandCategoryVO queryBrandCategory = new BrandCategoryVO();
                    queryBrandCategory.setBrandId(brandId);
                    requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryBrandCategory);
                    resultObjectVO = feignBrandCategoryService.findByBrandId(requestJsonVO);
                    if (resultObjectVO.isSuccess()) {
                        brandCategoryVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), BrandCategoryVO.class);
                    }
                }

                AtomicLong id = new AtomicLong();
                for (CategoryTreeVO categoryTreeVO : categoryTreeVOList) {
                    //保留数据库ID
                    categoryTreeVO.setNodeId(categoryTreeVO.getId());
                    //将ID替换成自增
                    categoryTreeVO.setId(id.incrementAndGet());
                    categoryTreeVO.setText(categoryTreeVO.getTitle());

                    if (!CollectionUtils.isEmpty(brandCategoryVOS)) {
                        for (BrandCategory brandCategory : brandCategoryVOS) {
                            if (categoryTreeVO.getNodeId().longValue() == brandCategory.getCategoryId().longValue()) {
                                //设置节点被选中
                                categoryTreeVO.getState().setChecked(true);
                            }
                        }
                    }
                    setTreeNodeSelect(id,categoryTreeVO, categoryTreeVO.getChildren(), brandCategoryVOS);
                }
                resultObjectVO.setData(categoryTreeVOList);
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



}

