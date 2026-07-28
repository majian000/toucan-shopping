package com.toucan.shopping.modules.message.business.service;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.util.MD5Util;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.message.entity.MessageBody;
import com.toucan.shopping.modules.message.entity.MessageType;
import com.toucan.shopping.modules.message.entity.MessageUser;
import com.toucan.shopping.modules.message.page.MessageUserPageInfo;
import com.toucan.shopping.modules.message.redis.MessageLockKey;
import com.toucan.shopping.modules.message.redis.service.MessageTypeRedisService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessageUserBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private MessageBodyService messageBodyService;

    @Autowired
    private MessageUserService messageUserService;

    @Autowired
    private MessageTypeService messageTypeService;

    @Autowired
    private MessageTypeRedisService messageTypeRedisService;

    /**
     * 根据编码从缓存中拿到消息分类
     * @param code
     * @return
     */
    MessageTypeVO queryByCode(String code) {
        MessageTypeVO messageTypeCacheVO = messageTypeRedisService.queryByCode(code);
        if (messageTypeCacheVO != null) {
            return messageTypeCacheVO;
        }

        // 缓存未命中，从数据库查询
        MessageType query = new MessageType();
        query.setCode(code);
        List<MessageType> entitys = messageTypeService.findListByEntity(query);
        if (CollectionUtils.isEmpty(entitys)) {
            return null;
        }

        // 刷新全部消息分类到缓存
        refreshMessageTypeCache();

        MessageTypeVO returnMessageTypeVO = new MessageTypeVO();
        try {
            BeanUtils.copyProperties(returnMessageTypeVO, entitys.get(0));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return returnMessageTypeVO;
    }

    /**
     * 刷新全部消息分类缓存
     */
    private void refreshMessageTypeCache() {
        try {
            List<MessageType> messageTypes = messageTypeService.findListByEntity(new MessageType());
            List<MessageTypeVO> messageTypeVOS = new ArrayList<MessageTypeVO>();
            if (!CollectionUtils.isEmpty(messageTypes)) {
                for (MessageType mt : messageTypes) {
                    MessageTypeVO vo = new MessageTypeVO();
                    BeanUtils.copyProperties(vo, mt);
                    messageTypeVOS.add(vo);
                }
            }
            messageTypeRedisService.flush(messageTypeVOS);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }


    /**
     * 为消息用户列表填充消息主体（标题、内容、内容类型）
     * @param messageUserVOS 消息用户列表
     */
    private void enrichWithMessageBodies(List<MessageUserVO> messageUserVOS) {
        if (CollectionUtils.isEmpty(messageUserVOS)) {
            return;
        }
        Set<Long> messageBodyIdSet = new HashSet<>();
        for (MessageUserVO vo : messageUserVOS) {
            messageBodyIdSet.add(vo.getMessageBodyId());
        }
        MessageBodyVO messageBodyVO = new MessageBodyVO();
        messageBodyVO.setIdSet(messageBodyIdSet);
        List<MessageBody> messageBodyList = messageBodyService.queryList(messageBodyVO);
        if (CollectionUtils.isEmpty(messageBodyList)) {
            return;
        }
        for (MessageUserVO vo : messageUserVOS) {
            for (MessageBody messageBody : messageBodyList) {
                if (vo.getMessageBodyId().longValue() == messageBody.getId().longValue()) {
                    vo.setTitle(messageBody.getTitle());
                    vo.setContent(messageBody.getContent());
                    vo.setContentType(messageBody.getContentType());
                    break;
                }
            }
        }
    }

    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            MessageUserVO messageUserVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageUserVO.class);

            Check.notNull(messageUserVO.getId(), ResultVO.FAILD, "ID不能为空!");


            int ret = messageUserService.deleteById(messageUserVO.getId());
            if(ret<=0)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在该消息!");
                return resultObjectVO;
            }

        }catch(BusinessValidationException e){
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    @RequestCheck(requireEntity = true)
    public ResultObjectVO send(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        MessageVO messageVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageVO.class);
        Check.notEmpty(messageVO.getUsers(), ResultVO.FAILD, "没有找到接收消息的用户");

        String lockKey = null;
        try {
            lockKey = MD5Util.md5(messageVO.getTitle());
            boolean lockStatus = skylarkLock.lock(MessageLockKey.getSaveLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            String messageTypeCode = messageVO.getMessageBody().getMessageTypeCode();
            MessageTypeVO messageTypeVO = queryByCode(messageTypeCode);
            Check.notNull(messageTypeVO, ResultVO.FAILD, "消息分类对象为空");
            messageVO.setMessageType(messageTypeVO.getCode(),messageTypeVO.getName(),messageTypeVO.getAppCode());

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
        }catch(BusinessValidationException e){
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            if(lockKey!=null) {
                skylarkLock.unLock(MessageLockKey.getSaveLockKey(lockKey), lockKey);
            }
        }
        return resultObjectVO;
    }


    /**
     * 編輯
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        String lockKey = null;
        try {
            MessageUserVO entity = JSONObject.parseObject(requestVo.getEntityJson(),MessageUserVO.class);
            lockKey = MD5Util.md5(entity.getTitle());
            boolean lockStatus = skylarkLock.lock(MessageLockKey.getUpdateLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }
            Check.notEmpty(entity.getTitle(), ResultVO.FAILD, "标题不能为空!");
            Check.notEmpty(entity.getContent(), ResultVO.FAILD, "内容不能为空!");
            Check.notNull(entity.getUserMainId(), ResultVO.FAILD, "用户ID不能为空!");
            Check.notNull(entity.getId(), ResultVO.FAILD, "请传入ID");
            Check.notNull(entity.getMessageBodyId(), ResultVO.FAILD, "消息主体ID不能为空");

            MessageBodyVO queryMessageBodyVO =new MessageBodyVO();
            queryMessageBodyVO.setId(entity.getMessageBodyId());
            List<MessageBody> messageBodyList = messageBodyService.queryList(queryMessageBodyVO);

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
        }catch(BusinessValidationException e){
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            if(lockKey!=null) {
                skylarkLock.unLock(MessageLockKey.getUpdateLockKey(lockKey), lockKey);
            }
        }
        return resultObjectVO;
    }




    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            MessageUserVO messageUserVO = JSONObject.parseObject(requestVo.getEntityJson(),MessageUserVO.class);
            Check.notNull(messageUserVO.getId(), ResultVO.FAILD, "没有找到ID");

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

            List<MessageUserVO> messageUserVOS = new ArrayList<MessageUserVO>();
            for(MessageUser messageUser:entitys)
            {
                MessageUserVO muVo = new MessageUserVO();
                BeanUtils.copyProperties(muVo,messageUser);
                messageUserVOS.add(muVo);
            }
            enrichWithMessageBodies(messageUserVOS);
            resultObjectVO.setData(messageUserVOS);

        }catch(BusinessValidationException e){
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageUserPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageUserPageInfo.class);
            PageInfo<MessageUserVO> pageInfo =  messageUserService.queryListPage(queryPageInfo);
            enrichWithMessageBodies(pageInfo.getList());
            resultObjectVO.setData(pageInfo);
        }catch(BusinessValidationException e){
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPageByUserMianId(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageUserPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageUserPageInfo.class);
            Check.notNull(queryPageInfo.getUserMainId(), ResultVO.FAILD, "用户ID不能为空!");
            PageInfo<MessageUserVO> pageInfo =  messageUserService.queryListPage(queryPageInfo);
            enrichWithMessageBodies(pageInfo.getList());
            resultObjectVO.setData(pageInfo);
        }catch(BusinessValidationException e){
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryUnreadCountByUserMainId(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageUserVO messageUserVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageUserVO.class);
            Check.notNull(messageUserVO.getUserMainId(), ResultVO.FAILD, "用户ID不能为空!");
            messageUserVO.setStatus(0);
            Long unreadCount  =  messageUserService.queryListCount(messageUserVO);
            resultObjectVO.setData(unreadCount);
        }catch(BusinessValidationException e){
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO updateReadStatus(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageUserVO messageUserVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageUserVO.class);
            Check.notNull(messageUserVO.getId(), ResultVO.FAILD, "ID不能为空!");
            Check.notNull(messageUserVO.getUserMainId(), ResultVO.FAILD, "用户ID不能为空!");
            messageUserVO.setStatus(1);
            int ret  =  messageUserService.updateStatus(messageUserVO);
            if(ret<=0)
            {
                logger.warn("更新消息状态失败 {}",JSONObject.toJSONString(messageUserVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("更新消息状态失败!");
            }
        }catch(BusinessValidationException e){
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO updateAllReadStatus(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageUserVO messageUserVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageUserVO.class);
            Check.notNull(messageUserVO.getUserMainId(), ResultVO.FAILD, "用户ID不能为空!");
            int ret  =  messageUserService.updateAllReadStatus(messageUserVO.getUserMainId(),messageUserVO.getMessageTypeAppCode());
            if(ret<=0)
            {
                logger.warn("更新消息状态失败 {}",JSONObject.toJSONString(messageUserVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("更新消息状态失败!");
            }
        }catch(BusinessValidationException e){
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


}
