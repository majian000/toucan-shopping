package com.toucan.shopping.modules.admin.auth.business.service;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.admin.auth.page.AppPageInfo;
import com.toucan.shopping.modules.admin.auth.redis.AdminAuthRedisKey;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AppService;
import com.toucan.shopping.modules.admin.auth.service.OrgnazitionAppService;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.common.util.AlphabetNumberUtils;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;

/**
 * 管理员应用管理
 */
@Service
public class AppBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());



    @Autowired
    private AppService appService;

    @Autowired
    private AdminAppService adminAppService;

    @Autowired
    private OrgnazitionAppService orgnazitionAppService;


    /**
     * 添加应用
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            App app = JSONObject.parseObject(requestVo.getEntityJson(),App.class);
            Check.notEmpty(app.getName(), ResultVO.FAILD, "添加失败,请输入应用名称");
            Check.notEmpty(app.getCode(), ResultVO.FAILD, "添加失败,请输入应用编码");
            Check.isTrue(AlphabetNumberUtils.isAlphabetNumber(app.getCode(),1,8), ResultVO.FAILD, "添加失败,应用编码只允许字母、数字、下划线组成,长度1-8位");

            //应用编码全局唯一,删除的数据也会被校验
            //删除应用的话并没有级联删除所有业务数据,如果编码重复,进行编码查询的话会将旧应用的数据也查询出来
            Check.isTrue(!appService.existsByCode(app.getCode()), ResultVO.FAILD, "该应用编码已被使用了!");
            app.setCreateDate(new Date());
            app.setEnableStatus((short)1);
            app.setDeleteStatus((short)0);
            int row = appService.save(app);
            Check.isTrue(row >= 1, ResultVO.FAILD, "添加失败,请重试!");

            resultObjectVO.setData(app);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "添加失败,请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 編輯应用
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            App app = JSONObject.parseObject(requestVo.getEntityJson(),App.class);
            Check.notEmpty(app.getName(), ResultVO.FAILD, "请传入应用名称");
            Check.notEmpty(app.getCode(), ResultVO.FAILD, "请传入应用编码");
            Check.notNull(app.getId(), ResultVO.FAILD, "请传入应用ID");


            App query=new App();
            query.setId(app.getId());
            query.setDeleteStatus((short)0);
            List<App> appList = appService.findListByEntity(query);
            Check.isTrue(!CollectionUtils.isEmpty(appList), ResultVO.FAILD, "该应用不存在!");
            Check.isTrue(StringUtils.equals(appList.get(0).getCode(),app.getCode()), ResultVO.FAILD, "应用编码不允许修改!");

            app.setUpdateDate(new Date());
            int row = appService.update(app);
            Check.isTrue(row >= 1, ResultVO.FAILD, "请重试!");

            AdminAuthCacheHelper.getAppCacheService().deleteByAppCode(app.getCode());

            //应用禁用
            if(app.getEnableStatus()!=null&&app.getEnableStatus().intValue()==0)
            {
                //直接删除所有关联这个应用的登录账号会话
                AdminApp adminApp = new AdminApp();
                adminApp.setAppCode(app.getCode());
                List<AdminApp> adminApps = adminAppService.findListByEntity(adminApp);
                for(AdminApp aa:adminApps)
                {
                    //删除所有的登录会话
                    AdminAuthCacheHelper.getAdminLoginCacheService().deleteLoginToken(aa.getAdminId(),aa.getAppCode());
                    //更新登录状态
                    adminAppService.updateLoginStatus(aa.getAdminId(), aa.getAppCode(), (short) 0);
                }

            }

            resultObjectVO.setData(app);

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
    public ResultObjectVO listPage(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AppPageInfo appPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), AppPageInfo.class);
            resultObjectVO.setData(appService.queryListPage(appPageInfo));

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
     * 查询列表
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO list(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            App app = JSONObject.parseObject(requestVo.getEntityJson(), App.class);
            resultObjectVO.setData(appService.findListByEntity(app));
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
     * 查询列表
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListByCodes(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AppVO appVO = JSONObject.parseObject(requestVo.getEntityJson(), AppVO.class);
            resultObjectVO.setData(appService.queryListByCodesIngoreDelete(appVO.getCodes()));
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
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            App app = JSONObject.parseObject(requestVo.getEntityJson(),App.class);
            Check.notNull(app.getId(), ResultVO.FAILD, "没有找到应用ID");

            //查询是否存在该应用
            App query=new App();
            query.setId(app.getId());
            List<App> appList = appService.findListByEntity(query);
            Check.isTrue(!CollectionUtils.isEmpty(appList), ResultVO.FAILD, "应用不存在!");
            resultObjectVO.setData(appList);

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
     * 根据编码查询启用状态
     * @param requestVo
     * @return true:启用 false:停用
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryEnableStatusByCode(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setData(false);

        try {
            App app = JSONObject.parseObject(requestVo.getEntityJson(), App.class);
            Check.notNull(app.getCode(), ResultVO.FAILD, "没有找到应用编码");
            AppVO appVO = AdminAuthCacheHelper.getAppCacheService().findByAppCode(app.getCode());
            if(appVO!=null)
            {
                if(appVO.getEnableStatus()!=null&&appVO.getEnableStatus().intValue()==1)
                {
                    resultObjectVO.setData(true);
                }else{
                    resultObjectVO.setData(false);
                }
                return resultObjectVO;
            }
            //查询是否存在该应用
            app = appService.findByAppCode(app.getCode());
            if(app!=null&&app.getEnableStatus()!=null&&app.getEnableStatus().intValue()==1)
            {
                resultObjectVO.setData(true);
            }
            if(app!=null)
            {
                appVO = new AppVO();
                BeanUtils.copyProperties(appVO,app);
                AdminAuthCacheHelper.getAppCacheService().save(appVO);
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
     * 根据编码查询
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findByCode(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            App app = JSONObject.parseObject(requestVo.getEntityJson(),App.class);
            Check.notNull(app.getCode(), ResultVO.FAILD, "没有找到应用编码");

            //查询是否存在该应用
            App query=new App();
            query.setCode(app.getCode());
            List<App> appList = appService.findListByEntity(query);
            Check.isTrue(!CollectionUtils.isEmpty(appList), ResultVO.FAILD, "应用不存在!");
            resultObjectVO.setData(appList.get(0));

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
     * 删除指定应用
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            App app = JSONObject.parseObject(requestVo.getEntityJson(),App.class);
            Check.notNull(app.getId(), ResultVO.FAILD, "没有找到应用ID");

            //查询是否存在该应用
            App query=new App();
            query.setId(app.getId());
            List<App> appList = appService.findListByEntity(query);
            Check.isTrue(!CollectionUtils.isEmpty(appList), ResultVO.FAILD, "应用不存在!");


            int row = appService.deleteById(app.getId(),app.getUpdateAdminId());
            Check.isTrue(row >= 1, ResultVO.FAILD, "请重试!");

            //删除应用下所有关联

            AdminApp queryAdminApp =new AdminApp();
            queryAdminApp.setAppCode(appList.get(0).getCode());

            List<AdminApp> adminApps = adminAppService.findListByEntity(queryAdminApp);
            if(!CollectionUtils.isEmpty(adminApps)) {
                row = adminAppService.deleteByAppCode(appList.get(0).getCode());

                Check.isTrue(row > 0, ResultVO.FAILD, "删除应用下所有管理账户失败!");
            }

            //删除应用机构关联
            orgnazitionAppService.deleteByAppCode(appList.get(0).getCode());


            resultObjectVO.setData(app);

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
     * 批量删除应用
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<App> appList = JSON.parseArray(requestVo.getEntityJson(),App.class);
            Check.isTrue(!CollectionUtils.isEmpty(appList), ResultVO.FAILD, "没有找到应用ID");
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(App app:appList) {
                if(app.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(app);

                    //查询是否存在该应用
                    App query = new App();
                    query.setId(app.getId());
                    List<App> appEntityList = appService.findListByEntity(query);
                    if (CollectionUtils.isEmpty(appEntityList)) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("应用不存在!");
                        continue;
                    }


                    int row = appService.deleteById(app.getId());
                    if (row < 1) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请重试!");
                        continue;
                    }

                    //删除应用下所有关联
                    AdminApp queryAdminApp =new AdminApp();
                    queryAdminApp.setAppCode(appList.get(0).getCode());

                    List<AdminApp> adminApps = adminAppService.findListByEntity(queryAdminApp);
                    if(!CollectionUtils.isEmpty(adminApps)) {
                        row = adminAppService.deleteByAppCode(appList.get(0).getCode());

                        if (row <= 0) {
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg("删除应用下所有管理账户失败!");
                            continue;
                        }
                    }

                    //删除应用机构关联
                    orgnazitionAppService.deleteByAppCode(appList.get(0).getCode());

                }
            }
            resultObjectVO.setData(resultObjectVOList);

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
     * 查询全部启用应用(无分页)
     * @param requestVo
     * @return
     */
    public ResultObjectVO queryAllList(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            resultObjectVO.setData(appService.findAllEnabled());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }


}
