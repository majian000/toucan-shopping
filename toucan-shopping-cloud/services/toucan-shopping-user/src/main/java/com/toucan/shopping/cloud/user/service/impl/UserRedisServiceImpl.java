package com.toucan.shopping.cloud.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.user.service.UserRedisService;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.user.entity.UserDetail;
import com.toucan.shopping.modules.user.entity.UserEmail;
import com.toucan.shopping.modules.user.entity.UserMobilePhone;
import com.toucan.shopping.modules.user.entity.UserUserName;
import com.toucan.shopping.modules.user.redis.UserCenterLoginRedisKey;
import com.toucan.shopping.modules.user.service.UserDetailService;
import com.toucan.shopping.modules.user.service.UserEmailService;
import com.toucan.shopping.modules.user.service.UserMobilePhoneService;
import com.toucan.shopping.modules.user.service.UserUserNameService;
import com.toucan.shopping.modules.user.vo.UserLoginVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserRedisServiceImpl implements UserRedisService {

    @Autowired
    private UserEmailService userEmailService;

    @Autowired
    private UserMobilePhoneService userMobilePhoneService;

    @Autowired
    private UserUserNameService userUserNameService;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Override
    public boolean flushLoginCache(String userMainId,String appCode) throws Exception {
        if(userMainId!=null) {
            String loginGroupKey = UserCenterLoginRedisKey.getLoginInfoGroupKey(userMainId);
            String loginInfoAppKey = UserCenterLoginRedisKey.getLoginInfoAppKey(userMainId, appCode);
            if (toucanStringRedisService.keys(loginGroupKey) != null) {
                UserLoginVO userLogin = new UserLoginVO();

                //查询关联手机号
                UserMobilePhone queryUserMobilePhone = new UserMobilePhone();
                queryUserMobilePhone.setUserMainId(Long.parseLong(userMainId));
                List<UserMobilePhone> userMobilePhones = userMobilePhoneService.findListByEntity(queryUserMobilePhone);
                if (!CollectionUtils.isEmpty(userMobilePhones)) {
                    if (userMobilePhones.get(0) != null && userMobilePhones.get(0).getUserMainId() != null) {
                        userLogin.setMobilePhone(userMobilePhones.get(0).getMobilePhone());
                    }
                }

                //查询邮箱
                List<UserEmail> userEmails = userEmailService.findListByUserMainId(Long.parseLong(userMainId));
                if (!CollectionUtils.isEmpty(userEmails)) {
                    if (userEmails.get(0) != null && userEmails.get(0).getUserMainId() != null) {
                        userLogin.setEmail(userEmails.get(0).getEmail());
                    }
                }

                //查询用户名
                List<UserUserName> userUserNames = userUserNameService.findListByUserMainId(Long.parseLong(userMainId));
                if (!CollectionUtils.isEmpty(userUserNames)) {
                    if (userUserNames.get(0) != null && userUserNames.get(0).getUserMainId() != null) {
                        userLogin.setUsername(userUserNames.get(0).getUsername());
                    }
                }


                List<UserDetail> userDetails = userDetailService.findByUserMainId(Long.parseLong(userMainId));
                if (CollectionUtils.isNotEmpty(userDetails)) {
                    UserDetail userDetail = userDetails.get(0);
                    userLogin.setNickName(userDetail.getNickName());
                    userLogin.setTrueName(userDetail.getTrueName()); //姓名
                    userLogin.setIdCard(userDetail.getIdCard()); //身份证
                    userLogin.setHeadSculpture(userDetail.getHeadSculpture()); //头像
                    userLogin.setSex(userDetail.getSex()); //性别
                    userLogin.setType(userDetail.getType()); //用户类型
                    userLogin.setIdcardType(userDetail.getIdcardType());
                    userLogin.setIdcardImg1(userDetail.getIdcardImg1());
                    userLogin.setIdcardImg2(userDetail.getIdcardImg2());
                    userLogin.setTrueNameStatus(userDetail.getTrueNameStatus());
                }


                toucanStringRedisService.put(loginGroupKey,
                        loginInfoAppKey, JSONObject.toJSONString(userLogin));
                //设置登录token5个小时超时
                toucanStringRedisService.expire(loginGroupKey,
                        UserCenterLoginRedisKey.LOGIN_TIMEOUT_SECOND, TimeUnit.SECONDS);

            }
        }
        return false;
    }
}
