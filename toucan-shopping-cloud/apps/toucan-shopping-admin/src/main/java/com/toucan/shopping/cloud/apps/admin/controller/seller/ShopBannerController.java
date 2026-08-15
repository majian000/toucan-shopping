package com.toucan.shopping.cloud.apps.admin.controller.seller;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.seller.api.ShopBannerServiceAPI;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.seller.page.ShopBannerPageInfo;
import com.toucan.shopping.modules.seller.vo.ShopBannerDetailVO;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
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
 * 卖家轮播图
 */
@RestController
@RequestMapping("/seller/shopBanner")
public class ShopBannerController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ShopBannerServiceAPI shopBannerService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private AdminServiceAPI adminServiceAPI;



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:seller:shopBanner:list"})
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public TableVO list(HttpServletRequest request, @RequestBody ShopBannerPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = shopBannerService.queryListPage(requestJsonVO);
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
                        if(!bannerVO.getCreaterId().startsWith(AuthHeaderUtil.getAdminPrefix())) {
                            bannerVO.setCreaterName("掌柜ID:"+bannerVO.getCreaterId());
                        }
                        if(StringUtils.isNotEmpty(bannerVO.getUpdaterId())&&!bannerVO.getUpdaterId().startsWith(AuthHeaderUtil.getAdminPrefix())) {
                            bannerVO.setUpdaterName("掌柜ID:"+bannerVO.getUpdaterId());
                        }
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
                        resultObjectVO = adminServiceAPI.queryListByEntity( requestJsonVO);
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
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:seller:shop:shopBanner:delete"})
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResultObjectVO deleteById(@RequestBody ShopBannerVO entity)
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
            resultObjectVO = shopBannerService.deleteByIdForAdmin(requestVo);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:seller:shopBanner:update"})
    @RequestMapping(value="/update")
    public ResultObjectVO update(HttpServletRequest request, @RequestBody ShopBannerVO shopBannerVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try {

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


            if(StringUtils.isNotEmpty(shopBannerVO.getImgBase64())) {
                // 查询旧图片并删除
                ShopBannerVO banner = new ShopBannerVO();
                banner.setId(shopBannerVO.getId());
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, banner);
                resultObjectVO = shopBannerService.findById(requestJsonVO);
                if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                    if (resultObjectVO.getData() != null) {
                        banner = resultObjectVO.formatData(ShopBannerVO.class);
                        if (banner != null && StringUtils.isNotEmpty(banner.getImgPath())) {
                            imageUploadService.deleteFile(banner.getImgPath());
                        }
                    }
                }
                // 上传新图片
                shopBannerVO.setImgPath(imageUploadService.uploadBase64(shopBannerVO.getImgBase64()));
            }

            shopBannerVO.setUpdateDate(new Date());
            shopBannerVO.setUpdaterId(AuthHeaderUtil.getAdminIdAndPrefix(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, shopBannerVO);
            resultObjectVO = shopBannerService.update(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:seller:shopBanner:list"})
    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    public ResultObjectVO queryById(HttpServletRequest request, @RequestBody ShopBannerVO shopBannerVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(shopBannerVO.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, shopBannerVO);
            ResultObjectVO detailResult = shopBannerService.findById(requestJsonVO);
            if(detailResult.isSuccess())
            {
                if(detailResult.getData() == null)
                {
                    resultObjectVO.setMsg("轮播图不存在");
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    return resultObjectVO;
                }
                ShopBannerVO vo = detailResult.formatData(ShopBannerVO.class);
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
                if(vo.getStartShowDate()!=null) {
                    vo.setStartShowDateString(DateUtils.format(vo.getStartShowDate(), DateUtils.FORMATTER_SS.get()));
                }
                if(vo.getEndShowDate()!=null) {
                    vo.setEndShowDateString(DateUtils.format(vo.getEndShowDate(), DateUtils.FORMATTER_SS.get()));
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:seller:shopBanner:list"})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultObjectVO detail(HttpServletRequest request, @RequestBody ShopBannerVO shopBannerVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(shopBannerVO.getId() == null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, shopBannerVO);
            ResultObjectVO detailResult = shopBannerService.findById(requestJsonVO);
            if(detailResult.isSuccess())
            {
                if(detailResult.getData() == null)
                {
                    resultObjectVO.setMsg("轮播图不存在");
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    return resultObjectVO;
                }
                ShopBannerVO vo = detailResult.formatData(ShopBannerVO.class);
                if(StringUtils.isNotEmpty(vo.getImgPath()))
                {
                    vo.setHttpImgPath(imageUploadService.getImageHttpPrefix() + vo.getImgPath());
                }
                if(vo.getStartShowDate()!=null) {
                    vo.setStartShowDateString(DateUtils.format(vo.getStartShowDate(), DateUtils.FORMATTER_SS.get()));
                }
                if(vo.getEndShowDate()!=null) {
                    vo.setEndShowDateString(DateUtils.format(vo.getEndShowDate(), DateUtils.FORMATTER_SS.get()));
                }
                fillCreaterAndUpdaterName(vo);
                ShopBannerDetailVO detailVO = new ShopBannerDetailVO();
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
    private void fillCreaterAndUpdaterName(ShopBannerVO bannerVO)
    {
        if(bannerVO == null)
        {
            return;
        }
        // 非管理员(掌柜ID)直接显示ID前缀
        if(bannerVO.getCreaterId() != null && !bannerVO.getCreaterId().startsWith(AuthHeaderUtil.getAdminPrefix()))
        {
            bannerVO.setCreaterName("掌柜ID:"+bannerVO.getCreaterId());
        }
        if(bannerVO.getUpdaterId() != null && !bannerVO.getUpdaterId().startsWith(AuthHeaderUtil.getAdminPrefix()))
        {
            bannerVO.setUpdaterName("掌柜ID:"+bannerVO.getUpdaterId());
        }
        // 管理员ID查询用户名
        List<String> adminIdList = new ArrayList<String>();
        if(bannerVO.getCreaterId() != null && bannerVO.getCreaterId().startsWith(AuthHeaderUtil.getAdminPrefix()))
        {
            adminIdList.add(bannerVO.getCreaterId().substring(AuthHeaderUtil.getAdminPrefix().length()));
        }
        if(bannerVO.getUpdaterId() != null && bannerVO.getUpdaterId().startsWith(AuthHeaderUtil.getAdminPrefix()))
        {
            adminIdList.add(bannerVO.getUpdaterId().substring(AuthHeaderUtil.getAdminPrefix().length()));
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
                        if(bannerVO.getCreaterId() != null && bannerVO.getCreaterId().equals(AuthHeaderUtil.getAdminPrefix()+adminVO.getAdminId()))
                        {
                            bannerVO.setCreaterName(adminVO.getUsername());
                        }
                        if(bannerVO.getUpdaterId() != null && bannerVO.getUpdaterId().equals(AuthHeaderUtil.getAdminPrefix()+adminVO.getAdminId()))
                        {
                            bannerVO.setUpdaterName(adminVO.getUsername());
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
