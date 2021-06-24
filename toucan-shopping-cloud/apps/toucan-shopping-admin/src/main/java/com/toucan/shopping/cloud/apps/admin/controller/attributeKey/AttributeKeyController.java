package com.toucan.shopping.cloud.apps.admin.controller.attributeKey;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.area.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.area.api.feign.service.FeignBannerAreaService;
import com.toucan.shopping.cloud.area.api.feign.service.FeignBannerService;
import com.toucan.shopping.cloud.category.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyService;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.area.entity.Banner;
import com.toucan.shopping.modules.area.entity.BannerArea;
import com.toucan.shopping.modules.area.page.BannerPageInfo;
import com.toucan.shopping.modules.area.vo.AreaTreeVO;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.area.vo.BannerAreaVO;
import com.toucan.shopping.modules.area.vo.BannerVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.page.CategoryTreeInfo;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.SignUtil;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 商品属性键管理
 */
@Controller
@RequestMapping("/product/attributeKey")
public class AttributeKeyController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignAttributeKeyService feignAttributeKeyService;

    @Autowired
    private FeignCategoryService feignCategoryService;

    @Autowired
    private FeignAdminService feignAdminService;



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/product/attributeKey/listPage",feignFunctionService);
        return "pages/product/attributeKey/list.html";
    }



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, AttributeKeyPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignAttributeKeyService.queryListPage(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
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
                        resultObjectVO = feignCategoryService.findByIdArray(requestJsonVO.sign(),requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            List<CategoryVO> categoryVOS = (List<CategoryVO>)resultObjectVO.formatDataArray(CategoryVO.class);
                            if(CollectionUtils.isNotEmpty(categoryVOS))
                            {
                                for(AttributeKeyVO attributeKeyVO:list)
                                {
                                    for(CategoryVO categoryVO:categoryVOS)
                                    {
                                        if(attributeKeyVO.getCategoryId().longValue()==categoryVO.getId().longValue())
                                        {
                                            attributeKeyVO.setCategoryName(categoryVO.getName());
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
                        resultObjectVO = feignAdminService.queryListByEntity(requestJsonVO.sign(),requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            List<AdminVO> adminVOS = (List<AdminVO>)resultObjectVO.formatDataArray(AdminVO.class);
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
            tableVO.setMsg("请求失败,请重试");
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
    public ResultObjectVO save(HttpServletRequest request, @RequestBody AttributeKeyVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setCreateAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignAttributeKeyService.save(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {

        return "pages/product/attributeKey/add.html";
    }




    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request,@RequestBody AttributeKeyVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignAttributeKeyService.update(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
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
            AttributeKeyVO attributeKeyVO = new AttributeKeyVO();
            attributeKeyVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, attributeKeyVO);
            ResultObjectVO resultObjectVO = feignAttributeKeyService.findById(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<AttributeKeyVO> attributeKeyVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),AttributeKeyVO.class);
                    if(!CollectionUtils.isEmpty(attributeKeyVOS))
                    {
                        attributeKeyVO = attributeKeyVOS.get(0);

                        //查询类别名称
                        CategoryVO queryCategoryVO = new CategoryVO();
                        queryCategoryVO.setId(attributeKeyVO.getCategoryId());
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryCategoryVO);
                        resultObjectVO = feignCategoryService.findById(requestJsonVO.sign(),requestJsonVO);
                        if(resultObjectVO.isSuccess())
                        {
                            List<CategoryVO> categoryVOS = (List<CategoryVO>)resultObjectVO.formatDataArray(CategoryVO.class);
                            if(CollectionUtils.isNotEmpty(categoryVOS))
                            {
                                for(CategoryVO categoryVO:categoryVOS)
                                {
                                    if(attributeKeyVO.getCategoryId().longValue()==categoryVO.getId().longValue())
                                    {
                                        attributeKeyVO.setCategoryName(categoryVO.getName());
                                        break;
                                    }
                                }
                            }
                        }
                        request.setAttribute("model",attributeKeyVO);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/product/attributeKey/edit.html";
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/query/category/tree",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO queryTree(HttpServletRequest request)
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

