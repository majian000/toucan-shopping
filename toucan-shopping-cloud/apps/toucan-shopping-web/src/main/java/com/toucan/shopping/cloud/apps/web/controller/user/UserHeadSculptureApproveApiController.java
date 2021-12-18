package com.toucan.shopping.cloud.apps.web.controller.user;


import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.VerifyCodeRedisKey;
import com.toucan.shopping.cloud.apps.web.util.VCodeUtil;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserHeadSculptureApproveService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserHeadSculptureApproveService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.util.MultipartFileUtil;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.UserHeadSculptureApprove;
import com.toucan.shopping.modules.user.redis.UserCenterHeadSculptureApproveKey;
import com.toucan.shopping.modules.user.vo.UserHeadSculptureApproveVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;


/**
 * 用户头像审核
 */
@RestController("apiUserHeadSculptureApproveApiController")
@RequestMapping("/api/user/head/sculpture/approve")
public class UserHeadSculptureApproveApiController extends BaseController {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;



    @Autowired
    private Toucan toucan;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignUserHeadSculptureApproveService feignUserHeadSculptureApproveService;



    @UserAuth(requestType = UserAuth.REQUEST_AJAX)
    @RequestMapping(value="/save")
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, UserHeadSculptureApproveVO userHeadSculptureApproveVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(userHeadSculptureApproveVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("提交失败,没有找到头像信息");
            return resultObjectVO;
        }

        String userMainId="-1";
        try {

            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            userHeadSculptureApproveVO.setUserMainId(Long.parseLong(userMainId));

            if(userHeadSculptureApproveVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("提交失败,用户ID不能为空");
                return resultObjectVO;
            }

            String headSculptureBase64 = userHeadSculptureApproveVO.getHeadSculptureBase64();
            if(StringUtils.isEmpty(headSculptureBase64))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD-4);
                resultObjectVO.setMsg("提交失败,请上传头像");
                return resultObjectVO;
            }
            boolean acceptUpload = false;
            String headSculptureImgExt="jpeg";
            if(headSculptureBase64.startsWith("data:image/jpeg;"))
            {
                acceptUpload = true;
                headSculptureImgExt = "jpeg";
            }else if(headSculptureBase64.startsWith("data:image/jpg;")){
                acceptUpload = true;
                headSculptureImgExt = "jpg";
            }else if(headSculptureBase64.startsWith("data:image/png;")){
                acceptUpload = true;
                headSculptureImgExt = "png";
            }else if(headSculptureBase64.startsWith("data:image/gif;")){
                acceptUpload = true;
                headSculptureImgExt = "gif";
            }else if(headSculptureBase64.startsWith("data:image/bmp;")){
                acceptUpload = true;
                headSculptureImgExt = "bmp";
            }

            if(!acceptUpload)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD-4);
                resultObjectVO.setMsg("提交失败,请上传图片格式(.jpg|.jpeg|.png|.gif|.bmp)");
                return resultObjectVO;
            }


            boolean lockStatus = skylarkLock.lock(UserCenterHeadSculptureApproveKey.getSaveApproveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            UserHeadSculptureApproveVO queryUserHeadSculptureApproveVO = new UserHeadSculptureApproveVO();
            queryUserHeadSculptureApproveVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(getAppCode(),queryUserHeadSculptureApproveVO);
            resultObjectVO = feignUserHeadSculptureApproveService.queryByUserMainId(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                MultipartFile multipartFile = MultipartFileUtil.base64ConvertMutipartFile(headSculptureBase64);
                //头像上传
                String headSculptureImgFilePath = imageUploadService.uploadFile(multipartFile.getBytes(), headSculptureImgExt);
                userHeadSculptureApproveVO.setHeadSculpture(headSculptureImgFilePath);

                List<UserHeadSculptureApprove> userHeadSculptureApproves = (List<UserHeadSculptureApprove>)resultObjectVO.formatDataList(UserHeadSculptureApprove.class);
                if(CollectionUtils.isNotEmpty(userHeadSculptureApproves)) {
                    UserHeadSculptureApprove userHeadSculptureApprove = userHeadSculptureApproves.get(0);

                    userHeadSculptureApproveVO.setId(userHeadSculptureApprove.getId());
                    userHeadSculptureApproveVO.setUpdateDate(new Date());
                    userHeadSculptureApproveVO.setHeadSculptureFile(null);
                    userHeadSculptureApproveVO.setApproveStatus(1);

                    requestJsonVO = RequestJsonVOGenerator.generator(getAppCode(), userHeadSculptureApproveVO);

                    logger.info(" 用户头像重新发起 {} ", requestJsonVO.getEntityJson());

                    resultObjectVO = feignUserHeadSculptureApproveService.update(requestJsonVO.sign(), requestJsonVO);

                    return resultObjectVO;
                }else{
                    userHeadSculptureApproveVO.setApproveStatus(1);
                    userHeadSculptureApproveVO.setCreateDate(new Date());
                    userHeadSculptureApproveVO.setHeadSculptureFile(null);

                    requestJsonVO = RequestJsonVOGenerator.generator(getAppCode(), userHeadSculptureApproveVO);

                    logger.info(" 用户头像审核 {} ", requestJsonVO.getEntityJson());

                    resultObjectVO = feignUserHeadSculptureApproveService.save(requestJsonVO.sign(), requestJsonVO);
                    if (!resultObjectVO.isSuccess()) {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("提交失败,请稍后重试!");
                        return resultObjectVO;
                    }
                }
                resultObjectVO.setData(null);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("提交失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterHeadSculptureApproveKey.getSaveApproveLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }








}