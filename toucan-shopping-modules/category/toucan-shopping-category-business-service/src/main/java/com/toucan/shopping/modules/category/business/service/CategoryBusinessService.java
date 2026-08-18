package com.toucan.shopping.modules.category.business.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.toucan.shopping.modules.category.cache.service.CategoryRedisService;
import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.page.CategoryTreeInfo;
import com.toucan.shopping.modules.category.service.CategoryService;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 类别业务服务
 */
@Service
public class CategoryBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // region Constants
    private static final String MSG_RETRY = "请重试!";
    private static final String MSG_RETRY_LATER = "请稍后重试";
    private static final String MSG_NO_ENTITY = "没有找到实体对象";
    private static final String MSG_QUERY_FAIL = "查询失败!";
    private static final String MSG_NO_ID = "没有找到ID";
    private static final String MSG_NO_ID_ARRAY = "没有找到ID数组";
    private static final String MSG_OBJECT_NOT_EXIST = "对象不存在!";
    private static final String MSG_CATEGORY_LIST_EMPTY = "分类列表为空";
    private static final long ROOT_PARENT_ID = -1L;
    private static final String ROOT_TITLE = "商城分类";
    private static final String SPLITER = "&toucan_spliter_2021&";
    // endregion

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private CategoryRedisService categoryRedisService;

    // region Category-to-treeVO transformation

    /**
     * 将Category转换为CategoryTreeVO并填充title/text字段
     */
    private CategoryTreeVO toCategoryTreeVO(Category category) {
        CategoryTreeVO treeVO = new CategoryTreeVO();
        try {
            BeanUtils.copyProperties(treeVO, category);
        } catch (Exception e) {
            logger.warn("BeanUtils.copyProperties failed", e);
        }
        treeVO.setTitle(category.getName());
        treeVO.setText(category.getName());
        return treeVO;
    }

    /**
     * 从分类列表中构建顶级节点树VO列表（parentId==-1 为顶级节点）
     */
    private List<CategoryVO> buildTopLevelTreeVOs(List<Category> categoryList) {
        List<CategoryVO> result = new ArrayList<>();
        for (Category category : categoryList) {
            if (category.getParentId().longValue() == ROOT_PARENT_ID) {
                CategoryTreeVO treeVO = toCategoryTreeVO(category);
                treeVO.setChildren(new ArrayList<>());
                try {
                    categoryService.setChildren(categoryList, treeVO);
                } catch (Exception e) {
                    logger.warn("setChildren failed", e);
                }
                result.add(treeVO);
            }
        }
        return result;
    }

    /**
     * 创建根节点
     */
    private CategoryTreeVO createRootTreeVO() {
        CategoryTreeVO root = new CategoryTreeVO();
        root.setTitle(ROOT_TITLE);
        root.setParentId(ROOT_PARENT_ID);
        root.setPid(ROOT_PARENT_ID);
        root.setId(ROOT_PARENT_ID);
        root.setText(ROOT_TITLE);
        return root;
    }

    /**
     * 收集分类列表中所有创建人和修改人ID
     */
    private List<String> collectAdminIds(List<? extends CategoryVO> categoryVOS) {
        Set<String> adminIdSet = new HashSet<>();
        for (CategoryVO vo : categoryVOS) {
            addAdminId(adminIdSet, vo.getCreateAdminId());
            addAdminId(adminIdSet, vo.getUpdateAdminId());
        }
        return new ArrayList<>(adminIdSet);
    }

    private void addAdminId(Set<String> adminIdSet, String adminId) {
        if (adminId != null && !"-1".equals(adminId)) {
            adminIdSet.add(adminId);
        }
    }

    /**
     * 将分类列表转换为CategoryVO列表并构建名称路径
     */
    private List<CategoryVO> buildCategoryVOsWithNamePath(List<Category> categoryList) {
        List<CategoryVO> categoryVOS = new ArrayList<>();
        List<Long> parentIds = new LinkedList<>();
        for (Category category : categoryList) {
            CategoryVO categoryVO = new CategoryVO();
            try {
                BeanUtils.copyProperties(categoryVO, category);
            } catch (Exception e) {
                logger.warn("BeanUtils.copyProperties failed", e);
            }
            categoryVO.setNamePath(categoryVO.getName());
            categoryVO.setParentIdPoint(categoryVO.getParentId());
            categoryVOS.add(categoryVO);
            if (categoryVO.getParentId() != null && categoryVO.getParentId().longValue() != ROOT_PARENT_ID) {
                parentIds.add(categoryVO.getParentId());
            }
        }
        categoryService.setNamePath(categoryVOS, parentIds);
        return categoryVOS;
    }
    // endregion

    // region CRUD methods

    /**
     * 保存类别
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(String signHeader, RequestJsonVO requestJsonVO) {
        try {
            Category category = JSONObject.parseObject(requestJsonVO.getEntityJson(), Category.class);

            Check.notEmpty(category.getName(), ResultVO.FAILD, "类别名称不能为空!");

            CategoryVO queryCategory = new CategoryVO();
            queryCategory.setName(category.getName());
            queryCategory.setDeleteStatus((short) 0);

            if (!CollectionUtils.isEmpty(categoryService.queryList(queryCategory))) {
                return ResultObjectVO.fail(ResultVO.FAILD, "已存在该类别!");
            }

            category.setId(idGenerator.id());
            category.setCreateDate(new Date());
            int row = categoryService.save(category);
            if (row != 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY);
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY);
        }
        return ResultObjectVO.success();
    }

    /**
     * 更新类别
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        try {
            Category category = JSONObject.parseObject(requestJsonVO.getEntityJson(), Category.class);

            if (category.getId().longValue() == category.getParentId().longValue()) {
                logger.info("上级节点不能为自己 param:{}", JSONObject.toJSONString(category));
                return ResultObjectVO.fail(ResultVO.FAILD, "上级节点不能为自己!");
            }

            Check.notEmpty(category.getName(), ResultVO.FAILD, "类别名称不能为空!");
            Check.notNull(category.getId(), ResultVO.FAILD, "类别ID不能为空!");

            CategoryVO queryCategory = new CategoryVO();
            queryCategory.setName(category.getName());
            queryCategory.setDeleteStatus((short) 0);

            List<Category> categoryList = categoryService.queryList(queryCategory);
            if (!CollectionUtils.isEmpty(categoryList)) {
                if (category.getId().longValue() != categoryList.get(0).getId().longValue()) {
                    return ResultObjectVO.fail(ResultVO.FAILD, "该类别名称已存在!");
                }
            }

            category.setUpdateDate(new Date());
            int row = categoryService.update(category);
            if (row != 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY);
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY);
        }
        return ResultObjectVO.success();
    }

    /**
     * 根据ID删除类别
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestJsonVO) {
        try {
            Category category = JSONObject.parseObject(requestJsonVO.getEntityJson(), Category.class);

            Check.notNull(category.getId(), ResultVO.FAILD, "类别ID不能为空!");

            CategoryVO queryCategory = new CategoryVO();
            queryCategory.setId(category.getId());
            queryCategory.setDeleteStatus((short) 0);

            if (CollectionUtils.isEmpty(categoryService.queryList(queryCategory))) {
                return ResultObjectVO.fail(ResultVO.FAILD, "不存在该类别!");
            }

            categoryService.deleteChildrenByParentId(category.getId());
            int row = categoryService.deleteById(category.getId());
            if (row <= 0) {
                return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY);
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY);
        }
        return ResultObjectVO.success();
    }

    /**
     * 批量删除分类
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        try {
            List<Category> categorys = JSON.parseArray(requestVo.getEntityJson(), Category.class);
            if (CollectionUtils.isEmpty(categorys)) {
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找ID");
            }

            List<ResultObjectVO> resultObjectVOList = new ArrayList<>();
            for (Category category : categorys) {
                if (category.getId() == null) {
                    continue;
                }
                List<Category> children = new ArrayList<>();
                categoryService.queryChildren(children, category);
                children.add(category);

                for (Category c : children) {
                    int row = categoryService.deleteById(c.getId());
                    if (row < 1) {
                        logger.warn("删除类别失败 {} ", JSONObject.toJSONString(c));
                        ResultObjectVO resultObjectRowVO = ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY);
                        resultObjectRowVO.setData(c.getId());
                        resultObjectVOList.add(resultObjectRowVO);
                    }
                }
            }
            return ResultObjectVO.success(resultObjectVOList);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }
    // endregion

    // region Query methods

    /**
     * 根据ID查询
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryById(RequestJsonVO requestJsonVO) {
        try {
            Category queryCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), Category.class);
            Category category = categoryService.queryById(queryCategory.getId());
            CategoryTreeVO categoryTreeVO = null;
            if (category != null) {
                categoryTreeVO = new CategoryTreeVO();
                BeanUtils.copyProperties(categoryTreeVO, category);
                categoryTreeVO.setIdPath(new LinkedList<>());
                categoryTreeVO.getIdPath().add(categoryTreeVO.getId());
                categoryService.setPath(categoryTreeVO, categoryTreeVO.getParentId());
            }
            return ResultObjectVO.success(categoryTreeVO);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_QUERY_FAIL);
        }
    }

    /**
     * 根据ID查询
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        try {
            CategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(), CategoryVO.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, MSG_NO_ID);
            CategoryVO query = new CategoryVO();
            query.setId(entity.getId());
            List<Category> categorys = categoryService.queryList(query);
            if (CollectionUtils.isEmpty(categorys)) {
                return ResultObjectVO.fail(ResultVO.FAILD, MSG_OBJECT_NOT_EXIST);
            }
            return ResultObjectVO.success(categorys);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }

    /**
     * 根据ID查询返回分类ID路径
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findIdPathById(RequestJsonVO requestVo) {
        try {
            CategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(), CategoryVO.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, MSG_NO_ID);
            CategoryVO query = new CategoryVO();
            query.setId(entity.getId());
            List<Category> categorys = categoryService.queryList(query);
            if (CollectionUtils.isEmpty(categorys)) {
                return ResultObjectVO.fail(ResultVO.FAILD, MSG_OBJECT_NOT_EXIST);
            }
            Category category = categorys.get(0);
            CategoryTreeVO categoryTreeVO = new CategoryTreeVO();
            BeanUtils.copyProperties(categoryTreeVO, category);
            categoryTreeVO.setIdPath(new ArrayList<Long>());
            categoryTreeVO.setNamePaths(new ArrayList<>());
            categoryTreeVO.getIdPath().add(category.getId());
            categoryTreeVO.getNamePaths().add(category.getName());
            categoryService.setIdPath(categoryTreeVO);
            return ResultObjectVO.success(categoryTreeVO);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }

    /**
     * 根据ID数组查询
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryByIdList(RequestJsonVO requestJsonVO) {
        try {
            List<Category> categorys = JSONArray.parseArray(requestJsonVO.getEntityJson(), Category.class);
            if (!CollectionUtils.isEmpty(categorys)) {
                List<Long> categoryIdList = new ArrayList<>();
                for (Category category : categorys) {
                    categoryIdList.add(category.getId());
                }
                List<Category> categoryList = categoryService.queryListByIdList(categoryIdList);
                List<CategoryVO> categoryVOS = new ArrayList<>();
                if (!CollectionUtils.isEmpty(categoryList)) {
                    categoryVOS = buildCategoryVOsWithNamePath(categoryList);
                }
                return ResultObjectVO.success(categoryVOS);
            }
            return ResultObjectVO.success();
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_QUERY_FAIL);
        }
    }

    /**
     * 根据ID数组查询
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findByIdArray(RequestJsonVO requestVo) {
        try {
            CategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(), CategoryVO.class);
            Check.isTrue(entity.getIdArray() != null && entity.getIdArray().length > 0, ResultVO.FAILD, MSG_NO_ID_ARRAY);
            CategoryVO query = new CategoryVO();
            query.setIdArray(entity.getIdArray());
            List<Category> categorys = categoryService.queryList(query);
            if (CollectionUtils.isEmpty(categorys)) {
                return ResultObjectVO.fail(ResultVO.FAILD, MSG_CATEGORY_LIST_EMPTY);
            }
            List<CategoryVO> categoryVOS = buildCategoryVOsWithNamePath(categorys);
            return ResultObjectVO.success(categoryVOS);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }

    /**
     * 查询全部
     */
    @RequestCheck
    public ResultObjectVO queryAllList(RequestJsonVO requestJsonVO) {
        try {
            CategoryVO query = new CategoryVO();
            return ResultObjectVO.success(categoryService.queryList(query));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_QUERY_FAIL);
        }
    }
    // endregion

    // region Tree query methods

    /**
     * 查询树
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryTree(RequestJsonVO requestJsonVO) {
        try {
            CategoryVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryVO.class);

            List<CategoryTreeVO> cachedTree = categoryRedisService.queryFullCategoryTree();
            if (!CollectionUtils.isEmpty(cachedTree)) {
                return ResultObjectVO.success(cachedTree);
            }

            List<Category> categoryList = categoryService.queryList(query);
            CategoryTreeVO rootTreeVO = createRootTreeVO();

            List<CategoryTreeVO> categoryTreeVOS = new ArrayList<>();
            if (!CollectionUtils.isEmpty(categoryList)) {
                for (Category category : categoryList) {
                    if (category.getParentId().longValue() == ROOT_PARENT_ID) {
                        CategoryTreeVO treeVO = toCategoryTreeVO(category);
                        treeVO.setPid(category.getParentId());
                        treeVO.setPath(category.getName());
                        treeVO.setChildren(new ArrayList<>());
                        categoryService.setChildren(categoryList, treeVO);
                        categoryTreeVOS.add(treeVO);
                    }
                }
                rootTreeVO.setChildren(categoryTreeVOS);
            }

            categoryRedisService.addFullCategoryCache(categoryTreeVOS);
            return ResultObjectVO.success(categoryTreeVOS);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }

    /**
     * 查询迷你树
     */
    @RequestCheck
    public ResultObjectVO queryMiniTree(RequestJsonVO requestJsonVO) {
        try {
            List<CategoryTreeVO> categoryMiniTree = categoryRedisService.queryCategoryMiniTree();
            if (CollectionUtils.isEmpty(categoryMiniTree)) {
                flushWMiniTreeCacheInternal();
                categoryMiniTree = categoryRedisService.queryCategoryMiniTree();
                if (CollectionUtils.isEmpty(categoryMiniTree)) {
                    return ResultObjectVO.success();
                }
            }

            for (CategoryTreeVO categoryTreeVO : categoryMiniTree) {
                categoryTreeVO.setPid(ROOT_PARENT_ID);
                categoryTreeVO.setParentId(ROOT_PARENT_ID);
                categoryTreeVO.setTitle(categoryTreeVO.getName());
                categoryTreeVO.setText(categoryTreeVO.getName());
                categoryTreeVO.setPath(categoryTreeVO.getName());
                if (!CollectionUtils.isEmpty(categoryTreeVO.getChildren())) {
                    categoryService.complementChildren(categoryTreeVO);
                }
            }

            return ResultObjectVO.success(categoryMiniTree);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }

    /**
     * 查询PC端首页类别树
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryWebIndexTree(RequestJsonVO requestJsonVO) {
        try {
            CategoryVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryVO.class);
            List<Category> categoryList = categoryService.queryPcIndexList(query);
            List<CategoryVO> categoryTreeVOS = new ArrayList<>();
            if (!CollectionUtils.isEmpty(categoryList)) {
                categoryTreeVOS = buildTopLevelTreeVOs(categoryList);
            }
            return ResultObjectVO.success(categoryTreeVOS);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }

    /**
     * 查询树表格
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryTreeTable(RequestJsonVO requestJsonVO) {
        try {
            CategoryTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryTreeInfo.class);
            List<CategoryVO> categoryVOS = categoryService.findTreeTable(queryPageInfo);

            if (!CollectionUtils.isEmpty(categoryVOS)) {
                collectAdminIds(categoryVOS);
                setTopLevelParentId(categoryVOS);
            }

            return ResultObjectVO.success(categoryVOS);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }

    /**
     * 查询树表格byPid
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
        try {
            CategoryTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryTreeInfo.class);
            List<CategoryTreeVO> categoryTreeVOS = new ArrayList<>();

            if (StringUtils.isNotEmpty(queryPageInfo.getName())) {
                CategoryVO queryCategory = new CategoryVO();
                queryCategory.setName(queryPageInfo.getName());
                List<Category> categories = categoryService.queryList(queryCategory);
                for (Category category : categories) {
                    CategoryTreeVO categoryTreeVO = new CategoryTreeVO();
                    BeanUtils.copyProperties(categoryTreeVO, category);
                    categoryTreeVOS.add(categoryTreeVO);
                }
            } else {
                CategoryVO queryCategory = new CategoryVO();
                queryCategory.setParentId(queryPageInfo.getParentId() != null
                        ? queryPageInfo.getParentId() : ROOT_PARENT_ID);
                List<Category> categories = categoryService.queryList(queryCategory);
                for (Category category : categories) {
                    CategoryTreeVO categoryTreeVO = new CategoryTreeVO();
                    BeanUtils.copyProperties(categoryTreeVO, category);

                    CategoryVO childQuery = new CategoryVO();
                    childQuery.setParentId(category.getId());
                    categoryTreeVO.setHaveChild(categoryService.queryCount(childQuery) > 0);
                    categoryTreeVOS.add(categoryTreeVO);
                }
            }

            collectAdminIds(categoryTreeVOS);

            if (StringUtils.isNotEmpty(queryPageInfo.getName())) {
                for (Category category : categoryTreeVOS) {
                    category.setParentId(ROOT_PARENT_ID);
                }
            }

            return ResultObjectVO.success(categoryTreeVOS);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }

    /**
     * 查询指定节点下所有子节点
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListByPid(RequestJsonVO requestJsonVO) {
        try {
            CategoryVO queryCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryVO.class);
            List<Category> categories = categoryService.queryList(queryCategory);
            List<CategoryTreeVO> categoryTreeVOS = new LinkedList<>();
            if (!CollectionUtils.isEmpty(categories)) {
                // 收集所有父节点ID,一次性批量查询子节点数量,避免循环查询(N+1)
                List<Long> parentIds = new ArrayList<>(categories.size());
                for (Category category : categories) {
                    if (category.getId() != null) {
                        parentIds.add(category.getId());
                    }
                }
                Map<Long, Long> childCountMap = categoryService.queryChildCountByParentIds(parentIds);
                for (Category category : categories) {
                    CategoryTreeVO categoryTreeVO = new CategoryTreeVO();
                    BeanUtils.copyProperties(categoryTreeVO, category);
                    Long categoryChildCount = childCountMap.get(categoryTreeVO.getId());
                    categoryTreeVO.setIsParent(categoryChildCount != null && categoryChildCount > 0);
                    categoryTreeVOS.add(categoryTreeVO);
                }
            }
            return ResultObjectVO.success(categoryTreeVOS);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }

    /**
     * 查询指定节点下所有子节点（递归）
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryChildListByPid(RequestJsonVO requestJsonVO) {
        try {
            CategoryVO queryCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryVO.class);
            List<Category> childList = new ArrayList<>();
            categoryService.queryChildren(childList, queryCategory);
            return ResultObjectVO.success(childList);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }

    /**
     * 查询指定节点下一级子节点
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryNextOneLevelChildListByPid(RequestJsonVO requestJsonVO) {
        try {
            CategoryVO queryCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryVO.class);
            return ResultObjectVO.success(categoryService.queryList(queryCategory));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }

    /**
     * 查询类别树
     */
    @RequestCheck
    public ResultObjectVO queryCategoryTree(RequestJsonVO requestJsonVO) {
        try {
            return ResultObjectVO.success(categoryRedisService.queryMiniTree());
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }

    /**
     * 查询指定节点下子节点
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryTreeChildByPid(RequestJsonVO requestJsonVO) {
        try {
            CategoryVO categoryVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryVO.class);
            List<CategoryTreeVO> categoryTreeVOS = new ArrayList<>();

            if (categoryVO.getParentId() == null) {
                CategoryTreeVO rootNode = new CategoryTreeVO();
                rootNode.setId(ROOT_PARENT_ID);
                rootNode.setName("根节点");
                rootNode.setParentId(ROOT_PARENT_ID);
                rootNode.setIsParent(categoryService.queryOneChildCountByPid(ROOT_PARENT_ID) > 0);
                categoryTreeVOS.add(rootNode);
            } else {
                List<Category> categoryList = categoryService.queryList(categoryVO);
                for (Category category : categoryList) {
                    CategoryTreeVO categoryTreeVO = new CategoryTreeVO();
                    BeanUtils.copyProperties(categoryTreeVO, category);
                    categoryTreeVO.setTitle(category.getName());
                    Long childCount = categoryService.queryOneChildCountByPid(categoryTreeVO.getId());
                    categoryTreeVO.setIsParent(childCount > 0);
                    categoryTreeVOS.add(categoryTreeVO);
                }
            }

            return ResultObjectVO.success(categoryTreeVOS);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }
    // endregion

    // region Cache management

    /**
     * 刷新全部缓存
     */
    @RequestCheck
    public ResultObjectVO flushAllCache(RequestJsonVO requestVo) {
        try {
            this.flushWebIndexCache(requestVo);
            this.flushWMiniTreeCache(requestVo);
            this.flushNavigationMiniTreeCache(requestVo);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
        return ResultObjectVO.success();
    }

    /**
     * 刷新首页缓存
     */
    @RequestCheck
    public ResultObjectVO flushWebIndexCache(RequestJsonVO requestVo) {
        try {
            CategoryVO query = new CategoryVO();
            List<Category> categoryList = categoryService.queryPcIndexList(query);
            if (!CollectionUtils.isEmpty(categoryList)) {
                List<CategoryVO> categoryTreeVOS = new ArrayList<>();
                for (Category category : categoryList) {
                    if (category.getParentId().longValue() == ROOT_PARENT_ID) {
                        CategoryTreeVO treeVO = toCategoryTreeVO(category);
                        buildRootLinks(treeVO, category.getId());
                        treeVO.setChildren(new ArrayList<>());
                        categoryService.setChildren(categoryList, treeVO);
                        categoryTreeVOS.add(treeVO);
                    }
                }
                categoryRedisService.flushWebIndexCaches(categoryTreeVOS);
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
        return ResultObjectVO.success();
    }

    /**
     * 构建根节点链接
     */
    private void buildRootLinks(CategoryTreeVO treeVO, Long categoryId) {
        if (StringUtils.isEmpty(treeVO.getName())) {
            return;
        }
        StringBuilder linkhtml = new StringBuilder();
        String name = treeVO.getName();
        if (name.indexOf('/') != -1) {
            String[] names = name.split("/");
            String[] hrefs = new String[0];
            if (StringUtils.isNotEmpty(treeVO.getHref())) {
                hrefs = treeVO.getHref().split(SPLITER);
            }
            for (int i = 0; i < names.length; i++) {
                if (names.length == hrefs.length) {
                    linkhtml.append("<a class=\"category_a\" href=\"").append(hrefs[i])
                            .append("\" attr-id=\"").append(categoryId).append("\">");
                } else {
                    linkhtml.append("<a class=\"category_a\" href=\"#\" attr-id=\"").append(categoryId).append("\">");
                }
                linkhtml.append(names[i]).append("</a>");
                if (i + 1 < names.length) {
                    linkhtml.append("<a class=\"category_a\" >/</a>");
                }
            }
        } else {
            linkhtml.append("<a class=\"category_a\" href=\"").append(treeVO.getHref())
                    .append("\">").append(name).append("</a>");
        }
        treeVO.setRootLinks(linkhtml.toString());
    }

    /**
     * 刷新预览树缓存
     */
    @RequestCheck
    public ResultObjectVO flushWMiniTreeCache(RequestJsonVO requestVo) {
        try {
            flushWMiniTreeCacheInternal();
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
        return ResultObjectVO.success();
    }

    /**
     * 导航分类树
     */
    @RequestCheck
    public ResultObjectVO flushNavigationMiniTreeCache(RequestJsonVO requestVo) {
        try {
            CategoryVO query = new CategoryVO();
            List<Category> categoryList = categoryService.queryPcIndexList(query);
            if (!CollectionUtils.isEmpty(categoryList)) {
                List<CategoryVO> categoryTreeVOS = buildTopLevelTreeVOs(categoryList);
                categoryRedisService.flushNavigationMiniTree(categoryTreeVOS);
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
        return ResultObjectVO.success();
    }

    /**
     * 清空首页缓存
     */
    @RequestCheck
    public ResultObjectVO clearWebIndexCache(RequestJsonVO requestVo) {
        try {
            categoryRedisService.clearCaches();
            return ResultObjectVO.success(true);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }

    /**
     * 内部刷新区预览树缓存
     */
    private void flushWMiniTreeCacheInternal() {
        CategoryVO query = new CategoryVO();
        List<Category> categoryList = categoryService.queryPcIndexList(query);
        if (!CollectionUtils.isEmpty(categoryList)) {
            List<CategoryVO> categoryTreeVOS = buildTopLevelTreeVOs(categoryList);
            categoryRedisService.flushWMiniTree(categoryTreeVOS);
        }
    }
    // endregion

    // region Utility

    /**
     * 如果当前分类列表中某个节点的父节点不在列表中，则将该节点设置为顶级节点
     */
    private void setTopLevelParentId(List<CategoryVO> categoryVOS) {
        for (CategoryVO c : categoryVOS) {
            boolean isFind = false;
            for (CategoryVO cv : categoryVOS) {
                if (c.getParentId().longValue() == cv.getId().longValue()) {
                    isFind = true;
                    break;
                }
            }
            if (!isFind) {
                c.setParentId(ROOT_PARENT_ID);
            }
        }
    }
    // endregion
}
