package com.toucan.shopping.modules.admin.auth.controller.adminInfo;

import com.toucan.shopping.modules.admin.auth.business.service.AdminInfoBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员信息管理
 */
@RestController
@RequestMapping("/adminInfo")
public class AdminInfoController {

    @Autowired
    private AdminInfoBusinessService adminInfoBusinessService;

    /**
     * 保存/更新管理员信息
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO saveOrUpdate(@RequestBody RequestJsonVO requestVo) {
        return adminInfoBusinessService.saveOrUpdate(requestVo);
    }

    /**
     * 根据adminId查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/findByAdminId", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findByAdminId(@RequestBody RequestJsonVO requestVo) {
        return adminInfoBusinessService.findByAdminId(requestVo);
    }
}
