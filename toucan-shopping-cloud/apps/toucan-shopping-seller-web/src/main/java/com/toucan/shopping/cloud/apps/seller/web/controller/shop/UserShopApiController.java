package com.toucan.shopping.cloud.apps.seller.web.controller.shop;

import com.toucan.shopping.cloud.apps.seller.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.seller.web.redis.ShopRegistRedisKey;
import com.toucan.shopping.cloud.apps.seller.web.redis.VerifyCodeRedisKey;
import com.toucan.shopping.cloud.apps.seller.web.util.VCodeUtil;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.xss.XSSConvert;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopRegistVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * 个人店铺
 */
@Controller("userShopApiController")
@RequestMapping("/api/user/shop")
public class UserShopApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private Toucan toucan;

    @Autowired
    private SkylarkLock redisLock;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Autowired
    private ImageUploadService imageUploadService;

    /**
     * 个人店铺申请
     * @return
     */
    @UserAuth
    @RequestMapping(value="/regist",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO regist(HttpServletRequest request,@RequestBody SellerShopRegistVO sellerShopVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try {


            if(StringUtils.isEmpty(sellerShopVO.getVcode()))
            {
                resultObjectVO.setMsg("请输入验证码");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            boolean lockStatus = redisLock.lock(ShopRegistRedisKey.getRegistLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }


            //判断验证码
            Object vcodeObject = toucanStringRedisService.get(ShopRegistRedisKey.getVerifyCodeKey(sellerShopVO.getMobilePhone()));
            if (vcodeObject == null) {
                redisLock.unLock(ShopRegistRedisKey.getRegistLockKey(userMainId), userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请发送验证码");
                return resultObjectVO;
            }
            //TODO:临时开放注册
            vcodeObject = "1234";
            if (!sellerShopVO.getVcode().equals(String.valueOf(vcodeObject))) {
                //释放注册锁
                redisLock.unLock(ShopRegistRedisKey.getRegistLockKey(userMainId), userMainId);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("验证码输入有误");
                return resultObjectVO;
            }

            UserVO userVO = new UserVO();
            userVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), userVO);
            resultObjectVO = feignUserService.verifyRealName(requestJsonVO.sign(), requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                boolean result = Boolean.valueOf(String.valueOf(resultObjectVO.getData()));
                if(result)
                {
                    sellerShopVO.setType(1);
                    sellerShopVO.setUserMainId(userVO.getUserMainId());
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), sellerShopVO);
                    resultObjectVO = feignSellerShopService.save(requestJsonVO.sign(),requestJsonVO);
                    //店铺注册成功修改用户状态为存在店铺
                    if(resultObjectVO.isSuccess())
                    {
                        userVO.setIsShop((short)1); // 存在店铺
                        requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), userVO);
                        feignUserService.updateIsShop(requestJsonVO.sign(),requestJsonVO);
                    }
                }else{
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请先实名认证");
                }
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
            logger.warn(e.getMessage(),e);
        }finally{
            redisLock.unLock(ShopRegistRedisKey.getRegistLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }





    /**
     * 个人店铺编辑
     * @return
     */
    @UserAuth(requestType = UserAuth.REQUEST_AJAX)
    @RequestMapping(value="/edit")
    @ResponseBody
    public ResultObjectVO edit(HttpServletRequest request, @RequestParam(value="logoFile",required=false) MultipartFile logoFile, SellerShopVO sellerShopVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String userMainId="-1";
        try {

            if(logoFile!=null) {
                if (!ImageUtils.isImage(logoFile.getOriginalFilename())) {
                    resultObjectVO.setCode(ResultObjectVO.FAILD - 4);
                    resultObjectVO.setMsg("请上传图片格式(.jpg|.jpeg|.png|.gif|bmp)");
                    return resultObjectVO;
                }
            }

            if(StringUtils.isEmpty(sellerShopVO.getVcode()))
            {
                resultObjectVO.setMsg("请输入验证码");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            String cookie = request.getHeader("Cookie");
            if(StringUtils.isEmpty(cookie))
            {
                resultObjectVO.setMsg("请重新刷新验证码");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String ClientVCodeId = VCodeUtil.getClientVCodeId(cookie);
            if(StringUtils.isEmpty(ClientVCodeId))
            {
                resultObjectVO.setMsg("验证码异常");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String vcodeRedisKey = VerifyCodeRedisKey.getVerifyCodeKey(this.getAppCode(),ClientVCodeId);
            Object vCodeObject = toucanStringRedisService.get(vcodeRedisKey);
            if(vCodeObject==null)
            {
                resultObjectVO.setMsg("验证码过期请刷新");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            if(!StringUtils.equals(sellerShopVO.getVcode().toUpperCase(),String.valueOf(vCodeObject).toUpperCase()))
            {
                resultObjectVO.setMsg("验证码输入有误");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            //删除缓存中验证码
            toucanStringRedisService.delete(vcodeRedisKey);


            if(StringUtils.isEmpty(sellerShopVO.getName()))
            {
                resultObjectVO.setMsg("修改失败,名称不能为空");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }


            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            boolean lockStatus = redisLock.lock(ShopRegistRedisKey.getEditLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), querySellerShop);
            //判断是个人店铺还是企业店铺
            resultObjectVO = feignSellerShopService.findByUser(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {

                if(logoFile!=null) {
                    //LOGO上传
                    String logoImgExt = ImageUtils.getImageExt(logoFile.getOriginalFilename());
                    if (logoImgExt.indexOf(".") != -1) {
                        logoImgExt = logoImgExt.substring(logoImgExt.indexOf(".") + 1, logoImgExt.length());
                    }
                    String logoImgFilePath = imageUploadService.uploadFile(logoFile.getBytes(), logoImgExt);
                    sellerShopVO.setLogo(logoImgFilePath);
                }

                //该账号存在店铺
                SellerShopVO sellerShopVORet = resultObjectVO.formatData(SellerShopVO.class);
                if(sellerShopVORet!=null&&sellerShopVORet.getEnableStatus().intValue()==1)
                {
                    sellerShopVO.setUserMainId(Long.parseLong(userMainId));
                    requestJsonVO = RequestJsonVOGenerator.generator(this.getAppCode(), sellerShopVO);
                    resultObjectVO = feignSellerShopService.updateInfo(requestJsonVO.sign(), requestJsonVO);
                }
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("修改失败,请稍后重试");
            logger.warn(e.getMessage(),e);
        }finally{
            redisLock.unLock(ShopRegistRedisKey.getEditLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }

}
