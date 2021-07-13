package com.toucan.shopping.cloud.apps.admin.controller.category;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.category.api.feign.service.FeignCategoryService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.page.CategoryTreeInfo;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 类别控制器
 */
@Controller
@RequestMapping("/category")
public class CategoryController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignCategoryService feignCategoryService;

    @Autowired
    private FeignFunctionService feignFunctionService;






    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String page(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/category/listPage",feignFunctionService);

        return "pages/category/list.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {
        return "pages/category/add.html";
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            Category entity = new Category();
            entity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            ResultObjectVO resultObjectVO = feignCategoryService.findById(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<Category> entitys = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Category.class);
                    if(!CollectionUtils.isEmpty(entitys))
                    {
                        CategoryVO categoryVO = new CategoryVO();
                        BeanUtils.copyProperties(categoryVO,entitys.get(0));
                        //如果是顶级节点,上级节点就是根节点
                        if(categoryVO.getParentId().longValue()==-1)
                        {
                            categoryVO.setParentName("根节点");
                        }else {
                            Category queryParent = new Category();
                            queryParent.setId(categoryVO.getParentId());
                            requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryParent);
                            resultObjectVO = feignCategoryService.findById(SignUtil.sign(requestJsonVO), requestJsonVO);
                            if (resultObjectVO.isSuccess()) {
                                List<Category> parentAreaList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), Category.class);
                                if (!CollectionUtils.isEmpty(parentAreaList)) {
                                    categoryVO.setParentName(parentAreaList.get(0).getName());
                                }
                            }
                        }
                        request.setAttribute("model",categoryVO);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/category/edit.html";
    }





    /**
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody CategoryVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setCreateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignCategoryService.save(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 刷新redis缓存
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/flush/index/cache",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO flushIndexCache(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestVo =  RequestJsonVOGenerator.generator(appCode, new CategoryVO());
            resultObjectVO = feignCategoryService.flushWebIndexCache(SignUtil.sign(requestVo), requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 清空PC首页缓存
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/clear/index/cache",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO clearIndexCache(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryVO bannerVO = new CategoryVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, bannerVO);
            resultObjectVO = feignCategoryService.clearWebIndexCache(requestJsonVO.sign(),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
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
    public ResultObjectVO update(HttpServletRequest request,@RequestBody CategoryVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignCategoryService.update(requestJsonVO.sign(),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/query/tree",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO queryTree(HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            CategoryVO query = new CategoryVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            return feignCategoryService.queryTree(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 查询列表
     * @param queryPageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/tree/table",method = RequestMethod.GET)
    @ResponseBody
    public ResultObjectVO treeTable(HttpServletRequest request, CategoryTreeInfo queryPageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryPageInfo);
            resultObjectVO = feignCategoryService.queryTreeTable(SignUtil.sign(requestJsonVO),requestJsonVO);
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 删除
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(HttpServletRequest request,  @PathVariable String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            Category entity =new Category();
            entity.setId(Long.parseLong(id));
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));

            String entityJson = JSONObject.toJSONString(entity);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(toucan.getAppCode());
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignCategoryService.deleteById(SignUtil.sign(requestVo),requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 删除
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/delete/ids",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<CategoryVO> categoryVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(categoryVOS))
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(categoryVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(toucan.getAppCode());
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignCategoryService.deleteByIds(SignUtil.sign(requestVo), requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



}

