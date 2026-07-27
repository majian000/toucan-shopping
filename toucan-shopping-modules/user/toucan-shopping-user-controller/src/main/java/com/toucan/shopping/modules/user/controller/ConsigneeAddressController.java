package com.toucan.shopping.modules.user.controller;


import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.ConsigneeAddressBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 收货地址
 */
@RestController
@RequestMapping("/consignee/address")
public class ConsigneeAddressController {

    @Autowired
    private ConsigneeAddressBusinessService consigneeAddressBusinessService;


    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        return consigneeAddressBusinessService.save(requestJsonVO);
    }


    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id/userMainId/appCode", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO deleteByIdAndUserMainIdAndAppCode(@RequestBody RequestJsonVO requestVo){
        return consigneeAddressBusinessService.deleteByIdAndUserMainIdAndAppCode(requestVo);
    }


    /**
     * 根据用户ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/list/userMainId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO listByUserMainId(@RequestBody RequestJsonVO requestJsonVO){
        return consigneeAddressBusinessService.listByUserMainId(requestJsonVO);
    }


    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo){
        return consigneeAddressBusinessService.queryListPage(requestVo);
    }


    /**
     * 设置默认
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/set/default/id/userMainId", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO setDefaultByIdAndUserMainId(@RequestBody RequestJsonVO requestVo){
        return consigneeAddressBusinessService.setDefaultByIdAndUserMainId(requestVo);
    }


    /**
     * 根据ID、用户ID、应用编码 查询单条数据
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id/userMainId/appCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO findByIdAndUserMainIdAndAppcode(@RequestBody RequestJsonVO requestVo){
        return consigneeAddressBusinessService.findByIdAndUserMainIdAndAppcode(requestVo);
    }


    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO){
        return consigneeAddressBusinessService.update(requestJsonVO);
    }


    /**
     * 查询设置为默认的收货信息,如果没有默认就查询最新一条
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/default/by/userMainId/appCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO findDefaultByUserMainIdAndAppcode(@RequestBody RequestJsonVO requestVo){
        return consigneeAddressBusinessService.findDefaultByUserMainIdAndAppcode(requestVo);
    }


}
