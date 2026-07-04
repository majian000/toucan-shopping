package com.toucan.shopping.modules.admin.auth.controller.admin;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.business.service.AdminAppBusinessService;
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
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 管理员应用 增删改查
 */
@RestController
@RequestMapping("/adminApp")
public class AdminAppController {



    @Autowired
    private AdminAppBusinessService adminAppBusinessService;

    /**
     * 保存管理员账户
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestVo){
        return adminAppBusinessService.save(requestVo);
    }





    /**
     * 根据实体查询对象
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryListByEntity", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListByEntity(@RequestBody RequestJsonVO requestVo){
        return adminAppBusinessService.queryListByEntity(requestVo);
    }




    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO list(@RequestBody RequestJsonVO requestVo){
        return adminAppBusinessService.list(requestVo);
    }




    /**
     * 查询在线用户列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/online/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO onlineList(@RequestBody RequestJsonVO requestVo){
        return adminAppBusinessService.onlineList(requestVo);
    }




    /**
     * 查询在线用户列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/logout", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO logout(@RequestBody RequestJsonVO requestVo){
        return adminAppBusinessService.logout(requestVo);
    }

    /**
     * 查询登录列表分页(给定时任务刷新状态使用)
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/login/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO loginList(@RequestBody RequestJsonVO requestVo){
        return adminAppBusinessService.loginList(requestVo);
    }





    /**
     * 根据实体查询对象
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryAppListByAdminId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryAppListByAdminId(@RequestBody RequestJsonVO requestVo){
        return adminAppBusinessService.queryAppListByAdminId(requestVo);
    }

    /**
     * 根据应用编码删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/deleteByAppCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO deleteByAppCode(@RequestBody RequestJsonVO requestVo){
        return adminAppBusinessService.deleteByAppCode(requestVo);
    }



    /**
     * 修改账号登录状态
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/batchUpdateLoginStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO batchUpdateLoginStatus(@RequestBody RequestJsonVO requestVo){
        return adminAppBusinessService.batchUpdateLoginStatus(requestVo);
    }


    /**
     * 查询APP登录用户信息
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryAppLoginUserCountList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryAppLoginUserCountList(@RequestBody RequestJsonVO requestVo){
        return adminAppBusinessService.queryAppLoginUserCountList(requestVo);
    }




}
