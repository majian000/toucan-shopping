package com.toucan.shopping.modules.admin.auth.business.service;


import com.alibaba.fastjson2.JSONObject;
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
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 管理员应用 增删改查
 */
@Service
public class AdminAppBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private AdminAppService adminAppService;



    /**
     * 保存管理员账户
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AdminApp adminApp = JSONObject.parseObject(requestVo.getEntityJson(),AdminApp.class);
            Check.notEmpty(adminApp.getAppCode(), AdminResultVO.FAILD, "请传入应用编码");
            Check.notEmpty(adminApp.getAdminId(), AdminResultVO.FAILD, "请传入账号ID");

            adminApp.setCreateDate(new Date());
            adminApp.setDeleteStatus((short)0);
            int row = adminAppService.save(adminApp);
            Check.isTrue(row >= 1, ResultVO.FAILD, "添加失败,创建当前账号与该应用关联失败!");

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
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
            AdminApp adminAppQuery = JSONObject.parseObject(requestVo.getEntityJson(),AdminApp.class);
            List<AdminApp> adminApps = adminAppService.findListByEntity(adminAppQuery);
            resultObjectVO.setData(adminApps);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
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
            AdminAppPageInfo adminAppPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), AdminAppPageInfo.class);


            //查询账号应用
            PageInfo<AdminAppVO> pageInfo =  adminAppService.queryListPage(adminAppPageInfo);
            resultObjectVO.setData(pageInfo);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 查询在线用户列表分页
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO onlineList(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AdminAppPageInfo adminAppPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), AdminAppPageInfo.class);


            //查询账号应用
            PageInfo<AdminAppVO> pageInfo =  adminAppService.queryOnlineListPage(adminAppPageInfo);
            resultObjectVO.setData(pageInfo);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 查询在线用户列表分页
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO logout(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AdminAppVO adminAppVO = JSONObject.parseObject(requestVo.getEntityJson(), AdminAppVO.class);

            Check.notNull(adminAppVO.getId(), ResultVO.FAILD, "没有找到ID");

            adminAppVO = adminAppService.findById(adminAppVO.getId());
            adminAppService.updateLoginStatus(adminAppVO.getAdminId(),adminAppVO.getAppCode(),(short)0);

            //删除缓存
            AdminAuthCacheHelper.getAdminLoginCacheService().deleteLoginToken(adminAppVO.getAdminId(),adminAppVO.getAppCode());

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 查询登录列表分页(给定时任务刷新状态使用)
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO loginList(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AdminAppPageInfo adminAppPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), AdminAppPageInfo.class);


            //查询账号应用登录列表
            PageInfo<AdminAppVO> pageInfo =  adminAppService.queryLoginListPage(adminAppPageInfo);
            resultObjectVO.setData(pageInfo);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 根据实体查询对象
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryAppListByAdminId(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AdminApp adminAppQuery = JSONObject.parseObject(requestVo.getEntityJson(),AdminApp.class);
            List<AdminAppVO> adminAppVOs = adminAppService.findAppListByAdminAppEntity(adminAppQuery);
            resultObjectVO.setData(adminAppVOs);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据应用编码删除
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByAppCode(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AdminApp adminApp = JSONObject.parseObject(requestVo.getEntityJson(),AdminApp.class);
            resultObjectVO.setData(adminAppService.deleteByAppCode(adminApp.getAppCode()));

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 修改账号登录状态
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO batchUpdateLoginStatus(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<AdminAppVO> adminApps = requestVo.formatEntityList(AdminAppVO.class);
            if(CollectionUtils.isNotEmpty(adminApps))
            {
                for(AdminAppVO adminAppVO:adminApps) {
                    if(adminAppVO!=null) {
                        try {
                            adminAppService.updateLoginStatus(adminAppVO.getAdminId(), adminAppVO.getAppCode(), adminAppVO.getLoginStatus());

                        }catch(Exception e)
                        {
                            logger.warn(e.getMessage(),e);
                        }
                    }
                }
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询APP登录用户信息
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryAppLoginUserCountList(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AppLoginUserVO appLoginUserVO = JSONObject.parseObject(requestVo.getEntityJson(), AppLoginUserVO.class);
            List<AppLoginUserVO> appLoginUserVOS = adminAppService.queryAppLoginUserCountList(appLoginUserVO);
            resultObjectVO.setData(appLoginUserVOS);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }




}
