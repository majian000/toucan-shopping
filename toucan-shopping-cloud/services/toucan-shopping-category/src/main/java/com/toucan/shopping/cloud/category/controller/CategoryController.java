package com.toucan.shopping.cloud.category.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.category.cache.service.CategoryRedisService;
import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.page.CategoryTreeInfo;
import com.toucan.shopping.modules.category.service.CategoryService;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 类别控制器
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FeignAdminService feignAdminService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private CategoryRedisService categoryRedisService;



    /**
     * 保存类别
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestHeader(value = "toucan-sign-header",defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO)
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
                resultObjectVO.setMsg("保存失败,请重试!");
                return resultObjectVO;
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("保存失败,请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 更新类别
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO)
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
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 根据ID删除类别
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO deleteById(@RequestHeader(value = "toucan-sign-header",defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO)
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
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryById(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
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
            Category Category = JSONObject.parseObject(requestJsonVO.getEntityJson(), Category.class);
            resultObjectVO.setData(categoryService.queryById(Category.getId()));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/ids",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByIdList(@RequestBody RequestJsonVO requestJsonVO)
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
                resultObjectVO.setData(categoryService.queryListByIdList(categoryIdList));
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
     * 批量刷新缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/index/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO flushWebIndexCache(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
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
                            if(treeVO.getName().indexOf("/")!=-1&&treeVO.getHref().indexOf("&toucan_spliter_2021&")!=-1)
                            {
                                String[] names = treeVO.getName().split("/");
                                String[] hrefs = treeVO.getHref().split("&toucan_spliter_2021&");
                                if(names.length==hrefs.length) {
                                    for (int i = 0; i < names.length; i++) {
                                        linkhtml.append("<a class=\"category_a\" href=\""+hrefs[i]+"\">");
                                        linkhtml.append(names[i]);
                                        linkhtml.append("</a>");
                                        if(i+1<names.length)
                                        {
                                            linkhtml.append("<a class=\"category_a\" >/</a>");
                                        }
                                    }
                                }else{
                                    for (int i = 0; i < names.length; i++) {
                                        linkhtml.append("<a class=\"category_a\" href=\"#\">");
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

                        treeVO.setChildren(new ArrayList<CategoryVO>());
                        categoryService.setChildren(categoryList,treeVO);
                    }
                }
                categoryRedisService.flushWebIndexCaches(categoryTreeVOS);
                categoryRedisService.flushWMiniTree(categoryTreeVOS);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 清空首页缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/clear/index/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO clearWebIndexCache(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            resultObjectVO.setData(categoryRedisService.clearWebIndexCache());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            CategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),CategoryVO.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该功能项
            CategoryVO query=new CategoryVO();
            query.setId(entity.getId());
            List<Category> categorys = categoryService.queryList(query);
            if(CollectionUtils.isEmpty(categorys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,对象不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(categorys);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据ID数组查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/idArray",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findByIdArray(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            CategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),CategoryVO.class);
            if(entity.getIdArray()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID数组");
                return resultObjectVO;
            }

            //查询是否存在该功能项
            CategoryVO query=new CategoryVO();
            query.setIdArray(entity.getIdArray());
            List<Category> categorys = categoryService.queryList(query);
            if(CollectionUtils.isEmpty(categorys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(categorys);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryVO.class);

            List<Category> categoryList = categoryService.queryList(query);
            if(!CollectionUtils.isEmpty(categoryList))
            {
                List<CategoryVO> categoryTreeVOS = new ArrayList<CategoryVO>();
                for(Category category : categoryList)
                {
                    if(category.getParentId().longValue()==-1) {
                        CategoryTreeVO treeVO = new CategoryTreeVO();
                        BeanUtils.copyProperties(treeVO, category);

                        treeVO.setTitle(category.getName());
                        treeVO.setText(category.getName());

                        categoryTreeVOS.add(treeVO);

                        treeVO.setChildren(new ArrayList<CategoryVO>());
                        categoryService.setChildren(categoryList,treeVO);
                    }
                }

                CategoryTreeVO rootTreeVO = new CategoryTreeVO();
                rootTreeVO.setTitle("根节点");
                rootTreeVO.setParentId(-1L);
                rootTreeVO.setId(-1L);
                rootTreeVO.setText("根节点");
                rootTreeVO.setChildren(categoryTreeVOS);
                List<CategoryVO> rootCategoryTreeVOS = new ArrayList<CategoryVO>();
                rootCategoryTreeVOS.add(rootTreeVO);
                resultObjectVO.setData(rootCategoryTreeVOS);

            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询PC端首页类别树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/web/index/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryWebIndexTree(@RequestBody RequestJsonVO requestJsonVO)
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

                        treeVO.setChildren(new ArrayList<CategoryVO>());
                        categoryService.setChildren(categoryList,treeVO);
                    }
                }
                resultObjectVO.setData(categoryTreeVOS);
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    public boolean existsAdminId(List<String> adminIds,String adminId)
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
    @RequestMapping(value="/query/tree/table",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTable(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
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


            if(!CollectionUtils.isEmpty(adminIds))
            {
                AdminVO query = new AdminVO();
                String[] adminIdArray = new String[adminIds.size()];
                for(int i=0;i<adminIds.size();i++)
                {
                    adminIdArray[i]=adminIds.get(i);
                }
                query.setAdminIds(adminIdArray);
                RequestJsonVO adminRequestJsonVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),query);
                resultObjectVO = feignAdminService.queryListByEntity(adminRequestJsonVo.sign(),adminRequestJsonVo);
                if(resultObjectVO.isSuccess())
                {
                    List<AdminVO> admins = JSONObject.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),AdminVO.class);
                    if(!CollectionUtils.isEmpty(admins))
                    {
                        if(!CollectionUtils.isEmpty(categoryVOS)) {
                            for (CategoryVO categoryVO : categoryVOS) {
                                for(AdminVO adminVO:admins)
                                {
                                    if (categoryVO.getCreateAdminId() != null&&categoryVO.getCreateAdminId().equals(adminVO.getAdminId())) {
                                        categoryVO.setCreateAdminUsername(adminVO.getUsername());
                                    }
                                    if (categoryVO.getUpdateAdminId() != null&&categoryVO.getUpdateAdminId().equals(adminVO.getAdminId())) {
                                        categoryVO.setUpdateAdminUsername(adminVO.getUsername());
                                    }
                                }
                            }
                        }
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
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryListByPid(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }
        try {
            CategoryVO queryCategory = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryVO.class);
            resultObjectVO.setData(categoryService.queryList(queryCategory));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/child/list/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryChildListByPid(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
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
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
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

            if(!CollectionUtils.isEmpty(adminIds))
            {
                AdminVO query = new AdminVO();
                String[] adminIdArray = new String[adminIds.size()];
                for(int i=0;i<adminIds.size();i++)
                {
                    adminIdArray[i]=adminIds.get(i);
                }
                query.setAdminIds(adminIdArray);
                RequestJsonVO adminRequestJsonVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),query);
                resultObjectVO = feignAdminService.queryListByEntity(adminRequestJsonVo.sign(),adminRequestJsonVo);
                if(resultObjectVO.isSuccess())
                {
                    List<AdminVO> admins = JSONObject.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),AdminVO.class);
                    if(!CollectionUtils.isEmpty(admins))
                    {
                        if(!CollectionUtils.isEmpty(categoryTreeVOS)) {
                            for (CategoryVO categoryVO : categoryTreeVOS) {
                                for(AdminVO adminVO:admins)
                                {
                                    if (categoryVO.getCreateAdminId() != null&&categoryVO.getCreateAdminId().equals(adminVO.getAdminId())) {
                                        categoryVO.setCreateAdminUsername(adminVO.getUsername());
                                    }
                                    if (categoryVO.getUpdateAdminId() != null&&categoryVO.getUpdateAdminId().equals(adminVO.getAdminId())) {
                                        categoryVO.setUpdateAdminUsername(adminVO.getUsername());
                                    }
                                }
                            }
                        }
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
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        try {
            Category category = JSONObject.parseObject(requestJsonVO.getEntityJson(), Category.class);



            if(category.getId()==null)
            {
                logger.info("ID为空 param:"+ JSONObject.toJSONString(category));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("ID不能为空!");
                return resultObjectVO;
            }


            CategoryVO queryCategory = new CategoryVO();
            queryCategory.setId(category.getId());

            List<Category> categoryList = categoryService.queryList(queryCategory);
            if(CollectionUtils.isEmpty(categoryList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在该类别!");
                return resultObjectVO;
            }

            category = categoryList.get(0);
            categoryService.deleteChildrenByParentId(category.getId());
            int row = categoryService.deleteById(category.getId());
            if (row <=0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 批量删除功能项
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<Category> categorys = JSONObject.parseArray(requestVo.getEntityJson(),Category.class);
            if(CollectionUtils.isEmpty(categorys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找ID");
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
                        //删除当前功能项
                        int row = categoryService.deleteById(c.getId());
                        if (row < 1) {
                            logger.warn("删除类别失败 {} ",JSONObject.toJSONString(c));
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg("请求失败,请重试!");
                            continue;
                        }

                    }

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 查询类别树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/category/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryCategoryTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            resultObjectVO.setData(categoryRedisService.queryMiniTree());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }




}
