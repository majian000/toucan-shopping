package com.toucan.shopping.cloud.apps.admin.controller.common.ueditor;

import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.ueditor.vo.ImgUploadResult;
import com.toucan.shopping.modules.ueditor.vo.UEditorConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/ueditor")
public class UEditorController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;


    @Autowired
    private ImageUploadService imageUploadService;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping("/config")
    @ResponseBody
    public Object config(@RequestParam(value = "action", required = false) String action,HttpServletRequest request, HttpServletRequest response) throws Exception {
        if ("config".equals(action)) {
            UEditorConfig uEditorConfig = new UEditorConfig();
            uEditorConfig.setImageUrlPrefix(imageUploadService.getImageHttpPrefix());
            return uEditorConfig;
        }else if("/ueditor/uploadImg".equals(action)) {// 图片上传
            return this.uploadImg(((MultipartHttpServletRequest)request).getFile("file"));
        }
        return action;
    }


    public ImgUploadResult uploadImg(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String fileExt = ".jpg";
        if(StringUtils.isNotEmpty(fileName)&&fileName.indexOf(".")!=-1)
        {
            fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
        }
        String groupPath = imageUploadService.uploadFile(file.getBytes(),fileExt);
        ImgUploadResult imgUploadResult = new ImgUploadResult();
        imgUploadResult.setUrl(imageUploadService.getImageHttpPrefix()+groupPath);
        imgUploadResult.setTitle(file.getOriginalFilename());
        imgUploadResult.setOriginal(file.getOriginalFilename());
        imgUploadResult.setState("SUCCESS");
        return imgUploadResult;
    }
}
