package com.toucan.shopping.cloud.apps.admin.controller.article;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.content.api.ArticleServiceAPI;
import com.toucan.shopping.cloud.content.api.ColumnServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.column.vo.ColumnTreeVO;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.content.page.ArticlePageInfo;
import com.toucan.shopping.modules.content.vo.ArticleDetailVO;
import com.toucan.shopping.modules.content.vo.ArticleVO;
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
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文章
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ColumnServiceAPI columnService;

    @Autowired
    private ArticleServiceAPI articleService;

    @Autowired
    private AdminServiceAPI adminServiceAPI;

    @Autowired
    private ImageUploadService imageUploadService;


    /**
     * 查询列表
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:article:list"})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public TableVO list(HttpServletRequest request, @RequestBody ArticlePageInfo pageInfo) {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            ResultObjectVO resultObjectVO = articleService.queryListPage(requestJsonVO);
            if (resultObjectVO.getCode() == ResultObjectVO.SUCCESS) {
                if (resultObjectVO.getData() != null) {
                    Map<String, Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total") != null ? resultObjectDataMap.get("total") : "0")));
                    List<ArticleVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), ArticleVO.class);

                    // 查询创建人和修改人
                    List<String> adminIdList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        ArticleVO articleVO = list.get(i);
                        if (StringUtils.isNotEmpty(articleVO.getCoverImgUrl())) {
                            articleVO.setHttpCoverImgUrl(imageUploadService.getImageHttpPrefix() + articleVO.getCoverImgUrl());
                        }
                        if (articleVO.getCreateAdminId() != null) {
                            adminIdList.add(articleVO.getCreateAdminId());
                        }
                        if (articleVO.getUpdateAdminId() != null) {
                            adminIdList.add(articleVO.getUpdateAdminId());
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
                            for (ArticleVO articleVO : list) {
                                for (AdminVO adminVO : adminVOS) {
                                    if (articleVO.getCreateAdminId() != null && articleVO.getCreateAdminId().equals(adminVO.getAdminId())) {
                                        articleVO.setCreateAdminName(adminVO.getUsername());
                                    }
                                    if (articleVO.getUpdateAdminId() != null && articleVO.getUpdateAdminId().equals(adminVO.getAdminId())) {
                                        articleVO.setUpdateAdminName(adminVO.getUsername());
                                    }
                                }
                            }
                        }
                    }

                    if (tableVO.getCount() > 0) {
                        tableVO.setData(list);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:article:add"})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultObjectVO save(HttpServletRequest request, @RequestBody ArticleVO articleVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (articleVO.getColumnId() == null) {
                resultObjectVO.setMsg("栏目不能为空");
                resultObjectVO.setCode(TableVO.FAILD);
                return resultObjectVO;
            }

            // 如果有imgBase64，解码后上传到文件服务
            if (StringUtils.isNotEmpty(articleVO.getImgBase64())) {
                articleVO.setCoverImgUrl(imageUploadService.uploadBase64(articleVO.getImgBase64()));
            }

            articleVO.setAppCode(toucan.getShoppingPC().getAppCode());
            articleVO.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            articleVO.setImageHttpPrefix(imageUploadService.getImageHttpPrefix());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, articleVO);
            resultObjectVO = articleService.save(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:article:update"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultObjectVO update(HttpServletRequest request, @RequestBody ArticleVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (StringUtils.isNotEmpty(entity.getImgBase64())) {
                // 查询旧图片并删除
                ArticleVO queryVO = new ArticleVO();
                queryVO.setId(entity.getId());
                RequestJsonVO queryRequest = RequestJsonVOGenerator.generator(appCode, queryVO);
                ResultTypeObjectVO<ArticleVO> oldResult = articleService.findById(queryRequest);
                if (oldResult.isSuccess() && oldResult.getData() != null) {
                    ArticleVO oldArticle = oldResult.getData();
                    if (StringUtils.isNotEmpty(oldArticle.getCoverImgUrl())) {
                        imageUploadService.deleteFile(oldArticle.getCoverImgUrl());
                    }
                }
                // 上传新图片
                entity.setCoverImgUrl(imageUploadService.uploadBase64(entity.getImgBase64()));
            }
            entity.setAppCode(toucan.getShoppingPC().getAppCode());
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = articleService.update(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:article:delete"})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody ArticleVO article) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (article.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            article.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());

            resultObjectVO = articleService.deleteById(RequestJsonVOGenerator.generator(toucan.getAppCode(), article));
        } catch (Exception e) {
            resultObjectVO.setMsg("删除失败,请稍后重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 批量删除
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:article:deletes"})
    @RequestMapping(value = "/delete/ids", method = RequestMethod.POST)
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<ArticleVO> articleVOS) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (CollectionUtils.isEmpty(articleVOS)) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            resultObjectVO = articleService.deleteByIds(RequestJsonVOGenerator.generator(appCode, articleVOS));
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:article:image:upload"})
    @RequestMapping("/upload/img")
    public ResultObjectVO uploadImg(@RequestParam("file") MultipartFile file) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(0);
        try {
            String fileName = file.getOriginalFilename();
            if (!ImageUtils.isImage(fileName)) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("上传图片只支持(" + ImageUtils.imageExtScope.stream().collect(Collectors.joining("、")) + ")");
                return resultObjectVO;
            }
            String fileExt = "jpg";
            if (StringUtils.isNotEmpty(fileName) && fileName.indexOf(".") != -1) {
                fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
            }
            String groupPath = imageUploadService.uploadFile(file.getBytes(), fileExt);

            if (StringUtils.isEmpty(groupPath)) {
                throw new RuntimeException("上传失败");
            }
            ArticleVO articleVO = new ArticleVO();
            articleVO.setCoverImgUrl(groupPath);
            articleVO.setHttpCoverImgUrl(imageUploadService.getImageHttpPrefix() + groupPath);
            resultObjectVO.setData(articleVO);
        } catch (Exception e) {
            resultObjectVO.setCode(1);
            resultObjectVO.setMsg("上传失败");
            logger.warn(e.getMessage(), e);
        }

        return resultObjectVO;
    }


    /**
     * 根据ID查询（回显用，含base64图片数据）
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:article:list"})
    @RequestMapping(value = "/queryById", method = RequestMethod.POST)
    public ResultObjectVO queryById(HttpServletRequest request, @RequestBody ArticleVO articleVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (articleVO.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, articleVO);
            ResultTypeObjectVO<ArticleVO> detailResult = articleService.findById(requestJsonVO);
            if (detailResult.isSuccess() && detailResult.getData() != null) {
                ArticleVO vo = detailResult.getData();
                if (StringUtils.isNotEmpty(vo.getCoverImgUrl())) {
                    vo.setHttpCoverImgUrl(imageUploadService.getImageHttpPrefix() + vo.getCoverImgUrl());
                    // 下载文件并转base64
                    byte[] fileBytes = imageUploadService.downloadFile(vo.getCoverImgUrl());
                    if (fileBytes != null && fileBytes.length > 0) {
                        String path = vo.getCoverImgUrl();
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:article:detail"})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultObjectVO detail(HttpServletRequest request, @RequestBody ArticleVO articleVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (articleVO.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, articleVO);
            ResultTypeObjectVO<ArticleVO> detailResult = articleService.findById(requestJsonVO);
            if (detailResult.isSuccess() && detailResult.getData() != null) {
                ArticleVO vo = detailResult.getData();
                if (StringUtils.isNotEmpty(vo.getCoverImgUrl())) {
                    vo.setHttpCoverImgUrl(imageUploadService.getImageHttpPrefix() + vo.getCoverImgUrl());
                }
                // 填充创建人/修改人姓名
                fillAdminName(vo);
                // 填充栏目名称
                fillColumnName(vo);
                ArticleDetailVO detailVO = new ArticleDetailVO();
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
     * 填充创建人/修改人姓名
     */
    private void fillAdminName(ArticleVO articleVO) throws NoSuchAlgorithmException {
        List<String> adminIdList = new ArrayList<>();
        if (articleVO.getCreateAdminId() != null) {
            adminIdList.add(articleVO.getCreateAdminId());
        }
        if (articleVO.getUpdateAdminId() != null) {
            adminIdList.add(articleVO.getUpdateAdminId());
        }
        if (adminIdList.isEmpty()) {
            return;
        }
        String[] adminIds = adminIdList.toArray(new String[0]);
        AdminVO queryAdminVO = new AdminVO();
        queryAdminVO.setAdminIds(adminIds);
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAdminVO);
        ResultObjectVO adminResultVO = adminServiceAPI.queryListByEntity(requestJsonVO);
        if (adminResultVO.isSuccess()) {
            List<AdminVO> adminVOS = adminResultVO.formatDataList(AdminVO.class);
            if (!CollectionUtils.isEmpty(adminVOS)) {
                for (AdminVO adminVO : adminVOS) {
                    if (articleVO.getCreateAdminId() != null && articleVO.getCreateAdminId().equals(adminVO.getAdminId())) {
                        articleVO.setCreateAdminName(adminVO.getUsername());
                    }
                    if (articleVO.getUpdateAdminId() != null && articleVO.getUpdateAdminId().equals(adminVO.getAdminId())) {
                        articleVO.setUpdateAdminName(adminVO.getUsername());
                    }
                }
            }
        }
    }


    /**
     * 填充栏目名称
     */
    private void fillColumnName(ArticleVO articleVO) throws NoSuchAlgorithmException {
        if (articleVO.getColumnId() == null) {
            return;
        }
        ColumnVO queryColumnVO = new ColumnVO();
        queryColumnVO.setId(articleVO.getColumnId());
        RequestJsonVO columnRequestJsonVO = RequestJsonVOGenerator.generator(appCode, queryColumnVO);
        ResultTypeObjectVO<ColumnVO> columnResult = columnService.findById(columnRequestJsonVO);
        if (columnResult.isSuccess() && columnResult.getData() != null) {
            articleVO.setColumnName(columnResult.getData().getTitle());
        }
    }


    /**
     * 查询栏目树（按父ID）
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:article:column:tree"})
    @RequestMapping(value = "/query/column/tree/pid", method = RequestMethod.POST)
    public ResultObjectVO queryColumnTreeByParentId(@RequestParam(defaultValue = "-1") Long id) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ColumnVO query = new ColumnVO();
            query.setPid(id);
            query.setAppCode(toucan.getShoppingPC().getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
            resultObjectVO = columnService.queryListByPid(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    List<ColumnTreeVO> columnTreeVOS = resultObjectVO.formatDataList(ColumnTreeVO.class);
                    for (ColumnTreeVO columnTreeVO : columnTreeVOS) {
                        columnTreeVO.setName("[" + columnTreeVO.getColumnTypeName() + "]" + columnTreeVO.getTitle());
                        columnTreeVO.setOpen(false);
                    }
                    resultObjectVO.setData(columnTreeVOS);
                }
            }
            return resultObjectVO;
        } catch (Exception e) {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

}
