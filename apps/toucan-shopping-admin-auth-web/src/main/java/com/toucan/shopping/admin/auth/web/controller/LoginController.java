package com.toucan.shopping.admin.auth.web.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.admin.auth.web.util.VCodeUtil;
import com.toucan.shopping.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.admin.auth.export.entity.Admin;
import com.toucan.shopping.admin.auth.web.vo.admin.AdminVO;
import com.toucan.shopping.common.util.GlobalUUID;
import com.toucan.shopping.common.util.SignUtil;
import com.toucan.shopping.common.util.VerifyCodeUtil;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.common.vo.ResultVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private FeignAdminService feignAdminService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public String loginPage()
    {
        return "login.html";
    }


    /**
     * 管理员账号登录
     * @param response
     * @param adminVo
     * @return
     */
    @RequestMapping(value = "/submit",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO login(@RequestHeader("Cookie") String cookie,HttpServletResponse response, @RequestBody AdminVO adminVo)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(adminVo.getVcode()))
            {
                resultObjectVO.setMsg("登录失败,请输入验证码");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(cookie))
            {
                resultObjectVO.setMsg("登录失败,请检查是否禁用Cookie");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String ClientVCodeId = VCodeUtil.getClientVCodeId(cookie);
            if(StringUtils.isEmpty(ClientVCodeId))
            {
                resultObjectVO.setMsg("登录失败,验证码异常");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String vcodeRedisKey = appCode+"_vcode_"+ClientVCodeId;

            Object vCodeObject = stringRedisTemplate.opsForValue().get(vcodeRedisKey);
            if(vCodeObject==null)
            {
                resultObjectVO.setMsg("登录失败,验证码过期请刷新页面");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            if(!StringUtils.equals(adminVo.getVcode(),String.valueOf(vCodeObject)))
            {
                resultObjectVO.setMsg("登录失败,验证码输入有误");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            //删除缓存中验证码
            stringRedisTemplate.delete(vcodeRedisKey);

            String entityJson = JSONObject.toJSONString(adminVo);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setSign(SignUtil.sign(appCode, entityJson));
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignAdminService.login(requestVo.getSign(),requestVo);
            if(resultObjectVO.getCode()== ResultVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Admin admin = JSONObject.parseObject(JSON.toJSONString(resultObjectVO.getData()), Admin.class);
                    Cookie adminIdCookie = new Cookie("adminId",admin.getAdminId());
                    adminIdCookie.setPath("/");
                    //1小时过期
                    adminIdCookie.setMaxAge(3600);
                    response.addCookie(adminIdCookie);

                    Cookie loginTokenCookie = new Cookie("loginToken",admin.getLoginToken());
                    loginTokenCookie.setPath("/");
                    //1小时过期
                    loginTokenCookie.setMaxAge(3600);
                    response.addCookie(loginTokenCookie);
                }
            }

        }catch(Exception e)
        {
            resultObjectVO.setMsg("登录失败,请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    @RequestMapping(value="/vcode", method = RequestMethod.GET)
    public void verifyCode(HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            int w = 200, h = 80;
            //生成4位验证码
            String code = VerifyCodeUtil.generateVerifyCode(4);


            //生成客户端验证码ID
            String vcodeKey = GlobalUUID.uuid();

            String vcodeRedisKey = appCode+"_vcode_"+vcodeKey;
            stringRedisTemplate.opsForValue().set(vcodeRedisKey,code);
            stringRedisTemplate.expire(vcodeRedisKey,60, TimeUnit.SECONDS);

            Cookie clientVCodeId = new Cookie("clientVCodeId",vcodeKey);
            clientVCodeId.setPath("/");
            //60秒过期
            clientVCodeId.setMaxAge(60);
            response.addCookie(clientVCodeId);

            VerifyCodeUtil.outputImage(w, h, outputStream, code);
        } catch (IOException e) {
            logger.warn(e.getMessage(),e);
        }finally{
            if(outputStream!=null)
            {
                try {
                    outputStream.flush();
                    outputStream.close();
                }catch(Exception e)
                {
                    logger.warn(e.getMessage(),e);
                }
            }
        }
    }

}

