package com.toucan.shopping.modules.seller.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.SellerDesignerPageModelBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 设计器页面操作
 */
@RestController
@RequestMapping("/seller/designer/page/model")
public class SellerDesignerPageModelController {

    @Autowired
    private SellerDesignerPageModelBusinessService sellerDesignerPageModelBusinessService;

    /**
     * 只保存1个
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/onlySaveOne",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO onlySaveOne(@RequestBody RequestJsonVO requestJsonVO)
    {
        return sellerDesignerPageModelBusinessService.onlySaveOne(requestJsonVO);
    }

    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo){
        return sellerDesignerPageModelBusinessService.queryListPage(requestVo);
    }

    /**
     * 查询最后一个模型
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/queryLastOne",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryLastOne(@RequestBody RequestJsonVO requestJsonVO)
    {
        return sellerDesignerPageModelBusinessService.queryLastOne(requestJsonVO);
    }

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/admin/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIdForAdmin(@RequestBody RequestJsonVO requestJsonVO)
    {
        return sellerDesignerPageModelBusinessService.deleteByIdForAdmin(requestJsonVO);
    }

}
