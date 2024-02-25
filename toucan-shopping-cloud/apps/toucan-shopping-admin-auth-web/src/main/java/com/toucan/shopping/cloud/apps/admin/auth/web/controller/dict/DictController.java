package com.toucan.shopping.cloud.apps.admin.auth.web.controller.dict;


import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAppService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignDictCategoryService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignDictService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.modules.admin.auth.entity.DictCategory;
import com.toucan.shopping.modules.admin.auth.entity.DictCategoryApp;
import com.toucan.shopping.modules.admin.auth.page.DictCategoryPageInfo;
import com.toucan.shopping.modules.admin.auth.page.DictPageInfo;
import com.toucan.shopping.modules.admin.auth.page.DictTreeInfo;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.admin.auth.vo.DictCategoryTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.DictCategoryVO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        this.setDictCategoryApps(request,dictCategoryId);
        return "pages/dict/dict/add.html";
    }


    /**
     * 设置分类字典的应用
     * @param request
     * @param id
     * @throws Exception
     */
    private void setDictCategoryApps(HttpServletRequest request,Integer id) throws Exception {
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
                    //设置复选框选中状态
                    Object appsObject = request.getAttribute("apps");
                    if(appsObject!=null) {
                        List<AppVO> appVos = (List<AppVO>) appsObject;
                        if(!CollectionUtils.isEmpty(dictCategory.getDictCategoryApps()))
                        {
                            for(DictCategoryApp dictCategoryApp:dictCategory.getDictCategoryApps())
                            {
                                for(AppVO aa:appVos)
                                {
                                    if(dictCategoryApp.getAppCode().equals(aa.getCode()))
                                    {
                                        aa.setChecked(true);
                                    }
                                }
                            }
                        }
                    }
                    request.setAttribute("model",dictCategory);
                }
            }

        }
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
            return resultObjectVO;
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
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




}

