package com.toucan.shopping.cloud.message.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.MD5Util;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.message.page.MessageTypePageInfo;
import com.toucan.shopping.modules.message.redis.MessageKey;
import com.toucan.shopping.modules.message.redis.MessageTypeKey;
import com.toucan.shopping.modules.message.service.MessageBodyService;
import com.toucan.shopping.modules.message.service.MessageTypeService;
import com.toucan.shopping.modules.message.service.MessageUserService;
import com.toucan.shopping.modules.message.vo.MessageBodyVO;
import com.toucan.shopping.modules.message.vo.MessageTypeVO;
import com.toucan.shopping.modules.message.vo.MessageUserVO;
import com.toucan.shopping.modules.message.vo.MessageVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 消息内容
 */
@RestController
@RequestMapping("/message")
public class MessageController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private MessageTypeService messageTypeService;

    @Autowired
    private MessageBodyService messageBodyService;

    @Autowired
    private MessageUserService messageUserService;



    @RequestMapping(value="/send",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO send(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("保存失败,没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("保存失败,没有找到应用编码");
            return resultObjectVO;
        }
        MessageVO messageVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageVO.class);
        if(CollectionUtils.isEmpty(messageVO.getUsers()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("保存失败,没有找到接收消息的用户");
            return resultObjectVO;
        }

        String lockKey = null;
        try {
            lockKey = MD5Util.md5(messageVO.getTitle());
            boolean lockStatus = skylarkLock.lock(MessageKey.getSaveLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存失败,请稍后重试");
                return resultObjectVO;
            }
            MessageBodyVO messageBodyVO = messageVO.getMessageBody();
            messageBodyVO.setId(idGenerator.id());
            messageBodyVO.setCreateDate(new Date());

            int ret = messageBodyService.save(messageBodyVO);
            if(ret<=0)
            {
                logger.warn("保存消息主体失败 messageBodyVO {} ",JSONObject.toJSONString(messageBodyVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("保存失败,请稍后重试");
            }else{
                List<MessageUserVO> messageUserVOList = messageVO.getUsers();
                if(CollectionUtils.isNotEmpty(messageUserVOList))
                {
                    for(MessageUserVO messageUserVO:messageUserVOList)
                    {
                        messageUserVO.setId(idGenerator.id());
                        messageUserVO.setMessageBodyId(messageBodyVO.getId());
                        messageUserVO.setCreateDate(new Date());
                    }
                    ret = messageUserService.saves(messageUserVOList);
                    if(ret!=messageUserVOList.size())
                    {
                        logger.warn("保存消息用户关联 失败 ret {} messageBodyVO {} ",ret,JSONObject.toJSONString(messageUserVOList));
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("推送失败,请稍后重试");
                    }
                }
            }
            resultObjectVO.setData(null);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("保存失败,请稍后重试");
        }finally{
            if(lockKey!=null) {
                skylarkLock.unLock(MessageKey.getSaveLockKey(lockKey), lockKey);
            }
        }
        return resultObjectVO;
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
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到对象: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到对象!");
            return resultObjectVO;
        }
        try {
            MessageTypePageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypePageInfo.class);
            PageInfo<MessageTypeVO> pageInfo =  messageTypeService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }



}
