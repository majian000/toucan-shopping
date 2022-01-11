package com.toucan.shopping.standard.admin.auth.proxy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AdminService;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminResultVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.standard.admin.auth.proxy.service.AdminAppServiceProxy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AdminAppProxyImpl implements AdminAppServiceProxy {



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
    
    public ResultObjectVO save( RequestJsonVO requestVo){
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
    
    public ResultObjectVO queryListByEntity( RequestJsonVO requestVo){
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
     * 根据实体查询对象
     * @param requestVo
     * @return
     */
    public ResultObjectVO queryAppListByAdminId( RequestJsonVO requestVo){
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
    public ResultObjectVO deleteByAppCode( RequestJsonVO requestVo){
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

}
