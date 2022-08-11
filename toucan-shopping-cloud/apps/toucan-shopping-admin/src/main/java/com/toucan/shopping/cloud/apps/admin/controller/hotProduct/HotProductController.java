package com.toucan.shopping.cloud.apps.admin.controller.hotProduct;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignAreaService;
import com.toucan.shopping.cloud.content.api.feign.service.FeignHotProductService;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.area.vo.AreaTreeVO;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.column.page.HotProductPageInfo;
import com.toucan.shopping.modules.column.vo.*;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 热门商品
 */
@Controller
@RequestMapping("/hotProduct")
public class HotProductController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignHotProductService feignHotProductService;

    @Autowired
    private FeignAdminService feignAdminService;


    @Autowired
    private FeignAreaService feignAreaService;


    @Autowired
    private ImageUploadService imageUploadService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/hotProduct/listPage",feignFunctionService);


        return "pages/hotProduct/list.html";
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {
        return "pages/hotProduct/add.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            HotProductVO hotProductVO = new HotProductVO();
            hotProductVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, hotProductVO);
            ResultObjectVO resultObjectVO = feignHotProductService.findById(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                hotProductVO = resultObjectVO.formatData(HotProductVO.class);
                if(hotProductVO!=null)
                {
                    hotProductVO.setHttpImgPath(imageUploadService.getImageHttpPrefix()+hotProductVO.getImgPath());
                }
                request.setAttribute("model", hotProductVO);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/hotProduct/edit.html";
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/showPage/{id}",method = RequestMethod.GET)
    public String showPage(HttpServletRequest request,@PathVariable Long id)
    {
        request.setAttribute("id",id);
        return "pages/hotProduct/show.html";
    }


    /**
     * 保存
     * @param hotProductVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request,@RequestBody HotProductVO hotProductVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            hotProductVO.setAppCode(toucan.getShoppingPC().getAppCode());
            hotProductVO.setCreateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, hotProductVO);
            resultObjectVO = feignHotProductService.save(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请稍后重试");
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request, @RequestBody HotProductVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setAppCode(toucan.getShoppingPC().getAppCode());
            entity.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
//            resultObjectVO = feignHotProductService.update(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/findById",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(HttpServletRequest request, @RequestBody HotProductVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setAppCode(toucan.getShoppingPC().getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
//            resultObjectVO = feignHotProductService.findById(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
            }
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, HotProductPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            pageInfo.setAppCode(toucan.getShoppingPC().getAppCode());

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignHotProductService.queryListPage(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<HotProductVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),HotProductVO.class);

                    //查询创建人和修改人
                    List<String> adminIdList = new ArrayList<String>();
                    for(int i=0;i<list.size();i++)
                    {
                        HotProductVO columnVO = list.get(i);
                        if(columnVO.getCreateAdminId()!=null) {
                            adminIdList.add(columnVO.getCreateAdminId());
                        }
                        if(columnVO.getUpdateAdminId()!=null)
                        {
                            adminIdList.add(columnVO.getUpdateAdminId());
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
                            for(HotProductVO hotProductVO:list)
                            {
                                for(AdminVO adminVO:adminVOS)
                                {
                                    if(hotProductVO.getCreateAdminId()!=null&&hotProductVO.getCreateAdminId().equals(adminVO.getAdminId()))
                                    {
                                        hotProductVO.setCreateAdminName(adminVO.getUsername());
                                    }
                                    if(hotProductVO.getUpdateAdminId()!=null&&hotProductVO.getUpdateAdminId().equals(adminVO.getAdminId()))
                                    {
                                        hotProductVO.setUpdateAdminName(adminVO.getUsername());
                                    }
                                }

                                if(StringUtils.isNotEmpty(hotProductVO.getImgPath())) {
                                    hotProductVO.setHttpImgPath(imageUploadService.getImageHttpPrefix() + hotProductVO.getImgPath());
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






    /**
     * 删除
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
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
            HotProductVO columnVO =new HotProductVO();
            columnVO.setId(Long.parseLong(id));

            String entityJson = JSONObject.toJSONString(columnVO);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);

//            resultObjectVO = feignHotProductService.deleteById(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping("/upload/img")
    @ResponseBody
    public ResultObjectVO  uploadImg(@RequestParam("file") MultipartFile file)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(0);
        try{
            String fileName = file.getOriginalFilename();
            String fileExt = "jpg";
            if(StringUtils.isNotEmpty(fileName)&&fileName.indexOf(".")!=-1)
            {
                fileExt = fileName.substring(fileName.lastIndexOf(".")+1);

            }
            if(!ImageUtils.isImage(file.getOriginalFilename()))
            {
                throw new RuntimeException("只能上传JPG、JPEG、PNG、GIF、BMP格式");
            }
            String groupPath = imageUploadService.uploadFile(file.getBytes(),fileExt);

            if(StringUtils.isEmpty(groupPath))
            {
                throw new RuntimeException("上传失败");
            }
            HotProductVO hotProductVO = new HotProductVO();
            hotProductVO.setImgPath(groupPath);
            hotProductVO.setHttpImgPath(imageUploadService.getImageHttpPrefix()+groupPath);
            resultObjectVO.setData(hotProductVO);
        }catch (Exception e)
        {
            resultObjectVO.setCode(1);
            resultObjectVO.setMsg("上传失败");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }


}

