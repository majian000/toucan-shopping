package com.toucan.shopping.cloud.apps.admin.controller.product.brand;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.cloud.product.api.BrandCategoryServiceAPI;
import com.toucan.shopping.cloud.product.api.BrandServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.BrandCategory;
import com.toucan.shopping.modules.product.page.BrandPageInfo;
import com.toucan.shopping.modules.product.vo.BrandCategoryVO;
import com.toucan.shopping.modules.product.vo.BrandVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 品牌管理
 */
@RestController
@RequestMapping("/product/brand")
public class BrandController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private BrandServiceAPI brandService;

    @Autowired
    private CategoryServiceAPI categoryService;

    @Autowired
    private BrandCategoryServiceAPI brandCategoryService;

    @Autowired
    private ImageUploadService imageUploadService;


    /**
     * 查询列表
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:brand:list"})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public TableVO list(HttpServletRequest request, @RequestBody BrandPageInfo pageInfo) {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            ResultObjectVO resultObjectVO = brandService.queryListPage(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    Map<String, Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total") != null ? resultObjectDataMap.get("total") : "0")));
                    List<BrandVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), BrandVO.class);
                    if (tableVO.getCount() > 0) {
                        List<Category> categories = new LinkedList<>();
                        for (BrandVO brandVO : list) {
                            brandVO.setCategoryNamePathList(new LinkedList<>());
                            String[] categoryIdArray = brandVO.getCategoryIdCacheArray();
                            if (categoryIdArray != null && categoryIdArray.length > 0) {
                                for (String categoryId : categoryIdArray) {
                                    if (categoryId != null) {
                                        Category category = new Category();
                                        category.setId(Long.parseLong(categoryId));
                                        categories.add(category);
                                    }
                                }
                            }

                            if (StringUtils.isNotEmpty(brandVO.getLogoPath())) {
                                brandVO.setHttpLogoPath(imageUploadService.getImageHttpPrefix() + brandVO.getLogoPath());
                            }
                        }
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categories);
                        resultObjectVO = categoryService.queryByIdList(requestJsonVO);
                        if (resultObjectVO.isSuccess()) {
                            List<CategoryVO> categoryList = resultObjectVO.formatDataList(CategoryVO.class);
                            if (!CollectionUtils.isEmpty(categoryList)) {
                                for (CategoryVO categoryVO : categoryList) {
                                    for (BrandVO brandVO : list) {
                                        String[] categoryIdArray = brandVO.getCategoryIdCacheArray();
                                        if (categoryIdArray != null && categoryIdArray.length > 0) {
                                            for (String categoryId : categoryIdArray) {
                                                if (String.valueOf(categoryVO.getId()).equals(categoryId)) {
                                                    brandVO.getCategoryNamePathList().add(categoryVO.getNamePath());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        tableVO.setData((List) list);
                    }
                }
            }
        } catch (Exception e) {
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return tableVO;
    }


    /**
     * 保存
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:brand:save"})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultObjectVO save(@RequestBody BrandVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = brandService.save(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 修改
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:brand:update"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultObjectVO update(@RequestBody BrandVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = brandService.update(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 删除
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:brand:delete"})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody Brand brand) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (brand.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(brand);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = brandService.deleteById(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 批量删除
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:brand:delete"})
    @RequestMapping(value = "/delete/ids", method = RequestMethod.POST)
    public ResultObjectVO deleteByIds(@RequestBody List<BrandVO> brandVOS) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (CollectionUtils.isEmpty(brandVOS)) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(brandVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = brandService.deleteByIds(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 上传LOGO
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:brand:upload:logo"})
    @RequestMapping("/upload/logo")
    public ResultObjectVO uploadLogo(@RequestParam("file") MultipartFile file, @RequestParam("id") Long brandId) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(0);
        try {
            String fileName = file.getOriginalFilename();
            if (!ImageUtils.isStaticImage(fileName)) {
                throw new RuntimeException("请上传图片格式(.jpg|.jpeg|.png)");
            }
            String fileExt = ".jpg";
            if (StringUtils.isNotEmpty(fileName) && fileName.indexOf(".") != -1) {
                fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
            }
            String groupPath = imageUploadService.uploadFile(file.getBytes(), fileExt);

            if (StringUtils.isEmpty(groupPath)) {
                throw new RuntimeException("LOGO上传失败");
            }
            BrandVO brandVO = new BrandVO();
            brandVO.setLogoPath(groupPath);

            // 设置预览头像
            if (brandVO.getLogoPath() != null) {
                brandVO.setHttpLogoPath(imageUploadService.getImageHttpPrefix() + brandVO.getLogoPath());
            }
            resultObjectVO.setData(brandVO);
        } catch (Exception e) {
            resultObjectVO.setCode(1);
            resultObjectVO.setMsg("LOGO上传失败");
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 列表页 - 查询分类树
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:brand:category:list"})
    @RequestMapping(value = "/list/page/query/category/tree", method = RequestMethod.POST)
    public ResultObjectVO queryCategoryTreeForListPage(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryVO query = new CategoryVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
            resultObjectVO = categoryService.queryTree(requestJsonVO);
            return resultObjectVO;
        } catch (Exception e) {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询类别树（含品牌关联选中状态）
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:brand:category:tree"})
    @RequestMapping(value = "/query/category/tree", method = RequestMethod.POST)
    public ResultObjectVO queryCategoryTree(Long brandId) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            // 查询类别树
            CategoryVO query = new CategoryVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), query);
            resultObjectVO = categoryService.queryMiniTree(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<CategoryTreeVO> categoryTreeVOList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), CategoryTreeVO.class);
                List<BrandCategoryVO> brandCategoryVOS = null;
                if (brandId != null && brandId.longValue() != -1L) {
                    BrandCategoryVO queryBrandCategory = new BrandCategoryVO();
                    queryBrandCategory.setBrandId(brandId);
                    requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryBrandCategory);
                    resultObjectVO = brandCategoryService.findByBrandId(requestJsonVO);
                    if (resultObjectVO.isSuccess()) {
                        brandCategoryVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), BrandCategoryVO.class);
                    }
                }

                AtomicLong id = new AtomicLong();
                for (CategoryTreeVO categoryTreeVO : categoryTreeVOList) {
                    // 保留数据库ID
                    categoryTreeVO.setNodeId(categoryTreeVO.getId());
                    // 将ID替换成自增
                    categoryTreeVO.setId(id.incrementAndGet());
                    categoryTreeVO.setText(categoryTreeVO.getTitle());

                    if (!CollectionUtils.isEmpty(brandCategoryVOS)) {
                        for (BrandCategory brandCategory : brandCategoryVOS) {
                            if (categoryTreeVO.getNodeId().longValue() == brandCategory.getCategoryId().longValue()) {
                                // 设置节点被选中
                                categoryTreeVO.getState().setChecked(true);
                            }
                        }
                    }
                    setTreeNodeSelect(id, categoryTreeVO, categoryTreeVO.getChildren(), brandCategoryVOS);
                }
                resultObjectVO.setData(categoryTreeVOList);
            }
            return resultObjectVO;
        } catch (Exception e) {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 递归设置树节点选中状态
     */
    private void setTreeNodeSelect(AtomicLong id, CategoryTreeVO parentTreeVO, List<CategoryTreeVO> categoryTreeVOList, List<BrandCategoryVO> brandCategories) {
        for (CategoryTreeVO categoryTreeVO : categoryTreeVOList) {
            // 保留数据库ID
            categoryTreeVO.setNodeId(categoryTreeVO.getId());
            // 将ID替换成自增
            categoryTreeVO.setId(id.incrementAndGet());
            categoryTreeVO.setParentId(parentTreeVO.getId());
            categoryTreeVO.setPid(parentTreeVO.getId());
            if (!CollectionUtils.isEmpty(brandCategories)) {
                for (BrandCategory brandCategory : brandCategories) {
                    if (categoryTreeVO.getNodeId().longValue() == brandCategory.getCategoryId().longValue()) {
                        // 设置节点被选中
                        categoryTreeVO.getState().setChecked(true);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(categoryTreeVO.getChildren())) {
                setTreeNodeSelect(id, categoryTreeVO, categoryTreeVO.getChildren(), brandCategories);
            }
        }
    }

}
