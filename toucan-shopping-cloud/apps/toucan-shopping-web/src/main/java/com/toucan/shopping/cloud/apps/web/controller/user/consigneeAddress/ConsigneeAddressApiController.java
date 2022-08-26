package com.toucan.shopping.cloud.apps.web.controller.user.consigneeAddress;


import com.toucan.shopping.cloud.apps.web.controller.BaseController;
import com.toucan.shopping.cloud.apps.web.redis.VerifyCodeRedisKey;
import com.toucan.shopping.cloud.apps.web.service.VerifyCodeService;
import com.toucan.shopping.cloud.apps.web.util.VCodeUtil;
import com.toucan.shopping.cloud.user.api.feign.service.FeignConsigneeAddressService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserHeadSculptureApproveService;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.ImageUtils;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.redis.UserCenterConsigneeAddressKey;
import com.toucan.shopping.modules.user.redis.UserCenterHeadSculptureApproveKey;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
import com.toucan.shopping.modules.user.vo.UserHeadSculptureApproveVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * 用户头像审核
 */
@RestController("consigneeAddressApiController")
@RequestMapping("/api/user/consigneeAddress")
public class ConsigneeAddressApiController extends BaseController {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private FeignConsigneeAddressService feignConsigneeAddressService;


    @Autowired
    private Toucan toucan;


    @UserAuth(requestType = UserAuth.REQUEST_AJAX)
    @RequestMapping(value="/save")
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody ConsigneeAddressVO consigneeAddressVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(consigneeAddressVO==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,没有找到收货信息");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,收货人不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getAddress())||StringUtils.isEmpty(consigneeAddressVO.getAddress().replace(" ","")))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,详细地址不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getProvinceCode())||StringUtils.isEmpty(consigneeAddressVO.getProvinceName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,省/直辖市不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(consigneeAddressVO.getAreaCode())||StringUtils.isEmpty(consigneeAddressVO.getAreaName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("提交失败,市区不能为空");
            return resultObjectVO;
        }


        String userMainId="-1";
        try {
            //从请求头中拿到uid
            userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()));
            consigneeAddressVO.setUserMainId(Long.parseLong(userMainId));

            if(consigneeAddressVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("提交失败,用户ID不能为空");
                return resultObjectVO;
            }


            resultObjectVO = verifyCodeService.verifyCode(request,consigneeAddressVO.getVcode(),toucan.getAppCode());
            if(resultObjectVO.isSuccess()) {
                String vcodeRedisKey = VCodeUtil.getVerifyCodeKey(toucan.getAppCode(), request);
                //删除缓存中验证码
                toucanStringRedisService.delete(vcodeRedisKey);

                resultObjectVO = feignConsigneeAddressService.save(RequestJsonVOGenerator.generator(toucan.getAppCode(),consigneeAddressVO));
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }








}
