package com.toucan.shopping.cloud.apps.admin.controller.product.attribute;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.cloud.product.api.AttributeKeyServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.product.entity.AttributeKey;
import com.toucan.shopping.modules.product.page.AttributeKeyPageInfo;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 商品属性管理
 */
@RestController
@RequestMapping("/product/attribute/attributeKey")
public class AttributeKeyController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private AttributeKeyServiceAPI attributeKeyService;

    @Autowired
    private CategoryServiceAPI categoryService;

    @Autowired
    private AdminServiceAPI adminServiceAPI;




    /**
     * 查询列表
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:attributeKey:tree:list:pid"})
    @RequestMapping(value = "/tree/table/by/pid",method = RequestMethod.POST)
    public ResultObjectVO queryTreeTableByPid(AttributeKeyPageInfo pageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = null;
            if(pageInfo.getCategoryId()!=null&&pageInfo.getCategoryId().longValue()!=-1) {
                //查询分类以及子分类
                CategoryVO categoryVO = new CategoryVO();
                categoryVO.setId(pageInfo.getCategoryId());
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), categoryVO);
                resultObjectVO = categoryService.queryChildListByPid(requestJsonVO);
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
            resultObjectVO = attributeKeyService.queryTreeTableByPid(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    List<AttributeKeyVO> list = resultObjectVO.formatDataList(AttributeKeyVO.class);
                    if(CollectionUtils.isNotEmpty(list))
                    {
                        Long[] categoryIds = new Long[list.size()];
                        List<String> adminIdList = new ArrayList<String>();
                        for(int i=0;i<list.size();i++)
                        {
                            AttributeKeyVO attributeKeyVO = list.get(i);
                            categoryIds[i] = attributeKeyVO.getCategoryId();
                            if(attributeKeyVO.getCreateAdminId()!=null) {
                                adminIdList.add(attributeKeyVO.getCreateAdminId());
                            }
                            if(attributeKeyVO.getUpdateAdminId()!=null)
                            {
                                adminIdList.add(attributeKeyVO.getUpdateAdminId());
                            }
                        }
                        //查询类别名称
                        CategoryVO queryCategoryVO = new CategoryVO();
                        queryCategoryVO.setIdArray(categoryIds);
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryCategoryVO);
                        resultObjectVO = categoryService.findByIdArray(requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            List<CategoryVO> categoryVOS = resultObjectVO.formatDataList(CategoryVO.class);
                            if(CollectionUtils.isNotEmpty(categoryVOS))
                            {
                                for(AttributeKeyVO attributeKeyVO:list)
                                {
                                    for(CategoryVO categoryVO:categoryVOS)
                                    {
                                        if(attributeKeyVO.getCategoryId().longValue()==categoryVO.getId().longValue())
                                        {
                                            attributeKeyVO.setCategoryName(categoryVO.getName());
                                            attributeKeyVO.setCategoryPath(categoryVO.getNamePath());
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        //查询创建人和修改人
                        String[] createOrUpdateAdminIds = new String[adminIdList.size()];
                        adminIdList.toArray(createOrUpdateAdminIds);
                        AdminVO queryAdminVO = new AdminVO();
                        queryAdminVO.setAdminIds(createOrUpdateAdminIds);
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryAdminVO);
                        resultObjectVO = adminServiceAPI.queryListByEntity(requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            List<AdminVO> adminVOS = (List<AdminVO>)resultObjectVO.formatDataList(AdminVO.class);
                            if(CollectionUtils.isNotEmpty(adminVOS))
                            {
                                for(AttributeKeyVO attributeKeyVO:list)
                                {
                                    for(AdminVO adminVO:adminVOS)
                                    {
                                        if(attributeKeyVO.getCreateAdminId()!=null&&attributeKeyVO.getCreateAdminId().equals(adminVO.getAdminId()))
                                        {
                                            attributeKeyVO.setCreateAdminName(adminVO.getUsername());
                                        }
                                        if(attributeKeyVO.getUpdateAdminId()!=null&&attributeKeyVO.getUpdateAdminId().equals(adminVO.getAdminId()))
                                        {
                                            attributeKeyVO.setUpdateAdminName(adminVO.getUsername());
                                        }
                                    }
                                }
                            }
                        }
                        resultObjectVO.setData(list);
                    }
                }
            }
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





    /**
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:attributeKey:save"})
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResultObjectVO save(@RequestBody AttributeKeyVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = attributeKeyService.save(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:attributeKey:update"})
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResultObjectVO update(@RequestBody AttributeKeyVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = attributeKeyService.update(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 删除
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:attributeKey:delete"})
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    public ResultObjectVO deleteById(@PathVariable String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            AttributeKey attributeKey =new AttributeKey();
            attributeKey.setId(Long.parseLong(id));
            attributeKey.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());

            String entityJson = JSONObject.toJSONString(attributeKey);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = attributeKeyService.deleteById( requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 删除
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:attributeKey:deletes"})
    @RequestMapping(value = "/delete/ids",method = RequestMethod.DELETE)
    public ResultObjectVO deleteByIds( @RequestBody List<AttributeKeyVO> attributeKeyVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(org.springframework.util.CollectionUtils.isEmpty(attributeKeyVOS))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(attributeKeyVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = attributeKeyService.deleteByIds( requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:attributeKey:list"})
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public TableVO list(AttributeKeyPageInfo pageInfo)
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
                resultObjectVO = categoryService.queryChildListByPid(requestJsonVO);
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
            resultObjectVO = attributeKeyService.queryListPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<AttributeKeyVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),AttributeKeyVO.class);
                    if(CollectionUtils.isNotEmpty(list))
                    {
                        Long[] categoryIds = new Long[list.size()];
                        List<String> adminIdList = new ArrayList<String>();
                        for(int i=0;i<list.size();i++)
                        {
                            AttributeKeyVO attributeKeyVO = list.get(i);
                            categoryIds[i] = attributeKeyVO.getCategoryId();
                            if(attributeKeyVO.getCreateAdminId()!=null) {
                                adminIdList.add(attributeKeyVO.getCreateAdminId());
                            }
                            if(attributeKeyVO.getUpdateAdminId()!=null)
                            {
                                adminIdList.add(attributeKeyVO.getUpdateAdminId());
                            }
                        }
                        //查询类别名称
                        CategoryVO queryCategoryVO = new CategoryVO();
                        queryCategoryVO.setIdArray(categoryIds);
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryCategoryVO);
                        resultObjectVO = categoryService.findByIdArray(requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            List<CategoryVO> categoryVOS = (List<CategoryVO>)resultObjectVO.formatDataList(CategoryVO.class);
                            if(CollectionUtils.isNotEmpty(categoryVOS))
                            {
                                for(AttributeKeyVO attributeKeyVO:list)
                                {
                                    for(CategoryVO categoryVO:categoryVOS)
                                    {
                                        if(attributeKeyVO.getCategoryId().longValue()==categoryVO.getId().longValue())
                                        {
                                            attributeKeyVO.setCategoryName(categoryVO.getName());
                                            attributeKeyVO.setCategoryPath(categoryVO.getNamePath());
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        //查询创建人和修改人
                        String[] createOrUpdateAdminIds = new String[adminIdList.size()];
                        adminIdList.toArray(createOrUpdateAdminIds);
                        AdminVO queryAdminVO = new AdminVO();
                        queryAdminVO.setAdminIds(createOrUpdateAdminIds);
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryAdminVO);
                        resultObjectVO = adminServiceAPI.queryListByEntity(requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            List<AdminVO> adminVOS = (List<AdminVO>)resultObjectVO.formatDataList(AdminVO.class);
                            if(CollectionUtils.isNotEmpty(adminVOS))
                            {
                                for(AttributeKeyVO attributeKeyVO:list)
                                {
                                    for(AdminVO adminVO:adminVOS)
                                    {
                                        if(attributeKeyVO.getCreateAdminId()!=null&&attributeKeyVO.getCreateAdminId().equals(adminVO.getAdminId()))
                                        {
                                            attributeKeyVO.setCreateAdminName(adminVO.getUsername());
                                        }
                                        if(attributeKeyVO.getUpdateAdminId()!=null&&attributeKeyVO.getUpdateAdminId().equals(adminVO.getAdminId()))
                                        {
                                            attributeKeyVO.setUpdateAdminName(adminVO.getUsername());
                                        }
                                    }
                                }
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


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:attributeKey:category:tree"})
    @RequestMapping(value = "/query/category/tree",method = RequestMethod.POST)
    public ResultObjectVO queryCategoryTree()
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryVO query = new CategoryVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            resultObjectVO = categoryService.queryTree(requestJsonVO);
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:attributeKey:tree:category:id"})
    @RequestMapping(value = "/query/tree/category/id",method = RequestMethod.POST)
    public ResultObjectVO queryTreeByCategoryId(@RequestParam Long categoryId, @RequestParam Short attributeType)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AttributeKeyVO attributeKeyVO = new AttributeKeyVO();
            attributeKeyVO.setCategoryId(categoryId);
            attributeKeyVO.setAttributeType(attributeType);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,attributeKeyVO);
            resultObjectVO = attributeKeyService.queryTreeByCategoryId(requestJsonVO);
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:product:attributeKey:tree:list:pid"})
    @RequestMapping(value = "/query/category/tree/pid",method = RequestMethod.POST)
    public ResultObjectVO queryCategoryTreeByParentId(@RequestParam(defaultValue = "-1") Long id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryVO query = new CategoryVO();
            query.setParentId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            resultObjectVO = categoryService.queryListByPid(requestJsonVO);
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








}
