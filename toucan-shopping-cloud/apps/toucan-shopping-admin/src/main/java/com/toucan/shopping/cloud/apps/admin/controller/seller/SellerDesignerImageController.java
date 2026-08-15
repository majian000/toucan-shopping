package com.toucan.shopping.cloud.apps.admin.controller.seller;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.seller.api.SellerDesignerImageServiceAPI;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.seller.page.SellerDesignerImagePageInfo;
import com.toucan.shopping.modules.seller.vo.SellerDesignerImageDetailVO;
import com.toucan.shopping.modules.seller.vo.SellerDesignerImageVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * 装修图片
 */
@RestController
@RequestMapping("/seller/designer/image")
public class SellerDesignerImageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private SellerDesignerImageServiceAPI sellerDesignerImageService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private AdminServiceAPI adminServiceAPI;



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:seller:designerImage:list"})
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public TableVO list(HttpServletRequest request, @RequestBody SellerDesignerImagePageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = sellerDesignerImageService.queryListPage(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                SellerDesignerImagePageInfo shopBannerPageInfo = resultObjectVO.formatData(SellerDesignerImagePageInfo.class);
                if(shopBannerPageInfo!=null)
                {
                    tableVO.setCount(shopBannerPageInfo.getTotal());
                    if(CollectionUtils.isNotEmpty(shopBannerPageInfo.getList()))
                    {
                        for(SellerDesignerImageVO shopSellerDesignerImageVO:shopBannerPageInfo.getList())
                        {
                            if(shopSellerDesignerImageVO.getImgPath()!=null) {
                                shopSellerDesignerImageVO.setHttpImgPath(imageUploadService.getImageHttpPrefix()+shopSellerDesignerImageVO.getImgPath());
                            }
                        }
                    }

                    for (SellerDesignerImageVO sellerDesignerImageVO : shopBannerPageInfo.getList()) {
                        if(sellerDesignerImageVO.getCreaterId()!=null&&!sellerDesignerImageVO.getCreaterId().startsWith(AuthHeaderUtil.getAdminPrefix())) {
                            sellerDesignerImageVO.setCreaterName("掌柜ID:"+sellerDesignerImageVO.getCreaterId());
                        }
                        if(sellerDesignerImageVO.getUpdaterId()!=null&&!sellerDesignerImageVO.getUpdaterId().startsWith(AuthHeaderUtil.getAdminPrefix())) {
                            sellerDesignerImageVO.setUpdaterName("掌柜ID:"+sellerDesignerImageVO.getUpdaterId());
                        }
                    }


                    //查询创建管理员和修改管理员
                    if(CollectionUtils.isNotEmpty(shopBannerPageInfo.getList())) {
                        List<String> adminIdList = new ArrayList<String>();
                        for (int i = 0; i < shopBannerPageInfo.getList().size(); i++) {
                            SellerDesignerImageVO shopBannerVO = shopBannerPageInfo.getList().get(i);
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
                        resultObjectVO = adminServiceAPI.queryListByEntity( requestJsonVO);
                        if (resultObjectVO.isSuccess()) {
                            List<AdminVO> adminVOS = resultObjectVO.formatDataList(AdminVO.class);
                            if (!CollectionUtils.isEmpty(adminVOS)) {
                                for (SellerDesignerImageVO bannerVO : shopBannerPageInfo.getList()) {
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
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:seller:designerImage:delete"})
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResultObjectVO deleteById(@RequestBody SellerDesignerImageVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(entity.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            String entityJson = JSONObject.toJSONString(entity);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(toucan.getAppCode());
            requestVo.setEntityJson(entityJson);
            resultObjectVO = sellerDesignerImageService.deleteByIdForAdmin(requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 添加轮播图
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:seller:designerImage:update"})
    @RequestMapping(value="/update")
    public ResultObjectVO update(HttpServletRequest request, @RequestBody SellerDesignerImageVO sellerDesignerImageVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try {

            if(StringUtils.isEmpty(sellerDesignerImageVO.getTitle()))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("标题不能为空");
                return resultObjectVO;
            }



            if(StringUtils.isNotEmpty(sellerDesignerImageVO.getImgBase64())) {
                // 查询旧图片并删除
                SellerDesignerImageVO banner = new SellerDesignerImageVO();
                banner.setId(sellerDesignerImageVO.getId());
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, banner);
                resultObjectVO = sellerDesignerImageService.findById(requestJsonVO);
                if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                    if (resultObjectVO.getData() != null) {
                        banner = resultObjectVO.formatData(SellerDesignerImageVO.class);
                        if (banner != null && StringUtils.isNotEmpty(banner.getImgPath())) {
                            imageUploadService.deleteFile(banner.getImgPath());
                        }
                    }
                }
                // 上传新图片
                sellerDesignerImageVO.setImgPath(imageUploadService.uploadBase64(sellerDesignerImageVO.getImgBase64()));
            }

            sellerDesignerImageVO.setUpdateDate(new Date());
            sellerDesignerImageVO.setUpdaterId(AuthHeaderUtil.getAdminIdAndPrefix(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, sellerDesignerImageVO);
            resultObjectVO = sellerDesignerImageService.update(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("添加失败,请稍后重试");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 根据ID查询（编辑回显用，含base64图片数据）
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:seller:designerImage:list"})
    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    public ResultObjectVO queryById(HttpServletRequest request, @RequestBody SellerDesignerImageVO sellerDesignerImageVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(sellerDesignerImageVO.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, sellerDesignerImageVO);
            ResultObjectVO detailResult = sellerDesignerImageService.findById(requestJsonVO);
            if(detailResult.isSuccess())
            {
                if(detailResult.getData() == null)
                {
                    resultObjectVO.setMsg("装修图片不存在");
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    return resultObjectVO;
                }
                SellerDesignerImageVO vo = detailResult.formatData(SellerDesignerImageVO.class);
                if(StringUtils.isNotEmpty(vo.getImgPath()))
                {
                    vo.setHttpImgPath(imageUploadService.getImageHttpPrefix() + vo.getImgPath());
                    // 下载文件并转base64
                    byte[] fileBytes = imageUploadService.downloadFile(vo.getImgPath());
                    if(fileBytes != null && fileBytes.length > 0)
                    {
                        String path = vo.getImgPath();
                        String ext = "jpg";
                        if(path.contains("."))
                        {
                            ext = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
                        }
                        String mime;
                        switch (ext) {
                            case "png": mime = "image/png"; break;
                            case "gif": mime = "image/gif"; break;
                            case "bmp": mime = "image/bmp"; break;
                            case "jpeg": mime = "image/jpeg"; break;
                            case "jpg": mime = "image/jpeg"; break;
                            default: mime = "image/jpeg"; break;
                        }
                        vo.setImgBase64("data:" + mime + ";base64," + Base64.getEncoder().encodeToString(fileBytes));
                    }
                }
                resultObjectVO.setData(vo);
            }else
            {
                resultObjectVO.setMsg(detailResult.getMsg());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 查看详情
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:seller:designerImage:list"})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultObjectVO detail(HttpServletRequest request, @RequestBody SellerDesignerImageVO sellerDesignerImageVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(sellerDesignerImageVO.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, sellerDesignerImageVO);
            ResultObjectVO detailResult = sellerDesignerImageService.findById(requestJsonVO);
            if(detailResult.isSuccess())
            {
                if(detailResult.getData() == null)
                {
                    resultObjectVO.setMsg("装修图片不存在");
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    return resultObjectVO;
                }
                SellerDesignerImageVO vo = detailResult.formatData(SellerDesignerImageVO.class);
                if(StringUtils.isNotEmpty(vo.getImgPath()))
                {
                    vo.setHttpImgPath(imageUploadService.getImageHttpPrefix() + vo.getImgPath());
                }
                fillCreaterAndUpdaterName(vo);
                SellerDesignerImageDetailVO detailVO = new SellerDesignerImageDetailVO();
                detailVO.setBasicInfo(vo);
                resultObjectVO.setData(detailVO);
            }else
            {
                resultObjectVO.setMsg(detailResult.getMsg());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 填充创建人/修改人名称
     */
    private void fillCreaterAndUpdaterName(SellerDesignerImageVO imageVO)
    {
        if(imageVO == null)
        {
            return;
        }
        // 非管理员(掌柜ID)直接显示ID前缀
        if(imageVO.getCreaterId() != null && !imageVO.getCreaterId().startsWith(AuthHeaderUtil.getAdminPrefix()))
        {
            imageVO.setCreaterName("掌柜ID:"+imageVO.getCreaterId());
        }
        if(imageVO.getUpdaterId() != null && !imageVO.getUpdaterId().startsWith(AuthHeaderUtil.getAdminPrefix()))
        {
            imageVO.setUpdaterName("掌柜ID:"+imageVO.getUpdaterId());
        }
        // 管理员ID查询用户名
        List<String> adminIdList = new ArrayList<String>();
        if(imageVO.getCreaterId() != null && imageVO.getCreaterId().startsWith(AuthHeaderUtil.getAdminPrefix()))
        {
            adminIdList.add(imageVO.getCreaterId().substring(AuthHeaderUtil.getAdminPrefix().length()));
        }
        if(imageVO.getUpdaterId() != null && imageVO.getUpdaterId().startsWith(AuthHeaderUtil.getAdminPrefix()))
        {
            adminIdList.add(imageVO.getUpdaterId().substring(AuthHeaderUtil.getAdminPrefix().length()));
        }
        if(adminIdList.isEmpty())
        {
            return;
        }
        try {
            String[] adminIds = adminIdList.toArray(new String[0]);
            AdminVO queryAdminVO = new AdminVO();
            queryAdminVO.setAdminIds(adminIds);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAdminVO);
            ResultObjectVO resultObjectVO = adminServiceAPI.queryListByEntity(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<AdminVO> adminVOS = resultObjectVO.formatDataList(AdminVO.class);
                if(adminVOS != null)
                {
                    for(AdminVO adminVO : adminVOS)
                    {
                        if(imageVO.getCreaterId() != null && imageVO.getCreaterId().equals(AuthHeaderUtil.getAdminPrefix()+adminVO.getAdminId()))
                        {
                            imageVO.setCreaterName(adminVO.getUsername());
                        }
                        if(imageVO.getUpdaterId() != null && imageVO.getUpdaterId().equals(AuthHeaderUtil.getAdminPrefix()+adminVO.getAdminId()))
                        {
                            imageVO.setUpdaterName(adminVO.getUsername());
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }

}
