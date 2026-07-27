package com.toucan.shopping.modules.seller.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.seller.business.service.FreightTemplateBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 运费模板
 * @author majian
 * @date 2022-9-21 14:13:19
 */
@RestController
@RequestMapping("/freightTemplate")
public class FreightTemplateController {

    @Autowired
    private FreightTemplateBusinessService freightTemplateBusinessService;

    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page", method = RequestMethod.POST)
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo){
        return freightTemplateBusinessService.queryListPage(requestVo);
    }

    /**
     * 保存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save", method = RequestMethod.POST)
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO) {
        return freightTemplateBusinessService.save(requestJsonVO);
    }

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id/userMainId", method = RequestMethod.POST)
    public ResultObjectVO findByIdAndUserMainId(@RequestBody RequestJsonVO requestVo){
        return freightTemplateBusinessService.findByIdAndUserMainId(requestVo);
    }

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id", method = RequestMethod.POST)
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return freightTemplateBusinessService.findById(requestVo);
    }

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id/list", method = RequestMethod.POST)
    public ResultObjectVO findByIdList(@RequestBody RequestJsonVO requestVo){
        return freightTemplateBusinessService.findByIdList(requestVo);
    }

    /**
     * 修改
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO) {
        return freightTemplateBusinessService.update(requestJsonVO);
    }

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id", method = RequestMethod.DELETE)
    public ResultObjectVO deleteById(@RequestHeader("toucan-sign-header") String signHeader, @RequestBody RequestJsonVO requestJsonVO) {
        return freightTemplateBusinessService.deleteById(signHeader, requestJsonVO);
    }

}
