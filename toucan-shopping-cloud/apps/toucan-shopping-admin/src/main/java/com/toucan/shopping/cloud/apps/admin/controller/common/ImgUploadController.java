package com.toucan.shopping.cloud.apps.admin.controller.common;

import com.toucan.shopping.cloud.apps.admin.vo.image.ImgUploadVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;



@Controller
@RequestMapping("/common")
public class ImgUploadController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ImageUploadService imageUploadService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping("/img/upload")
    @ResponseBody
    public ResultObjectVO imgUpload(@RequestParam("file") MultipartFile file)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(0);
        try{
            String fileName = file.getOriginalFilename();
            if(!ImageUtils.isStaticImage(fileName))
            {
                throw new RuntimeException("请上传图片格式(.jpg|.jpeg|.png)");
            }
            String fileExt = ".jpg";
            if(StringUtils.isNotEmpty(fileName)&&fileName.indexOf(".")!=-1)
            {
                fileExt = fileName.substring(fileName.lastIndexOf(".")+1);

            }
            String groupPath = imageUploadService.uploadFile(file.getBytes(),fileExt);

            if(StringUtils.isEmpty(groupPath))
            {
                throw new RuntimeException("图片上传失败");
            }

            ImgUploadVO imgUploadVO = new ImgUploadVO(groupPath,imageUploadService.getImageHttpPrefix()+groupPath);

            resultObjectVO.setData(imgUploadVO);

        }catch (Exception e)
        {
            resultObjectVO.setCode(1);
            resultObjectVO.setMsg("图片上传失败");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }

}
