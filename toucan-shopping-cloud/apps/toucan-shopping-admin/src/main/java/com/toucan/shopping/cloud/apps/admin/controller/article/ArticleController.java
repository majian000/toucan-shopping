package com.toucan.shopping.cloud.apps.admin.controller.article;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAppService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignDictService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.content.api.feign.service.FeignArticleService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignColumnService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignColumnTypeService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductService;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.column.constant.ColumnDictConstant;
import com.toucan.shopping.modules.column.entity.Column;
import com.toucan.shopping.modules.column.page.ColumnPageInfo;
import com.toucan.shopping.modules.column.vo.ColumnTreeVO;
import com.toucan.shopping.modules.column.vo.ColumnTypeTreeVO;
import com.toucan.shopping.modules.column.vo.ColumnTypeVO;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AlphabetNumberUtils;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.content.page.ArticlePageInfo;
import com.toucan.shopping.modules.content.vo.ArticleVO;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文章
 */
@Controller
@RequestMapping("/article")
public class ArticleController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignColumnService feignColumnService;

    @Autowired
    private FeignArticleService feignArticleService;

    @Autowired
    private FeignAdminService feignAdminService;

    @Autowired
    private ImageUploadService imageUploadService;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request){
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/article/listPage",feignFunctionService);
        return "pages/article/list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request,@RequestParam Long columnId) throws NoSuchAlgorithmException {
        ColumnVO queryColumnVO= new ColumnVO();
        queryColumnVO.setId(columnId);
        ResultTypeObjectVO<ColumnVO> resultTypeObjectVO = feignColumnService.findById(RequestJsonVOGenerator.generator(toucan.getAppCode(),queryColumnVO));
        if(resultTypeObjectVO.isSuccess()){
            if(resultTypeObjectVO.getData()!=null){
                request.setAttribute("columnId",resultTypeObjectVO.getData().getId());
                request.setAttribute("columnName",resultTypeObjectVO.getData().getTitle());
            }
        }

        ResultTypeObjectVO<Long> resultMaxSort = feignArticleService.queryMaxSort(RequestJsonVOGenerator.generator(toucan.getAppCode(),columnId));
        request.setAttribute("maxSort",resultMaxSort.getData()+1);
        return "pages/article/add.html";
    }



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, ArticlePageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignArticleService.queryListPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<ArticleVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),ArticleVO.class);

                    //查询创建人和修改人
                    List<String> adminIdList = new ArrayList<String>();
                    for(int i=0;i<list.size();i++)
                    {
                        ArticleVO articleVO = list.get(i);
                        if(StringUtils.isNotEmpty(articleVO.getCoverImgUrl())){
                            articleVO.setHttpCoverImgUrl(imageUploadService.getImageHttpPrefix()+articleVO.getCoverImgUrl());
                        }
                        if(articleVO.getCreateAdminId()!=null) {
                            adminIdList.add(articleVO.getCreateAdminId());
                        }
                        if(articleVO.getUpdateAdminId()!=null)
                        {
                            adminIdList.add(articleVO.getUpdateAdminId());
                        }
                    }
                    String[] createOrUpdateAdminIds = new String[adminIdList.size()];
                    adminIdList.toArray(createOrUpdateAdminIds);
                    AdminVO queryAdminVO = new AdminVO();
                    queryAdminVO.setAdminIds(createOrUpdateAdminIds);
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryAdminVO);
                    resultObjectVO = feignAdminService.queryListByEntity(requestJsonVO.sign(),requestJsonVO);
                    if(resultObjectVO.isSuccess())
                    {
                        List<AdminVO> adminVOS = (List<AdminVO>)resultObjectVO.formatDataList(AdminVO.class);
                        if(!CollectionUtils.isEmpty(adminVOS))
                        {
                            for(ArticleVO articleVO:list)
                            {
                                for(AdminVO adminVO:adminVOS)
                                {
                                    if(articleVO.getCreateAdminId()!=null&&articleVO.getCreateAdminId().equals(adminVO.getAdminId()))
                                    {
                                        articleVO.setCreateAdminName(adminVO.getUsername());
                                    }
                                    if(articleVO.getUpdateAdminId()!=null&&articleVO.getUpdateAdminId().equals(adminVO.getAdminId()))
                                    {
                                        articleVO.setUpdateAdminName(adminVO.getUsername());
                                    }
                                }
                            }
                        }
                    }

                    if(tableVO.getCount()>0) {
                        tableVO.setData(list);
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




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/query/column/tree/pid",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryColumnTreeByParentId(@RequestParam Long id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(id==null)
            {
                id=-1L;
            }
            ColumnVO query = new ColumnVO();
            query.setPid(id);
            query.setAppCode(toucan.getShoppingPC().getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,query);
            resultObjectVO = feignColumnService.queryListByPid(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    List<ColumnTreeVO> columnTreeVOS = resultObjectVO.formatDataList(ColumnTreeVO.class);
                    for(ColumnTreeVO columnTreeVO:columnTreeVOS)
                    {
                        columnTreeVO.setName("["+columnTreeVO.getColumnTypeName()+"]"+columnTreeVO.getTitle());
                        columnTreeVO.setOpen(false);
                    }
                    resultObjectVO.setData(columnTreeVOS);
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
     * 保存
     * @param articleVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request,@RequestBody ArticleVO articleVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(articleVO.getColumnId()==null){
                resultObjectVO.setMsg("栏目不能为空");
                resultObjectVO.setCode(TableVO.FAILD);
                return resultObjectVO;
            }

            articleVO.setAppCode(toucan.getShoppingPC().getAppCode());
            articleVO.setCreateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, articleVO);
            resultObjectVO = feignArticleService.save(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请稍后重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping("/upload/img")
    @ResponseBody
    public ResultObjectVO  uploadImg(@RequestParam("file") MultipartFile file)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(0);
        try{
            String fileName = file.getOriginalFilename();
            if(!ImageUtils.isImage(fileName)){
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("上传图片只支持("+ImageUtils.imageExtScope.stream().collect(Collectors.joining("、"))+")");
                return resultObjectVO;
            }
            String fileExt = "jpg";
            if(StringUtils.isNotEmpty(fileName)&&fileName.indexOf(".")!=-1)
            {
                fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
            }
            String groupPath = imageUploadService.uploadFile(file.getBytes(),fileExt);

            if(StringUtils.isEmpty(groupPath))
            {
                throw new RuntimeException("上传失败");
            }
            ArticleVO articleVO = new ArticleVO();
            articleVO.setCoverImgUrl(groupPath);
            articleVO.setHttpCoverImgUrl(imageUploadService.getImageHttpPrefix()+groupPath);
            resultObjectVO.setData(articleVO);
        }catch (Exception e)
        {
            resultObjectVO.setCode(1);
            resultObjectVO.setMsg("上传失败");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }

}

