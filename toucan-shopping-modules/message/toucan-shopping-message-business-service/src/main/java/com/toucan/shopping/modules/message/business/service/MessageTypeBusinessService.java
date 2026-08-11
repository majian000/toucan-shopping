package com.toucan.shopping.modules.message.business.service;


import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
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


    /**
     * 保存
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageTypeVO messageTypeVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypeVO.class);
            Check.notEmpty(messageTypeVO.getName(), ResultObjectVO.FAILD, "类型名称不能为空");
            Check.notEmpty(messageTypeVO.getCode(), ResultObjectVO.FAILD, "类型编码不能为空");
            Check.notEmpty(messageTypeVO.getAppCode(), ResultObjectVO.FAILD, "所属应用不能为空");
            String lockKey = messageTypeVO.getAppCode() + "_" + messageTypeVO.getCode();
            try {
                boolean lockStatus = skylarkLock.lock(MessageTypeLockKey.getSaveLockKey(lockKey), lockKey);
                if (!lockStatus) {
                    return ResultObjectVO.fail(ResultObjectVO.FAILD, "请稍后重试");
                }

                MessageTypeVO query = new MessageTypeVO();
                query.setCode(messageTypeVO.getCode());
                List<MessageTypeVO> messageTypes = messageTypeService.queryList(query);
                if (!CollectionUtils.isEmpty(messageTypes)) {
                    return ResultObjectVO.fail(ResultObjectVO.FAILD, "该编码已存在");
                }

                messageTypeVO.setId(idGenerator.id());
                messageTypeVO.setDeleteStatus((short) 0);
                messageTypeVO.setCreateDate(new Date());
                messageTypeVO.setCreateAdminId(requestJsonVO.getAdminId());
                int ret = messageTypeService.save(messageTypeVO);
                if (ret <= 0) {
                    logger.warn("保存消息类型失败 requestJson{} id{}", requestJsonVO.getEntityJson(), messageTypeVO.getId());
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("请稍后重试");
                }
                resultObjectVO.setData(messageTypeVO);

                flushCache();
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            } finally {
                skylarkLock.unLock(MessageTypeLockKey.getSaveLockKey(lockKey), lockKey);
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }
        return resultObjectVO;
    }


    /**
     * 刷新缓存
     */
    @RequestCheck
    public ResultObjectVO flushCache(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            flushCache();
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 批量删除
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            List<MessageTypeVO> messageTypeVOS = JSON.parseArray(requestJsonVO.getEntityJson(), MessageTypeVO.class);
            if (CollectionUtils.isEmpty(messageTypeVOS)) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "没有找到ID");
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<>();
            for (MessageTypeVO messageTypeVO : messageTypeVOS) {
                if (messageTypeVO.getId() != null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(messageTypeVO);

                    int row = messageTypeService.deleteById(messageTypeVO.getId());
                    if (row < 1) {
                        logger.warn("删除消息类型失败，id:{}", messageTypeVO.getId());
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("请重试!");
                        continue;
                    }
                    resultObjectVOList.add(appResultObjectVO);
                }
            }
            resultObjectVO.setData(resultObjectVOList);

            flushCache();
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据ID删除
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageTypeVO messageTypeVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypeVO.class);
            Check.notNull(messageTypeVO.getId(), ResultObjectVO.FAILD, "ID不能为空!");

            int ret = messageTypeService.deleteById(messageTypeVO.getId());
            if (ret <= 0) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "不存在该类型!");
            }

            flushCache();
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请重试!");
        }
        return resultObjectVO;
    }


    /**
     * 编辑
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageTypeVO entity = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypeVO.class);

            Check.notEmpty(entity.getCode(), ResultObjectVO.FAILD, "编码不能为空!");
            Check.notEmpty(entity.getName(), ResultObjectVO.FAILD, "名称不能为空!");
            Check.notNull(entity.getId(), ResultObjectVO.FAILD, "请传入ID");

            MessageTypeVO query = new MessageTypeVO();
            query.setCode(entity.getCode());
            List<MessageTypeVO> messageTypes = messageTypeService.queryList(query);
            if (!CollectionUtils.isEmpty(messageTypes)) {
                for (MessageTypeVO messageTypeVO : messageTypes) {
                    if (messageTypeVO.getId().longValue() != entity.getId().longValue()) {
                        return ResultObjectVO.fail(ResultObjectVO.FAILD, "该编码已存在");
                    }
                }
            }

            entity.setUpdateDate(new Date());
            entity.setUpdateAdminId(requestJsonVO.getAdminId());
            int row = messageTypeService.update(entity);
            if (row < 1) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请重试!");
            }

            resultObjectVO.setData(entity);

            flushCache();
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据code查询
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findCacheByCode(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageTypeVO messageTypeVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypeVO.class);
            Check.notNull(messageTypeVO.getCode(), ResultObjectVO.FAILD, "没有找到Code");

            MessageTypeVO messageTypeCacheVO = messageTypeRedisService.queryByCode(messageTypeVO.getCode());
            if (messageTypeCacheVO != null) {
                resultObjectVO.setData(messageTypeCacheVO);
                return resultObjectVO;
            }

            // 如果缓存为空，从数据库查询并刷新缓存
            MessageType query = new MessageType();
            query.setCode(messageTypeVO.getCode());
            List<MessageType> entitys = messageTypeService.findListByEntity(query);
            if (CollectionUtils.isEmpty(entitys)) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "不存在!");
            }

            resultObjectVO.setData(entitys.get(0));

            try {
                flushCache();
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据ID查询
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageTypeVO messageTypeVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypeVO.class);
            Check.notNull(messageTypeVO.getId(), ResultObjectVO.FAILD, "没有找到ID");

            MessageTypeVO query = new MessageTypeVO();
            query.setId(messageTypeVO.getId());
            List<MessageTypeVO> entitys = messageTypeService.queryList(query);
            if (CollectionUtils.isEmpty(entitys)) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "不存在!");
            }

            resultObjectVO.setData(entitys);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询分页列表
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageTypePageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypePageInfo.class);
            PageInfo<MessageTypeVO> pageInfo = messageTypeService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }
        return resultObjectVO;
    }


    /**
     * 查询列表
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryList(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageTypeVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypeVO.class);
            resultObjectVO.setData(messageTypeService.queryList(query));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }
        return resultObjectVO;
    }


    // ==================== private helpers ====================

    /**
     * 刷新缓存：查询全量数据并写入Redis
     */
    private void flushCache() throws InvocationTargetException, IllegalAccessException {
        List<MessageType> messageTypes = messageTypeService.findListByEntity(new MessageType());
        messageTypeRedisService.flush(toVoList(messageTypes));
    }

    /**
     * 将MessageType实体列表转为MessageTypeVO列表
     */
    private static List<MessageTypeVO> toVoList(List<MessageType> entities)
            throws InvocationTargetException, IllegalAccessException {
        List<MessageTypeVO> vos = new ArrayList<>();
        if (CollectionUtils.isEmpty(entities)) {
            return vos;
        }
        for (MessageType entity : entities) {
            MessageTypeVO vo = new MessageTypeVO();
            BeanUtils.copyProperties(vo, entity);
            vos.add(vo);
        }
        return vos;
    }

}
