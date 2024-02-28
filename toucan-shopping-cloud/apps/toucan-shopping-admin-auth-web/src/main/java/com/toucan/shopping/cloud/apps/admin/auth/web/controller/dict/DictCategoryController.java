package com.toucan.shopping.cloud.apps.admin.auth.web.controller.dict;


import com.toucan.shopping.cloud.admin.auth.api.feign.service.*;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.entity.DictCategory;
import com.toucan.shopping.modules.admin.auth.page.DictCategoryPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.admin.auth.vo.DictCategoryVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dictCategory")
public class DictCategoryController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignDictCategoryService feignDictCategoryService;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignAppService feignAppService;






    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String page(HttpServletRequest request)
    {

        //初始化选择应用控件
        super.initSelectApp(request,toucan,feignAppService);

        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/dictCategory/listPage",feignFunctionService);
        return "pages/dict/dictCategory/list.html";
    }






    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, DictCategoryPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignDictCategoryService.listPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    DictCategoryPageInfo dictCategoryPageInfo = resultObjectVO.formatData(DictCategoryPageInfo.class);
                    tableVO.setCount(dictCategoryPageInfo.getTotal());
                    if(tableVO.getCount()>0) {
                        Set<String> appCodes = new HashSet<>();
                        for (DictCategoryVO dictCategoryVO : dictCategoryPageInfo.getList()) {
                            appCodes.add(dictCategoryVO.getAppCode());
                        }
                        this.setAppNames(appCodes,dictCategoryPageInfo.getList());
                        tableVO.setData(dictCategoryPageInfo.getList());
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
     * 设置关联应用
     * @param appCodes
     * @throws Exception
     */
    private void setAppNames(Set<String> appCodes,List<DictCategoryVO> list) throws Exception{
        if(CollectionUtils.isNotEmpty(appCodes)){
            AppVO appVO=new AppVO();
            appVO.setCodes(new ArrayList(appCodes));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,appVO);
            ResultObjectVO resultObjectVO = feignAppService.queryListByCodes(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                List<AppVO> apps = resultObjectVO.formatDataList(AppVO.class);
                if(CollectionUtils.isNotEmpty(apps)) {
                    for (DictCategoryVO dictCategoryVO : list) {
                        for(AppVO apv:apps){
                            if(dictCategoryVO.getAppCode().equals(apv.getCode())){
                                dictCategoryVO.setAppName(apv.getName());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {
        super.initSelectApp(request,toucan,feignAppService);

        return "pages/dict/dictCategory/add.html";
    }



    /**
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody DictCategoryVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setCreateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignDictCategoryService.save(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Integer id)
    {
        try {
            super.initSelectApp(request,toucan,feignAppService);

            DictCategoryVO dictCategory = new DictCategoryVO();
            dictCategory.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, dictCategory);
            ResultObjectVO resultObjectVO = feignDictCategoryService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {

                    List<DictCategoryVO> dictCategoryVOS = resultObjectVO.formatDataList(DictCategoryVO.class);
                    if(!CollectionUtils.isEmpty(dictCategoryVOS))
                    {
                        dictCategory = dictCategoryVOS.get(0);
                        request.setAttribute("model",dictCategory);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/dict/dictCategory/edit.html";
    }




    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request,@RequestBody DictCategoryVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignDictCategoryService.update(requestJsonVO);
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
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
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
            DictCategory dictCategory =new DictCategory();
            dictCategory.setId(Integer.parseInt(id));
            dictCategory.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode,dictCategory);
            resultObjectVO = feignDictCategoryService.deleteById(requestVo);
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
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/delete/ids",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<DictCategory> dictCategories)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(dictCategories))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode,dictCategories);
            resultObjectVO = feignDictCategoryService.deleteByIds(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/queryAppListByCategoryId",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAppListByCategoryId(@RequestBody DictCategoryVO query)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(query.getId()==null){
                resultObjectVO.setMsg("字典分类ID不能为空");
                resultObjectVO.setCode(TableVO.FAILD);
                return resultObjectVO;
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

