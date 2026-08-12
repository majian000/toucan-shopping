package com.toucan.shopping.cloud.apps.admin.controller.hotProduct;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.content.api.HotProductServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.column.vo.HotProductDetailVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.column.page.HotProductPageInfo;
import com.toucan.shopping.modules.column.vo.HotProductVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * 热门商品
 */
@RestController
@RequestMapping("/hotProduct")
public class HotProductController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private HotProductServiceAPI hotProductService;

    @Autowired
    private AdminServiceAPI adminServiceAPI;

    @Autowired
    private ImageUploadService imageUploadService;


    /**
     * 查询列表
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:dashboard:hot:product:list"})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public TableVO list(HttpServletRequest request,@RequestBody HotProductPageInfo pageInfo) {
        TableVO tableVO = new TableVO();
        try {
            pageInfo.setAppCode(toucan.getShoppingPC().getAppCode());

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            ResultObjectVO resultObjectVO = hotProductService.queryListPage(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    Map<String, Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total") != null ? resultObjectDataMap.get("total") : "0")));
                    List<HotProductVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), HotProductVO.class);

                    // 查询创建人和修改人
                    List<String> adminIdList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        HotProductVO hotProductVO = list.get(i);
                        if (hotProductVO.getCreateAdminId() != null) {
                            adminIdList.add(hotProductVO.getCreateAdminId());
                        }
                        if (hotProductVO.getUpdateAdminId() != null) {
                            adminIdList.add(hotProductVO.getUpdateAdminId());
                        }
                    }
                    String[] createOrUpdateAdminIds = new String[adminIdList.size()];
                    adminIdList.toArray(createOrUpdateAdminIds);
                    AdminVO queryAdminVO = new AdminVO();
                    queryAdminVO.setAdminIds(createOrUpdateAdminIds);
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAdminVO);
                    resultObjectVO = adminServiceAPI.queryListByEntity(requestJsonVO);
                    if (resultObjectVO.isSuccess()) {
                        List<AdminVO> adminVOS = resultObjectVO.formatDataList(AdminVO.class);
                        if (!CollectionUtils.isEmpty(adminVOS)) {
                            for (HotProductVO hotProductVO : list) {
                                for (AdminVO adminVO : adminVOS) {
                                    if (hotProductVO.getCreateAdminId() != null && hotProductVO.getCreateAdminId().equals(adminVO.getAdminId())) {
                                        hotProductVO.setCreateAdminName(adminVO.getUsername());
                                    }
                                    if (hotProductVO.getUpdateAdminId() != null && hotProductVO.getUpdateAdminId().equals(adminVO.getAdminId())) {
                                        hotProductVO.setUpdateAdminName(adminVO.getUsername());
                                    }
                                }

                                if (StringUtils.isNotEmpty(hotProductVO.getImgPath())) {
                                    hotProductVO.setHttpImgPath(imageUploadService.getImageHttpPrefix() + hotProductVO.getImgPath());
                                }
                            }
                        }
                    }

                    if (tableVO.getCount() > 0) {
                        tableVO.setData((List) list);
                    }
                }
            }
        } catch (Exception e) {
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return tableVO;
    }


    /**
     * 保存
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:dashboard:hot:product:save"})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultObjectVO save(HttpServletRequest request, @RequestBody HotProductVO hotProductVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            // 如果有imgBase64，解码后上传到文件服务
            if (StringUtils.isNotEmpty(hotProductVO.getImgBase64())) {
                hotProductVO.setImgPath(imageUploadService.uploadBase64(hotProductVO.getImgBase64()));
            }
            hotProductVO.setAppCode(toucan.getShoppingPC().getAppCode());
            hotProductVO.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, hotProductVO);
            resultObjectVO = hotProductService.save(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请稍后重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 修改
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:dashboard:hot:product:update"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultObjectVO update(HttpServletRequest request, @RequestBody HotProductVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (StringUtils.isNotEmpty(entity.getImgBase64())) {
                // 查询旧图片并删除
                HotProductVO queryVO = new HotProductVO();
                queryVO.setId(entity.getId());
                RequestJsonVO queryRequest = RequestJsonVOGenerator.generator(appCode, queryVO);
                ResultObjectVO oldResult = hotProductService.findById(queryRequest);
                if (oldResult.isSuccess()) {
                    HotProductVO oldVO = oldResult.formatData(HotProductVO.class);
                    if (oldVO != null && StringUtils.isNotEmpty(oldVO.getImgPath())) {
                        imageUploadService.deleteFile(oldVO.getImgPath());
                    }
                }
                // 上传新图片
                entity.setImgPath(imageUploadService.uploadBase64(entity.getImgBase64()));
            }
            entity.setAppCode(toucan.getShoppingPC().getAppCode());
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = hotProductService.update(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 删除
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:dashboard:hot:product:delete"})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody HotProductVO hotProductVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (hotProductVO.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            String entityJson = JSONObject.toJSONString(hotProductVO);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);

            resultObjectVO = hotProductService.deleteById(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 批量删除
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:dashboard:hotProduct:toolbar:delete"})
    @RequestMapping(value = "/delete/ids", method = RequestMethod.POST)
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<HotProductVO> hotProductVOS) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (CollectionUtils.isEmpty(hotProductVOS)) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(hotProductVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = hotProductService.deleteByIds(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 根据ID查询（回显用，含base64图片数据）
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:dashboard:hotProduct:row:view"})
    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    public ResultObjectVO queryById(HttpServletRequest request, @RequestBody HotProductVO hotProductVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (hotProductVO.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, hotProductVO);
            ResultObjectVO detailResult = hotProductService.findById(requestJsonVO);
            if (detailResult.isSuccess()) {
                HotProductVO vo = detailResult.formatData(HotProductVO.class);
                if (vo != null && StringUtils.isNotEmpty(vo.getImgPath())) {
                    vo.setHttpImgPath(imageUploadService.getImageHttpPrefix() + vo.getImgPath());
                    // 下载文件并转base64
                    byte[] fileBytes = imageUploadService.downloadFile(vo.getImgPath());
                    if (fileBytes != null && fileBytes.length > 0) {
                        String path = vo.getImgPath();
                        String ext = "jpg";
                        if (path.contains(".")) {
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
            } else {
                resultObjectVO.setMsg(detailResult.getMsg());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
            }
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查看详情
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:dashboard:hotProduct:row:view"})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultObjectVO detail(HttpServletRequest request, @RequestBody HotProductVO hotProductVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (hotProductVO.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, hotProductVO);
            ResultObjectVO detailResult = hotProductService.findById(requestJsonVO);
            if (detailResult.isSuccess()) {
                HotProductVO vo = detailResult.formatData(HotProductVO.class);
                if (vo != null && StringUtils.isNotEmpty(vo.getImgPath())) {
                    vo.setHttpImgPath(imageUploadService.getImageHttpPrefix() + vo.getImgPath());
                }
                HotProductDetailVO detailVO = new HotProductDetailVO();
                detailVO.setBasicInfo(vo);
                resultObjectVO.setData(detailVO);
            } else {
                resultObjectVO.setMsg(detailResult.getMsg());
                resultObjectVO.setCode(ResultObjectVO.FAILD);
            }
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 上传图片
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:dashboard:hot:product:upload:img"})
    @RequestMapping("/upload/img")
    public ResultObjectVO uploadImg(@RequestParam("file") MultipartFile file) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(0);
        try {
            String fileName = file.getOriginalFilename();
            String fileExt = "jpg";
            if (StringUtils.isNotEmpty(fileName) && fileName.indexOf(".") != -1) {
                fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
            }
            if (!ImageUtils.isImage(file.getOriginalFilename())) {
                throw new RuntimeException("只能上传JPG、JPEG、PNG、GIF、BMP格式");
            }
            String groupPath = imageUploadService.uploadFile(file.getBytes(), fileExt);

            if (StringUtils.isEmpty(groupPath)) {
                throw new RuntimeException("上传失败");
            }
            HotProductVO hotProductVO = new HotProductVO();
            hotProductVO.setImgPath(groupPath);
            hotProductVO.setHttpImgPath(imageUploadService.getImageHttpPrefix() + groupPath);
            resultObjectVO.setData(hotProductVO);
        } catch (Exception e) {
            resultObjectVO.setCode(1);
            resultObjectVO.setMsg("上传失败");
            logger.warn(e.getMessage(), e);
        }

        return resultObjectVO;
    }


}
