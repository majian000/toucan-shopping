package com.toucan.shopping.cloud.admin.auth.controller.admin;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.page.AdminPageInfo;
import com.toucan.shopping.modules.admin.auth.redis.AdminCenterRedisKey;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AdminService;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.util.MD5Util;
import com.toucan.shopping.modules.common.util.UserRegistUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminResultVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 管理员 增删改查
 */
@RestController
@RequestMapping("/admin")
public class AdminController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminAppService adminAppService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 保存管理员账户
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USER);
            resultObjectVO.setMsg("添加失败,没有找到要注册的用户");
            return resultObjectVO;
        }

        try {
            Admin admin = JSONObject.parseObject(requestVo.getEntityJson(),Admin.class);
            if(StringUtils.isEmpty(admin.getUsername()))
            {
                resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USERNAME);
                resultObjectVO.setMsg("添加失败,请输入账号");
                return resultObjectVO;
            }

            if(admin.getUsername().length()>20)
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("登录失败,账号长度不能大与20位");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(admin.getPassword()))
            {
                resultObjectVO.setCode(AdminResultVO.PASSWORD_NOT_FOUND);
                resultObjectVO.setMsg("添加失败,请输入密码");
                return resultObjectVO;
            }

            if(!UserRegistUtil.checkPwd(admin.getPassword()))
            {
                resultObjectVO.setCode(AdminResultVO.PASSWORD_ERROR);
                resultObjectVO.setMsg("添加失败,请输入6至15位的密码");
                return resultObjectVO;
            }


            Admin query=new Admin();
            query.setUsername(admin.getUsername());
            query.setDeleteStatus((short)0);
            if(!CollectionUtils.isEmpty(adminService.findListByEntity(query)))
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("账号已注册!");
                return resultObjectVO;
            }
            admin.setCreateDate(new Date());
            admin.setPassword(MD5Util.md5(admin.getPassword()));
            admin.setEnableStatus((short)1);
            admin.setDeleteStatus((short)0);
            admin.setAdminId(GlobalUUID.uuid());
            int row = adminService.save(admin);
            if (row < 1) {

                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,请重试!");
                return resultObjectVO;
            }
            if(!CollectionUtils.isEmpty(admin.getAdminApps())) {
                for (AdminApp adminApp : admin.getAdminApps()) {
                    adminApp.setAdminId(admin.getAdminId());
                    adminApp.setDeleteStatus((short) 0);
                    adminApp.setCreateDate(new Date());
                    adminApp.setCreateAdminId(admin.getCreateAdminId());
                    adminAppService.save(adminApp);
                }
            }

            resultObjectVO.setData(admin);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("添加失败,请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 管理员账户登录
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/login",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO login(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USER);
            resultObjectVO.setMsg("登录失败,没有找到要登录的用户");
            return resultObjectVO;
        }

        try {
            String entityJson = requestVo.getEntityJson();
            Admin admin =JSONObject.parseObject(entityJson,Admin.class);
            if(StringUtils.isEmpty(admin.getUsername()))
            {
                resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USERNAME);
                resultObjectVO.setMsg("登录失败,请输入账号");
                return resultObjectVO;
            }
            if(admin.getUsername().length()>20)
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("登录失败,账号长度不能大与20位");
                return resultObjectVO;
            }

            if(StringUtils.isEmpty(admin.getPassword()))
            {
                resultObjectVO.setCode(AdminResultVO.PASSWORD_NOT_FOUND);
                resultObjectVO.setMsg("登录失败,请输入密码");
                return resultObjectVO;
            }

            if(admin.getPassword().length()>15)
            {
                resultObjectVO.setCode(AdminResultVO.PASSWORD_NOT_FOUND);
                resultObjectVO.setMsg("登录失败,密码长度不能大与15位");
                return resultObjectVO;
            }
            Admin query=new Admin();
            query.setUsername(admin.getUsername());
            query.setDeleteStatus((short)0);
            query.setEnableStatus((short)1);
            List<Admin> adminPersistence = adminService.findListByEntity(query);
            if(CollectionUtils.isEmpty(adminPersistence))
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("登录失败,账号不存在!");
                return resultObjectVO;
            }

            admin.setAdminId(adminPersistence.get(0).getAdminId());

            if(!MD5Util.md5(admin.getPassword()).equals(adminPersistence.get(0).getPassword()))
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("登录失败,密码输入有误!");
                return resultObjectVO;
            }

            AdminApp queryAdminApp =new AdminApp();
            queryAdminApp.setDeleteStatus((short)0);
            queryAdminApp.setAdminId(adminPersistence.get(0).getAdminId());
            queryAdminApp.setAppCode(requestVo.getAppCode());
            List<AdminApp> adminApps=adminAppService.findListByEntity(queryAdminApp);
            if(CollectionUtils.isEmpty(adminApps))
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("登录失败,没有权限登录应用!");
                return resultObjectVO;
            }

            String loginToken =UUID.randomUUID().toString().replace("-","");
            admin.setLoginToken(loginToken);
            //登录哈希表 例如 00DDEXXAA0_LOGIN_TOKENS:[{00DDEXXAA0_LOGIN_TOKENS_10000001:"XXXXXX"},{00DDEXXAA0_LOGIN_TOKENS_10000002:"YYYYYYY"}]
            redisTemplate.opsForHash().put(AdminCenterRedisKey.getLoginTokenGroupKey(admin.getAdminId()),
                    AdminCenterRedisKey.getLoginTokenAppKey(admin.getAdminId(),requestVo.getAppCode()),loginToken);
            //设置登录token1个小时超时
            redisTemplate.expire(AdminCenterRedisKey.getLoginTokenGroupKey(admin.getAdminId()),
                    AdminCenterRedisKey.LOGIN_TIMEOUT_SECOND, TimeUnit.SECONDS);
            resultObjectVO.setData(admin);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("登录失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 管理员账户注销
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/logout",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO logout(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USER);
            resultObjectVO.setMsg("请求失败,没有找到要注销的用户");
            return resultObjectVO;
        }

        try {
            String entityJson = requestVo.getEntityJson();
            Admin admin =JSONObject.parseObject(entityJson,Admin.class);
            if(StringUtils.isEmpty(admin.getAdminId()))
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入adminId");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(admin.getLoginToken()))
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入登录token");
                return resultObjectVO;
            }

            Object loginTokenObject = redisTemplate.opsForHash().get(AdminCenterRedisKey.getLoginTokenAppKey(admin.getAdminId(),requestVo.getAppCode()),AdminCenterRedisKey.getLoginTokenGroupKey(admin.getAdminId()));
            if(loginTokenObject!=null)
            {
                String redisLoginToken = String.valueOf(loginTokenObject);
                if(redisLoginToken.equals(admin.getLoginToken()))
                {
                    //删除对应的登录会话
                    redisTemplate.opsForHash().delete(AdminCenterRedisKey.getLoginTokenGroupKey(admin.getAdminId())
                            ,AdminCenterRedisKey.getLoginTokenAppKey(admin.getAdminId(),requestVo.getAppCode()));

                }else{
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                }
            }else{
                resultObjectVO.setCode(ResultObjectVO.FAILD);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("登录失败,请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            Admin admin = JSONObject.parseObject(requestVo.getEntityJson(),Admin.class);
            if(admin.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该应用
            Admin query=new Admin();
            query.setId(admin.getId());
            List<Admin> adminList = adminService.findListByEntity(query);
            if(CollectionUtils.isEmpty(adminList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,账号不存在!");
                return resultObjectVO;
            }

            for(Admin adminEntity:adminList)
            {
                AdminApp queryAdminApp = new AdminApp();
                queryAdminApp.setAdminId(adminEntity.getAdminId());
                queryAdminApp.setDeleteStatus((short)0);
                adminEntity.setAdminApps(adminAppService.findListByEntity(queryAdminApp));
            }
            resultObjectVO.setData(adminList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询管理员登录token
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/login/token",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryLoginToken(@RequestBody RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到请求对象");
            return resultObjectVO;
        }
        try{
            String entityJson = requestVo.getEntityJson();
            Admin admin =JSONObject.parseObject(entityJson,Admin.class);
            if(StringUtils.isEmpty(admin.getUsername()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入账号");
                return resultObjectVO;
            }
            Admin queryAdmin = new Admin();
            queryAdmin.setUsername(admin.getUsername());
            queryAdmin.setDeleteStatus((short)0);
            List<Admin> adminList = adminService.findListByEntity(queryAdmin);
            if(CollectionUtils.isEmpty(adminList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,账号不存在");
                return resultObjectVO;
            }

            admin.setAdminId(adminList.get(0).getAdminId());
            Object loginTokenObject = redisTemplate.opsForHash().get(
                    AdminCenterRedisKey.getLoginTokenGroupKey(admin.getAdminId()),
                    AdminCenterRedisKey.getLoginTokenAppKey(admin.getAdminId(), requestVo.getAppCode()));
            if(loginTokenObject!=null) {
                admin.setLoginToken(String.valueOf(loginTokenObject));
            }

            resultObjectVO.setData(admin);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
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
            Admin admin =JSONObject.parseObject(entityJson,Admin.class);
            if(StringUtils.isEmpty(admin.getUsername())&&StringUtils.isEmpty(admin.getAdminId()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入账号或管理员ID");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(admin.getLoginToken()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入loginToken");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(admin.getAdminId()))
            {
                Admin queryAdmin = new Admin();
                queryAdmin.setUsername(admin.getUsername());
                queryAdmin.setDeleteStatus((short) 0);
                List<Admin> adminList = adminService.findListByEntity(queryAdmin);
                if (CollectionUtils.isEmpty(adminList)) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请求失败,账号不存在");
                    return resultObjectVO;
                }

                admin.setAdminId(adminList.get(0).getAdminId());
            }
            Object loginTokenObject = redisTemplate.opsForHash().get(
                    AdminCenterRedisKey.getLoginTokenGroupKey(admin.getAdminId()),
                    AdminCenterRedisKey.getLoginTokenAppKey(admin.getAdminId(), requestVo.getAppCode()));
            if (loginTokenObject != null) {
                if(StringUtils.equals(admin.getLoginToken(),String.valueOf(loginTokenObject)))
                {
                    //每次操作都延长登录会话10秒钟
                    redisTemplate.opsForHash().getOperations().expire(AdminCenterRedisKey.getLoginTokenGroupKey(admin.getAdminId()),10*1000,TimeUnit.SECONDS);
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
     * 編輯账号
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            Admin admin = JSONObject.parseObject(requestVo.getEntityJson(),Admin.class);
            if(StringUtils.isEmpty(admin.getAdminId()))
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,adminId为空");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(admin.getUsername()))
            {
                resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USERNAME);
                resultObjectVO.setMsg("修改失败,请输入账号");
                return resultObjectVO;
            }

            if(admin.getUsername().length()>20)
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,账号长度不能大与20位");
                return resultObjectVO;
            }

            Admin query=new Admin();
            query.setUsername(admin.getUsername());
            query.setDeleteStatus((short)0);
            List<Admin> queryAdmins = adminService.findListByEntity(query);
            if(!CollectionUtils.isEmpty(queryAdmins))
            {
                if(!queryAdmins.get(0).getAdminId().equals(admin.getAdminId()))
                {
                    resultObjectVO.setCode(AdminResultVO.FAILD);
                    resultObjectVO.setMsg("账号已存在!");
                    return resultObjectVO;
                }
            }
            int row = adminService.update(admin);
            if (row < 1) {

                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,请重试!");
                return resultObjectVO;
            }

            //查询出当前账号数据库中保存的应用关联
            AdminApp queryAdminApp = new AdminApp();
            queryAdminApp.setAdminId(admin.getAdminId());
            List<AdminApp> adminAppPersistentList = adminAppService.findListByEntity(queryAdminApp);
            if(!CollectionUtils.isEmpty(adminAppPersistentList))
            {
                //这次修改将所有应用都取消关联了
                if(CollectionUtils.isEmpty(admin.getAdminApps()))
                {

                    //将数据库中保存的关联全部删除
                    for(AdminApp adminAppPersistent:adminAppPersistentList)
                    {
                        adminAppService.deleteByAdminIdAndAppCode(admin.getAdminId(),adminAppPersistent.getAppCode());
                    }

                }else{
                    //找到这次取消掉的关联,把那个关联删除掉
                    for(AdminApp adminAppPersistent:adminAppPersistentList)
                    {
                        boolean find=false;
                        for(AdminApp adminApp : admin.getAdminApps())
                        {
                            //如果这次修改数据库中保存的关联没有任何操作,那么就什么都不做
                            if(adminAppPersistent.getAppCode().equals(adminApp.getAppCode()))
                            {
                                find=true;
                            }
                        }
                        if(!find)
                        {
                            adminAppService.deleteByAdminIdAndAppCode(admin.getAdminId(),adminAppPersistent.getAppCode());
                        }
                    }
                }
            }

            if(!CollectionUtils.isEmpty(admin.getAdminApps())) {
                //重新保存关联
                for (AdminApp adminApp : admin.getAdminApps()) {
                    boolean find=false;
                    for(AdminApp adminAppPersistent:adminAppPersistentList) {
                        if(adminAppPersistent.getAppCode().equals(adminApp.getAppCode()))
                        {
                            find=true;
                        }
                    }
                    //如果这次保存传过来的应用已经存在关联 就什么都不做
                    if(!find) {
                        adminApp.setAdminId(admin.getAdminId());
                        adminApp.setDeleteStatus((short) 0);
                        adminApp.setCreateDate(new Date());
                        adminApp.setCreateAdminId(admin.getUpdateAdminId());
                        adminAppService.save(adminApp);
                    }
                }
            }


            resultObjectVO.setData(admin);


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
            AdminPageInfo adminPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), AdminPageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }


            //查询用户主表
            PageInfo<AdminVO> pageInfo =  adminService.queryListPage(adminPageInfo);
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
