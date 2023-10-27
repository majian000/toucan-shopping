package com.toucan.shopping.cloud.apps.admin.controller.seller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerLoginHistoryService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopBannerService;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.seller.entity.SellerLoginHistory;
import com.toucan.shopping.modules.seller.page.SellerLoginHistoryPageInfo;
import com.toucan.shopping.modules.seller.page.ShopBannerPageInfo;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
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

/**
 * 卖家轮播图
 */
@Controller
@RequestMapping("/seller/shopBanner")
public class ShopBannerController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignShopBannerService feignShopBannerService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignAdminService feignAdminService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/seller/shopBanner/listPage",feignFunctionService);
        return "pages/seller/shopBanner/list.html";
    }



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, ShopBannerPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignShopBannerService.queryListPage(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                ShopBannerPageInfo shopBannerPageInfo = resultObjectVO.formatData(ShopBannerPageInfo.class);
                if(shopBannerPageInfo!=null)
                {
                    tableVO.setCount(shopBannerPageInfo.getTotal());
                    if(CollectionUtils.isNotEmpty(shopBannerPageInfo.getList()))
                    {
                        for(ShopBannerVO shopShopBannerVO:shopBannerPageInfo.getList())
                        {
                            if(shopShopBannerVO.getImgPath()!=null) {
                                shopShopBannerVO.setHttpImgPath(imageUploadService.getImageHttpPrefix()+shopShopBannerVO.getImgPath());
                            }
                        }
                    }

                    for (ShopBannerVO bannerVO : shopBannerPageInfo.getList()) {
                        bannerVO.setCreaterName(bannerVO.getCreaterId());
                        bannerVO.setUpdaterName(bannerVO.getUpdaterId());
                    }


                    //查询创建管理员和修改管理员
                    if(CollectionUtils.isNotEmpty(shopBannerPageInfo.getList())) {
                        List<String> adminIdList = new ArrayList<String>();
                        for (int i = 0; i < shopBannerPageInfo.getList().size(); i++) {
                            ShopBannerVO shopBannerVO = shopBannerPageInfo.getList().get(i);
                            if (shopBannerVO.getCreaterId() != null) {
                                if(shopBannerVO.getCreaterId().startsWith(AuthHeaderUtil.getAdminPrefix())) {
                                    adminIdList.add(shopBannerVO.getCreaterId().substring(AuthHeaderUtil.getAdminPrefix().length(),shopBannerVO.getCreaterId().length()));
                                }
                            }
                            if (shopBannerVO.getUpdaterId() != null) {
                                if(shopBannerVO.getUpdaterId().startsWith(AuthHeaderUtil.getAdminPrefix())) {
                                    adminIdList.add(shopBannerVO.getUpdaterId().substring(AuthHeaderUtil.getAdminPrefix().length(),shopBannerVO.getUpdaterId().length()));
                                }
                            }
                        }
                        String[] createOrUpdateAdminIds = new String[adminIdList.size()];
                        adminIdList.toArray(createOrUpdateAdminIds);
                        AdminVO queryAdminVO = new AdminVO();
                        queryAdminVO.setAdminIds(createOrUpdateAdminIds);
                        requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAdminVO);
                        resultObjectVO = feignAdminService.queryListByEntity(requestJsonVO.sign(), requestJsonVO);
                        if (resultObjectVO.isSuccess()) {
                            List<AdminVO> adminVOS = resultObjectVO.formatDataList(AdminVO.class);
                            if (!org.springframework.util.CollectionUtils.isEmpty(adminVOS)) {
                                for (ShopBannerVO bannerVO : shopBannerPageInfo.getList()) {
                                    for (AdminVO adminVO : adminVOS) {
                                        if (bannerVO.getCreaterId() != null && bannerVO.getCreaterId().equals(AuthHeaderUtil.getAdminPrefix()+adminVO.getAdminId())) {
                                            bannerVO.setCreaterName(adminVO.getUsername());
                                        }
                                        if (bannerVO.getUpdaterId() != null && bannerVO.getUpdaterId().equals(AuthHeaderUtil.getAdminPrefix()+adminVO.getAdminId())) {
                                            bannerVO.setUpdaterName(adminVO.getUsername());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    tableVO.setData(shopBannerPageInfo.getList());
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("查询失败,请稍后请重试");
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/delete/{id}/{shopId}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(HttpServletRequest request,  @PathVariable String id,  @PathVariable String shopId)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            ShopBannerVO entity =new ShopBannerVO();
            entity.setId(Long.parseLong(id));
            entity.setShopId(Long.parseLong(shopId));

            String entityJson = JSONObject.toJSONString(entity);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(toucan.getAppCode());
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignShopBannerService.deleteByIdForAdmin(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 编辑页
     * @param request
     * @param id
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            ShopBannerVO banner = new ShopBannerVO();
            banner.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, banner);
            ResultObjectVO resultObjectVO = feignShopBannerService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    banner = resultObjectVO.formatData(ShopBannerVO.class);
                    if(banner!=null)
                    {
                        banner.setHttpImgPath(imageUploadService.getImageHttpPrefix() + banner.getImgPath());
                        if(banner.getStartShowDate()!=null) {
                            banner.setStartShowDateString(DateUtils.format(banner.getStartShowDate(), DateUtils.FORMATTER_SS.get()));
                        }

                        if(banner.getEndShowDate()!=null) {
                            banner.setEndShowDateString(DateUtils.format(banner.getEndShowDate(), DateUtils.FORMATTER_SS.get()));
                        }

                        request.setAttribute("model",banner);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/seller/shopBanner/edit.html";
    }


    /**
     * 查看详情页
     * @param request
     * @param id
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/detailPage/{id}",method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            ShopBannerVO banner = new ShopBannerVO();
            banner.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, banner);
            ResultObjectVO resultObjectVO = feignShopBannerService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    banner = resultObjectVO.formatData(ShopBannerVO.class);
                    if(banner!=null)
                    {
                        banner.setHttpImgPath(imageUploadService.getImageHttpPrefix() + banner.getImgPath());
                        if(banner.getStartShowDate()!=null) {
                            banner.setStartShowDateString(DateUtils.format(banner.getStartShowDate(), DateUtils.FORMATTER_SS.get()));
                        }

                        if(banner.getEndShowDate()!=null) {
                            banner.setEndShowDateString(DateUtils.format(banner.getEndShowDate(), DateUtils.FORMATTER_SS.get()));
                        }

                        request.setAttribute("model",banner);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/seller/shopBanner/detail.html";
    }


    /**
     * 添加轮播图
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value="/update")
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request, @RequestParam(value="bannerImgFile",required=false) MultipartFile bannerImgFile, ShopBannerVO shopBannerVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try {

            if(bannerImgFile==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请上传图片");
                return resultObjectVO;
            }

            if(bannerImgFile!=null&&bannerImgFile.getBytes()!=null&&bannerImgFile.getBytes().length>0) {
                if (!ImageUtils.isImage(bannerImgFile.getOriginalFilename())) {
                    resultObjectVO.setCode(ResultObjectVO.FAILD - 4);
                    resultObjectVO.setMsg("请上传图片格式(.jpg|.jpeg|.png|.gif|bmp)");
                    return resultObjectVO;
                }
            }

            if(StringUtils.isEmpty(shopBannerVO.getTitle()))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("标题不能为空");
                return resultObjectVO;
            }

            if(StringUtils.isEmpty(shopBannerVO.getPosition()))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("显示位置不能为空");
                return resultObjectVO;
            }

            shopBannerVO.setStartShowDate(DateUtils.FORMATTER_SS.get().parse(shopBannerVO.getStartShowDateString()));
            shopBannerVO.setEndShowDate(DateUtils.FORMATTER_SS.get().parse(shopBannerVO.getEndShowDateString()));


            if(bannerImgFile!=null&&bannerImgFile.getBytes()!=null&&bannerImgFile.getBytes().length>0) {
                ShopBannerVO banner = new ShopBannerVO();
                banner.setId(shopBannerVO.getId());
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, banner);
                resultObjectVO = feignShopBannerService.findById(requestJsonVO);
                if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                    if (resultObjectVO.getData() != null) {
                        banner = resultObjectVO.formatData(ShopBannerVO.class);

                        if(banner==null)
                        {
                            resultObjectVO.setCode(ResultObjectVO.FAILD);
                            resultObjectVO.setMsg("没有找到该轮播图");
                            return resultObjectVO;
                        }
                        //LOGO上传
                        imageUploadService.deleteFile(banner.getImgPath());

                        String logoImgExt = ImageUtils.getImageExt(bannerImgFile.getOriginalFilename());
                        if (logoImgExt.indexOf(".") != -1) {
                            logoImgExt = logoImgExt.substring(logoImgExt.indexOf(".") + 1, logoImgExt.length());
                        }
                        String logoImgFilePath = imageUploadService.uploadFile(bannerImgFile.getBytes(), logoImgExt);
                        shopBannerVO.setImgPath(logoImgFilePath);

                    }

                }
            }

            shopBannerVO.setUpdateDate(new Date());
            shopBannerVO.setUpdaterId(AuthHeaderUtil.getAdminIdAndPrefix(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, shopBannerVO);
            resultObjectVO = feignShopBannerService.update(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("添加失败,请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

}

