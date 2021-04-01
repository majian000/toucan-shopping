package com.toucan.shopping.center.user.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.toucan.shopping.center.user.constant.UserLoginConstant;
import com.toucan.shopping.center.user.constant.UserRegistConstant;
import com.toucan.shopping.center.user.entity.UserDetail;
import com.toucan.shopping.center.user.entity.UserMobilePhone;
import com.toucan.shopping.center.user.entity.UserUserName;
import com.toucan.shopping.center.user.page.UserPageInfo;
import com.toucan.shopping.center.user.vo.UserVO;
import com.toucan.shopping.common.generator.IdGenerator;
import com.toucan.shopping.center.user.redis.UserCenterLoginRedisKey;
import com.toucan.shopping.center.user.redis.UserCenterRegistRedisKey;
import com.toucan.shopping.center.user.service.*;
import com.toucan.shopping.center.user.vo.UserLoginVO;
import com.toucan.shopping.center.user.vo.UserRegistVO;
import com.toucan.shopping.center.user.vo.UserResultVO;
import com.toucan.shopping.common.util.*;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.lock.redis.RedisLock;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.common.vo.ResultVO;
import com.toucan.shopping.center.user.entity.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户注册、用户登录
 */
@RestController
@RequestMapping("/user")
public class UserController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserRedisService userRedisService;

    @Autowired
    private UserElasticSearchService userElasticSearchService;

    @Autowired
    private UserMobilePhoneService userMobilePhoneService;

    @Autowired
    private UserUserNameService userUserNameService;

    @Autowired
    private UserDetailService userDetailService;

    @RequestMapping(value="/find/mobile/phone",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findByMobilePhone(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("查询失败,没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("查询失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        try {

            if(StringUtils.isEmpty(userRegistVO.getMobilePhone())||!PhoneUtils.isChinaPhoneLegal(userRegistVO.getMobilePhone()))
            {
                resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
                resultObjectVO.setMsg("查询失败,手机号错误");
                return resultObjectVO;
            }
            List<UserMobilePhone> userEntityList = userMobilePhoneService.findListByMobilePhone(userRegistVO.getMobilePhone());
            resultObjectVO.setData(userEntityList);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("注册失败,请稍后重试");
        }
        return resultObjectVO;
    }


    @RequestMapping(value="/regist/mobile/phone",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO registByMobilePhone(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("注册失败,没有找到要注册的用户");
            return resultObjectVO;
        }

        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("登录失败,没有找到应用编码");
            return resultObjectVO;
        }
        UserRegistVO userRegistVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserRegistVO.class);
        if(userRegistVO==null)
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("注册失败,没有找到要注册的用户");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userRegistVO.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.NOT_FOUND_MOBILE);
            resultObjectVO.setMsg("注册失败,请输入注册手机号");
            return resultObjectVO;
        }

        if(!PhoneUtils.isChinaPhoneLegal(userRegistVO.getMobilePhone()))
        {
            resultObjectVO.setCode(UserRegistConstant.MOBILE_ERROR);
            resultObjectVO.setMsg("注册失败,手机号错误");
            return resultObjectVO;
        }

        if(StringUtils.isEmpty(userRegistVO.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("注册失败,请输入密码");
            return resultObjectVO;
        }

        if(!UserRegistUtil.checkPwd(userRegistVO.getPassword()))
        {
            resultObjectVO.setCode(UserRegistConstant.PASSWORD_ERROR);
            resultObjectVO.setMsg("注册失败,请输入6至15位的密码");
            return resultObjectVO;
        }



        try {
            boolean lockStatus = redisLock.lock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }

//            for(int i=0;i<500000;i++) {
//                userRegistVO.setMobilePhone(RandomUtil.random(11));
                //查询手机号是否已注册
                List<UserMobilePhone> userEntityList = userMobilePhoneService.findListByMobilePhone(userRegistVO.getMobilePhone());
                if (!CollectionUtils.isEmpty(userEntityList)) {
                    resultObjectVO.setCode(UserResultVO.FAILD);
                    resultObjectVO.setMsg("手机号码已注册!");
                } else {
                    User user = new User();
                    user.setId(idGenerator.id());
                    user.setCreateDate(new Date());
                    user.setPassword(MD5Util.md5(userRegistVO.getPassword()));
                    user.setEnableStatus((short) 1);
                    user.setDeleteStatus((short) 0);


                    //保存用户主表
                    int row = userService.save(user);
                    if (row < 1) {
                        logger.warn("用户注册失败 {}", requestJsonVO.getEntityJson());
                        resultObjectVO.setCode(UserResultVO.FAILD);
                        resultObjectVO.setMsg("注册失败,请重试!");
                    } else {
                        //保存用户手机子表
                        UserMobilePhone userMobilePhone = new UserMobilePhone();
                        userMobilePhone.setId(idGenerator.id());
                        //设置手机号
                        userMobilePhone.setMobilePhone(userRegistVO.getMobilePhone());
                        //设置用户主表ID
                        userMobilePhone.setUserMainId(user.getId());
                        userMobilePhone.setCreateDate(new Date());
                        userMobilePhone.setDeleteStatus((short) 0);
                        row = userMobilePhoneService.save(userMobilePhone);
                        if (row < 1) {
                            //修改用户主表数据删除状态
                            userService.deleteById(user.getId());
                            logger.warn("手机号注册失败 {}", requestJsonVO.getEntityJson());
                            resultObjectVO.setCode(UserResultVO.FAILD);
                            resultObjectVO.setMsg("手机号注册失败,请重试!");
                        } else {
                            //保存用户昵称
                            UserDetail userDetail = new UserDetail();
                            userDetail.setId(idGenerator.id());
                            userDetail.setUserMainId(user.getId());
                            userDetail.setNickName("用户"+userRegistVO.getMobilePhone());
                            userDetail.setSex((short)1);
                            userDetail.setCreateDate(new Date());
                            userDetail.setDeleteStatus((short) 0);

                            row = userDetailService.save(userDetail);
                            if (row < 1) {
                                logger.warn("保存默认用户昵称失败 {}",requestJsonVO.getEntityJson());
                            }else {
                                resultObjectVO.setData(userRegistVO);
                            }
                        }
                    }
                }
//            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("注册失败,请稍后重试");
        }finally{
            redisLock.unLock(UserCenterRegistRedisKey.getRegistLockKey(userRegistVO.getMobilePhone()), userRegistVO.getMobilePhone());
        }
        return resultObjectVO;
    }




    /**
     * 用户密码登录
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/login/password",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO loginByPassword(@RequestBody RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null) {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("登录失败,没有找到要登录的用户");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("登录失败,没有找到应用编码");
            return resultObjectVO;
        }

        UserLoginVO userLogin = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserLoginVO.class);
        if(userLogin==null)
        {
            resultObjectVO.setCode(UserLoginConstant.NOT_FOUND_USER);
            resultObjectVO.setMsg("登录失败,没有找到账号");
            return resultObjectVO;
        }
        logger.info(" 用户登录 {} ",requestJsonVO.getEntityJson());

        if (StringUtils.isEmpty(userLogin.getPassword())) {
            resultObjectVO.setCode(UserLoginConstant.PASSWORD_NOT_FOUND);
            resultObjectVO.setMsg("登录失败,请输入密码");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userLogin.getLoginUserName()))
        {
            resultObjectVO.setCode(UserLoginConstant.USERNAME_NOT_FOUND);
            resultObjectVO.setMsg("登录失败,请输入账号");
            return resultObjectVO;
        }

        try {
            boolean lockStatus = redisLock.lock(UserCenterLoginRedisKey.getLoginLockKey(userLogin.getLoginUserName()), userLogin.getLoginUserName());
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("超时重试");
                return resultObjectVO;
            }

            //如果当前输入的是手机号判断手机号是否存在
            Long userId = 0L;
            if(PhoneUtils.isChinaPhoneLegal(userLogin.getLoginUserName()))
            {
                List<UserMobilePhone> userMobilePhones = userMobilePhoneService.findListByMobilePhone(userLogin.getLoginUserName());
                if(!CollectionUtils.isEmpty(userMobilePhones))
                {
                    if(userMobilePhones.get(0)!=null&&userMobilePhones.get(0).getUserMainId()!=null) {
                        userId = userMobilePhones.get(0).getUserMainId();
                    }
                }

            }else if(EmailUtils.isEmail(userLogin.getLoginUserName()))
            {

            }

            List<User> users = userService.findById(userId);
            if(CollectionUtils.isEmpty(users))
            {
                resultObjectVO.setCode(UserLoginConstant.NOT_REGIST);
                resultObjectVO.setMsg("登录失败,请先注册");
            }else {
                String pwdMd5 = MD5Util.md5(userLogin.getPassword());
                for (User userEntity : users) {
                    //登录成功 生成token
                    if (pwdMd5.equals(userEntity.getPassword())) {
                        String loginTokenGroupKey =UserCenterLoginRedisKey.getLoginTokenGroupKey(String.valueOf(userEntity.getId()));
                        String loginTokenAppKey = UserCenterLoginRedisKey.getLoginTokenAppKey(String.valueOf(userEntity.getId()),requestJsonVO.getAppCode());
                        //判断是否已有登录token,如果有将删除掉
                        if (stringRedisTemplate.opsForHash().keys(loginTokenGroupKey) != null) {
                            long deleteRows = 0;
                            int tryCount = 0;
                            do {

                                deleteRows = stringRedisTemplate.opsForHash().delete(loginTokenGroupKey,loginTokenAppKey);
                                tryCount++;
                            } while (deleteRows<=0 && tryCount < 50);
                        }

                        String token = UUID.randomUUID().toString().replace("-", "");
                        stringRedisTemplate.opsForHash().put(loginTokenGroupKey,
                                loginTokenAppKey,token);
                        //设置登录token1个小时超时
                        stringRedisTemplate.expire(loginTokenGroupKey,
                                UserCenterLoginRedisKey.LOGIN_TIMEOUT_SECOND, TimeUnit.SECONDS);

                        userLogin.setId(userId);
                        userLogin.setLoginToken(token);
                        userLogin.setPassword(null);

                        resultObjectVO.setData(userLogin);

                    }
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("登录失败,请稍后重试");
        }finally{
            redisLock.unLock(UserCenterLoginRedisKey.getLoginLockKey(userLogin.getLoginUserName()), userLogin.getLoginUserName());
        }

        return resultObjectVO;
    }






    /**
     * 判断是否在线
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/is/online",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO isOnline(@RequestBody RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setData(false);
        if (requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到请求对象");
            return resultObjectVO;
        }
        try{
            String entityJson = requestVo.getEntityJson();
            UserLoginVO userLoginVO =JSONObject.parseObject(entityJson,UserLoginVO.class);
            if(userLoginVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入用户ID");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(userLoginVO.getLoginToken()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入loginToken");
                return resultObjectVO;
            }
            String loginTokenGroupKey =UserCenterLoginRedisKey.getLoginTokenGroupKey(String.valueOf(userLoginVO.getId()));
            String loginTokenAppKey = UserCenterLoginRedisKey.getLoginTokenAppKey(String.valueOf(userLoginVO.getId()),requestVo.getAppCode());

            Object loginTokenObject = stringRedisTemplate.opsForHash().get(loginTokenGroupKey,loginTokenAppKey);
            if (loginTokenObject != null) {
                if(StringUtils.equals(userLoginVO.getLoginToken(),String.valueOf(loginTokenObject)))
                {
                    resultObjectVO.setData(true);
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }






    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO list(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserPageInfo userPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), UserPageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }


            //查询用户主表
            PageInfo<UserVO> pageInfo =  userService.queryListPage(userPageInfo);
            if(CollectionUtils.isNotEmpty(pageInfo.getList()))
            {
                Long[] userIdArray = new Long[pageInfo.getList().size()];

                for(int i=0;i<pageInfo.getList().size();i++)
                {
                    userIdArray[i] = pageInfo.getList().get(i).getId();
                }

                //查询用户手机号关联表
                List<UserMobilePhone> userMobilePhones = userMobilePhoneService.queryListByUserId(userIdArray);
                if(CollectionUtils.isNotEmpty(userMobilePhones))
                {
                    //设置用户对象中的手机号
                    for(UserMobilePhone userMobilePhone:userMobilePhones)
                    {
                        for(UserVO userVO:pageInfo.getList())
                        {
                            if(userMobilePhone.getUserMainId().longValue()==userVO.getId().longValue())
                            {
                                userVO.setMobilePhone(userMobilePhone.getMobilePhone());
                                continue;
                            }
                        }
                    }
                }
                //查询用户用户名关联表
                List<UserUserName> userUserNames = userUserNameService.queryListByUserId(userIdArray);
                if(CollectionUtils.isNotEmpty(userUserNames))
                {
                    //设置用户用户名
                    for(UserUserName userUserName:userUserNames)
                    {
                        for(UserVO userVO:pageInfo.getList())
                        {
                            if(userUserName.getUserMainId().longValue()==userVO.getId().longValue())
                            {
                                userVO.setUsername(userUserName.getUsername());
                                continue;
                            }
                        }
                    }

                }
            }
            resultObjectVO.setData(pageInfo);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }



}
