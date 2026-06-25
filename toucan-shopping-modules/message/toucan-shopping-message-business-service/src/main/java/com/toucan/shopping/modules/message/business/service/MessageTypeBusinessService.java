package com.toucan.shopping.modules.message.business.service;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.message.entity.MessageType;
import com.toucan.shopping.modules.message.page.MessageTypePageInfo;
import com.toucan.shopping.modules.message.redis.MessageTypeLockKey;
import com.toucan.shopping.modules.message.redis.service.MessageTypeRedisService;
import com.toucan.shopping.modules.message.service.MessageTypeService;
import com.toucan.shopping.modules.message.vo.MessageTypeVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageTypeBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private MessageTypeService messageTypeService;

    @Autowired
    private MessageTypeRedisService messageTypeRedisService;


    public ResultObjectVO save(RequestJsonVO requestJsonVO){
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
        MessageTypeVO messageTypeVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypeVO.class);
        if(StringUtils.isEmpty(messageTypeVO.getName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("类型名称不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(messageTypeVO.getCode()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("类型编码不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(messageTypeVO.getAppCode()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("所属应用不能为空");
            return resultObjectVO;
        }
        String lockKey = messageTypeVO.getAppCode()+"_"+messageTypeVO.getCode();
        try {
            boolean lockStatus = skylarkLock.lock(MessageTypeLockKey.getSaveLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            MessageTypeVO query = new MessageTypeVO();
            query.setCode(messageTypeVO.getCode());
            List<MessageTypeVO> messageTypes = messageTypeService.queryList(query);
            if(!CollectionUtils.isEmpty(messageTypes))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("该编码已存在");
                return resultObjectVO;
            }

            messageTypeVO.setId(idGenerator.id());
            messageTypeVO.setDeleteStatus((short)0);
            messageTypeVO.setCreateDate(new Date());
            int ret = messageTypeService.save(messageTypeVO);
            if(ret<=0)
            {
                logger.warn("保存消息类型失败 requestJson{} id{}",requestJsonVO.getEntityJson(),messageTypeVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(messageTypeVO);

            this.flushCache();
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(MessageTypeLockKey.getSaveLockKey(lockKey), lockKey);
        }
        return resultObjectVO;
    }

    private void flushCache() throws InvocationTargetException, IllegalAccessException {
        MessageType query=new MessageType();
        List<MessageType> messageTypes = messageTypeService.findListByEntity(query);
        List<MessageTypeVO> messageTypeVOS = new ArrayList<MessageTypeVO>();
        if(!CollectionUtils.isEmpty(messageTypes))
        {
            for(MessageType messageType:messageTypes)
            {
                MessageTypeVO messageTypeCache = new MessageTypeVO();
                BeanUtils.copyProperties(messageTypeCache,messageType);
                messageTypeVOS.add(messageTypeCache);
            }
        }
        messageTypeRedisService.flush(messageTypeVOS);
    }



    public ResultObjectVO flushCache(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        try {
            flushCache();
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<MessageTypeVO> messageTypeVOS = JSONObject.parseArray(requestVo.getEntityJson(),MessageTypeVO.class);
            if(CollectionUtils.isEmpty(messageTypeVOS))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(MessageTypeVO messageTypeVO:messageTypeVOS) {
                if(messageTypeVO.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(messageTypeVO);

                    int row = messageTypeService.deleteById(messageTypeVO.getId());
                    if (row < 1) {
                        logger.warn("删除消息类型失败，id:{}",messageTypeVO.getId());
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请重试!");
                        continue;
                    }
                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        try {
            MessageTypeVO messageTypeVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypeVO.class);

            if(messageTypeVO.getId()==null)
            {
                logger.info("ID为空 param:"+ JSONObject.toJSONString(messageTypeVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("ID不能为空!");
                return resultObjectVO;
            }


            int ret = messageTypeService.deleteById(messageTypeVO.getId());
            if(ret<=0)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在该类型!");
                return resultObjectVO;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 編輯
     * @param requestVo
     * @return
     */
    public ResultObjectVO update(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            MessageTypeVO entity = JSONObject.parseObject(requestVo.getEntityJson(),MessageTypeVO.class);

            if(StringUtils.isEmpty(entity.getCode()))
            {
                logger.info("编码为空 param:"+ JSONObject.toJSONString(entity));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("编码不能为空!");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(entity.getName()))
            {
                logger.info("名称为空 param:"+ JSONObject.toJSONString(entity));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("名称不能为空!");
                return resultObjectVO;
            }

            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入ID");
                return resultObjectVO;
            }

            MessageTypeVO query = new MessageTypeVO();
            query.setCode(entity.getCode());
            List<MessageTypeVO> messageTypes = messageTypeService.queryList(query);
            if(!CollectionUtils.isEmpty(messageTypes))
            {
                for(MessageTypeVO messageTypeVO:messageTypes) {
                    if(messageTypeVO.getId().longValue()!=entity.getId().longValue()) {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("该编码已存在");
                        return resultObjectVO;
                    }
                }
            }

            entity.setUpdateDate(new Date());
            int row = messageTypeService.update(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            resultObjectVO.setData(entity);

            this.flushCache();

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据code查询
     * @param requestVo
     * @return
     */
    public ResultObjectVO findCacheByCode(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            MessageTypeVO messageTypeVO = JSONObject.parseObject(requestVo.getEntityJson(),MessageTypeVO.class);
            if(messageTypeVO.getCode()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到Code");
                return resultObjectVO;
            }

            MessageTypeVO messageTypeCacheVO = messageTypeRedisService.queryByCode(messageTypeVO.getCode());
            if(messageTypeCacheVO!=null)
            {
                resultObjectVO.setData(messageTypeCacheVO);
                return resultObjectVO;
            }

            //如果缓存为空 刷新到缓存
            MessageType query=new MessageType();
            query.setCode(messageTypeVO.getCode());
            List<MessageType> entitys = messageTypeService.findListByEntity(query);
            if(CollectionUtils.isEmpty(entitys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在!");
                return resultObjectVO;
            }

            resultObjectVO.setData(entitys.get(0));

            try {
                query.setCode(null);
                List<MessageType> messageTypes = messageTypeService.findListByEntity(query);
                List<MessageTypeVO> messageTypeVOS = new ArrayList<MessageTypeVO>();
                if(!CollectionUtils.isEmpty(messageTypes))
                {
                    for(MessageType messageType:messageTypes)
                    {
                        MessageTypeVO messageTypeCache = new MessageTypeVO();
                        BeanUtils.copyProperties(messageTypeCache,messageType);
                        messageTypeVOS.add(messageTypeCache);
                    }
                }
                messageTypeRedisService.flush(messageTypeVOS);
            }catch(Exception e)
            {
                logger.warn(e.getMessage(),e);
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    public ResultObjectVO findById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            MessageTypeVO messageTypeVO = JSONObject.parseObject(requestVo.getEntityJson(),MessageTypeVO.class);
            if(messageTypeVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该对象
            MessageTypeVO query=new MessageTypeVO();
            query.setId(messageTypeVO.getId());
            List<MessageTypeVO> entitys = messageTypeService.queryList(query);
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
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO)
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

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    public ResultObjectVO queryList(RequestJsonVO requestJsonVO)
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
            MessageTypeVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypeVO.class);
            resultObjectVO.setData(messageTypeService.queryList(query));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

}
