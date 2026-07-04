package com.toucan.shopping.modules.user.controller;


import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserCollectProductBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户收藏商品
 */
@RestController
@RequestMapping("/user/collect/product")
public class UserCollectProductController {


    @Autowired
    private UserCollectProductBusinessService userCollectProductBusinessService;


    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        return userCollectProductBusinessService.save(requestJsonVO);
    }


    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/productSkuId/userMainId/appCode", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO deleteBySkuIdAndUserMainIdAndAppCode(@RequestBody RequestJsonVO requestVo){
        return userCollectProductBusinessService.deleteBySkuIdAndUserMainIdAndAppCode(requestVo);
    }


    /**
     * 查询收藏列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryCollectProducts", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryCollectProducts(@RequestBody RequestJsonVO requestVo){
        return userCollectProductBusinessService.queryCollectProducts(requestVo);
    }


    /**
     * 查询列表页
     */
    @RequestMapping(value="/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO){
        return userCollectProductBusinessService.queryListPage(requestJsonVO);
    }


    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        return userCollectProductBusinessService.deleteById(requestVo);
    }


    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return userCollectProductBusinessService.deleteByIds(requestVo);
    }

}
