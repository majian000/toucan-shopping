package com.toucan.shopping.modules.user.business.service;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.sms.constant.SmsTypeConstant;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.redis.UserCenterSendRegistSmsRedisKey;
import com.toucan.shopping.modules.user.service.UserService;
import com.toucan.shopping.modules.user.vo.UserSmsVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;

@Service
public class SmsBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;




    /**
     * 发送短信验证码
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO send(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        UserSmsVO userSmsVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserSmsVO.class);
        Check.notNull(userSmsVO, UserRegistConstant.NOT_FOUND_USER, "短信发送失败,没有找到目标");

        Check.notEmpty(userSmsVO.getMobilePhone(), UserRegistConstant.NOT_FOUND_MOBILE, "短信发送失败,请输入手机号");

        Check.isTrue(userSmsVO.getType()!=null && (userSmsVO.getType().intValue()==SmsTypeConstant.USER_REGIST_TYPE||userSmsVO.getType().intValue()==SmsTypeConstant.USER_LOGIN_TYPE||userSmsVO.getType().intValue()==SmsTypeConstant.SHOP_REGIST_TYPE), UserRegistConstant.NOT_FOUND_SMS_TYPE, "短信发送失败,请选择发送类型");

        try {

            boolean lockStatus = skylarkLock.lock(UserCenterSendRegistSmsRedisKey.getSendRegistVerifyCodeLockKey(userSmsVO.getMobilePhone()), userSmsVO.getMobilePhone());
            if (!lockStatus) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请求超时,请稍后重试");
            }

//            int a = 1/0;

            //调用第三方短信接口
            logger.info("{} send messsage {}",userSmsVO.getMobilePhone(),userSmsVO.getMsg());

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "发送短信验证码失败,请稍后重试");
        }finally {
            skylarkLock.unLock(UserCenterSendRegistSmsRedisKey.getSendRegistVerifyCodeLockKey(userSmsVO.getMobilePhone()), userSmsVO.getMobilePhone());
        }

        return resultObjectVO;
    }




}
