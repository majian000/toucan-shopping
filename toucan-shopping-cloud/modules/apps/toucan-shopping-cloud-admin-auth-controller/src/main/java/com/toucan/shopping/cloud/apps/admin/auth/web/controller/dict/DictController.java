package com.toucan.shopping.cloud.apps.admin.auth.web.controller.dict;


import com.toucan.shopping.cloud.admin.auth.api.feign.service.*;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.entity.Dict;
import com.toucan.shopping.modules.admin.auth.page.DictPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.*;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
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

@Controller
@RequestMapping("/dict")
public class DictController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignDictService feignDictService;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignAppService feignAppService;

    @Autowired
    private FeignDictCategoryService feignDictCategoryService;

    @Autowired
    private FeignAdminService feignAdminService;



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String page(HttpServletRequest request)
    {

        //初始化选择应用控件
        super.initSelectApp(request,toucan,feignAppService);

        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/dict/listPage",feignFunctionService);
        return "pages/dict/dict/list.html";
    }





    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request,@RequestParam Integer dictCategoryId) throws Exception
    {
        DictCategoryVO dictCategoryVO = new DictCategoryVO();
        dictCategoryVO.setId(dictCategoryId);;
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,dictCategoryVO);
        ResultObjectVO resultObjectVO = feignDictCategoryService.findById(requestJsonVO);
        if(resultObjectVO.isSuccess())
        {
            List<DictCategoryVO> dictCategoryVOS = resultObjectVO.formatDataList(DictCategoryVO.class);
            if(CollectionUtils.isNotEmpty(dictCategoryVOS)) {
                DictCategoryVO dictCategoryModel = dictCategoryVOS.get(0);
                request.setAttribute("dictCategoryModel", dictCategoryModel);
            }else {
                request.setAttribute("dictCategoryModel", new DictCategoryVO());
            }
        }

        return "pages/dict/dict/add.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            super.initSelectApp(request,toucan,feignAppService);

            DictVO dictVO = new DictVO();
            dictVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, dictVO);
            ResultObjectVO resultObjectVO = feignDictService.findById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<DictVO> dictVOS = resultObjectVO.formatDataList(DictVO.class);
                if(!CollectionUtils.isEmpty(dictVOS))
                {
                    dictVO = dictVOS.get(0);
                    DictCategoryVO dictCategory = new DictCategoryVO();
                    dictCategory.setId(dictVO.getCategoryId());
                    requestJsonVO = RequestJsonVOGenerator.generator(appCode, dictCategory);
                    resultObjectVO = feignDictCategoryService.findById(requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        List<DictCategoryVO> dictCategoryVOS = resultObjectVO.formatDataList(DictCategoryVO.class);
                        if(!CollectionUtils.isEmpty(dictCategoryVOS))
                        {
                            dictCategory = dictCategoryVOS.get(0);
                            dictVO.setCategoryName(dictCategory.getName());
                        }
                    }
                    request.setAttribute("model",dictVO);
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/dict/dict/edit.html";
    }



    /**
     * 查询列表
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/tree/table/by/pid",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTableByPid(HttpServletRequest request, DictPageInfo pageInfo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = null;
            if(pageInfo.getCategoryId()==null||pageInfo.getCategoryId().longValue()==-1) {
                resultObjectVO.setMsg("字典分类ID不能为空");
                resultObjectVO.setCode(TableVO.FAILD);
                return resultObjectVO;
            }

            requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            resultObjectVO = feignDictService.queryTreeTableByPid(requestJsonVO);

            if(resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {

                    Set<String> adminIdList = new HashSet<String>();
                    Set<String> appCodes = new HashSet<>();
                    List<DictTreeVO> dictTreeVOS = resultObjectVO.formatDataList(DictTreeVO.class);
                    if(CollectionUtils.isNotEmpty(dictTreeVOS)) {
                        for (DictTreeVO dictTreeVO : dictTreeVOS) {
                            if (dictTreeVO.getCreateAdminId() != null) {
                                adminIdList.add(dictTreeVO.getCreateAdminId());
                            }
                            if (dictTreeVO.getUpdateAdminId() != null) {
                                adminIdList.add(dictTreeVO.getUpdateAdminId());
                            }
                            appCodes.add(dictTreeVO.getAppCode());
                        }
                        this.setAdminNames(adminIdList, dictTreeVOS);
                        this.setAppNames(appCodes,dictTreeVOS);
                        resultObjectVO.setData(dictTreeVOS);
                    }
                }
            }
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
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
    public ResultObjectVO update(HttpServletRequest request,@RequestBody DictVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignDictService.update(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 设置关联应用
     * @param appCodes
     * @throws Exception
     */
    private void setAppNames(Set<String> appCodes,List<DictTreeVO> list) throws Exception{
        if(CollectionUtils.isNotEmpty(appCodes)){
            AppVO appVO=new AppVO();
            appVO.setCodes(new ArrayList(appCodes));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,appVO);
            ResultObjectVO resultObjectVO = feignAppService.queryListByCodes(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                List<AppVO> apps = resultObjectVO.formatDataList(AppVO.class);
                if(CollectionUtils.isNotEmpty(apps)) {
                    for (DictTreeVO dictTreeVO : list) {
                        for(AppVO apv:apps){
                            if(dictTreeVO.getAppCode().equals(apv.getCode())){
                                dictTreeVO.setAppName(apv.getName());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 设置管理员名称
     * @param adminIdList
     * @throws Exception
     */
    private void setAdminNames(Set<String> adminIdList,List<DictTreeVO> list) throws Exception{

        //查询创建人和修改人
        String[] createOrUpdateAdminIds = new String[adminIdList.size()];
        adminIdList.toArray(createOrUpdateAdminIds);
        AdminVO queryAdminVO = new AdminVO();
        queryAdminVO.setAdminIds(createOrUpdateAdminIds);
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryAdminVO);
        ResultObjectVO resultObjectVO = feignAdminService.queryListByEntity(requestJsonVO.sign(),requestJsonVO);
        if(resultObjectVO.isSuccess())
        {
            List<AdminVO> adminVOS = (List<AdminVO>)resultObjectVO.formatDataList(AdminVO.class);
            if(CollectionUtils.isNotEmpty(adminVOS))
            {
                for(DictVO dictVO:list)
                {
                    for(AdminVO adminVO:adminVOS)
                    {
                        if(dictVO.getCreateAdminId()!=null&&dictVO.getCreateAdminId().equals(adminVO.getAdminId()))
                        {
                            dictVO.setCreateAdminName(adminVO.getUsername());
                        }
                        if(dictVO.getUpdateAdminId()!=null&&dictVO.getUpdateAdminId().equals(adminVO.getAdminId()))
                        {
                            dictVO.setUpdateAdminName(adminVO.getUsername());
                        }
                    }
                }
            }
        }
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/query/category/list",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryCategoryTreeByParentId(@RequestParam Long id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(id==null){
                id=-1L;
            }
            DictCategoryVO query = new DictCategoryVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            resultObjectVO = feignDictCategoryService.queryList(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    List<DictCategoryTreeVO> dictCategoryTreeVOS = resultObjectVO.formatDataList(DictCategoryTreeVO.class);
                    Set<String> appCodes = new HashSet<>();
                    for(DictCategoryTreeVO dictCategoryTreeVO:dictCategoryTreeVOS)
                    {
                        dictCategoryTreeVO.setOpen(false);
                        dictCategoryTreeVO.setIcon(null);
                        appCodes.add(dictCategoryTreeVO.getAppCode());
                    }

                    if(CollectionUtils.isNotEmpty(appCodes)){
                        AppVO appVO=new AppVO();
                        appVO.setCodes(new ArrayList(appCodes));
                        requestJsonVO = RequestJsonVOGenerator.generator(appCode,appVO);
                        resultObjectVO = feignAppService.queryListByCodes(requestJsonVO);
                        if(resultObjectVO.isSuccess()) {
                            List<AppVO> apps = resultObjectVO.formatDataList(AppVO.class);
                            if(CollectionUtils.isNotEmpty(apps)) {
                                for (DictCategoryVO dictCategoryVO : dictCategoryTreeVOS) {
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

                    resultObjectVO.setData(dictCategoryTreeVOS);
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



    /**
     * 查询树的子节点列表
     * @param queryParam
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/query/tree/child",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeChildById(HttpServletRequest request, DictTreeVO queryParam)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            DictPageInfo dictTreeVO = new DictPageInfo();
//            dictTreeVO.setAppCode(toucan.getShoppingPC().getAppCode());  //应用端调用需要传编码
            dictTreeVO.setPid(queryParam.getId());
            dictTreeVO.setCategoryId(queryParam.getCategoryId());
            dictTreeVO.setIsActive((short)1); //查询活动的版本
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),dictTreeVO);
            resultObjectVO = feignDictService.queryTreeChildByPid(requestJsonVO);
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody DictVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setCreateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignDictService.save(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
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
            Dict entity =new Dict();
            entity.setId(Long.parseLong(id));
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));


            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode,entity);
            resultObjectVO = feignDictService.deleteById(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 批量删除
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/delete/ids",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<Dict> dicts)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(dicts))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode,dicts);
            resultObjectVO = feignDictService.deleteByIds(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}

