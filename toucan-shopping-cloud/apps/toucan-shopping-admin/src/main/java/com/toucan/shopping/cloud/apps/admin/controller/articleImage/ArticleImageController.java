package com.toucan.shopping.cloud.apps.admin.controller.articleImage;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignArticleImageService;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.content.entity.ArticleImage;
import com.toucan.shopping.modules.content.page.ArticleImagePageInfo;
import com.toucan.shopping.modules.content.vo.ArticleImageVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文章图片管理
 */
@Controller
@RequestMapping("/articleImage")
public class ArticleImageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private FeignArticleImageService feignArticleImageService;

    @Autowired
    private FeignAreaService feignAreaService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private AdminServiceAPI adminServiceAPI;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/articleImage/listPage", functionServiceAPI);
        return "pages/articleImage/list.html";
    }



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, ArticleImagePageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignArticleImageService.queryListPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<ArticleImageVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),ArticleImageVO.class);


                    //查询创建人和修改人
                    List<String> adminIdList = new ArrayList<String>();
                    for(int i=0;i<list.size();i++)
                    {
                        ArticleImageVO bannerVO = list.get(i);
                        if(bannerVO.getCreateAdminId()!=null) {
                            adminIdList.add(bannerVO.getCreateAdminId());
                        }
                        if(bannerVO.getUpdateAdminId()!=null)
                        {
                            adminIdList.add(bannerVO.getUpdateAdminId());
                        }
                    }
                    String[] createOrUpdateAdminIds = new String[adminIdList.size()];
                    adminIdList.toArray(createOrUpdateAdminIds);
                    AdminVO queryAdminVO = new AdminVO();
                    queryAdminVO.setAdminIds(createOrUpdateAdminIds);
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryAdminVO);
                    resultObjectVO = adminServiceAPI.queryListByEntity(requestJsonVO.sign(),requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        List<AdminVO> adminVOS = (List<AdminVO>)resultObjectVO.formatDataList(AdminVO.class);
                        if(!CollectionUtils.isEmpty(adminVOS))
                        {
                            for(ArticleImageVO bannerVO:list)
                            {
                                for(AdminVO adminVO:adminVOS)
                                {
                                    if(bannerVO.getCreateAdminId()!=null&&bannerVO.getCreateAdminId().equals(adminVO.getAdminId()))
                                    {
                                        bannerVO.setCreateAdminName(adminVO.getUsername());
                                    }
                                    if(bannerVO.getUpdateAdminId()!=null&&bannerVO.getUpdateAdminId().equals(adminVO.getAdminId()))
                                    {
                                        bannerVO.setUpdateAdminName(adminVO.getUsername());
                                    }
                                }
                            }
                        }
                    }
                    for(ArticleImageVO articleImageVO:list)
                    {
                        if(articleImageVO.getImgPath()!=null) {
                            articleImageVO.setHttpImgPath(imageUploadService.getImageHttpPrefix() + articleImageVO.getImgPath());
                        }
                    }
                    if(tableVO.getCount()>0) {
                        tableVO.setData(list);
                    }
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("查询失败,请稍后重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
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
            ArticleImage articleImage =new ArticleImage();
            articleImage.setId(Long.parseLong(id));
            articleImage.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));

            //先查询出实体对象,后面删除文件服务器的资源
            resultObjectVO = feignArticleImageService.deleteById(RequestJsonVOGenerator.generator(toucan.getAppCode(),articleImage));
        }catch(Exception e)
        {
            resultObjectVO.setMsg("删除失败,请稍后重试");
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
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<ArticleImageVO> articleImageVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(articleImageVOS))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            String entityJson = JSONObject.toJSONString(articleImageVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignArticleImageService.deleteByIds(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}

