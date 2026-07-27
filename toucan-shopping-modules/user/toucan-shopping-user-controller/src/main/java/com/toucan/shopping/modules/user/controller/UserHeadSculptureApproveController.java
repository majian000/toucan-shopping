package com.toucan.shopping.modules.user.controller;


import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.business.service.UserHeadSculptureApproveBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户头像制审核
 */
@RestController
@RequestMapping("/user/head/sculpture/approve")
public class UserHeadSculptureApproveController {

    @Autowired
    private UserHeadSculptureApproveBusinessService userHeadSculptureApproveBusinessService;

    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        return userHeadSculptureApproveBusinessService.save(requestJsonVO);
    }


    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO){
        return userHeadSculptureApproveBusinessService.update(requestJsonVO);
    }


    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo){
        return userHeadSculptureApproveBusinessService.queryListPage(requestVo);
    }


    @RequestMapping(value="/queryByUserMainId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryByUserMainId(@RequestBody RequestJsonVO requestJsonVO){
        return userHeadSculptureApproveBusinessService.queryByUserMainId(requestJsonVO);
    }


    @RequestMapping(value="/queryAliveByUserMainId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryAliveByUserMainId(@RequestBody RequestJsonVO requestJsonVO){
        return userHeadSculptureApproveBusinessService.queryAliveByUserMainId(requestJsonVO);
    }


    @RequestMapping(value="/queryListByUserMainIdAndOrderByUpdateDateDesc", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryListByUserMainIdAndOrderByUpdateDateDesc(@RequestBody RequestJsonVO requestJsonVO){
        return userHeadSculptureApproveBusinessService.queryListByUserMainIdAndOrderByUpdateDateDesc(requestJsonVO);
    }


    @RequestMapping(value="/queryById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO){
        return userHeadSculptureApproveBusinessService.queryById(requestJsonVO);
    }


    /**
     * 通过指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/pass/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO passById(@RequestBody RequestJsonVO requestVo){
        return userHeadSculptureApproveBusinessService.passById(requestVo);
    }


    /**
     * 驳回指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/reject/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO rejectById(@RequestBody RequestJsonVO requestVo){
        return userHeadSculptureApproveBusinessService.rejectById(requestVo);
    }


    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return userHeadSculptureApproveBusinessService.deleteByIds(requestVo);
    }


}
