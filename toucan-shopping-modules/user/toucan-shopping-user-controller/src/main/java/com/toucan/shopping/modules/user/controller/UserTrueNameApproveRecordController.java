package com.toucan.shopping.modules.user.controller;


import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserTrueNameApproveRecordBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户实名制审核记录
 */
@RestController
@RequestMapping("/user/true/name/approve/record")
public class UserTrueNameApproveRecordController {

    @Autowired
    private UserTrueNameApproveRecordBusinessService userTrueNameApproveRecordBusinessService;


    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        return userTrueNameApproveRecordBusinessService.save(requestJsonVO);
    }


}
