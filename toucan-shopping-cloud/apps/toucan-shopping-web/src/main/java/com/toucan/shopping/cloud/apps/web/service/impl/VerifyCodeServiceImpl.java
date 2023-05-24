package com.toucan.shopping.cloud.apps.web.service.impl;

import com.toucan.shopping.cloud.apps.web.redis.VerifyCodeRedisKey;
import com.toucan.shopping.cloud.apps.web.service.VerifyCodeService;
import com.toucan.shopping.cloud.apps.web.util.VCodeUtil;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {


    @Autowired
    private ToucanStringRedisService toucanStringRedisService;

    @Override
    public ResultObjectVO verifyCode(HttpServletRequest request, String vode,String appCode) throws Exception {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(StringUtils.isEmpty(vode))
        {
            resultObjectVO.setMsg("提交失败,请输入验证码");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            return resultObjectVO;
        }

        String cookie = request.getHeader("Cookie");
        if(StringUtils.isEmpty(cookie))
        {
            resultObjectVO.setMsg("提交失败,请重新刷新验证码");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            return resultObjectVO;
        }
        String clientVCodeId = VCodeUtil.getClientVCodeId(cookie);
        if(StringUtils.isEmpty(clientVCodeId))
        {
            resultObjectVO.setMsg("提交失败,验证码输入有误");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            return resultObjectVO;
        }
        String vcodeRedisKey = VerifyCodeRedisKey.getVerifyCodeKey(appCode,clientVCodeId);
        Object vCodeObject = toucanStringRedisService.get(vcodeRedisKey);
        if(vCodeObject==null)
        {
            resultObjectVO.setMsg("提交失败,验证码过期请刷新");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            return resultObjectVO;
        }
        if(!StringUtils.equals(vode.toUpperCase(),String.valueOf(vCodeObject).toUpperCase()))
        {
            resultObjectVO.setMsg("提交失败,验证码输入有误");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            return resultObjectVO;
        }
        return resultObjectVO;
    }
}
