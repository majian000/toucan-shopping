package com.toucan.shopping.modules.message.controller;


import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.message.business.service.MessageTypeBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 消息类型
 */
@RestController
@RequestMapping("/messageType")
public class MessageTypeController {


    @Autowired
    private MessageTypeBusinessService messageTypeBusinessService;


    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        return messageTypeBusinessService.save(requestJsonVO);
    }



    @RequestMapping(value="/flush/cache",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO flushCache(@RequestBody RequestJsonVO requestJsonVO){
        return messageTypeBusinessService.flushCache(requestJsonVO);
    }



    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        return messageTypeBusinessService.deleteByIds(requestVo);
    }




    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO)
    {
        return messageTypeBusinessService.deleteById(requestJsonVO);
    }



    /**
     * 編輯
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        return messageTypeBusinessService.update(requestVo);
    }


    /**
     * 根据code查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/cache/find/code",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findCacheByCode(@RequestBody RequestJsonVO requestVo){
        return messageTypeBusinessService.findCacheByCode(requestVo);
    }



    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return messageTypeBusinessService.findById(requestVo);
    }





    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        return messageTypeBusinessService.queryListPage(requestJsonVO);
    }

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryList(@RequestBody RequestJsonVO requestJsonVO)
    {
        return messageTypeBusinessService.queryList(requestJsonVO);
    }

}
