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


    /**
     * 保存
     */
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null) {
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "没有找到请求对象");
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "没有找到应用编码");
        }
        MessageTypeVO messageTypeVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypeVO.class);
        if (StringUtils.isEmpty(messageTypeVO.getName())) {
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "类型名称不能为空");
        }
        if (StringUtils.isEmpty(messageTypeVO.getCode())) {
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "类型编码不能为空");
        }
        if (StringUtils.isEmpty(messageTypeVO.getAppCode())) {
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "所属应用不能为空");
        }
        String lockKey = messageTypeVO.getAppCode() + "_" + messageTypeVO.getCode();
        ResultObjectVO resultObjectVO = new ResultObjectVO();
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
        return resultObjectVO;
    }


    /**
     * 刷新缓存
     */
    public ResultObjectVO flushCache(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null) {
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "没有找到请求对象");
        }
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            flushCache();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 批量删除
     */
    public ResultObjectVO deleteByIds(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null || requestJsonVO.getEntityJson() == null) {
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "没有找到实体对象");
        }

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            List<MessageTypeVO> messageTypeVOS = JSONObject.parseArray(requestJsonVO.getEntityJson(), MessageTypeVO.class);
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
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据ID删除
     */
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null) {
            logger.info("请求参数为空");
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "请重试!");
        }
        if (requestJsonVO.getAppCode() == null) {
            logger.info("没有找到应用编码: param:" + JSONObject.toJSONString(requestJsonVO));
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "没有找到应用编码!");
        }

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageTypeVO messageTypeVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypeVO.class);
            if (messageTypeVO.getId() == null) {
                logger.info("ID为空 param:" + JSONObject.toJSONString(messageTypeVO));
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "ID不能为空!");
            }

            int ret = messageTypeService.deleteById(messageTypeVO.getId());
            if (ret <= 0) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "不存在该类型!");
            }

            flushCache();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请重试!");
        }
        return resultObjectVO;
    }


    /**
     * 编辑
     */
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null || requestJsonVO.getEntityJson() == null) {
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "没有找到实体对象");
        }

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageTypeVO entity = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypeVO.class);

            if (StringUtils.isEmpty(entity.getCode())) {
                logger.info("编码为空 param:" + JSONObject.toJSONString(entity));
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "编码不能为空!");
            }
            if (StringUtils.isEmpty(entity.getName())) {
                logger.info("名称为空 param:" + JSONObject.toJSONString(entity));
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "名称不能为空!");
            }
            if (entity.getId() == null) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请传入ID");
            }

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
            int row = messageTypeService.update(entity);
            if (row < 1) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请重试!");
            }

            resultObjectVO.setData(entity);

            flushCache();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据code查询
     */
    public ResultObjectVO findCacheByCode(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null || requestJsonVO.getEntityJson() == null) {
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "没有找到实体对象");
        }

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageTypeVO messageTypeVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypeVO.class);
            if (messageTypeVO.getCode() == null) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "没有找到Code");
            }

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
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 根据ID查询
     */
    public ResultObjectVO findById(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null || requestJsonVO.getEntityJson() == null) {
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "没有找到实体对象");
        }

        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageTypeVO messageTypeVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypeVO.class);
            if (messageTypeVO.getId() == null) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "没有找到ID");
            }

            MessageTypeVO query = new MessageTypeVO();
            query.setId(messageTypeVO.getId());
            List<MessageTypeVO> entitys = messageTypeService.queryList(query);
            if (CollectionUtils.isEmpty(entitys)) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "不存在!");
            }

            resultObjectVO.setData(entitys);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询分页列表
     */
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null) {
            logger.info("请求参数为空");
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "请重试!");
        }
        if (requestJsonVO.getAppCode() == null) {
            logger.info("没有找到对象: param:" + JSONObject.toJSONString(requestJsonVO));
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "没有找到对象!");
        }
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageTypePageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypePageInfo.class);
            PageInfo<MessageTypeVO> pageInfo = messageTypeService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }
        return resultObjectVO;
    }


    /**
     * 查询列表
     */
    public ResultObjectVO queryList(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null) {
            logger.info("请求参数为空");
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "请重试!");
        }
        if (requestJsonVO.getAppCode() == null) {
            logger.info("没有找到对象: param:" + JSONObject.toJSONString(requestJsonVO));
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "没有找到对象!");
        }
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageTypeVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), MessageTypeVO.class);
            resultObjectVO.setData(messageTypeService.queryList(query));
        } catch (Exception e) {
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
