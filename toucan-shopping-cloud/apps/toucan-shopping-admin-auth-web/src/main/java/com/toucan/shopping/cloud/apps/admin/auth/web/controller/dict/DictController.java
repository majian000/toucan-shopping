package com.toucan.shopping.cloud.apps.admin.auth.web.controller.dict;


import com.toucan.shopping.cloud.admin.auth.api.feign.service.*;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.entity.DictCategory;
import com.toucan.shopping.modules.admin.auth.entity.DictCategoryApp;
import com.toucan.shopping.modules.admin.auth.page.DictCategoryPageInfo;
import com.toucan.shopping.modules.admin.auth.page.DictPageInfo;
import com.toucan.shopping.modules.admin.auth.page.DictTreeInfo;
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
import java.util.stream.Collectors;

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
                requestJsonVO = RequestJsonVOGenerator.generator(appCode,dictCategoryModel);
                resultObjectVO = feignDictCategoryService.queryCategoryAppListByCategoryId(requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    List<DictCategoryApp> dictCategoryApps = resultObjectVO.formatDataList(DictCategoryApp.class);
                    resultObjectVO.setData(null);
                    if(CollectionUtils.isNotEmpty(dictCategoryApps))
                    {
                        List<String> appCodes = dictCategoryApps.stream().map(DictCategoryApp::getAppCode).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(appCodes)){
                            AppVO appVO=new AppVO();
                            appVO.setCodes(appCodes);
                            requestJsonVO = RequestJsonVOGenerator.generator(appCode,appVO);
                            resultObjectVO = feignAppService.queryListByCodes(requestJsonVO);
                            request.setAttribute("apps",resultObjectVO.formatDataList(AppVO.class));
                        }
                    }else{
                        request.setAttribute("apps",new LinkedList<>());
                    }
                }
            }else {
                request.setAttribute("dictCategoryModel", new DictCategoryVO());
            }
        }

        return "pages/dict/dict/add.html";
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
                            if(StringUtils.isNotEmpty(dictTreeVO.getAppCodesStr())) {
                                if(dictTreeVO.getAppCodesStr().indexOf(",")!=-1)
                                {
                                    String[] appCodeArray = dictTreeVO.getAppCodesStr().split(",");
                                    for(String ac:appCodeArray) {
                                        appCodes.add(ac);
                                    }
                                }else {
                                    appCodes.add(dictTreeVO.getAppCodesStr());
                                }
                            }
                        }
                        this.setAdminNames(adminIdList, dictTreeVOS);

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
     * 设置关联应用
     * @param appCodes
     * @throws Exception
     */
    private void setAppNames(List<String> appCodes,List<DictTreeVO> list) throws Exception{

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
                    for(DictCategoryTreeVO dictCategoryTreeVO:dictCategoryTreeVOS)
                    {
                        dictCategoryTreeVO.setOpen(false);
                        dictCategoryTreeVO.setIcon(null);
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
     * @param areaTreeVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/query/tree/child",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeChildById(HttpServletRequest request, DictTreeVO areaTreeVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            DictTreeVO dictTreeVO = new DictTreeVO();
            if(StringUtils.isNotEmpty(areaTreeVO.getAppCodesStr()))
            {
                dictTreeVO.setAppCodes(Arrays.asList(areaTreeVO.getAppCodesStr().split(",")));
            }

            dictTreeVO.setPid(areaTreeVO.getId());
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


}

