package com.toucan.shopping.cloud.apps.web.service.impl;

import com.toucan.shopping.cloud.apps.web.service.LoginUserService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginUserServiceImpl implements LoginUserService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Override
    public void setAttributeUser(HttpServletRequest request)
    {
        try {
            UserVO queryUserVO = new UserVO();
            queryUserVO.setUserMainId(Long.parseLong(UserAuthHeaderUtil.getUserMainId( request.getHeader(toucan.getUserAuth().getHttpToucanAuthHeader()))));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryUserVO);
            ResultObjectVO resultObjectVO = feignUserService.queryLoginInfo(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                UserVO userVO = resultObjectVO.formatData(UserVO.class);
                if(userVO!=null&& StringUtils.isNotEmpty(userVO.getHeadSculpture())) {
                    userVO.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix()+"/"+userVO.getHeadSculpture());
                }else{
                    userVO.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix()+"/"+toucan.getUser().getDefaultHeadSculpture());
                }
                if(StringUtils.isNotEmpty(userVO.getMobilePhone())
                        &&StringUtils.isNotEmpty(userVO.getEmail())
                        &&(userVO.getTrueNameStatus()!=null&&userVO.getTrueNameStatus().intValue()==1))
                {
                    userVO.setSecurityLevel(2); //高级别
                }
                request.setAttribute("userVO",userVO);
            }else{
                request.setAttribute("userVO",new UserVO());
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            request.setAttribute("userVO",new UserVO());
        }
    }

}
