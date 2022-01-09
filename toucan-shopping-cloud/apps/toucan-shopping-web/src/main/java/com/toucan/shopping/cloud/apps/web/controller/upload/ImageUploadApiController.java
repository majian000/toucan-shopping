package com.toucan.shopping.cloud.apps.web.controller.upload;

import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
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

import javax.servlet.http.HttpServletRequest;


/**
 * 图片上传
 */
@Controller("imageUploadApiController")
@RequestMapping("/page/api/upload")
public class ImageUploadApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignUserService feignUserService;


    @Autowired
    private Toucan toucan;

    @Autowired
    private ImageUploadService imageUploadService;



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping("/user/headsculpture")
    @ResponseBody
    public ResultObjectVO  uploadLogo(@RequestParam("file") MultipartFile file,HttpServletRequest request)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(0);
        try{
            String fileName = file.getOriginalFilename();
            String fileExt = ".jpg";
            if(StringUtils.isNotEmpty(fileName)&&fileName.indexOf(".")!=-1)
            {
                fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
            }
            String fileExtUpper=  fileExt.toUpperCase();
            if(!"JPG".equals(fileExtUpper)&&!"JPEG".equals(fileExtUpper)
                    &&!"PNG".equals(fileExtUpper)&&!"BMP".equals(fileExtUpper))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("图标只能上传JPG、JPEG、PNG、BMP格式!");
                return resultObjectVO;
            }
            long fileSize = file.getSize()/1024; //文件大小 KB单位
            if(fileSize>0&&fileSize>toucan.getUser().getHeadSculptureMaxSize().longValue()) //大于2MB
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("文件大小超过限制,不能大于"+(toucan.getUser().getHeadSculptureMaxSize().longValue()/1024)+"MB!");
                return resultObjectVO;
            }
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            String oldHeadSculpture = null;
            try{
                //查询该用户
                UserVO userVO = new UserVO();
                userVO.setUserMainId(Long.parseLong(userMainId));

                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), userVO);
                resultObjectVO = feignUserService.queryLoginInfo(requestJsonVO.sign(),requestJsonVO);
                //查询这个用户的旧头像
                if(resultObjectVO.isSuccess())
                {
                    userVO = resultObjectVO.formatData(UserVO.class);
                    oldHeadSculpture = userVO.getHeadSculpture();

                    String groupPath = imageUploadService.uploadFile(file.getBytes(),fileExt);

                    if(StringUtils.isEmpty(groupPath))
                    {
                        throw new RuntimeException("用户头像上传失败");
                    }

                    userVO = new UserVO();
                    userVO.setHeadSculpture(groupPath);
                    userVO.setUserMainId(Long.parseLong(userMainId));
                    userVO.setAppCode(toucan.getAppCode());

                    //修改头像
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), userVO);
                    resultObjectVO = feignUserService.updateHeadsculpture(requestJsonVO.sign(),requestJsonVO);
                    if(resultObjectVO.isSuccess()) {

                        userVO = resultObjectVO.formatData(UserVO.class);
                        //设置预览
                        if (userVO.getHeadSculpture() != null) {
                            userVO.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix() + userVO.getHeadSculpture());
                        }
                        resultObjectVO.setData(userVO);

                        //如果不是默认图标或者不为空的话,就去文件服务器删除这个文件
                        if(StringUtils.isNotEmpty(oldHeadSculpture)&&!toucan.getUser().getDefaultHeadSculpture().equals(oldHeadSculpture))
                        {
                            try{
                                imageUploadService.deleteFile(oldHeadSculpture);
                            }catch(Exception e)
                            {
                                logger.warn("删除旧头像失败, 用户ID {} 头像 {}",userMainId,oldHeadSculpture);
                                logger.warn(e.getMessage(),e);
                            }
                        }
                    }
                }
            }catch(Exception e)
            {
                logger.warn("查询用户失败 用户ID{}",userMainId);
                logger.warn(e.getMessage(),e);
            }

            String groupPath = imageUploadService.uploadFile(file.getBytes(),fileExt);

            if(StringUtils.isEmpty(groupPath))
            {
                throw new RuntimeException("用户头像上传失败");
            }


        }catch (Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("用户头像上传失败");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }



}
