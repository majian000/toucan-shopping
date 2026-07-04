package com.toucan.shopping.modules.message.controller;


import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.message.business.service.MessageUserBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 消息内容
 */
@RestController
@RequestMapping("/message/user")
public class MessageUserController {


    @Autowired
    private MessageUserBusinessService messageUserBusinessService;


    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO)
    {
        return messageUserBusinessService.deleteById(requestJsonVO);
    }


    @RequestMapping(value="/send", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO send(@RequestBody RequestJsonVO requestJsonVO){
        return messageUserBusinessService.send(requestJsonVO);
    }


    /**
     * 編輯
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        return messageUserBusinessService.update(requestVo);
    }





    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return messageUserBusinessService.findById(requestVo);
    }


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        return messageUserBusinessService.queryListPage(requestJsonVO);
    }


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/user/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPageByUserMianId(@RequestBody RequestJsonVO requestJsonVO)
    {
        return messageUserBusinessService.queryListPageByUserMianId(requestJsonVO);
    }




    /**
     * 查询未读数量
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/user/query/unread/count", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryUnreadCountByUserMainId(@RequestBody RequestJsonVO requestJsonVO)
    {
        return messageUserBusinessService.queryUnreadCountByUserMainId(requestJsonVO);
    }




    /**
     * 更新为已读
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/user/update/read/status", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateReadStatus(@RequestBody RequestJsonVO requestJsonVO)
    {
        return messageUserBusinessService.updateReadStatus(requestJsonVO);
    }



    /**
     * 更新全部为已读
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/user/update/all/read/status", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateAllReadStatus(@RequestBody RequestJsonVO requestJsonVO)
    {
        return messageUserBusinessService.updateAllReadStatus(requestJsonVO);
    }


}
