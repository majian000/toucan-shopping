package com.toucan.shopping.modules.admin.auth.business.service;


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
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 管理员 增删改查
 */
@Service
public class AdminBusinessService {


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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Admin admin = JSONObject.parseObject(requestVo.getEntityJson(),Admin.class);
            Check.notEmpty(admin.getUsername(), AdminResultVO.NOT_FOUND_USERNAME, "添加失败,请输入账号");

            if(admin.getUsername().length()>20)
            {
                return ResultObjectVO.fail(AdminResultVO.FAILD, "登录失败,账号长度不能大与20位");
            }
            Check.notEmpty(admin.getPassword(), AdminResultVO.PASSWORD_NOT_FOUND, "添加失败,请输入密码");

            Check.isTrue(AdminRegistUtil.checkPwd(admin.getPassword()), AdminResultVO.PASSWORD_ERROR, "添加失败,请输入6至25位的密码并且只能是字母、数字或下划线");


            Admin query=new Admin();
            query.setUsername(admin.getUsername());
            query.setDeleteStatus((short)0);
            Check.isTrue(CollectionUtils.isEmpty(adminService.findListByEntity(query)), AdminResultVO.FAILD, "账号已注册!");
            admin.setId(idGenerator.id());
            admin.setCreateDate(new Date());
            admin.setPassword(MD5Util.md5(admin.getPassword()));
            admin.setEnableStatus((short)1);
            admin.setDeleteStatus((short)0);
            admin.setAdminId(GlobalUUID.uuid());
            int row = adminService.save(admin);
            if (row < 1) {

                return ResultObjectVO.fail(AdminResultVO.FAILD, "添加失败,请重试!");
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

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListByEntity(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Admin adminQuery = JSONObject.parseObject(requestVo.getEntityJson(),Admin.class);
            List<Admin> admins = adminService.findListByEntity(adminQuery);
            resultObjectVO.setData(admins);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据实体查询对象
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryVOByEntity(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Admin adminQuery = JSONObject.parseObject(requestVo.getEntityJson(),Admin.class);
            AdminVO adminVO = adminService.findVOByEntity(adminQuery);
            resultObjectVO.setData(adminVO);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO updatePassword(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AdminVO adminVO = JSONObject.parseObject(requestVo.getEntityJson(),AdminVO.class);

            Check.notEmpty(adminVO.getPassword(), AdminResultVO.PASSWORD_NOT_FOUND, "请输入密码");

            Check.isTrue(AdminRegistUtil.checkPwd(adminVO.getPassword()), AdminResultVO.PASSWORD_ERROR, "请输入6至25位的密码并且只能是字母、数字或下划线");
            adminVO.setPassword(MD5Util.md5(adminVO.getPassword()));
            int row = adminService.updatePassword(adminVO);
            if (row < 1) {

                return ResultObjectVO.fail(AdminResultVO.FAILD, "请重试!");
            }

            resultObjectVO.setData(adminVO);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO login(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            String entityJson = requestVo.getEntityJson();
            AdminVO admin =JSONObject.parseObject(entityJson,AdminVO.class);
            Check.notEmpty(admin.getUsername(), AdminResultVO.NOT_FOUND_USERNAME, "登录失败,请输入账号");
            if(admin.getUsername().length()>20)
            {
                return ResultObjectVO.fail(AdminResultVO.FAILD, "登录失败,账号长度不能大与20位");
            }

            Check.notEmpty(admin.getPassword(), AdminResultVO.PASSWORD_NOT_FOUND, "登录失败,请输入密码");

            if(admin.getPassword().length()>25)
            {
                return ResultObjectVO.fail(AdminResultVO.PASSWORD_NOT_FOUND, "登录失败,密码长度不能大与25位");
            }
            Admin query=new Admin();
            query.setUsername(admin.getUsername());
            query.setDeleteStatus((short)0);
            query.setEnableStatus((short)1);
            List<Admin> adminPersistence = adminService.findListByEntity(query);
            if(CollectionUtils.isEmpty(adminPersistence))
            {
                return ResultObjectVO.fail(AdminResultVO.FAILD, "登录失败,账号不存在!");
            }

            admin.setAdminId(adminPersistence.get(0).getAdminId());

            Check.isTrue(MD5Util.md5(admin.getPassword()).equals(adminPersistence.get(0).getPassword()), AdminResultVO.FAILD, "登录失败,密码输入有误!");

            AdminApp queryAdminApp =new AdminApp();
            queryAdminApp.setDeleteStatus((short)0);
            queryAdminApp.setAdminId(adminPersistence.get(0).getAdminId());
            queryAdminApp.setAppCode(requestVo.getAppCode());
            List<AdminApp> adminApps=adminAppService.findListByEntity(queryAdminApp);
            if(CollectionUtils.isEmpty(adminApps))
            {
                return ResultObjectVO.fail(AdminResultVO.FAILD, "登录失败,没有权限登录应用!");
            }
            App app = appService.findByAppCode(requestVo.getAppCode());
            if(app!=null&&app.getEnableStatus()!=null&&app.getEnableStatus().intValue()==0)
            {
                return ResultObjectVO.fail(AdminResultVO.FAILD, "登录失败,该应用已被禁用!");
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

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO logout(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            String entityJson = requestVo.getEntityJson();
            Admin admin =JSONObject.parseObject(entityJson,Admin.class);
            Check.notEmpty(admin.getAdminId(), AdminResultVO.FAILD, "请传入adminId");
            Check.notEmpty(admin.getLoginToken(), AdminResultVO.FAILD, "请传入登录token");


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
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Admin admin = JSONObject.parseObject(requestVo.getEntityJson(),Admin.class);
            Check.notNull(admin.getId(), ResultVO.FAILD, "没有找到ID");

            //查询是否存在该应用
            Admin query=new Admin();
            query.setId(admin.getId());
            List<Admin> adminList = adminService.findListByEntity(query);
            if(CollectionUtils.isEmpty(adminList))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "账号不存在!");
            }

            for(Admin adminEntity:adminList)
            {
                AdminApp queryAdminApp = new AdminApp();
                queryAdminApp.setAdminId(adminEntity.getAdminId());
                queryAdminApp.setDeleteStatus((short)0);
                adminEntity.setAdminApps(adminAppService.findListByEntity(queryAdminApp));
            }
            resultObjectVO.setData(adminList);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryLoginToken(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try{
            String entityJson = requestVo.getEntityJson();
            Admin admin =JSONObject.parseObject(entityJson,Admin.class);
            Check.notEmpty(admin.getUsername(), ResultVO.FAILD, "请传入账号");
            Admin queryAdmin = new Admin();
            queryAdmin.setUsername(admin.getUsername());
            queryAdmin.setDeleteStatus((short)0);
            List<Admin> adminList = adminService.findListByEntity(queryAdmin);
            if(CollectionUtils.isEmpty(adminList))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "账号不存在");
            }

            admin.setAdminId(adminList.get(0).getAdminId());

            Object loginTokenObject = AdminAuthCacheHelper.getAdminLoginCacheService().getLoginToken(admin.getAdminId(),requestVo.getAppCode());
            if(loginTokenObject!=null) {
                admin.setLoginToken(String.valueOf(loginTokenObject));
            }

            resultObjectVO.setData(admin);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO isOnline(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setData(false);
        try{
            String entityJson = requestVo.getEntityJson();
            Admin admin =JSONObject.parseObject(entityJson,Admin.class);
            if(StringUtils.isEmpty(admin.getUsername())&&StringUtils.isEmpty(admin.getAdminId()))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "请传入账号或管理员ID");
            }
            Check.notEmpty(admin.getLoginToken(), ResultVO.FAILD, "请传入loginToken");
            if(StringUtils.isEmpty(admin.getAdminId()))
            {
                Admin queryAdmin = new Admin();
                queryAdmin.setUsername(admin.getUsername());
                queryAdmin.setDeleteStatus((short) 0);
                List<Admin> adminList = adminService.findListByEntity(queryAdmin);
                if (CollectionUtils.isEmpty(adminList)) {
                    return ResultObjectVO.fail(ResultVO.FAILD, "账号不存在");
                }

                admin.setAdminId(adminList.get(0).getAdminId());
            }

            try {
                Object loginTokenObject = AdminAuthCacheHelper.getAdminLoginCacheService().getLoginToken(admin.getAdminId(), requestVo.getAppCode());
                if (loginTokenObject != null) {
                    if (StringUtils.equals(admin.getLoginToken(), String.valueOf(loginTokenObject))) {
                        resultObjectVO.setData(true);
                    }
                }
            }catch(BusinessValidationException e){
                return ResultObjectVO.fail(e.getCode(), e.getMessage());
            } catch (Exception redisEx) {
                logger.warn("Redis 查询登录会话异常，临时放行 adminId={}: {}", admin.getAdminId(), redisEx.getMessage());
                resultObjectVO.setData(true);
                return resultObjectVO;
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Admin admin = JSONObject.parseObject(requestVo.getEntityJson(),Admin.class);
            Check.notEmpty(admin.getAdminId(), AdminResultVO.FAILD, "修改失败,adminId为空");
            Check.notEmpty(admin.getUsername(), AdminResultVO.NOT_FOUND_USERNAME, "修改失败,请输入账号");

            if(admin.getUsername().length()>20)
            {
                return ResultObjectVO.fail(AdminResultVO.FAILD, "修改失败,账号长度不能大与20位");
            }

            Admin query=new Admin();
            query.setUsername(admin.getUsername());
            query.setDeleteStatus((short)0);
            List<Admin> queryAdmins = adminService.findListByEntity(query);
            if(!CollectionUtils.isEmpty(queryAdmins))
            {
                if(!queryAdmins.get(0).getAdminId().equals(admin.getAdminId()))
                {
                    return ResultObjectVO.fail(AdminResultVO.FAILD, "账号已存在!");
                }
            }
            int row = adminService.update(admin);
            if (row < 1) {

                return ResultObjectVO.fail(AdminResultVO.FAILD, "修改失败,请重试!");
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


        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO list(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AdminPageInfo adminPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), AdminPageInfo.class);


            //查询账号主表
            PageInfo<AdminVO> pageInfo =  adminService.queryListPage(adminPageInfo);
            resultObjectVO.setData(pageInfo);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Admin entity = JSONObject.parseObject(requestVo.getEntityJson(),Admin.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到账号ID");

            //查询是否存在该角色
            Admin query=new Admin();
            query.setId(entity.getId());
            List<Admin> adminList = adminService.findListByEntity(query);
            if(CollectionUtils.isEmpty(adminList))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "账号不存在!");
            }


            int row = adminService.deleteById(entity.getId());
            if (row < 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
            }

            //删除账号应用关联
            adminAppService.deleteByAdminId(adminList.get(0).getAdminId());

            //删除账号角色关联
            adminRoleService.deleteByAdminId(adminList.get(0).getAdminId());


            //删除账号部门关联
            adminOrgnazitionService.deleteByAdminId(adminList.get(0).getAdminId());

            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<Admin> admins = JSONObject.parseArray(requestVo.getEntityJson(),Admin.class);
            if(CollectionUtils.isEmpty(admins))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找到账号ID");
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

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




}
