package com.toucan.shopping.cloud.apps.admin.controller.article.ueditor;

import com.toucan.shopping.cloud.content.api.ArticleImageServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.content.entity.ArticleImage;
import com.toucan.shopping.modules.content.vo.ArticleImageVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.ueditor.vo.ImgUploadResult;
import com.toucan.shopping.modules.ueditor.vo.UEditorConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/article/ueditor")
public class UEditorController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private ArticleImageServiceAPI articleImageService;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:common:ueditor:config"})
    @RequestMapping("/config")
    public Object config(@RequestParam(value = "action", required = false) String action, HttpServletRequest request, HttpServletRequest response) throws Exception {
        if ("config".equals(action)) {
            UEditorConfig uEditorConfig = new UEditorConfig();
            uEditorConfig.setImageUrlPrefix(imageUploadService.getImageHttpPrefix());
            return uEditorConfig;
        } else if ("/ueditor/uploadImg".equals(action)) {
            return this.uploadImg(AdminLoginHolder.getCurrentAdminId(),
                    ((MultipartHttpServletRequest) request).getFile("file"));
        }
        return action;
    }


    public ImgUploadResult uploadImg(String adminId, MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String fileExt = ".jpg";
        if (StringUtils.isNotEmpty(fileName) && fileName.indexOf(".") != -1) {
            fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        String groupPath = imageUploadService.uploadFile(file.getBytes(), fileExt);

        ArticleImageVO articleImageVO = new ArticleImageVO();
        articleImageVO.setFileName(fileName);
        articleImageVO.setFileSize(file.getSize());
        articleImageVO.setFileExt(fileExt);
        articleImageVO.setImgPath(groupPath);
        articleImageVO.setCreateAdminId(adminId);
        articleImageVO.setAppCode(toucan.getShoppingPC().getAppCode());

        ResultObjectVO resultObjectVO = articleImageService.save(RequestJsonVOGenerator.generator(toucan.getAppCode(), articleImageVO));
        ImgUploadResult imgUploadResult = new ImgUploadResult();
        if (resultObjectVO.isSuccess()) {
            imgUploadResult.setUrl(groupPath);
            imgUploadResult.setTitle(file.getOriginalFilename());
            imgUploadResult.setOriginal(file.getOriginalFilename());
            imgUploadResult.setState("SUCCESS");
        } else {
            imgUploadResult.setState("ERROR");
        }
        return imgUploadResult;
    }
}
