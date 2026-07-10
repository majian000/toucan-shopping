package com.toucan.shopping.modules.admin.auth.controller.loginHistory;

import com.toucan.shopping.modules.admin.auth.business.service.AdminLoginHistoryBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录历史管理
 */
@RestController
@RequestMapping("/adminLoginHistory")
public class AdminLoginHistoryController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AdminLoginHistoryBusinessService adminLoginHistoryBusinessService;

    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO listPage(@RequestBody RequestJsonVO requestVo) {
        return adminLoginHistoryBusinessService.listPage(requestVo);
    }

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value = "/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo) {
        return adminLoginHistoryBusinessService.findById(requestVo);
    }
}
