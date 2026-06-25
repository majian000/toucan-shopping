package com.toucan.shopping.modules.category.business.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.category.cache.service.CategoryRedisService;
import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.page.CategoryTreeInfo;
import com.toucan.shopping.modules.category.service.CategoryService;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
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
import java.util.LinkedList;
import java.util.List;


/**
 * 类别业务服务
 */
@Service
public class CategoryBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private CategoryRedisService categoryRedisService;


    /**
     * 保存类别
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO save(String signHeader, RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }

        try {
            Category category = JSONObject.parseObject(requestJsonVO.getEntityJson(), Category.class);



            if(StringUtils.isEmpty(category.getName()))
            {
                logger.info("类别名称为空 param:"+ JSONObject.toJSONString(category));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("类别名称不能为空!");
                return resultObjectVO;
            }

            CategoryVO queryCategory = new CategoryVO();
            queryCategory.setName(category.getName());
            queryCategory.setDeleteStatus((short)0);

            if(!CollectionUtils.isEmpty(categoryService.queryList(queryCategory)))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("已存在该类别!");
                return resultObjectVO;
            }

            category.setId(idGenerator.id());
            category.setCreateDate(new Date());
            int row = categoryService.save(category);
            if (row != 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }
            categoryRedisService.clearCaches();
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 更新类别
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO update(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }

        try {
            Category category = JSONObject.parseObject(requestJsonVO.getEntityJson(), Category.class);


            if(category.getId().longValue()==category.getParentId().longValue())
            {
                logger.info("上级节点不能为自己 param:"+ JSONObject.toJSONString(category));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("上级节点不能为自己!");
                return resultObjectVO;
            }

            if(StringUtils.isEmpty(category.getName()))
            {
                logger.info("类别名称为空 param:"+ JSONObject.toJSONString(category));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("类别名称不能为空!");
                return resultObjectVO;
            }


            if(category.getId()==null)
            {
                logger.info("类别ID为空 param:"+ JSONObject.toJSONString(category));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("类别ID不能为空!");
                return resultObjectVO;
            }

            CategoryVO queryCategory = new CategoryVO();
            queryCategory.setName(category.getName());
            queryCategory.setDeleteStatus((short)0);

            List<Category> categoryList = categoryService.queryList(queryCategory);
            if(!CollectionUtils.isEmpty(categoryList))
            {
                if(category.getId().longValue() != categoryList.get(0).getId().longValue())
                {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("该类别名称已存在!");
                    return resultObjectVO;
                }
            }

            category.setUpdateDate(new Date());
            int row = categoryService.update(category);
            if (row != 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }
            categoryRedisService.clearCaches();
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 根据ID删除类别
     * @param signHeader
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO deleteById(String signHeader, RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }

        try {
            Category category = JSONObject.parseObject(requestJsonVO.getEntityJson(), Category.class);



            if(category.getId()==null)
            {
                logger.info("类别ID为空 param:"+ JSONObject.toJSONString(category));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("类别ID不能为空!");
                return resultObjectVO;
            }

            CategoryVO queryCategory = new CategoryVO();
            queryCategory.setId(category.getId());
            queryCategory.setDeleteStatus((short)0);

            if(CollectionUtils.isEmpty(categoryService.queryList(queryCategory)))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在该类别!");
                return resultObjectVO;
            }

            categoryService.deleteChildrenByParentId(category.getId());
            int row = categoryService.deleteById(category.getId());
            if (row <=0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            categoryRedisService.clearCaches();

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryById(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        try {
            Category queryCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), Category.class);
            Category category = categoryService.queryById(queryCategory.getId());
            CategoryTreeVO categoryTreeVO = null;
            if(category!=null)
            {
                categoryTreeVO = new CategoryTreeVO();
                BeanUtils.copyProperties(categoryTreeVO,category);
                categoryTreeVO.setIdPath(new LinkedList<>());
                categoryTreeVO.getIdPath().add(categoryTreeVO.getId());
                categoryService.setPath(categoryTreeVO,categoryTreeVO.getParentId());
            }
            resultObjectVO.setData(categoryTreeVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


    /**
     * 根据ID数组查询
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryByIdList(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        try {
            List<Category> categorys = JSONArray.parseArray(requestJsonVO.getEntityJson(),Category.class);
            if(!CollectionUtils.isEmpty(categorys)) {
                List<Long> categoryIdList = new ArrayList<Long>();
                for(Category category:categorys) {
                    categoryIdList.add(category.getId());
                }
                List<Category> categoryList = categoryService.queryListByIdList(categoryIdList);
                List<CategoryVO> categoryVOS = new ArrayList<CategoryVO>();
                if(!CollectionUtils.isEmpty(categoryList))
                {
                    List<Long> parentIds = new LinkedList<Long>();
                    for(Category category:categoryList)
                    {
                        CategoryVO categoryVO = new CategoryVO();
                        BeanUtils.copyProperties(categoryVO,category);
                        categoryVO.setNamePath(categoryVO.getName());
                        categoryVO.setParentIdPoint(categoryVO.getParentId());
                        categoryVOS.add(categoryVO);
                        if(categoryVO.getParentId()!=null&&categoryVO.getParentId().longValue()!=-1L) {
                            parentIds.add(categoryVO.getParentId());
                        }
                    }
                    categoryService.setNamePath(categoryVOS,parentIds);
                }
                resultObjectVO.setData(categoryVOS);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


    /**
     * 刷新全部缓存
     * @param requestVo
     * @return
     */
    public ResultObjectVO flushAllCache(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }
        try {
            this.flushWebIndexCache(requestVo);
            this.flushWMiniTreeCache(requestVo);
            this.flushNavigationMiniTreeCache(requestVo);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 刷新首页缓存
     * @param requestVo
     * @return
     */
    public ResultObjectVO flushWebIndexCache(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }
        try {
            CategoryVO query = new CategoryVO();
            List<Category> categoryList = categoryService.queryPcIndexList(query);
            if(!CollectionUtils.isEmpty(categoryList)) {
                List<CategoryVO> categoryTreeVOS = new ArrayList<CategoryVO>();
                for(Category category : categoryList)
                {
                    if(category.getParentId().longValue()==-1) {
                        CategoryTreeVO treeVO = new CategoryTreeVO();
                        BeanUtils.copyProperties(treeVO, category);
                        treeVO.setTitle(category.getName());
                        treeVO.setText(category.getName());
                        if(StringUtils.isNotEmpty(treeVO.getName()))
                        {
                            StringBuilder linkhtml = new StringBuilder();
                            if(treeVO.getName().indexOf("/")!=-1)
                            {
                                String[] names = treeVO.getName().split("/");
                                String[] hrefs = new String[0];
                                if(StringUtils.isNotEmpty(treeVO.getHref())) {
                                    hrefs = treeVO.getHref().split("&toucan_spliter_2021&");
                                }
                                if(names.length==hrefs.length) {
                                    for (int i = 0; i < names.length; i++) {
                                        linkhtml.append("<a class=\"category_a\" href=\""+hrefs[i]+"\" attr-id=\""+category.getId()+"\">");
                                        linkhtml.append(names[i]);
                                        linkhtml.append("</a>");
                                        if(i+1<names.length)
                                        {
                                            linkhtml.append("<a class=\"category_a\" >/</a>");
                                        }
                                    }
                                }else{
                                    for (int i = 0; i < names.length; i++) {
                                        linkhtml.append("<a class=\"category_a\" href=\"#\" attr-id=\""+category.getId()+"\">");
                                        linkhtml.append(names[i]);
                                        linkhtml.append("</a>");
                                        if(i+1<names.length)
                                        {
                                            linkhtml.append("<a class=\"category_a\" >/</a>");
                                        }
                                    }
                                }
                            }else{
                                linkhtml.append("<a class=\"category_a\" href=\""+treeVO.getHref()+"\">"+treeVO.getName()+"</a>");
                            }
                            treeVO.setRootLinks(linkhtml.toString());
                        }
                        categoryTreeVOS.add(treeVO);

                        treeVO.setChildren(new ArrayList<CategoryTreeVO>());
                        categoryService.setChildren(categoryList,treeVO);
                    }
                }
                categoryRedisService.flushWebIndexCaches(categoryTreeVOS);
            }
        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 刷新预览树缓存
     * @param requestVo
     * @return
     */
    public ResultObjectVO flushWMiniTreeCache(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }
        try {
            CategoryVO query = new CategoryVO();
            List<Category> categoryList = categoryService.queryPcIndexList(query);
            if(!CollectionUtils.isEmpty(categoryList)) {
                List<CategoryVO> categoryTreeVOS = new ArrayList<CategoryVO>();
                for(Category category : categoryList)
                {
                    if(category.getParentId().longValue()==-1) {
                        CategoryTreeVO treeVO = new CategoryTreeVO();
                        BeanUtils.copyProperties(treeVO, category);
                        treeVO.setTitle(category.getName());
                        treeVO.setText(category.getName());
                        categoryTreeVOS.add(treeVO);

                        treeVO.setChildren(new ArrayList<CategoryTreeVO>());
                        categoryService.setChildren(categoryList,treeVO);
                    }
                }
                categoryRedisService.flushWMiniTree(categoryTreeVOS);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 导航分类树
     * @param requestVo
     * @return
     */
    public ResultObjectVO flushNavigationMiniTreeCache(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }
        try {
            CategoryVO query = new CategoryVO();
            List<Category> categoryList = categoryService.queryPcIndexList(query);
            if(!CollectionUtils.isEmpty(categoryList)) {
                List<CategoryVO> categoryTreeVOS = new ArrayList<CategoryVO>();
                for(Category category : categoryList)
                {
                    if(category.getParentId().longValue()==-1) {
                        CategoryTreeVO treeVO = new CategoryTreeVO();
                        BeanUtils.copyProperties(treeVO, category);
                        treeVO.setTitle(category.getName());
                        treeVO.setText(category.getName());
                        categoryTreeVOS.add(treeVO);

                        treeVO.setChildren(new ArrayList<CategoryTreeVO>());
                        categoryService.setChildren(categoryList,treeVO);
                    }
                }
                categoryRedisService.flushNavigationMiniTree(categoryTreeVOS);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 清空首页缓存
     * @param requestVo
     * @return
     */
    public ResultObjectVO clearWebIndexCache(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            categoryRedisService.clearCaches();
            resultObjectVO.setData(true);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    public ResultObjectVO findById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            CategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),CategoryVO.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该分类
            CategoryVO query=new CategoryVO();
            query.setId(entity.getId());
            List<Category> categorys = categoryService.queryList(query);
            if(CollectionUtils.isEmpty(categorys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("对象不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(categorys);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 根据ID查询返回分类ID路径
     * @param requestVo
     * @return
     */
    public ResultObjectVO findIdPathById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            CategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),CategoryVO.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该分类
            CategoryVO query=new CategoryVO();
            query.setId(entity.getId());
            List<Category> categorys = categoryService.queryList(query);
            if(CollectionUtils.isEmpty(categorys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("对象不存在!");
                return resultObjectVO;
            }
            Category category = categorys.get(0);
            CategoryTreeVO categoryTreeVO = new CategoryTreeVO();
            BeanUtils.copyProperties(categoryTreeVO,category);
            categoryTreeVO.setIdPath(new ArrayList<Long>());
            categoryTreeVO.setNamePaths(new ArrayList());
            categoryTreeVO.getIdPath().add(category.getId());
            categoryTreeVO.getNamePaths().add(category.getName());
            categoryService.setIdPath(categoryTreeVO);

            resultObjectVO.setData(categoryTreeVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据ID数组查询
     * @param requestVo
     * @return
     */
    public ResultObjectVO findByIdArray(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            CategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),CategoryVO.class);
            if(entity.getIdArray()==null||entity.getIdArray().length<=0)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID数组");
                return resultObjectVO;
            }

            //查询是否存在该分类
            CategoryVO query=new CategoryVO();
            query.setIdArray(entity.getIdArray());
            List<Category> categorys = categoryService.queryList(query);
            if(CollectionUtils.isEmpty(categorys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类列表为空");
                return resultObjectVO;
            }

            List<CategoryVO> categoryVOS = new ArrayList<CategoryVO>();
            if(!CollectionUtils.isEmpty(categorys))
            {
                List<Long> parentIds = new LinkedList<Long>();
                for(Category category:categorys)
                {
                    CategoryVO categoryVO = new CategoryVO();
                    BeanUtils.copyProperties(categoryVO,category);
                    categoryVO.setNamePath(categoryVO.getName());
                    categoryVO.setParentIdPoint(categoryVO.getParentId());
                    categoryVOS.add(categoryVO);
                    if(categoryVO.getParentId()!=null&&categoryVO.getParentId().longValue()!=-1L) {
                        parentIds.add(categoryVO.getParentId());
                    }
                }
                categoryService.setNamePath(categoryVOS,parentIds);
            }
            resultObjectVO.setData(categoryVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询树
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryTree(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryVO.class);

            List<CategoryTreeVO> categoryVOS = categoryRedisService.queryFullCategoryTree();
            if(!CollectionUtils.isEmpty(categoryVOS))
            {
                resultObjectVO.setData(categoryVOS);
                return resultObjectVO;
            }

            List<Category> categoryList = categoryService.queryList(query);

            CategoryTreeVO rootTreeVO = new CategoryTreeVO();
            rootTreeVO.setTitle("商城分类");
            rootTreeVO.setParentId(-1L);
            rootTreeVO.setPid(-1L);
            rootTreeVO.setId(-1L);
            rootTreeVO.setText("商城分类");
            if(!CollectionUtils.isEmpty(categoryList))
            {
                List<CategoryTreeVO> categoryTreeVOS = new ArrayList<CategoryTreeVO>();
                for(Category category : categoryList)
                {
                    if(category.getParentId().longValue()==-1) {
                        CategoryTreeVO treeVO = new CategoryTreeVO();
                        BeanUtils.copyProperties(treeVO, category);

                        treeVO.setTitle(category.getName());
                        treeVO.setText(category.getName());
                        treeVO.setPid(category.getParentId());
                        treeVO.setPath(category.getName());

                        categoryTreeVOS.add(treeVO);

                        treeVO.setChildren(new ArrayList<CategoryTreeVO>());
                        categoryService.setChildren(categoryList,treeVO);
                    }
                }
                rootTreeVO.setChildren(categoryTreeVOS);
            }

            List<CategoryTreeVO> rootCategoryTreeVOS = new ArrayList<CategoryTreeVO>();
            rootCategoryTreeVOS.add(rootTreeVO);
            resultObjectVO.setData(rootCategoryTreeVOS);

            categoryRedisService.addFullCategoryCache(rootCategoryTreeVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询迷你树
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryMiniTree(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            List<CategoryTreeVO> categoryMiniTree = categoryRedisService.queryCategoryMiniTree();
            if(CollectionUtils.isEmpty(categoryMiniTree))
            {
                try {
                    CategoryVO queryMiniTree = new CategoryVO();
                    List<Category> categoryList = categoryService.queryPcIndexList(queryMiniTree);
                    if(!CollectionUtils.isEmpty(categoryList)) {
                        List<CategoryVO> categoryTreeVOS = new ArrayList<CategoryVO>();
                        for(Category category : categoryList)
                        {
                            if(category.getParentId().longValue()==-1) {
                                CategoryTreeVO treeVO = new CategoryTreeVO();
                                BeanUtils.copyProperties(treeVO, category);
                                treeVO.setTitle(category.getName());
                                treeVO.setText(category.getName());
                                categoryTreeVOS.add(treeVO);

                                treeVO.setChildren(new ArrayList<CategoryTreeVO>());
                                categoryService.setChildren(categoryList,treeVO);
                            }
                        }
                        categoryRedisService.flushWMiniTree(categoryTreeVOS);
                    }
                }catch(Exception e)
                {
                    logger.warn(e.getMessage(),e);
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                }
            }

            for(CategoryTreeVO categoryTreeVO:categoryMiniTree)
            {
                categoryTreeVO.setPid(-1L);
                categoryTreeVO.setTitle(categoryTreeVO.getName());
                categoryTreeVO.setText(categoryTreeVO.getName());
                categoryTreeVO.setPid(categoryTreeVO.getParentId());
                categoryTreeVO.setPath(categoryTreeVO.getName());
            }

            CategoryTreeVO rootTreeVO = new CategoryTreeVO();
            rootTreeVO.setTitle("商城分类");
            rootTreeVO.setParentId(-1L);
            rootTreeVO.setPid(-1L);
            rootTreeVO.setId(-1L);
            rootTreeVO.setText("商城分类");
            if(!CollectionUtils.isEmpty(categoryMiniTree))
            {
                rootTreeVO.setChildren(categoryMiniTree);
            }

            categoryService.complementChildren(rootTreeVO);
            List<CategoryTreeVO> rootCategoryTreeVOS = new ArrayList<CategoryTreeVO>();
            rootCategoryTreeVOS.add(rootTreeVO);
            resultObjectVO.setData(rootCategoryTreeVOS);


        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询PC端首页类别树
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryWebIndexTree(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryVO.class);
            List<Category> categoryList = categoryService.queryPcIndexList(query);
            if(!CollectionUtils.isEmpty(categoryList)) {
                List<CategoryVO> categoryTreeVOS = new ArrayList<CategoryVO>();
                for(Category category : categoryList)
                {
                    if(category.getParentId().longValue()==-1) {
                        CategoryTreeVO treeVO = new CategoryTreeVO();
                        BeanUtils.copyProperties(treeVO, category);
                        treeVO.setTitle(category.getName());
                        treeVO.setText(category.getName());
                        categoryTreeVOS.add(treeVO);

                        treeVO.setChildren(new ArrayList<CategoryTreeVO>());
                        categoryService.setChildren(categoryList,treeVO);
                    }
                }
                resultObjectVO.setData(categoryTreeVOS);
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    private boolean existsAdminId(List<String> adminIds, String adminId)
    {
        for (String aid : adminIds) {
            if (aid != null && aid.equals(adminId)) {
                return true;
            }
        }
        return false;
    }



    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryTreeTable(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            CategoryTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryTreeInfo.class);

            //查询所有结构树
            List<CategoryVO>  categoryVOS = categoryService.findTreeTable(queryPageInfo);
            List<String> adminIds = new ArrayList<String>();
            //拿到树节点中所有创建人和修改人
            if(!CollectionUtils.isEmpty(categoryVOS)) {
                for (CategoryVO areaVO : categoryVOS) {
                    if (areaVO.getCreateAdminId() != null&&!"-1".equals(areaVO.getCreateAdminId())&&!existsAdminId(adminIds,areaVO.getCreateAdminId())) {
                        adminIds.add(areaVO.getCreateAdminId());
                    }
                    if (areaVO.getUpdateAdminId() != null&&!"-1".equals(areaVO.getUpdateAdminId())&&!existsAdminId(adminIds,areaVO.getUpdateAdminId())) {
                        adminIds.add(areaVO.getUpdateAdminId());
                    }
                }
            }




            //判断每个节点,如果集合中不存在父节点,那么就将这个节点设置为顶级节点
            boolean isFind =false;
            for(CategoryVO c:categoryVOS)
            {
                isFind=false;
                for(CategoryVO cv:categoryVOS)
                {
                    if(c.getParentId().longValue()==cv.getId().longValue())
                    {
                        isFind=true;
                        break;
                    }
                }
                if(!isFind)
                {
                    c.setParentId(-1L);
                }
            }

            resultObjectVO.setData(categoryVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryListByPid(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }
        try {
            CategoryVO queryCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryVO.class);
            List<Category> categories = categoryService.queryList(queryCategory);
            List<CategoryTreeVO> categoryTreeVOS = new LinkedList<>();
            if(!CollectionUtils.isEmpty(categories))
            {
                for(Category category:categories) {
                    CategoryTreeVO categoryTreeVO = new CategoryTreeVO();
                    BeanUtils.copyProperties(categoryTreeVO,category);
                    Long categoryChildCount = categoryService.findCountByParentId(categoryTreeVO.getId());
                    if(categoryChildCount!=null&&categoryChildCount.longValue()>0)
                    {
                        categoryTreeVO.setIsParent(true);
                    }else{
                        categoryTreeVO.setIsParent(false);
                    }
                    categoryTreeVOS.add(categoryTreeVO);
                }

            }
            resultObjectVO.setData(categoryTreeVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryChildListByPid(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }
        try {
            CategoryVO queryCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryVO.class);
            List<Category> childList = new ArrayList<Category>();
            categoryService.queryChildren(childList,queryCategory);
            resultObjectVO.setData(childList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 查询指定节点下一级子节点
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryNextOneLevelChildListByPid(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }
        try {
            CategoryVO queryCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryVO.class);
            resultObjectVO.setData(categoryService.queryList(queryCategory));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 查询树表格byPid
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            CategoryTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryTreeInfo.class);

            List<CategoryTreeVO> categoryTreeVOS = new ArrayList<CategoryTreeVO>();
            //按指定条件查询
            if(StringUtils.isNotEmpty(queryPageInfo.getName()))
            {
                CategoryVO queryCategory = new CategoryVO();
                queryCategory.setName(queryPageInfo.getName());
                List<Category> categories = categoryService.queryList(queryCategory);
                for (int i = 0; i < categories.size(); i++) {
                    Category category = categories.get(i);
                    CategoryTreeVO categoryTreeVO = new CategoryTreeVO();
                    BeanUtils.copyProperties(categoryTreeVO, category);
                    categoryTreeVOS.add(categoryTreeVO);
                }
            }else {
                //查询当前节点下的所有子节点
                CategoryVO queryCategory = new CategoryVO();
                if(queryPageInfo.getParentId()!=null) {
                    queryCategory.setParentId(queryPageInfo.getParentId());
                }else{
                    queryCategory.setParentId(-1L);
                }
                List<Category> categories = categoryService.queryList(queryCategory);
                for (int i = 0; i < categories.size(); i++) {
                    Category category = categories.get(i);
                    CategoryTreeVO categoryTreeVO = new CategoryTreeVO();
                    BeanUtils.copyProperties(categoryTreeVO, category);

                    queryCategory = new CategoryVO();
                    queryCategory.setParentId(category.getId());
                    Long childCount = categoryService.queryCount(queryCategory);
                    if (childCount > 0) {
                        categoryTreeVO.setHaveChild(true);
                    }
                    categoryTreeVOS.add(categoryTreeVO);
                }
            }

            List<String> adminIds = new ArrayList<String>();
            //拿到树节点中所有创建人和修改人
            if(!CollectionUtils.isEmpty(categoryTreeVOS)) {
                for (CategoryVO categoryVO : categoryTreeVOS) {
                    if (categoryVO.getCreateAdminId() != null&&!"-1".equals(categoryVO.getCreateAdminId())&&!existsAdminId(adminIds,categoryVO.getCreateAdminId())) {
                        adminIds.add(categoryVO.getCreateAdminId());
                    }
                    if (categoryVO.getUpdateAdminId() != null&&!"-1".equals(categoryVO.getUpdateAdminId())&&!existsAdminId(adminIds,categoryVO.getUpdateAdminId())) {
                        adminIds.add(categoryVO.getUpdateAdminId());
                    }
                }
            }


            //将查询的这个节点设置为顶级节点
            if(StringUtils.isNotEmpty(queryPageInfo.getName())) {
                if(!CollectionUtils.isEmpty(categoryTreeVOS)) {
                    for (Category category : categoryTreeVOS) {
                        category.setParentId(-1L);
                    }
                }
            }

            resultObjectVO.setData(categoryTreeVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 批量删除分类
     * @param requestVo
     * @return
     */
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<Category> categorys = JSONObject.parseArray(requestVo.getEntityJson(),Category.class);
            if(CollectionUtils.isEmpty(categorys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(Category category:categorys) {
                if(category.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(category);


                    List<Category> chidlren = new ArrayList<Category>();
                    categoryService.queryChildren(chidlren,category);

                    //把当前节点添加进去,循环这个集合
                    chidlren.add(category);

                    for(Category c:chidlren) {
                        //删除当前分类
                        int row = categoryService.deleteById(c.getId());
                        if (row < 1) {
                            logger.warn("删除类别失败 {} ",JSONObject.toJSONString(c));
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg("请重试!");

                            ResultObjectVO resultObjectRowVO = new ResultObjectVO();
                            resultObjectRowVO.setCode(ResultVO.FAILD);
                            resultObjectRowVO.setMsg("请重试!");
                            resultObjectRowVO.setData(c.getId());
                            resultObjectVOList.add(resultObjectRowVO);
                            continue;
                        }

                    }

                }
            }
            resultObjectVO.setData(resultObjectVOList);

            categoryRedisService.clearCaches();
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 查询类别树
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryCategoryTree(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            resultObjectVO.setData(categoryRedisService.queryMiniTree());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 查询指定节点下子节点
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryTreeChildByPid(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            CategoryVO categoryVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryVO.class);
            List<CategoryTreeVO> categoryTreeVOS = new ArrayList<CategoryTreeVO>();
            List<Category> categoryList = categoryService.queryList(categoryVO);
            if(categoryVO.getParentId()==null)
            {
                CategoryTreeVO categoryTreeVO = new CategoryTreeVO();
                categoryTreeVO.setId(-1L);
                categoryTreeVO.setName("根节点");
                categoryTreeVO.setParentId(-1L);
                Long childCount = categoryService.queryOneChildCountByPid(-1L);
                if(childCount>0)
                {
                    categoryTreeVO.setIsParent(true);
                }
                categoryTreeVOS.add(categoryTreeVO);
            }else {
                for (int i = 0; i < categoryList.size(); i++) {
                    Category category = categoryList.get(i);
                    CategoryTreeVO categoryTreeVO = new CategoryTreeVO();
                    BeanUtils.copyProperties(categoryTreeVO, category);
                    categoryTreeVO.setTitle(category.getName());
                    Long childCount = categoryService.queryOneChildCountByPid(categoryTreeVO.getId());
                    if (childCount > 0) {
                        categoryTreeVO.setIsParent(true);
                    } else {
                        categoryTreeVO.setIsParent(false);
                    }
                    categoryTreeVOS.add(categoryTreeVO);
                }
            }

            resultObjectVO.setData(categoryTreeVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询全部
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryAllList(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        try {
            CategoryVO query = new CategoryVO();
            resultObjectVO.setData(categoryService.queryList(query));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }



}
