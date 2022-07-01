package com.toucan.shopping.modules.admin.auth.controller.app;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.admin.auth.page.AppPageInfo;
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

    @Autowired
    private OrgnazitionAppService orgnazitionAppService;


    /**
     * 添加应用
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestVo){
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

            if(!AlphabetNumberUtils.isAlphabetNumber(app.getCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,应用编码只允许字母、数字、下划线组成,长度1-8位");
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
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            App app = JSONObject.parseObject(requestVo.getEntityJson(),App.class);
            if(StringUtils.isEmpty(app.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入应用名称");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(app.getCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入应用编码");
                return resultObjectVO;
            }
            if(app.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入应用ID");
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
            int row = appService.update(app);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            AdminAuthCacheHelper.getAppCacheService().deleteByAppCode(app.getCode());

            resultObjectVO.setData(app);

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
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO listPage(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AppPageInfo appPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), AppPageInfo.class);
            resultObjectVO.setData(appService.queryListPage(appPageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询列表
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
            App app = JSONObject.parseObject(requestVo.getEntityJson(), App.class);
            resultObjectVO.setData(appService.findListByEntity(app));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
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
            App app = JSONObject.parseObject(requestVo.getEntityJson(),App.class);
            if(app.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用ID");
                return resultObjectVO;
            }

            //查询是否存在该应用
            App query=new App();
            query.setId(app.getId());
            List<App> appList = appService.findListByEntity(query);
            if(CollectionUtils.isEmpty(appList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("应用不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(appList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据编码查询启用状态
     * @param requestVo
     * @return true:启用 false:停用
     */
    @RequestMapping(value="/enable/status/by/code",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryEnableStatusByCode(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setData(false);
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            App app = JSONObject.parseObject(requestVo.getEntityJson(), App.class);
            if (app.getCode() == null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }
            AppVO appVO = AdminAuthCacheHelper.getAppCacheService().findByAppCode(app.getCode());
            if(appVO!=null)
            {
//                if(appVO.getEnableStatus()!=null&&appVO.getEnableStatus().intValue()==1)
//                {
//                    resultObjectVO.setData(true);
//                }else{
                    resultObjectVO.setData(false);
//                }
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
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;

    }


    /**
     * 根据编码查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/code",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findByCode(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            App app = JSONObject.parseObject(requestVo.getEntityJson(),App.class);
            if(app.getCode()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }

            //查询是否存在该应用
            App query=new App();
            query.setCode(app.getCode());
            List<App> appList = appService.findListByEntity(query);
            if(CollectionUtils.isEmpty(appList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("应用不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(appList.get(0));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
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
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            App app = JSONObject.parseObject(requestVo.getEntityJson(),App.class);
            if(app.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用ID");
                return resultObjectVO;
            }

            //查询是否存在该应用
            App query=new App();
            query.setId(app.getId());
            List<App> appList = appService.findListByEntity(query);
            if(CollectionUtils.isEmpty(appList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("应用不存在!");
                return resultObjectVO;
            }


            int row = appService.deleteById(app.getId());
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
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
                    return resultObjectVO;
                }
            }

            //删除应用机构关联
            orgnazitionAppService.deleteByAppCode(appList.get(0).getCode());


            resultObjectVO.setData(app);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
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
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<App> appList = JSONObject.parseArray(requestVo.getEntityJson(),App.class);
            if(CollectionUtils.isEmpty(appList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用ID");
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

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


}
