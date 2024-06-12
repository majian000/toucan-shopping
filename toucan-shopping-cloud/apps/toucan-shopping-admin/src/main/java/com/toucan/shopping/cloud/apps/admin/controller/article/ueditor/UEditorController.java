package com.toucan.shopping.cloud.apps.admin.controller.article.ueditor;

import com.toucan.shopping.cloud.content.api.feign.service.FeignArticleImageService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/article/ueditor")
public class UEditorController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;


    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignArticleImageService feignArticleImageService;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping("/config")
    @ResponseBody
    public Object config(@RequestParam(value = "action", required = false) String action,HttpServletRequest request, HttpServletRequest response) throws Exception {
        if ("config".equals(action)) {
            UEditorConfig uEditorConfig = new UEditorConfig();
            uEditorConfig.setImageUrlPrefix(imageUploadService.getImageHttpPrefix());
            return uEditorConfig;
        }else if("/ueditor/uploadImg".equals(action)) {// 图片上传
            return this.uploadImg(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())),
                    ((MultipartHttpServletRequest)request).getFile("file"));
        }
        return action;
    }


    public ImgUploadResult uploadImg(String adminId,MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String fileExt = ".jpg";
        if(StringUtils.isNotEmpty(fileName)&&fileName.indexOf(".")!=-1)
        {
            fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
        }
        String groupPath = imageUploadService.uploadFile(file.getBytes(),fileExt);

        ArticleImageVO articleImageVO = new ArticleImageVO();
        articleImageVO.setFileName(fileName);
        articleImageVO.setFileSize(file.getSize());
        articleImageVO.setFileExt(fileExt);
        articleImageVO.setImgPath(groupPath);
        articleImageVO.setCreaterId(adminId);

        ResultObjectVO resultObjectVO = feignArticleImageService.save(RequestJsonVOGenerator.generator(toucan.getAppCode(),articleImageVO));
        ImgUploadResult imgUploadResult = new ImgUploadResult();
        if(resultObjectVO.isSuccess()) {
            imgUploadResult.setUrl(groupPath);
            imgUploadResult.setTitle(file.getOriginalFilename());
            imgUploadResult.setOriginal(file.getOriginalFilename());
            imgUploadResult.setState("SUCCESS");
        }else{
            imgUploadResult.setState("ERROR");
        }
        return imgUploadResult;
    }
}
