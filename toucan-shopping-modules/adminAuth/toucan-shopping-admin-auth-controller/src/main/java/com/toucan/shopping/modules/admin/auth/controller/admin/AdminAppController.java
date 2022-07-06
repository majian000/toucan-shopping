package com.toucan.shopping.modules.admin.auth.controller.admin;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.admin.auth.page.AdminAppPageInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AdminService;
import com.toucan.shopping.modules.admin.auth.service.AppService;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.AppLoginUserVO;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 管理员应用 增删改查
 */
@RestController
@RequestMapping("/adminApp")
public class AdminAppController {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private AdminAppService adminAppService;



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
            resultObjectVO.setMsg("没有找到参数");
            return resultObjectVO;
        }

        try {
            AdminApp adminApp = JSONObject.parseObject(requestVo.getEntityJson(),AdminApp.class);
            if(StringUtils.isEmpty(adminApp.getAppCode()))
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("请传入应用编码");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(adminApp.getAdminId()))
            {
                resultObjectVO.setCode(AdminResultVO.FAILD);
                resultObjectVO.setMsg("请传入账号ID");
                return resultObjectVO;
            }

            adminApp.setCreateDate(new Date());
            adminApp.setDeleteStatus((short)0);
            int row = adminAppService.save(adminApp);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,创建当前账号与该应用关联失败!");
                return resultObjectVO;
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
     * 根据实体查询对象
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryListByEntity",produces = "application/json;charset=UTF-8")
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
            AdminApp adminAppQuery = JSONObject.parseObject(requestVo.getEntityJson(),AdminApp.class);
            List<AdminApp> adminApps = adminAppService.findListByEntity(adminAppQuery);
            resultObjectVO.setData(adminApps);

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
            AdminAppPageInfo adminAppPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), AdminAppPageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }


            //查询账号应用
            PageInfo<AdminAppVO> pageInfo =  adminAppService.queryListPage(adminAppPageInfo);
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
     * 查询在线用户列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/online/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO onlineList(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AdminAppPageInfo adminAppPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), AdminAppPageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }


            //查询账号应用
            PageInfo<AdminAppVO> pageInfo =  adminAppService.queryOnlineListPage(adminAppPageInfo);
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
     * 查询在线用户列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/logout",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO logout(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AdminAppVO adminAppVO = JSONObject.parseObject(requestVo.getEntityJson(), AdminAppVO.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }
            if(adminAppVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            adminAppVO = adminAppService.findById(adminAppVO.getId());
            adminAppService.updateLoginStatus(adminAppVO.getAdminId(),adminAppVO.getAppCode(),(short)0);

            //删除缓存
            AdminAuthCacheHelper.getAdminLoginCacheService().deleteLoginToken(adminAppVO.getAdminId(),adminAppVO.getAppCode());

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 查询登录列表分页(给定时任务刷新状态使用)
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/login/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO loginList(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AdminAppPageInfo adminAppPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), AdminAppPageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }


            //查询账号应用登录列表
            PageInfo<AdminAppVO> pageInfo =  adminAppService.queryLoginListPage(adminAppPageInfo);
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
     * 根据实体查询对象
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryAppListByAdminId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryAppListByAdminId(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到参数");
            return resultObjectVO;
        }

        try {
            AdminApp adminAppQuery = JSONObject.parseObject(requestVo.getEntityJson(),AdminApp.class);
            List<AdminAppVO> adminAppVOs = adminAppService.findAppListByAdminAppEntity(adminAppQuery);
            resultObjectVO.setData(adminAppVOs);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据应用编码删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/deleteByAppCode",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO deleteByAppCode(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到参数");
            return resultObjectVO;
        }

        try {
            AdminApp adminApp = JSONObject.parseObject(requestVo.getEntityJson(),AdminApp.class);
            resultObjectVO.setData(adminAppService.deleteByAppCode(adminApp.getAppCode()));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 修改账号登录状态
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/batchUpdateLoginStatus",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO batchUpdateLoginStatus(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到参数");
            return resultObjectVO;
        }

        try {
            List<AdminAppVO> adminApps = requestVo.formatEntityList(AdminAppVO.class);
            if(CollectionUtils.isNotEmpty(adminApps))
            {
                for(AdminAppVO adminAppVO:adminApps) {
                    if(adminAppVO!=null) {
                        try {
                            if(adminAppVO.getLoginStatus().intValue()==0)
                            {
                                adminAppService.updateLoginStatus(adminAppVO.getAdminId(), adminAppVO.getAppCode(), adminAppVO.getLoginStatus(),null);
                            }else{
                                adminAppService.updateLoginStatus(adminAppVO.getAdminId(), adminAppVO.getAppCode(), adminAppVO.getLoginStatus(),new Date());
                            }

                        }catch(Exception e)
                        {
                            logger.warn(e.getMessage(),e);
                        }
                    }
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
     * 查询APP登录用户信息
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryAppLoginUserCountList",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryAppLoginUserCountList(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(AdminResultVO.NOT_FOUND_USER);
            resultObjectVO.setMsg("没有找到参数");
            return resultObjectVO;
        }

        try {
            AppLoginUserVO appLoginUserVO = JSONObject.parseObject(requestVo.getEntityJson(), AppLoginUserVO.class);
            List<AppLoginUserVO> appLoginUserVOS = adminAppService.queryAppLoginUserCountList(appLoginUserVO);
            resultObjectVO.setData(appLoginUserVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




}
