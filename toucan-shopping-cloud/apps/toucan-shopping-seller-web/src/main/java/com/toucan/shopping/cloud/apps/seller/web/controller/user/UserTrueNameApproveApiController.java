package com.toucan.shopping.cloud.apps.seller.web.controller.user;


import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.redis.VerifyCodeRedisKey;
import com.toucan.shopping.cloud.apps.seller.web.util.VCodeUtil;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserTrueNameApproveService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.common.xss.XSSConvert;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import com.toucan.shopping.modules.user.redis.UserCenterTrueNameApproveKey;
import com.toucan.shopping.modules.user.vo.UserTrueNameApproveVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


/**
 * 用户实名认证审核
 */
@RestController("apiUserTreeNameApproveApiController")
@RequestMapping("/api/user/true/name/approve")
public class UserTrueNameApproveApiController extends BaseController {


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
    private FeignUserTrueNameApproveService feignUserTrueNameApproveService;



    @UserAuth(requestType = UserAuth.REQUEST_AJAX)
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
            resultObjectVO.setCode(ResultObjectVO.FAILD-2);
            resultObjectVO.setMsg("请求失败,证件正面照片不能为空");
            return resultObjectVO;
        }
        if(userTrueNameApproveVO.getIdcardImg2File()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD-3);
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
                resultObjectVO.setCode(ResultObjectVO.FAILD-4);
                resultObjectVO.setMsg("请求失败,请上传图片格式(.jpg|.jpeg|.png|.gif|bmp)");
                return resultObjectVO;
            }

            if(StringUtils.isEmpty(userTrueNameApproveVO.getVcode()))
            {
                resultObjectVO.setMsg("请求失败,请输入验证码");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            String cookie = request.getHeader("Cookie");
            if(StringUtils.isEmpty(cookie))
            {
                resultObjectVO.setMsg("请求失败,请重新刷新验证码");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String ClientVCodeId = VCodeUtil.getClientVCodeId(cookie);
            if(StringUtils.isEmpty(ClientVCodeId))
            {
                resultObjectVO.setMsg("请求失败,验证码异常");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String vcodeRedisKey = VerifyCodeRedisKey.getVerifyCodeKey(this.getAppCode(),ClientVCodeId);
            Object vCodeObject = toucanStringRedisService.get(vcodeRedisKey);
            if(vCodeObject==null)
            {
                resultObjectVO.setMsg("请求失败,验证码过期请刷新");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            if(!StringUtils.equals(userTrueNameApproveVO.getVcode().toUpperCase(),String.valueOf(vCodeObject).toUpperCase()))
            {
                resultObjectVO.setMsg("请求失败,验证码输入有误");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            //删除缓存中验证码
            toucanStringRedisService.delete(vcodeRedisKey);

            //替换跨站脚本代码
            XSSConvert.replaceXSS(userTrueNameApproveVO);

            boolean lockStatus = skylarkLock.lock(UserCenterTrueNameApproveKey.getSaveApproveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            UserTrueNameApproveVO queryUserTrueNameApproveVO = new UserTrueNameApproveVO();
            queryUserTrueNameApproveVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(getAppCode(),queryUserTrueNameApproveVO);
            resultObjectVO = feignUserTrueNameApproveService.queryByUserMainId(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                //身份证 正面上传
                String idcard1ImgExt = ImageUtils.getImageExt(idcard1ImgFileName);
                if (idcard1ImgExt.indexOf(".") != -1) {
                    idcard1ImgExt = idcard1ImgExt.substring(idcard1ImgExt.indexOf(".") + 1, idcard1ImgExt.length());
                }
                String idcard1ImgFilePath = imageUploadService.uploadFile(userTrueNameApproveVO.getIdcardImg1File().getBytes(), idcard1ImgExt);
                userTrueNameApproveVO.setIdcardImg1(idcard1ImgFilePath);
                //身份证 背面上传
                String idcard2ImgExt = ImageUtils.getImageExt(idcard2ImgFileName);
                if (idcard2ImgExt.indexOf(".") != -1) {
                    idcard2ImgExt = idcard2ImgExt.substring(idcard2ImgExt.indexOf(".") + 1, idcard2ImgExt.length());
                }
                String idcard2ImgFilePath = imageUploadService.uploadFile(userTrueNameApproveVO.getIdcardImg2File().getBytes(), idcard2ImgExt);
                userTrueNameApproveVO.setIdcardImg2(idcard2ImgFilePath);

                List<UserTrueNameApprove> userTrueNameApproves = (List<UserTrueNameApprove>)resultObjectVO.formatDataArray(UserTrueNameApprove.class);
                if(CollectionUtils.isNotEmpty(userTrueNameApproves)) {
                    UserTrueNameApprove userTrueNameApprove = userTrueNameApproves.get(0);

                    userTrueNameApproveVO.setId(userTrueNameApprove.getId());
                    userTrueNameApproveVO.setUpdateDate(new Date());
                    userTrueNameApproveVO.setIdcardImg1File(null);
                    userTrueNameApproveVO.setIdcardImg2File(null);
                    userTrueNameApproveVO.setApproveStatus(1);

                    requestJsonVO = RequestJsonVOGenerator.generator(getAppCode(), userTrueNameApproveVO);

                    logger.info(" 用户实名重新发起 {} ", requestJsonVO.getEntityJson());

                    resultObjectVO = feignUserTrueNameApproveService.update(requestJsonVO.sign(), requestJsonVO);

                    return resultObjectVO;
                }else{
                    userTrueNameApproveVO.setApproveStatus(1);
                    userTrueNameApproveVO.setCreateDate(new Date());
                    userTrueNameApproveVO.setIdcardImg1File(null);
                    userTrueNameApproveVO.setIdcardImg2File(null);

                    requestJsonVO = RequestJsonVOGenerator.generator(getAppCode(), userTrueNameApproveVO);

                    logger.info(" 用户实名审核 {} ", requestJsonVO.getEntityJson());

                    resultObjectVO = feignUserTrueNameApproveService.save(requestJsonVO.sign(), requestJsonVO);
                    if (!resultObjectVO.isSuccess()) {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("请求失败,请稍后重试!");
                        return resultObjectVO;
                    }
                }
                resultObjectVO.setData(null);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
            skylarkLock.unLock(UserCenterTrueNameApproveKey.getSaveApproveLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }








}
