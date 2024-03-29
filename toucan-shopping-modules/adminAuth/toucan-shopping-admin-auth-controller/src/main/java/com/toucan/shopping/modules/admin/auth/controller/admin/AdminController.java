package com.toucan.shopping.modules.admin.auth.controller.admin;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.cache.service.AdminRoleCacheService;
import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.admin.auth.page.AdminPageInfo;
import com.toucan.shopping.modules.admin.auth.redis.AdminAuthRedisKey;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.service.impl.AdminLoginHistoryAsyncService;
import com.toucan.shopping.modules.admin.auth.service.impl.AdminLoginHistoryServiceImpl;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.AdminRegistUtil;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.util.MD5Util;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AdminRoleService adminRoleService;

    @Autowired
    private AdminOrgnazitionService adminOrgnazitionService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AppService appService;

    @Autowired
    private AdminLoginHistoryAsyncService adminLoginHistoryAsyncService;

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

            if(!AdminRegistUtil.checkPwd(admin.getPassword()))
            {
                resultObjectVO.setCode(AdminResultVO.PASSWORD_ERROR);
                resultObjectVO.setMsg("添加失败,请输入6至25位的密码并且只能是字母、数字或下划线");
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
            admin.setId(idGenerator.id());
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
     * 根据实体查询对象
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryListByEntity",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryListByEntity(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到参数");
            return resultObjectVO;
        }

        try {
            Admin adminQuery = JSONObject.parseObject(requestVo.getEntityJson(),Admin.class);
            List<Admin> admins = adminService.findListByEntity(adminQuery);
            resultObjectVO.setData(admins);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 修改密码
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update/password",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updatePassword(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USER);
            resultObjectVO.setMsg("修改失败,没有找到账号");
            return resultObjectVO;
        }

        try {
            AdminVO adminVO = JSONObject.parseObject(requestVo.getEntityJson(),AdminVO.class);

            if(StringUtils.isEmpty(adminVO.getPassword()))
            {
                resultObjectVO.setCode(AdminResultVO.PASSWORD_NOT_FOUND);
                resultObjectVO.setMsg("请输入密码");
                return resultObjectVO;
            }

            if(!AdminRegistUtil.checkPwd(adminVO.getPassword()))
            {
                resultObjectVO.setCode(AdminResultVO.PASSWORD_ERROR);
                resultObjectVO.setMsg("请输入6至25位的密码并且只能是字母、数字或下划线");
                return resultObjectVO;
            }
            adminVO.setPassword(MD5Util.md5(adminVO.getPassword()));
            int row = adminService.updatePassword(adminVO);
            if (row < 1) {

                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            resultObjectVO.setData(adminVO);

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
            AdminVO admin =JSONObject.parseObject(entityJson,AdminVO.class);
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

            if(admin.getPassword().length()>25)
            {
                resultObjectVO.setCode(AdminResultVO.PASSWORD_NOT_FOUND);
                resultObjectVO.setMsg("登录失败,密码长度不能大与25位");
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
            App app = appService.findByAppCode(requestVo.getAppCode());
            if(app!=null&&app.getEnableStatus()!=null&&app.getEnableStatus().intValue()==0)
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("登录失败,该应用已被禁用!");
                return resultObjectVO;
            }

            String loginToken =UUID.randomUUID().toString().replace("-","");
            admin.setLoginToken(loginToken);

            //保存登录缓存
            AdminAuthCacheHelper.getAdminLoginCacheService().loginToken(admin.getAdminId(),requestVo.getAppCode(),loginToken);

            //设置登录状态
            adminAppService.updateLoginStatus(queryAdminApp.getAdminId(),queryAdminApp.getAppCode(),(short)1);
            resultObjectVO.setData(admin);

            //保存登录日志
            adminLoginHistoryAsyncService.asyncSave(adminPersistence.get(0).getAdminId(),requestVo.getAppCode(),admin.getLoginIp(),admin.getLoginSrcType());

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
            resultObjectVO.setMsg("没有找到要注销的用户");
            return resultObjectVO;
        }

        try {
            String entityJson = requestVo.getEntityJson();
            Admin admin =JSONObject.parseObject(entityJson,Admin.class);
            if(StringUtils.isEmpty(admin.getAdminId()))
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("请传入adminId");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(admin.getLoginToken()))
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("请传入登录token");
                return resultObjectVO;
            }


            Object loginTokenObject = AdminAuthCacheHelper.getAdminLoginCacheService().getLoginToken(admin.getAdminId(),requestVo.getAppCode());
            if(loginTokenObject!=null)
            {
                String redisLoginToken = String.valueOf(loginTokenObject);
                if(redisLoginToken.equals(admin.getLoginToken()))
                {
                    //删除对应的登录会话
                    AdminAuthCacheHelper.getAdminLoginCacheService().deleteLoginToken(admin.getAdminId(),requestVo.getAppCode());

                    //更新登录状态
                    adminAppService.updateLoginStatus(admin.getAdminId(),requestVo.getAppCode(),(short)0);
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
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            Admin admin = JSONObject.parseObject(requestVo.getEntityJson(),Admin.class);
            if(admin.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该应用
            Admin query=new Admin();
            query.setId(admin.getId());
            List<Admin> adminList = adminService.findListByEntity(query);
            if(CollectionUtils.isEmpty(adminList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("账号不存在!");
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
            resultObjectVO.setMsg("请稍后重试");
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
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        try{
            String entityJson = requestVo.getEntityJson();
            Admin admin =JSONObject.parseObject(entityJson,Admin.class);
            if(StringUtils.isEmpty(admin.getUsername()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入账号");
                return resultObjectVO;
            }
            Admin queryAdmin = new Admin();
            queryAdmin.setUsername(admin.getUsername());
            queryAdmin.setDeleteStatus((short)0);
            List<Admin> adminList = adminService.findListByEntity(queryAdmin);
            if(CollectionUtils.isEmpty(adminList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("账号不存在");
                return resultObjectVO;
            }

            admin.setAdminId(adminList.get(0).getAdminId());

            Object loginTokenObject = AdminAuthCacheHelper.getAdminLoginCacheService().getLoginToken(admin.getAdminId(),requestVo.getAppCode());
            if(loginTokenObject!=null) {
                admin.setLoginToken(String.valueOf(loginTokenObject));
            }

            resultObjectVO.setData(admin);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
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
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        try{
            String entityJson = requestVo.getEntityJson();
            Admin admin =JSONObject.parseObject(entityJson,Admin.class);
            if(StringUtils.isEmpty(admin.getUsername())&&StringUtils.isEmpty(admin.getAdminId()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入账号或管理员ID");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(admin.getLoginToken()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入loginToken");
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
                    resultObjectVO.setMsg("账号不存在");
                    return resultObjectVO;
                }

                admin.setAdminId(adminList.get(0).getAdminId());
            }

            Object loginTokenObject = AdminAuthCacheHelper.getAdminLoginCacheService().getLoginToken(admin.getAdminId(),requestVo.getAppCode());
            if (loginTokenObject != null) {
                if(StringUtils.equals(admin.getLoginToken(),String.valueOf(loginTokenObject)))
                {
                    resultObjectVO.setData(true);
                }
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
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
            resultObjectVO.setMsg("没有找到实体对象");
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


            AdminApp queryAdminApp = new AdminApp();
            queryAdminApp.setAdminId(admin.getAdminId());
            //查询出当前账号数据库中保存的应用关联
            List<AdminApp> adminAppPersistentList = adminAppService.findListByEntity(queryAdminApp);

            //账号被禁用
            if (admin.getEnableStatus() != null && admin.getEnableStatus().intValue() == 0) {
                if (!CollectionUtils.isEmpty(adminAppPersistentList)) {
                    for (AdminApp adminApp : adminAppPersistentList) {
                        //删除登录会话
                        AdminAuthCacheHelper.getAdminLoginCacheService().deleteLoginToken(adminApp.getAdminId(),adminApp.getAppCode());

                        //更新登录状态
                        adminAppService.updateLoginStatus(adminApp.getAdminId(), adminApp.getAppCode(), (short) 0);
                    }
                }
            }

            //如果这次没有勾选任何应用
            if(CollectionUtils.isEmpty(admin.getAdminApps()))
            {
                //清空账号所有应用会话
                if (!CollectionUtils.isEmpty(adminAppPersistentList)) {
                    for (AdminApp adminApp : adminAppPersistentList) {
                        //删除登录会话
                        AdminAuthCacheHelper.getAdminLoginCacheService().deleteLoginToken(adminApp.getAdminId(),adminApp.getAppCode());

                        //更新登录状态
                        adminAppService.updateLoginStatus(adminApp.getAdminId(), adminApp.getAppCode(), (short) 0);
                    }
                }
            }else{
                boolean find=false;
                //遍历旧的关联,找到这次新操作中 删除旧的那个关联
                for(AdminApp adminAppPersistent:adminAppPersistentList) {
                    find = false;
                    for (AdminApp adminApp : admin.getAdminApps()) {
                        if(adminAppPersistent.getAppCode().equals(adminApp.getAppCode()))
                        {
                            find=true;
                            break;
                        }
                    }
                    //如果旧的关联 不包含在新的操作中,那么就删除旧关联的会话
                    if(!find) {
                        //删除登录会话
                        AdminAuthCacheHelper.getAdminLoginCacheService().deleteLoginToken(adminAppPersistent.getAdminId(),adminAppPersistent.getAppCode());

                        //更新登录状态
                        adminAppService.updateLoginStatus(adminAppPersistent.getAdminId(), adminAppPersistent.getAppCode(), (short) 0);
                    }
                }
            }


            //清空现有关联
            adminAppService.deleteByAdminId(admin.getAdminId());

            if(!CollectionUtils.isEmpty(admin.getAdminApps())) {
                //重新保存关联
                for (AdminApp adminApp : admin.getAdminApps()) {
                    adminApp.setAdminId(admin.getAdminId());
                    adminApp.setDeleteStatus((short) 0);
                    adminApp.setCreateDate(new Date());
                    adminApp.setCreateAdminId(admin.getUpdateAdminId());
                    adminAppService.save(adminApp);
                }
            }


            resultObjectVO.setData(admin);


        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
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
            resultObjectVO.setMsg("没有找到实体对象");
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


            //查询账号主表
            PageInfo<AdminVO> pageInfo =  adminService.queryListPage(adminPageInfo);
            resultObjectVO.setData(pageInfo);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            Admin entity = JSONObject.parseObject(requestVo.getEntityJson(),Admin.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到账号ID");
                return resultObjectVO;
            }

            //查询是否存在该角色
            Admin query=new Admin();
            query.setId(entity.getId());
            List<Admin> adminList = adminService.findListByEntity(query);
            if(CollectionUtils.isEmpty(adminList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("账号不存在!");
                return resultObjectVO;
            }


            int row = adminService.deleteById(entity.getId());
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            //删除账号应用关联
            adminAppService.deleteByAdminId(adminList.get(0).getAdminId());

            //删除账号角色关联
            adminRoleService.deleteByAdminId(adminList.get(0).getAdminId());


            //删除账号部门关联
            adminOrgnazitionService.deleteByAdminId(adminList.get(0).getAdminId());

            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<Admin> admins = JSONObject.parseArray(requestVo.getEntityJson(),Admin.class);
            if(CollectionUtils.isEmpty(admins))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到账号ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(Admin admin:admins) {
                if(admin.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(admin);

                    //查询是否存在该角色
                    Admin query = new Admin();
                    query.setId(admin.getId());
                    List<Admin> adminList = adminService.findListByEntity(query);
                    if (CollectionUtils.isEmpty(adminList)) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("账号不存在!");
                        continue;
                    }


                    int row = adminService.deleteById(admin.getId());
                    if (row < 1) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请重试!");
                        continue;
                    }

                    //删除账号应用关联
                    adminAppService.deleteByAdminId(adminList.get(0).getAdminId());


                    //删除账号角色关联
                    adminRoleService.deleteByAdminId(adminList.get(0).getAdminId());

                    //删除账号部门关联
                    adminOrgnazitionService.deleteByAdminId(adminList.get(0).getAdminId());

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




}
