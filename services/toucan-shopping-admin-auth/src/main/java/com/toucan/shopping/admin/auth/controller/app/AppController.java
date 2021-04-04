package com.toucan.shopping.admin.auth.controller.app;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.admin.auth.entity.AdminApp;
import com.toucan.shopping.admin.auth.entity.App;
import com.toucan.shopping.admin.auth.page.AppPageInfo;
import com.toucan.shopping.admin.auth.service.AdminAppService;
import com.toucan.shopping.admin.auth.service.AppService;
import com.toucan.shopping.common.util.AuthHeaderUtil;
import com.toucan.shopping.common.util.SignUtil;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.common.vo.ResultVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 管理员应用管理
 */
@RestController
@RequestMapping("/app")
public class AppController {


    private final Logger logger = LoggerFactory.getLogger(getClass());



    @Autowired
    private AppService appService;

    @Autowired
    private AdminAppService adminAppService;



    /**
     * 添加应用
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestHeader("toucan-admin-auth") String toucanAdminAuth, @RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("添加失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            App app = JSONObject.parseObject(requestVo.getEntityJson(),App.class);
            if(StringUtils.isEmpty(app.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,请输入应用名称");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(app.getCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,请输入应用编码");
                return resultObjectVO;
            }


            App query=new App();
            query.setCode(app.getCode());
            query.setDeleteStatus((short)0);
            if(!CollectionUtils.isEmpty(appService.findListByEntity(query)))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("已存在该应用编码!");
                return resultObjectVO;
            }
            app.setCreateDate(new Date());
            app.setEnableStatus((short)1);
            app.setDeleteStatus((short)0);
            int row = appService.save(app);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,请重试!");
                return resultObjectVO;
            }

            //账号与应用关联
            AdminApp adminApp = new AdminApp();
            adminApp.setCreateAdminId(app.getCreateAdminId());
            adminApp.setAppCode(app.getCode());
            adminApp.setAdminId(app.getCreateAdminId());

            adminAppService.save(adminApp);

            resultObjectVO.setData(app);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("添加失败,请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 編輯应用
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestHeader("toucan-admin-auth") String toucanAdminAuth, @RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            App app = JSONObject.parseObject(requestVo.getEntityJson(),App.class);
            if(StringUtils.isEmpty(app.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入应用名称");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(app.getCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入应用编码");
                return resultObjectVO;
            }
            if(app.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请传入应用ID");
                return resultObjectVO;
            }


            App query=new App();
            query.setId(app.getId());
            query.setDeleteStatus((short)0);
            List<App> appList = appService.findListByEntity(query);
            if(CollectionUtils.isEmpty(appList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("该应用不存在!");
                return resultObjectVO;
            }
            if(!StringUtils.equals(appList.get(0).getCode(),app.getCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("应用编码不允许修改!");
                return resultObjectVO;

            }

            app.setUpdateDate(new Date());
            app.setUpdateAdminId(AuthHeaderUtil.getAdminId(toucanAdminAuth));
            int row = appService.update(app);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }


            resultObjectVO.setData(app);

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
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO listPage(@RequestHeader("toucan-admin-auth") String toucanAdminAuth,@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AppPageInfo appPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), AppPageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(appPageInfo.getAdminId()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到管理账号ID");
                return resultObjectVO;
            }

            resultObjectVO.setData(appService.queryListPage(appPageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 删除指定应用
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestHeader("toucan-admin-auth") String toucanAdminAuth,@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            App app = JSONObject.parseObject(requestVo.getEntityJson(),App.class);
            if(app.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到应用ID");
                return resultObjectVO;
            }

            //查询是否存在该应用
            App query=new App();
            query.setId(app.getId());
            List<App> appList = appService.findListByEntity(query);
            if(CollectionUtils.isEmpty(appList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,应用不存在!");
                return resultObjectVO;
            }

            //查询账号有应用是否存在关联
            AdminApp adminAppQuery=new AdminApp();
            adminAppQuery.setAppCode(appList.get(0).getCode());
            adminAppQuery.setAdminId(AuthHeaderUtil.getAdminId(toucanAdminAuth));
            adminAppQuery.setDeleteStatus((short)0);



            List<AdminApp> adminApps = adminAppService.findListByEntity(adminAppQuery);
            if (CollectionUtils.isEmpty(adminApps)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有权限操作该应用!");
                return resultObjectVO;
            }

            int row = appService.deleteById(app.getId());
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }

            //删除应用下所有关联


            row = adminAppService.deleteByAppCode(appList.get(0).getCode());

            if(row<=0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,删除应用下所有管理账户失败!");
                return resultObjectVO;
            }

            resultObjectVO.setData(app);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 批量删除应用
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestHeader("toucan-admin-auth") String toucanAdminAuth,@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<App> appList = JSONObject.parseArray(requestVo.getEntityJson(),App.class);
            if(CollectionUtils.isEmpty(appList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,没有找到应用ID");
                return resultObjectVO;
            }
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
                        appResultObjectVO.setCode(ResultVO.FAILD);
                        appResultObjectVO.setMsg("请求失败,应用不存在!");
                        continue;
                    }

                    //查询账号有应用是否存在关联
                    AdminApp adminAppQuery=new AdminApp();
                    adminAppQuery.setAppCode(appEntityList.get(0).getCode());
                    adminAppQuery.setAdminId(AuthHeaderUtil.getAdminId(toucanAdminAuth));
                    adminAppQuery.setDeleteStatus((short)0);



                    List<AdminApp> adminApps = adminAppService.findListByEntity(adminAppQuery);
                    if (CollectionUtils.isEmpty(adminApps)) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请求失败,没有权限操作该应用!");
                        return resultObjectVO;
                    }

                    int row = appService.deleteById(app.getId());
                    if (row < 1) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        appResultObjectVO.setCode(ResultVO.FAILD);
                        appResultObjectVO.setMsg("请求失败,请重试!");
                        continue;
                    }

                    //删除应用下所有关联

                    row  = adminAppService.deleteByAppCode(appEntityList.get(0).getCode());
                    if(row<0) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请求失败,删除应用下所有管理账户失败!");
                        return resultObjectVO;
                    }

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }


}
