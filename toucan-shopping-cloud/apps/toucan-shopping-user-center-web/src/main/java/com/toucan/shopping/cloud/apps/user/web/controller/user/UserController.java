package com.toucan.shopping.cloud.apps.user.web.controller.user;


import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.lock.redis.RedisLock;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.PhoneUtils;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.util.UserRegistUtil;
import com.toucan.shopping.modules.common.util.UsernameUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.es.service.UserElasticSearchService;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.vo.UserRegistVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private UserElasticSearchService userElasticSearchService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String page()
    {
        return "pages/user/db/list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/registByMobilePage",method = RequestMethod.GET)
    public String addByMobilePage()
    {
        return "pages/user/db/registbymobile.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/registByUserNamePage",method = RequestMethod.GET)
    public String registByUserNamePage()
    {
        return "pages/user/db/registbyusername.html";
    }





    /**
     * 查询列表
     * @param userPageInfo
     * @return
     */
    @AdminAuth
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO listPage(HttpServletRequest request, UserPageInfo userPageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),userPageInfo);
            ResultObjectVO resultObjectVO = feignUserService.list(SignUtil.sign(requestVo),requestVo);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total"))));
                    if(tableVO.getCount()>0) {
                        tableVO.setData((List<Object>) resultObjectDataMap.get("list"));
                    }
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("请求失败,请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }





    @RequestMapping(value="/regist/mobile/phone")
    @ResponseBody
    public ResultObjectVO registMyMobilePhone(UserRegistVO user){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(user==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("注册失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(user.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("注册失败,请输入注册手机号");
            return resultObjectVO;
        }

        if(!PhoneUtils.isChinaPhoneLegal(user.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("注册失败,手机号错误");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(user.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("注册失败,请输入密码");
            return resultObjectVO;
        }
        if(!StringUtils.equals(user.getPassword(),user.getConfirmPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("注册失败,密码与确认密码不一致");
            return resultObjectVO;
        }

        if(!UserRegistUtil.checkPwd(user.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_ERROR);
            resultObjectVO.setMsg("注册失败,请输入6至15位的密码");
            return resultObjectVO;
        }


        //商城应用编码
        String shoppingAppCode = "10001001";
        String lockKey = toucan.getAppCode()+"_user_regist_mobile_"+user.getMobilePhone();
        try {

            boolean lockStatus = redisLock.lock(lockKey, user.getMobilePhone());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,user);

            logger.info(" 用户注册 {} ", user.getMobilePhone());

            resultObjectVO = feignUserService.registByMobilePhone(SignUtil.sign(requestJsonVO),requestJsonVO);

            resultObjectVO.setData(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("注册失败,请稍后重试");
        }finally{
            redisLock.unLock(lockKey, user.getMobilePhone());
        }
        return resultObjectVO;
    }




    @RequestMapping(value="/regist/username")
    @ResponseBody
    public ResultObjectVO registByUserName(UserRegistVO user){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(user==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("注册失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(user.getUsername()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USERNAME);
            resultObjectVO.setMsg("注册失败,请输入用户名");
            return resultObjectVO;
        }

        if(!UsernameUtils.isUsername(user.getUsername()))
        {
            resultObjectVO.setCode(UserRegistConstant.USERNAME_ERROR);
            resultObjectVO.setMsg("注册失败,用户名格式错误");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(user.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("注册失败,请输入密码");
            return resultObjectVO;
        }
        if(!StringUtils.equals(user.getPassword(),user.getConfirmPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("注册失败,密码与确认密码不一致");
            return resultObjectVO;
        }

        if(!UserRegistUtil.checkPwd(user.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_ERROR);
            resultObjectVO.setMsg("注册失败,请输入6至15位的密码");
            return resultObjectVO;
        }


        //商城应用编码
        String shoppingAppCode = "10001001";
        String lockKey = toucan.getAppCode()+"_user_regist_username_"+user.getMobilePhone();
        try {

            boolean lockStatus = redisLock.lock(lockKey, user.getMobilePhone());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,user);

            logger.info(" 用户注册 {} ", user.getMobilePhone());

            resultObjectVO = feignUserService.registByUsername(SignUtil.sign(requestJsonVO),requestJsonVO);

            resultObjectVO.setData(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("注册失败,请稍后重试");
        }finally{
            redisLock.unLock(lockKey, user.getMobilePhone());
        }
        return resultObjectVO;
    }


}

