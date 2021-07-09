package com.toucan.shopping.cloud.apps.web.controller.page.user;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.UserRegistRedisKey;
import com.toucan.shopping.cloud.user.api.feign.service.FeignSmsService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserTrueNameApproveService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.lock.redis.RedisLock;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.user.constant.SmsTypeConstant;
import com.toucan.shopping.modules.user.constant.UserLoginConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.UserMobilePhone;
import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import com.toucan.shopping.modules.user.redis.UserCenterTrueNameApproveKey;
import com.toucan.shopping.modules.user.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 用户实名认证审核
 */
@RestController("apiUserTreeNameApproveApiController")
@RequestMapping("/api/user/true/name/approve")
public class UserTreeNameApproveApiController extends BaseController {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private RedisLock redisLock;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignUserTrueNameApproveService feignUserTrueNameApproveService;



    @UserAuth(verifyMethod = UserAuth.VERIFYMETHOD_USER_AUTH,requestType = UserAuth.REQUEST_FORM)
    @RequestMapping(value="/save")
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request,UserTrueNameApproveVO userTrueNameApproveVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(userTrueNameApproveVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到实名信息");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userTrueNameApproveVO.getTrueName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,真实姓名不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userTrueNameApproveVO.getIdCard()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,证件号码不能为空");
            return resultObjectVO;
        }
        if(userTrueNameApproveVO.getIdcardType()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,证件类型不能为空");
            return resultObjectVO;
        }
        if(userTrueNameApproveVO.getIdcardImg1File()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,证件正面照片不能为空");
            return resultObjectVO;
        }
        if(userTrueNameApproveVO.getIdcardImg2File()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,证件背面照片不能为空");
            return resultObjectVO;
        }



        String userMainId="-1";
        try {

            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            userTrueNameApproveVO.setUserMainId(Long.parseLong(userMainId));

            if(userTrueNameApproveVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,用户ID不能为空");
                return resultObjectVO;
            }

            String idcard1ImgFileName = userTrueNameApproveVO.getIdcardImg1File().getOriginalFilename();
            String idcard2ImgFileName = userTrueNameApproveVO.getIdcardImg2File().getOriginalFilename();
            if(!ImageUtils.isImage(idcard1ImgFileName)||!ImageUtils.isImage(idcard2ImgFileName))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请上传图片格式(.jpg|.jpeg|.png|.gif|bmp)");
                return resultObjectVO;
            }
            boolean lockStatus = redisLock.lock(UserCenterTrueNameApproveKey.getSaveApproveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }

            UserTrueNameApproveVO queryUserTrueNameApproveVO = new UserTrueNameApproveVO();
            queryUserTrueNameApproveVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(getAppCode(),queryUserTrueNameApproveVO);
            resultObjectVO = feignUserTrueNameApproveService.queryByUserMainId(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<UserTrueNameApprove> userTrueNameApproves = (List<UserTrueNameApprove>)resultObjectVO.formatDataArray(UserTrueNameApprove.class);
                if(CollectionUtils.isNotEmpty(userTrueNameApproves))
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,已存在用户实名审核的记录");
                    return resultObjectVO;
                }

                //身份证 正面上传
                String idcard1ImgFilePath = imageUploadService.uploadFile(userTrueNameApproveVO.getIdcardImg1File().getBytes(),ImageUtils.getImageExt(idcard1ImgFileName));
                userTrueNameApproveVO.setIdcardImg1(idcard1ImgFilePath);
                //身份证 背面上传
                String idcard2ImgFilePath = imageUploadService.uploadFile(userTrueNameApproveVO.getIdcardImg2File().getBytes(),ImageUtils.getImageExt(idcard2ImgFileName));
                userTrueNameApproveVO.setIdcardImg2(idcard2ImgFilePath);

                logger.info(" 用户实名审核 {} ", requestJsonVO.getEntityJson());

                userTrueNameApproveVO.setApproveStatus(1);
                userTrueNameApproveVO.setIdcardImg1File(null);
                userTrueNameApproveVO.setIdcardImg2File(null);
                requestJsonVO = RequestJsonVOGenerator.generator(getAppCode(),userTrueNameApproveVO);
                resultObjectVO = feignUserTrueNameApproveService.save(requestJsonVO.sign(),requestJsonVO);
                if(!resultObjectVO.isSuccess())
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请求失败,请稍后重试!");
                    return resultObjectVO;
                }
                resultObjectVO.setData(null);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
            redisLock.unLock(UserCenterTrueNameApproveKey.getSaveApproveLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }








}
