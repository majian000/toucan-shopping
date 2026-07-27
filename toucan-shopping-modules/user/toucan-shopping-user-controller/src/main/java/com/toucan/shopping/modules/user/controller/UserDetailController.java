package com.toucan.shopping.modules.user.controller;


import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserDetailBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户详情
 */
@RestController
@RequestMapping("/user/detail")
public class UserDetailController {

    @Autowired
    private UserDetailBusinessService userDetailBusinessService;


    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO updateDetail(@RequestBody RequestJsonVO requestJsonVO){
        return userDetailBusinessService.updateDetail(requestJsonVO);
    }


}
