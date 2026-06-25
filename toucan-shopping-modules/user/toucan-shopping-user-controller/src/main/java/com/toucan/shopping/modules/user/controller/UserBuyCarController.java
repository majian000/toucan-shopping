package com.toucan.shopping.modules.user.controller;


import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserBuyCarBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 收货地址 增删改查
 */
@RestController
@RequestMapping("/userBuyCar")
public class UserBuyCarController {


    @Autowired
    private UserBuyCarBusinessService userBuyCarBusinessService;


    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        return userBuyCarBusinessService.save(requestJsonVO);
    }


    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/remove/buy/car",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO removeBuyCar(@RequestBody RequestJsonVO requestVo){
        return userBuyCarBusinessService.removeBuyCar(requestVo);
    }


    /**
     * 清空指定用户的购物车
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/clear/buy/car/userMainId",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO clearByUserMainId(@RequestBody RequestJsonVO requestVo){
        return userBuyCarBusinessService.clearByUserMainId(requestVo);
    }

    /**
     * 根据用户ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/list/userMainId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO listByUserMainId(@RequestBody RequestJsonVO requestJsonVO){
        return userBuyCarBusinessService.listByUserMainId(requestJsonVO);
    }


    @RequestMapping(value="/updates",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updates(@RequestBody RequestJsonVO requestJsonVO){
        return userBuyCarBusinessService.updates(requestJsonVO);
    }


    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO){
        return userBuyCarBusinessService.update(requestJsonVO);
    }

}
