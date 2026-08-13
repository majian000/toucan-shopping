package com.toucan.shopping.cloud.apps.admin.controller.category;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.DictServiceAPI;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.constant.CategoryDictConstant;
import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.page.CategoryTreeInfo;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryDetailVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类别控制器
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private CategoryServiceAPI categoryServiceAPI;

    @Autowired
    private DictServiceAPI dictServiceAPI;

    @Autowired
    private AdminServiceAPI adminServiceAPI;


    /**
     * 获取类别字典列表
     */
    private List<DictVO> getCategoryDictList() throws NoSuchAlgorithmException {
        DictVO queryDict = new DictVO();
        queryDict.setCategoryCode(CategoryDictConstant.CATEGORY_DICT_CATEGORY_CODE);
        queryDict.setCodes(new LinkedList<>());
        queryDict.getCodes().add(CategoryDictConstant.CATEGORY_DICT_TYPE_CODE);
        queryDict.setAppCode(toucan.getAppCode());
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryDict);
        ResultTypeObjectVO<List<DictVO>> resultObjectVO = dictServiceAPI.queryDictByCodesAndCategoryCode(requestJsonVO);
        if (resultObjectVO.isSuccess() && !CollectionUtils.isEmpty(resultObjectVO.getData())) {
            return resultObjectVO.getData();
        }
        return null;
    }


    /**
     * 保存
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:category:save"})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultObjectVO save(HttpServletRequest request, @RequestBody CategoryVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setAppCode(toucan.getShoppingPC().getAppCode());
            entity.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = categoryServiceAPI.save(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:category:update"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultObjectVO update(HttpServletRequest request, @RequestBody CategoryVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = categoryServiceAPI.update(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查看详情
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:category:detail"})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultObjectVO detail(HttpServletRequest request, @RequestBody CategoryVO categoryVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (categoryVO.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, categoryVO);
            ResultObjectVO detailResult = categoryServiceAPI.findById(requestJsonVO);
            if (detailResult.isSuccess()) {
                List<CategoryVO> list = detailResult.formatDataList(CategoryVO.class);
                if (CollectionUtils.isEmpty(list)) {
                    resultObjectVO.setMsg("类别不存在");
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    return resultObjectVO;
                }
                CategoryVO vo = list.get(0);
                // 填充类型名称
                fillTypeNames(vo);
                // 填充创建人/修改人姓名
                fillAdminUsername(vo);
                CategoryDetailVO detailVO = new CategoryDetailVO();
                detailVO.setBasicInfo(vo);
                resultObjectVO.setData(detailVO);
            } else {
                resultObjectVO.setMsg(detailResult.getMsg());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
            }
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 填充类型名称
     */
    private void fillTypeNames(CategoryVO categoryVO) throws NoSuchAlgorithmException {
        if (StringUtils.isEmpty(categoryVO.getType())) {
            return;
        }
        List<DictVO> categoryDictList = this.getCategoryDictList();
        if (categoryDictList == null) {
            return;
        }
        List<DictVO> categoryTypeList = null;
        for (DictVO dictVO : categoryDictList) {
            if (CategoryDictConstant.CATEGORY_DICT_TYPE_CODE.equals(dictVO.getCode())) {
                categoryTypeList = dictVO.getChildren();
                break;
            }
        }
        if (categoryTypeList == null) {
            return;
        }
        Map<String, DictVO> categoryTypeMap = categoryTypeList.stream().collect(Collectors.toMap(DictVO::getCode, dict -> dict));
        String[] types = categoryVO.getType().split(",");
        StringBuilder typeNames = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            DictVO dictVO = categoryTypeMap.get(types[i]);
            if (dictVO != null) {
                typeNames.append(dictVO.getName());
            }
            if ((i + 1) < types.length) {
                typeNames.append(",");
            }
        }
        categoryVO.setTypeNames(typeNames.toString());
    }


    /**
     * 填充创建人/修改人姓名
     */
    private void fillAdminUsername(CategoryVO categoryVO) throws NoSuchAlgorithmException {
        List<String> adminIdList = new ArrayList<>();
        if (categoryVO.getCreateAdminId() != null) {
            adminIdList.add(categoryVO.getCreateAdminId());
        }
        if (categoryVO.getUpdateAdminId() != null) {
            adminIdList.add(categoryVO.getUpdateAdminId());
        }
        if (adminIdList.isEmpty()) {
            return;
        }
        String[] adminIds = adminIdList.toArray(new String[0]);
        AdminVO queryAdminVO = new AdminVO();
        queryAdminVO.setAdminIds(adminIds);
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAdminVO);
        ResultObjectVO resultObjectVO = adminServiceAPI.queryListByEntity(requestJsonVO);
        if (resultObjectVO.isSuccess()) {
            List<AdminVO> adminVOS = resultObjectVO.formatDataList(AdminVO.class);
            if (!CollectionUtils.isEmpty(adminVOS)) {
                for (AdminVO adminVO : adminVOS) {
                    if (categoryVO.getCreateAdminId() != null && categoryVO.getCreateAdminId().equals(adminVO.getAdminId())) {
                        categoryVO.setCreateAdminUsername(adminVO.getUsername());
                    }
                    if (categoryVO.getUpdateAdminId() != null && categoryVO.getUpdateAdminId().equals(adminVO.getAdminId())) {
                        categoryVO.setUpdateAdminUsername(adminVO.getUsername());
                    }
                }
            }
        }
    }


    /**
     * 删除
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:category:delete"})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody Category entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (entity.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());

            String entityJson = JSONObject.toJSONString(entity);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(toucan.getAppCode());
            requestVo.setEntityJson(entityJson);
            resultObjectVO = categoryServiceAPI.deleteById(requestVo);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:category:deletes"})
    @RequestMapping(value = "/delete/ids", method = RequestMethod.POST)
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<CategoryVO> categoryVOS) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (CollectionUtils.isEmpty(categoryVOS)) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(categoryVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(toucan.getAppCode());
            requestVo.setEntityJson(entityJson);
            resultObjectVO = categoryServiceAPI.deleteByIds(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 刷新全部缓存
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:category:flushCache"})
    @RequestMapping(value = "/flush/all/cache", method = RequestMethod.POST)
    public ResultObjectVO flushAllCache(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode, new CategoryVO());
            resultObjectVO = categoryServiceAPI.flushAllCache(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 清空PC首页缓存
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:category:clear:index:cache"})
    @RequestMapping(value = "/clear/index/cache", method = RequestMethod.POST)
    public ResultObjectVO clearIndexCache(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryVO categoryVO = new CategoryVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, categoryVO);
            resultObjectVO = categoryServiceAPI.clearWebIndexCache(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询树
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:category:query:tree"})
    @RequestMapping(value = "/query/tree", method = RequestMethod.POST)
    public ResultObjectVO queryTree(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryVO query = new CategoryVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
            return categoryServiceAPI.queryTree(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询树表格
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:category:tree"})
    @RequestMapping(value = "/tree/table", method = RequestMethod.POST)
    public ResultObjectVO treeTable(HttpServletRequest request, @RequestBody CategoryTreeInfo queryPageInfo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryPageInfo);
            resultObjectVO = categoryServiceAPI.queryTreeTable(requestJsonVO);
            return resultObjectVO;
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 按父ID查询树表格
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:dashboard:category:listByPid"})
    @RequestMapping(value = "/tree/table/by/pid", method = RequestMethod.POST)
    public ResultObjectVO queryTreeTableByPid(HttpServletRequest request, @RequestBody CategoryTreeInfo categoryTreeInfo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categoryTreeInfo);
            resultObjectVO = categoryServiceAPI.queryTreeTableByPid(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<CategoryTreeVO> categoryTreeVOS = resultObjectVO.formatDataList(CategoryTreeVO.class);
                if (!CollectionUtils.isEmpty(categoryTreeVOS)) {
                    List<DictVO> categoryTypeList = null;
                    List<DictVO> categoryDictList = this.getCategoryDictList();
                    if (categoryDictList != null) {
                        for (DictVO dictVO : categoryDictList) {
                            if (CategoryDictConstant.CATEGORY_DICT_TYPE_CODE.equals(dictVO.getCode())) {
                                categoryTypeList = dictVO.getChildren();
                                break;
                            }
                        }
                    }
                    Map<String, DictVO> categoryTypeMap = null;
                    if (categoryTypeList != null) {
                        categoryTypeMap = categoryTypeList.stream().collect(Collectors.toMap(DictVO::getCode, dict -> dict));
                    }
                    for (CategoryTreeVO categoryTreeVO : categoryTreeVOS) {
                        categoryTreeVO.setOpen(false);
                        // 设置类型名称
                        if (StringUtils.isNotEmpty(categoryTreeVO.getType())) {
                            if (categoryTypeMap != null) {
                                String[] types = categoryTreeVO.getType().split(",");
                                String typeNames = "";
                                for (int i = 0; i < types.length; i++) {
                                    String type = types[i];
                                    DictVO dictVO = categoryTypeMap.get(type);
                                    if (dictVO != null) {
                                        typeNames += dictVO.getName();
                                    }
                                    if ((i + 1) < types.length) {
                                        typeNames += ",";
                                    }
                                }
                                categoryTreeVO.setTypeNames(typeNames);
                            }
                        }
                    }
                }
                resultObjectVO.setData(categoryTreeVOS);
            }
            return resultObjectVO;
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 按父ID查询类别树
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:common:category"})
    @RequestMapping(value = "/query/category/tree/pid", method = RequestMethod.POST)
    public ResultObjectVO queryCategoryTreeByParentId(@RequestParam(defaultValue = "-1") Long id) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryVO query = new CategoryVO();
            query.setParentId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
            resultObjectVO = categoryServiceAPI.queryListByPid(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    List<CategoryTreeVO> categoryVOS = resultObjectVO.formatDataList(CategoryTreeVO.class);
                    for (CategoryTreeVO categoryTreeVO : categoryVOS) {
                        categoryTreeVO.setOpen(false);
                        categoryTreeVO.setIcon(null);
                    }
                    resultObjectVO.setData(categoryVOS);
                }
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
     * 查询树的子节点列表
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:category:tree"})
    @RequestMapping(value = "/query/tree/child", method = RequestMethod.POST)
    public ResultObjectVO queryTreeChildById(HttpServletRequest request, CategoryTreeVO categoryTreeVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryTreeVO queryVO = new CategoryTreeVO();
            queryVO.setParentId(categoryTreeVO.getId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryVO);
            resultObjectVO = categoryServiceAPI.queryTreeChildByPid(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<CategoryTreeVO> categoryTreeVOS = resultObjectVO.formatDataList(CategoryTreeVO.class);
                for (CategoryTreeVO ctVO : categoryTreeVOS) {
                    ctVO.setIcon(null);
                }
                resultObjectVO.setData(categoryTreeVOS);
            }
            return resultObjectVO;
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

}
