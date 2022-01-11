package com.toucan.shopping.cloud.message.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.MD5Util;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.message.entity.MessageBody;
import com.toucan.shopping.modules.message.entity.MessageUser;
import com.toucan.shopping.modules.message.page.MessageTypePageInfo;
import com.toucan.shopping.modules.message.page.MessageUserPageInfo;
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
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 消息内容
 */
@RestController
@RequestMapping("/message/user")
public class MessageUserController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

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
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }
        MessageVO messageVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageVO.class);
        if(CollectionUtils.isEmpty(messageVO.getUsers()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到接收消息的用户");
            return resultObjectVO;
        }

        String lockKey = null;
        try {
            lockKey = MD5Util.md5(messageVO.getTitle());
            boolean lockStatus = skylarkLock.lock(MessageKey.getSaveLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
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
                resultObjectVO.setMsg("请稍后重试");
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
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            if(lockKey!=null) {
                skylarkLock.unLock(MessageKey.getSaveLockKey(lockKey), lockKey);
            }
        }
        return resultObjectVO;
    }


    /**
     * 編輯
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        String lockKey = null;
        try {
            MessageUserVO entity = JSONObject.parseObject(requestVo.getEntityJson(),MessageUserVO.class);
            lockKey = MD5Util.md5(entity.getTitle());
            boolean lockStatus = skylarkLock.lock(MessageKey.getUpdateLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(entity.getTitle()))
            {
                logger.info("标题为空 param:"+ JSONObject.toJSONString(entity));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("标题不能为空!");
                return resultObjectVO;
            }


            if(StringUtils.isEmpty(entity.getContent()))
            {
                logger.info("内容为空 param:"+ JSONObject.toJSONString(entity));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("内容不能为空!");
                return resultObjectVO;
            }

            if(entity.getUserMainId()==null)
            {
                logger.info("用户ID为空 param:"+ JSONObject.toJSONString(entity));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空!");
                return resultObjectVO;
            }

            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入ID");
                return resultObjectVO;
            }

            if(entity.getMessageBodyId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("消息主体ID不能为空");
                return resultObjectVO;
            }

            MessageBody queryMessageBody = new MessageBody();
            queryMessageBody.setId(entity.getMessageBodyId());
            List<MessageBody> messageBodyList = messageBodyService.findListByEntity(queryMessageBody);

            boolean isModify = false;
            MessageBody messageBodyNew =new MessageBody();
            if(CollectionUtils.isNotEmpty(messageBodyList)) {
                //找到当前关联的消息实体ID
                MessageBody messageBodyEntity = messageBodyList.get(0);
                //修改了这个用户的消息标题或者消息内容
                if(!messageBodyEntity.getTitle().equals(entity.getTitle())||!messageBodyEntity.getContent().equals(entity.getContent()))
                {
                    isModify = true;
                    BeanUtils.copyProperties(messageBodyNew,messageBodyEntity);
                }
            }
            //进行了消息标题或内容的修改
            if(isModify) {
                messageBodyNew.setId(idGenerator.id());
                messageBodyNew.setCreateDate(new Date());
                //设置新的标题和内容
                messageBodyNew.setTitle(entity.getTitle());
                messageBodyNew.setContent(entity.getContent());
                int ret = messageBodyService.save(messageBodyNew);
                if (ret <= 0) {
                    logger.warn("保存消息主体失败 messageBodyVO {} ", JSONObject.toJSONString(messageBodyNew));
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                    return resultObjectVO;
                }
                //关联到新的消息主体
                entity.setMessageBodyId(messageBodyNew.getId());
            }
            entity.setUpdateDate(new Date());
            int row = messageUserService.update(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }
            resultObjectVO.setData(entity);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            if(lockKey!=null) {
                skylarkLock.unLock(MessageKey.getUpdateLockKey(lockKey), lockKey);
            }
        }
        return resultObjectVO;
    }



    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            MessageUserVO messageUserVO = JSONObject.parseObject(requestVo.getEntityJson(),MessageUserVO.class);
            if(messageUserVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该对象
            MessageUserVO query=new MessageUserVO();
            query.setId(messageUserVO.getId());
            List<MessageUser> entitys = messageUserService.findListByEntity(query);
            if(CollectionUtils.isEmpty(entitys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在!");
                return resultObjectVO;
            }

            resultObjectVO.setData(entitys);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
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
            MessageUserPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageUserPageInfo.class);
            PageInfo<MessageUserVO> pageInfo =  messageUserService.queryListPage(queryPageInfo);
            if(CollectionUtils.isNotEmpty(pageInfo.getList()))
            {
                List<MessageUserVO> messageUserVOS = pageInfo.getList();
                if(CollectionUtils.isNotEmpty(messageUserVOS))
                {
                    Set<Long> messageBodyIdSet = new HashSet<>();
                    for(MessageUserVO messageUserVO:messageUserVOS)
                    {
                        messageBodyIdSet.add(messageUserVO.getMessageBodyId());
                    }
                    //查询消息主体
                    MessageBodyVO messageBodyVO = new MessageBodyVO();
                    messageBodyVO.setIdSet(messageBodyIdSet);
                    List<MessageBody> messageBodyList = messageBodyService.findListByEntity(messageBodyVO);
                    if(CollectionUtils.isNotEmpty(messageBodyList))
                    {

                        for(MessageUserVO messageUserVO:messageUserVOS) {
                            for (MessageBody messageBody : messageBodyList) {
                                if(messageUserVO.getMessageBodyId().longValue()==messageBody.getId().longValue())
                                {
                                    messageUserVO.setTitle(messageBody.getTitle());
                                    messageUserVO.setContent(messageBody.getContent());
                                    messageUserVO.setContentType(messageBody.getContentType());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            resultObjectVO.setData(pageInfo);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/user/query/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPageByUserMianId(@RequestBody RequestJsonVO requestJsonVO)
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
            MessageUserPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageUserPageInfo.class);
            if(queryPageInfo.getUserMainId()==null)
            {
                logger.info("用户ID不能为空 :"+ JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空!");
                return resultObjectVO;
            }
            PageInfo<MessageUserVO> pageInfo =  messageUserService.queryListPage(queryPageInfo);
            if(CollectionUtils.isNotEmpty(pageInfo.getList()))
            {
                List<MessageUserVO> messageUserVOS = pageInfo.getList();
                if(CollectionUtils.isNotEmpty(messageUserVOS))
                {
                    Set<Long> messageBodyIdSet = new HashSet<>();
                    for(MessageUserVO messageUserVO:messageUserVOS)
                    {
                        messageUserVO.setSendDateYearMonthDay(messageUserVO.getSendDate());
                        messageBodyIdSet.add(messageUserVO.getMessageBodyId());
                    }
                    //查询消息主体
                    MessageBodyVO messageBodyVO = new MessageBodyVO();
                    messageBodyVO.setIdSet(messageBodyIdSet);
                    List<MessageBody> messageBodyList = messageBodyService.findListByEntity(messageBodyVO);
                    if(CollectionUtils.isNotEmpty(messageBodyList))
                    {

                        for(MessageUserVO messageUserVO:messageUserVOS) {
                            for (MessageBody messageBody : messageBodyList) {
                                if(messageUserVO.getMessageBodyId().longValue()==messageBody.getId().longValue())
                                {
                                    messageUserVO.setTitle(messageBody.getTitle());
                                    messageUserVO.setContent(messageBody.getContent());
                                    messageUserVO.setContentType(messageBody.getContentType());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            resultObjectVO.setData(pageInfo);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }




    /**
     * 查询未读数量
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/user/query/unread/count",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryUnreadCountByUserMainId(@RequestBody RequestJsonVO requestJsonVO)
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
            MessageUserVO messageUserVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageUserVO.class);
            if(messageUserVO.getUserMainId()==null)
            {
                logger.info("用户ID不能为空 :"+ JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空!");
                return resultObjectVO;
            }
            messageUserVO.setStatus(0);
            Long unreadCount  =  messageUserService.queryListCount(messageUserVO);
            resultObjectVO.setData(unreadCount);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }




    /**
     * 更新为已读
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/user/update/read/status",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateReadStatus(@RequestBody RequestJsonVO requestJsonVO)
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
            MessageUserVO messageUserVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageUserVO.class);
            if(messageUserVO.getId()==null)
            {
                logger.info("ID不能为空 :"+ JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("ID不能为空!");
                return resultObjectVO;
            }
            if(messageUserVO.getUserMainId()==null)
            {
                logger.info("用户ID不能为空 :"+ JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空!");
                return resultObjectVO;
            }
            messageUserVO.setStatus(1);
            int ret  =  messageUserService.updateStatus(messageUserVO);
            if(ret<=0)
            {
                logger.warn("更新消息状态失败 {}",JSONObject.toJSONString(messageUserVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("更新消息状态失败!");
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }



    /**
     * 更新全部为已读
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/user/update/all/read/status",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateAllReadStatus(@RequestBody RequestJsonVO requestJsonVO)
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
            MessageUserVO messageUserVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageUserVO.class);
            if(messageUserVO.getUserMainId()==null)
            {
                logger.info("用户ID不能为空 :"+ JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空!");
                return resultObjectVO;
            }
            int ret  =  messageUserService.updateAllReadStatus(messageUserVO.getUserMainId());
            if(ret<=0)
            {
                logger.warn("更新消息状态失败 {}",JSONObject.toJSONString(messageUserVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("更新消息状态失败!");
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


}
