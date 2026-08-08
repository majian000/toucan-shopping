package com.toucan.shopping.cloud.apps.admin.controller.category;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.DictServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.CategoryServiceAPI;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.category.constant.CategoryDictConstant;
import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 类别控制器 - 页面控制器
 */
@Controller
public class CategoryPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private CategoryServiceAPI categoryServiceAPI;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private DictServiceAPI dictServiceAPI;




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/category/listPage",method = RequestMethod.GET)
    public String page(HttpServletRequest request) throws NoSuchAlgorithmException {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/category/listPage", functionServiceAPI);

        this.setCategoryDictList(request);

        return "pages/category/list.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/category/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request) throws NoSuchAlgorithmException {
        this.setCategoryDictList(request);
        return "pages/category/add.html";
    }



    private void setCategoryDictList(HttpServletRequest request) throws NoSuchAlgorithmException {
        //栏目字典
        DictVO queryDict=new DictVO();
        queryDict.setCategoryCode(CategoryDictConstant.CATEGORY_DICT_CATEGORY_CODE);
        queryDict.setCodes(new LinkedList<>());
        queryDict.getCodes().add(CategoryDictConstant.CATEGORY_DICT_TYPE_CODE);
        queryDict.setAppCode(toucan.getAppCode());
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryDict);
        ResultTypeObjectVO<List<DictVO>> resultObjectVO = dictServiceAPI.queryDictByCodesAndCategoryCode(requestJsonVO);
        if(resultObjectVO.isSuccess()) {
            if(!CollectionUtils.isEmpty(resultObjectVO.getData())){
                for(DictVO dictVO:resultObjectVO.getData()){
                    switch (dictVO.getCode()){
                        case CategoryDictConstant.CATEGORY_DICT_TYPE_CODE:
                            request.setAttribute("categoryTypeList",dictVO.getChildren());
                            break;
                    }
                }
            }
        }
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/category/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {

            this.setCategoryDictList(request);

            Category entity = new Category();
            entity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            ResultObjectVO resultObjectVO = categoryServiceAPI.findById(requestJsonVO);
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
                            resultObjectVO = categoryServiceAPI.findById(requestJsonVO);
                            if (resultObjectVO.isSuccess()) {
                                List<Category> parentAreaList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), Category.class);
                                if (!CollectionUtils.isEmpty(parentAreaList)) {
                                    categoryVO.setParentName(parentAreaList.get(0).getName());
                                }
                            }
                        }

                        List<String> selectTypes = new LinkedList<>();
                        if(StringUtils.isNotEmpty(categoryVO.getType())){
                            selectTypes.addAll(Arrays.asList(categoryVO.getType().split(",")));
                        }
                        request.setAttribute("selectTypes",selectTypes);

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

}
