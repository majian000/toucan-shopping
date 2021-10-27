package com.toucan.shopping.cloud.user.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.SmsTypeConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.redis.UserCenterSendRegistSmsRedisKey;
import com.toucan.shopping.modules.user.service.UserService;
import com.toucan.shopping.modules.user.vo.UserSmsVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信验证码
 */
@RestController
@RequestMapping("/sms")
public class SmsController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Value("${toucan.app-code}")
    private String appCode;




    /**
     * 发送短信验证码
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/send",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO send(@RequestBody RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("短信发送失败,没有找到目标用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(requestJsonVO.getAppCode()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("短信发送失败,没有找到应用编码");
            return resultObjectVO;
        }

        UserSmsVO userSmsVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserSmsVO.class);
        if (userSmsVO == null) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("短信发送失败,没有找到目标");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(userSmsVO.getMobilePhone())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("短信发送失败,请输入手机号");
            return resultObjectVO;
        }

        if (userSmsVO.getType()==null||(userSmsVO.getType().intValue()!= SmsTypeConstant.USER_REGIST_TYPE&&userSmsVO.getType().intValue()!=SmsTypeConstant.USER_LOGIN_TYPE)) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_SMS_TYPE);
            resultObjectVO.setMsg("短信发送失败,请选择发送类型");
            return resultObjectVO;
        }

        try {

            boolean lockStatus = skylarkLock.lock(UserCenterSendRegistSmsRedisKey.getSendRegistVerifyCodeLockKey(userSmsVO.getMobilePhone()), userSmsVO.getMobilePhone());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求超时,请稍后重试");
                return resultObjectVO;
            }

            //调用第三方短信接口
            logger.info("{} send messsage {}",userSmsVO.getMobilePhone(),userSmsVO.getMsg());
//            int a=1/0;

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("发送短信验证码失败,请稍后重试");
        }finally {
            skylarkLock.unLock(UserCenterSendRegistSmsRedisKey.getSendRegistVerifyCodeLockKey(userSmsVO.getMobilePhone()), userSmsVO.getMobilePhone());
        }

        return resultObjectVO;
    }




}
