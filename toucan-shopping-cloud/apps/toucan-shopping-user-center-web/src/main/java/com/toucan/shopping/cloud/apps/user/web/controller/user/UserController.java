package com.toucan.shopping.cloud.apps.user.web.controller.user;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
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
import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.es.service.UserElasticSearchService;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.vo.UserRegistVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends UIController {

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

    @Autowired
    private FeignFunctionService feignFunctionService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {

        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/user/listPage",feignFunctionService);
        return "pages/user/db/list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/registPage",method = RequestMethod.GET)
    public String registPage()
    {
        return "pages/user/db/regist.html";
    }


    /**
     * 手机号列表页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/mobilePhoneListPage/{userMainId}",method = RequestMethod.GET)
    public String mobilePhoneListPage(HttpServletRequest request,@PathVariable String userMainId)
    {
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/mobile_phone_list.html";
    }



    /**
     * 添加手机号页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/addMobilePhonePage/{userMainId}",method = RequestMethod.GET)
    public String addMobilePhonePage(HttpServletRequest request,@PathVariable String userMainId)
    {
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/add_mobile_phone.html";
    }






    /**
     * 查询列表
     * @param userPageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(UserPageInfo userPageInfo)
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




    /**
     * 手机号列表
     * @param userPageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/mobile/phone/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO mobilePhoneList(UserPageInfo userPageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),userPageInfo);
            ResultObjectVO resultObjectVO = feignUserService.mobilePhoneList(SignUtil.sign(requestVo),requestVo);
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


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value="/regist")
    @ResponseBody
    public ResultObjectVO regist(@RequestBody UserRegistVO user){
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
        String mobilePhone = user.getMobilePhone();
        String lockKey = toucan.getAppCode()+"_user_regist_mobile_"+mobilePhone;
        try {

            boolean lockStatus = redisLock.lock(lockKey, user.getMobilePhone());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }


            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,user);
            //查询是否已注册用户名
            if(StringUtils.isNotEmpty(user.getUsername()))
            {
                resultObjectVO = feignUserService.findUsernameListByUsername(requestJsonVO.sign(),requestJsonVO);
                if(!resultObjectVO.isSuccess())
                {
                    return resultObjectVO;
                }
                List userNames = (List)resultObjectVO.formatData(ArrayList.class);
                if(!CollectionUtils.isEmpty(userNames))
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("用户名已注册");
                    return resultObjectVO;
                }
            }

            //查询是否已注册邮箱
            if(StringUtils.isNotEmpty(user.getEmail()))
            {
                resultObjectVO = feignUserService.findEmailListByEmail(requestJsonVO.sign(),requestJsonVO);
                if(!resultObjectVO.isSuccess())
                {
                    return resultObjectVO;
                }
                List emails = (List)resultObjectVO.formatData(ArrayList.class);
                if(!CollectionUtils.isEmpty(emails))
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("邮箱已注册");
                    return resultObjectVO;
                }
            }


            logger.info(" 用户注册 {} ", user.getMobilePhone());

            resultObjectVO = feignUserService.registByMobilePhone(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                //拿到用户主ID
                UserRegistVO userRegistResult = (UserRegistVO) resultObjectVO.formatData(UserRegistVO.class);
                user.setUserMainId(userRegistResult.getUserMainId());

                //如果输入了用户名,进行用户名的关联
                if(StringUtils.isNotEmpty(user.getUsername())) {
                    requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode, user);
                    resultObjectVO = feignUserService.connectUsername(requestJsonVO.sign(), requestJsonVO);
                }

                //如果输入了邮箱,进行邮箱关联
                if (StringUtils.isNotEmpty(user.getEmail())) {
                    requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode, user);
                    resultObjectVO = feignUserService.connectEmail(requestJsonVO.sign(), requestJsonVO);
                }

                //修改用户详情
                requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode, user);
                resultObjectVO = feignUserService.updateDetail(requestJsonVO.sign(), requestJsonVO);
            }

            resultObjectVO.setData(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("注册失败,请稍后重试");
        }finally{
            redisLock.unLock(lockKey, mobilePhone);
        }
        return resultObjectVO;
    }


    /**
     * 添加手机号
     * @param user
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value="/add/mobile/phone")
    @ResponseBody
    public ResultObjectVO addMobilePhone(@RequestBody UserRegistVO user){
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
        String mobilePhone = user.getMobilePhone();
        String lockKey = toucan.getAppCode()+"_user_regist_mobile_"+mobilePhone;
        try {

            boolean lockStatus = redisLock.lock(lockKey, user.getMobilePhone());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }


            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,user);
            //查询是否已注册用户名
            if(StringUtils.isNotEmpty(user.getUsername()))
            {
                resultObjectVO = feignUserService.findUsernameListByUsername(requestJsonVO.sign(),requestJsonVO);
                if(!resultObjectVO.isSuccess())
                {
                    return resultObjectVO;
                }
                List userNames = (List)resultObjectVO.formatData(ArrayList.class);
                if(!CollectionUtils.isEmpty(userNames))
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("用户名已注册");
                    return resultObjectVO;
                }
            }

            //查询是否已注册邮箱
            if(StringUtils.isNotEmpty(user.getEmail()))
            {
                resultObjectVO = feignUserService.findEmailListByEmail(requestJsonVO.sign(),requestJsonVO);
                if(!resultObjectVO.isSuccess())
                {
                    return resultObjectVO;
                }
                List emails = (List)resultObjectVO.formatData(ArrayList.class);
                if(!CollectionUtils.isEmpty(emails))
                {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("邮箱已注册");
                    return resultObjectVO;
                }
            }


            logger.info(" 用户注册 {} ", user.getMobilePhone());

            resultObjectVO = feignUserService.registByMobilePhone(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                //拿到用户主ID
                UserRegistVO userRegistResult = (UserRegistVO) resultObjectVO.formatData(UserRegistVO.class);
                user.setUserMainId(userRegistResult.getUserMainId());

                //如果输入了用户名,进行用户名的关联
                if(StringUtils.isNotEmpty(user.getUsername())) {
                    requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode, user);
                    resultObjectVO = feignUserService.connectUsername(requestJsonVO.sign(), requestJsonVO);
                }

                //如果输入了邮箱,进行邮箱关联
                if (StringUtils.isNotEmpty(user.getEmail())) {
                    requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode, user);
                    resultObjectVO = feignUserService.connectEmail(requestJsonVO.sign(), requestJsonVO);
                }

                //修改用户详情
                requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode, user);
                resultObjectVO = feignUserService.updateDetail(requestJsonVO.sign(), requestJsonVO);
            }

            resultObjectVO.setData(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("注册失败,请稍后重试");
        }finally{
            redisLock.unLock(lockKey, mobilePhone);
        }
        return resultObjectVO;
    }




    /**
     * 禁用/启用
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/disabled/enabled/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledById(HttpServletRequest request,  @PathVariable String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            User user =new User();
            user.setUserMainId(Long.parseLong(id));

            String entityJson = JSONObject.toJSONString(user);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignUserService.disabledEnabledById(SignUtil.sign(requestVo),requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 批量禁用
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/disabled/ids",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledByIds(HttpServletRequest request, @RequestBody List<UserVO> userVOS)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(CollectionUtils.isEmpty(userVOS))
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(userVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = feignUserService.disabledByIds(SignUtil.sign(requestVo), requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}

