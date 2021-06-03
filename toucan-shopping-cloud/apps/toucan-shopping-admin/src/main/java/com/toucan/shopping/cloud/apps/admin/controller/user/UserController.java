package com.toucan.shopping.cloud.apps.admin.controller.user;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.apps.admin.constant.UserConstant;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.lock.redis.RedisLock;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.*;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.fastdfs.util.FastDFSClient;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.user.constant.UserRegistConstant;
import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.es.service.UserElasticSearchService;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.vo.UserRegistVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${fastdfs.http.url}")
    private String fastDfsHttpUrl;

    @Autowired
    private FastDFSClient fastDFSClient;

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
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/user/mobilePhoneListPage",feignFunctionService);
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/mobile_phone_list.html";
    }

    /**
     * 邮箱列表
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/emailListPage/{userMainId}",method = RequestMethod.GET)
    public String emailListPage(HttpServletRequest request,@PathVariable String userMainId)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/user/emailListPage",feignFunctionService);
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/email_list.html";
    }

    /**
     * 用户名列表
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/userNameListPage/{userMainId}",method = RequestMethod.GET)
    public String userNameListPage(HttpServletRequest request,@PathVariable String userMainId)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/user/userNameListPage",feignFunctionService);
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/username_list.html";
    }

    /**
     * 关联手机号页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/connectMobilePhonePage/{userMainId}",method = RequestMethod.GET)
    public String connectMobilePhonePage(HttpServletRequest request,@PathVariable String userMainId)
    {
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/connect_mobile_phone.html";
    }


    /**
     * 关联邮箱页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/connectEmailPage/{userMainId}",method = RequestMethod.GET)
    public String connectEmailPage(HttpServletRequest request,@PathVariable String userMainId)
    {
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/connect_email.html";
    }


    /**
     * 跳转到修改用户资料页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/update/detail/page/{userMainId}",method = RequestMethod.GET)
    public String updateDetailPage(HttpServletRequest request,@PathVariable String userMainId)
    {
        UserVO userVO = new UserVO();
        try {
            userVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userVO);
            ResultObjectVO resultObjectVO = feignUserService.findByUserMainId(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                userVO =  (UserVO) resultObjectVO.formatData(UserVO.class);
                if(userVO.getHeadSculpture()!=null) {
                    userVO.setHttpHeadSculpture(fastDfsHttpUrl + userVO.getHeadSculpture());
                }
                request.setAttribute("model", userVO);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("model", userVO);
        }
        return "pages/user/db/update_detail.html";
    }


    /**
     * 查看详情页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/detail/page/{userMainId}",method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request,@PathVariable String userMainId)
    {
        UserVO userVO = new UserVO();
        try {
            userVO.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userVO);
            ResultObjectVO resultObjectVO = feignUserService.findByUserMainId(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                userVO = (UserVO) resultObjectVO.formatData(UserVO.class);
                if(userVO.getHeadSculpture()!=null) {
                    userVO.setHttpHeadSculpture(fastDfsHttpUrl + userVO.getHeadSculpture());
                }
                request.setAttribute("model", userVO);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("model", userVO);
        }
        return "pages/user/db/detail.html";
    }


    /**
     * 关联用户名页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/connectUserNamePage/{userMainId}",method = RequestMethod.GET)
    public String connectUserNamePage(HttpServletRequest request,@PathVariable String userMainId)
    {
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/connect_username.html";
    }


    /**
     * 重置密码页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/resetPasswordPage/{userMainId}",method = RequestMethod.GET)
    public String resetPasswordPage(HttpServletRequest request,@PathVariable String userMainId)
    {
        request.setAttribute("userMainId",userMainId);
        return "pages/user/db/reset_password.html";
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
                    List<UserVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),UserVO.class);
                    for(UserVO userVO:list)
                    {
                        if(userVO.getHeadSculpture()!=null) {
                            userVO.setHttpHeadSculpture(fastDfsHttpUrl + userVO.getHeadSculpture());
                        }
                    }
                    if(tableVO.getCount()>0) {
                        tableVO.setData((List)list);
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





    /**
     * 邮箱列表
     * @param userPageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/email/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO emailList(UserPageInfo userPageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),userPageInfo);
            ResultObjectVO resultObjectVO = feignUserService.emailList(SignUtil.sign(requestVo),requestVo);
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
     * 用户名列表
     * @param userPageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/username/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO usernameList(UserPageInfo userPageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),userPageInfo);
            ResultObjectVO resultObjectVO = feignUserService.usernameList(SignUtil.sign(requestVo),requestVo);
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
     * 添加用户
     * @param user
     * @return
     */
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
     * 修改详情
     * @param user
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value="/update/detail")
    @ResponseBody
    public ResultObjectVO updateDetail(@RequestBody UserRegistVO user){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(user==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要修改的用户");
            return resultObjectVO;
        }

        if(user.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要用户ID");
            return resultObjectVO;
        }

        //商城应用编码
        String shoppingAppCode = "10001001";
        String userMainId = String.valueOf(user.getUserMainId());
        String lockKey = toucan.getAppCode()+"_user_update_detail_"+userMainId;
        try {

            boolean lockStatus = redisLock.lock(lockKey, userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }


            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,user);
            logger.info(" 修改详情 {} ", user.getUserMainId());
            resultObjectVO = feignUserService.updateDetail(SignUtil.sign(requestJsonVO),requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("注册失败,请稍后重试");
        }finally{
            redisLock.unLock(lockKey, userMainId);
        }
        return resultObjectVO;
    }




    /**
     * 刷新缓存
     * @param userMainId
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value="/flush/cache/{userMainId}",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO flushCache(@PathVariable String userMainId){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(StringUtils.isEmpty(userMainId))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("刷新失败,没有找到用户ID");
            return resultObjectVO;
        }


        //商城应用编码
        String shoppingAppCode = "10001001";
        try {
            UserRegistVO user = new UserRegistVO();
            user.setUserMainId(Long.parseLong(userMainId));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,user);
            resultObjectVO = feignUserService.flushCache(requestJsonVO.sign(),requestJsonVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("刷新失败,请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 修改密码
     * @param user
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value="/reset/password")
    @ResponseBody
    public ResultObjectVO resetPassword(@RequestBody UserRegistVO user){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(user==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("重置失败,没有找到要注册的用户");
            return resultObjectVO;
        }

        if(user.getUserMainId()==null)
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("重置失败,用户ID为空");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(user.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("重置失败,请输入密码");
            return resultObjectVO;
        }
        if(!StringUtils.equals(user.getPassword(),user.getConfirmPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("重置失败,密码与确认密码不一致");
            return resultObjectVO;
        }

        if(!UserRegistUtil.checkPwd(user.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_ERROR);
            resultObjectVO.setMsg("重置失败,请输入6至15位的密码");
            return resultObjectVO;
        }


        //商城应用编码
        String shoppingAppCode = "10001001";
        String userMainId = String.valueOf(user.getUserMainId());
        String lockKey = toucan.getAppCode()+"_user_reset_password_"+user.getUserMainId();
        try {

            boolean lockStatus = redisLock.lock(lockKey, userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }


            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,user);

            logger.info(" 重置密码 {} ", user.getUserMainId());

            resultObjectVO = feignUserService.resetPassword(SignUtil.sign(requestJsonVO),requestJsonVO);
            resultObjectVO.setData(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("注册失败,请稍后重试");
        }finally{
            redisLock.unLock(lockKey, userMainId);
        }
        return resultObjectVO;
    }




    /**
     * 关联手机号
     * @param user
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value="/connect/mobile/phone")
    @ResponseBody
    public ResultObjectVO connectMobilePhone(@RequestBody UserRegistVO user){
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
            resultObjectVO.setMsg("注册失败,请输入关联手机号");
            return resultObjectVO;
        }

        if(!PhoneUtils.isChinaPhoneLegal(user.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("注册失败,手机号错误");
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
            logger.info(" 关联手机号 {} ", user.getMobilePhone());

            resultObjectVO = feignUserService.connectMobilePhone(SignUtil.sign(requestJsonVO),requestJsonVO);

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
     * 关联邮箱
     * @param user
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value="/connect/email")
    @ResponseBody
    public ResultObjectVO connectEmail(@RequestBody UserRegistVO user){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(user==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(user.getEmail()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("关联失败,请输入关联邮箱");
            return resultObjectVO;
        }

        if(!EmailUtils.isEmail(user.getEmail()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("关联失败,邮箱错误");
            return resultObjectVO;
        }


        //商城应用编码
        String shoppingAppCode = "10001001";
        String email = user.getEmail();
        String lockKey = toucan.getAppCode()+"_user_regist_email_"+email;
        try {

            boolean lockStatus = redisLock.lock(lockKey, user.getEmail());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,user);
            logger.info(" 关联邮箱 {} ", user.getEmail());

            resultObjectVO = feignUserService.connectEmail(SignUtil.sign(requestJsonVO),requestJsonVO);

            resultObjectVO.setData(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("注册失败,请稍后重试");
        }finally{
            redisLock.unLock(lockKey, email);
        }
        return resultObjectVO;
    }


    /**
     * 关联用户名
     * @param user
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value="/connect/username")
    @ResponseBody
    public ResultObjectVO connectUsername(@RequestBody UserRegistVO user){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(user==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("关联失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(user.getUsername()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("关联失败,请输入关联用户名");
            return resultObjectVO;
        }

        if(!UsernameUtils.isUsername(user.getUsername()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("关联失败,用户名错误");
            return resultObjectVO;
        }


        //商城应用编码
        String shoppingAppCode = "10001001";
        String username = user.getUsername();
        String lockKey = toucan.getAppCode()+"_user_regist_username_"+username;
        try {

            boolean lockStatus = redisLock.lock(lockKey, user.getUsername());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,user);
            logger.info(" 关联用户名 {} ", user.getUsername());

            resultObjectVO = feignUserService.connectUsername(SignUtil.sign(requestJsonVO),requestJsonVO);

            resultObjectVO.setData(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("注册失败,请稍后重试");
        }finally{
            redisLock.unLock(lockKey, username);
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
     * 手机号 禁用/启用
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/mobile/phone/disabled/enabled/{id}/{mobilePhone}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledEnabledMobilePhoneByUserMainIdAndMobilePhone(HttpServletRequest request,  @PathVariable String id,  @PathVariable String mobilePhone)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            UserVO userVO =new UserVO();
            userVO.setUserMainId(Long.parseLong(id));
            userVO.setMobilePhone(mobilePhone);

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode,userVO);
            resultObjectVO = feignUserService.disabledEnabledMobilePhoneByUserMainIdAndMobilePhone(SignUtil.sign(requestVo),requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 邮箱 禁用/启用
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/email/disabled/enabled/{id}/{email}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledEnabledEmailByUserMainIdAndEmail(HttpServletRequest request,  @PathVariable String id,  @PathVariable String email)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            UserVO userVO =new UserVO();
            userVO.setUserMainId(Long.parseLong(id));
            userVO.setEmail(email);

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode,userVO);
            resultObjectVO = feignUserService.disabledEnabledEmailByUserMainIdAndEmail(SignUtil.sign(requestVo),requestVo);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 用户名 禁用/启用
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/username/disabled/enabled/{id}/{username}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO disabledEnabledUsernameByUserMainIdAndUsername(HttpServletRequest request,  @PathVariable String id,  @PathVariable String username)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            UserVO userVO =new UserVO();
            userVO.setUserMainId(Long.parseLong(id));
            userVO.setUsername(username);

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode,userVO);
            resultObjectVO = feignUserService.disabledEnabledUsernameByUserMainIdAndUsername(SignUtil.sign(requestVo),requestVo);
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



    @RequestMapping("/upload/head/sculpture")
    @ResponseBody
    public ResultObjectVO  uploadHeadSculpture(@RequestParam("file") MultipartFile file, @RequestParam("userMainId")Long userMainId)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(0);
        try{
            String fileName = file.getOriginalFilename();
            String fileExt = ".jpg";
            if(StringUtils.isNotEmpty(fileName)&&fileName.indexOf(".")!=-1)
            {
                fileExt = fileName.substring(fileName.lastIndexOf(".")+1);

            }
            String groupPath = fastDFSClient.uploadFile(file.getBytes(),fileExt);

            if(StringUtils.isEmpty(groupPath))
            {
                throw new RuntimeException("头像上传失败");
            }
            UserVO userVO = new UserVO();
            userVO.setUserMainId(userMainId);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userVO);
            ResultObjectVO userResultObjectVO = feignUserService.findByUserMainId(requestJsonVO.sign(),requestJsonVO);
            if(userResultObjectVO.isSuccess())
            {
                userVO = (UserVO) userResultObjectVO.formatData(UserVO.class);
                //如果不是默认头像就删除掉这个人的上一个头像
                if(!UserConstant.HEAD_SCULPTURE.equals(userVO.getHeadSculpture())&&StringUtils.isNotEmpty(userVO.getHeadSculpture()))
                {
                    int ret = fastDFSClient.delete_file(userVO.getHeadSculpture());
                    if(ret!=0)
                    {
                        logger.warn("删除旧头像失败 {} ",userVO.getHeadSculpture());
                    }
                }
                userVO.setHeadSculpture(groupPath);

                //商城应用编码
                String shoppingAppCode = "10001001";
                requestJsonVO = RequestJsonVOGenerator.generator(shoppingAppCode,userVO);
                userResultObjectVO = feignUserService.updateDetail(SignUtil.sign(requestJsonVO),requestJsonVO);
                if(!userResultObjectVO.isSuccess())
                {
                    resultObjectVO.setCode(1);
                    resultObjectVO.setMsg(userResultObjectVO.getMsg());

                    //设置预览头像
                    if(userVO.getHeadSculpture()!=null) {
                        userVO.setHttpHeadSculpture(fastDfsHttpUrl + userVO.getHeadSculpture());
                    }
                    resultObjectVO.setData(userVO);
                }
            }
        }catch (Exception e)
        {
            resultObjectVO.setCode(1);
            resultObjectVO.setMsg("头像上传失败");
            logger.warn(e.getMessage(),e);
        }

        return resultObjectVO;
    }




}

