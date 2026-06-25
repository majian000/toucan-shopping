package com.toucan.shopping.modules.seller.controller;


import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.SellerLoginHistoryBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 卖家登录历史 增删改查
 */
@RestController
@RequestMapping("/seller/loginHistory")
public class SellerLoginHistoryController {

    @Autowired
    private SellerLoginHistoryBusinessService sellerLoginHistoryBusinessService;



    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        return sellerLoginHistoryBusinessService.save(requestJsonVO);
    }


    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo){
        return sellerLoginHistoryBusinessService.queryListPage(requestVo);
    }





    /**
     * 查询10条最近登录的记录
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/list/latest/10",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListByLatest10(@RequestBody RequestJsonVO requestVo){
        return sellerLoginHistoryBusinessService.queryListByLatest10(requestVo);
    }


}
