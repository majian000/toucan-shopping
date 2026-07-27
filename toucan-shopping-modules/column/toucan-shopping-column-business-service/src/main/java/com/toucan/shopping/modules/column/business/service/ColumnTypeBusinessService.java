package com.toucan.shopping.modules.column.business.service;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.column.page.ColumnTypePageInfo;
import com.toucan.shopping.modules.column.redis.ColumnTypeLockKey;
import com.toucan.shopping.modules.column.service.ColumnTypeService;
import com.toucan.shopping.modules.column.vo.ColumnTypeVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ColumnTypeBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private ColumnTypeService columnTypeService;

    /**
     * 保存栏目类型
     */
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }
        ColumnTypeVO columnTypeVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnTypeVO.class);
        if (StringUtils.isEmpty(columnTypeVO.getName())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("类型名称不能为空");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(columnTypeVO.getCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("类型编码不能为空");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(columnTypeVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("所属应用不能为空");
            return resultObjectVO;
        }
        String lockKey = columnTypeVO.getAppCode() + "_" + columnTypeVO.getCode();
        try {
            boolean lockStatus = skylarkLock.lock(ColumnTypeLockKey.getSaveLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            ColumnTypeVO query = new ColumnTypeVO();
            query.setCode(columnTypeVO.getCode());
            List<ColumnTypeVO> columnTypeList = columnTypeService.queryList(query);
            if (!CollectionUtils.isEmpty(columnTypeList)) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("该编码已存在");
                return resultObjectVO;
            }

            columnTypeVO.setId(idGenerator.id());
            columnTypeVO.setDeleteStatus((short) 0);
            columnTypeVO.setCreateDate(new Date());
            int ret = columnTypeService.save(columnTypeVO);
            if (ret <= 0) {
                logger.warn("保存栏目类型失败 requestJson{} id{}", requestJsonVO.getEntityJson(), columnTypeVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(columnTypeVO);

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        } finally {
            skylarkLock.unLock(ColumnTypeLockKey.getSaveLockKey(lockKey), lockKey);
        }
        return resultObjectVO;
    }

    /**
     * 批量删除栏目类型
     */
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestVo == null || requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<ColumnTypeVO> columnTypeVOS = JSONObject.parseArray(requestVo.getEntityJson(), ColumnTypeVO.class);
            if (CollectionUtils.isEmpty(columnTypeVOS)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<>();
            for (ColumnTypeVO columnTypeVO : columnTypeVOS) {
                if (columnTypeVO.getId() != null) {
                    ResultObjectVO itemResult = new ResultObjectVO();
                    itemResult.setData(columnTypeVO);

                    int row = columnTypeService.deleteById(columnTypeVO.getId());
                    if (row < 1) {
                        logger.warn("删除栏目类型失败，id:{}", columnTypeVO.getId());
                        itemResult.setCode(ResultVO.FAILD);
                        itemResult.setMsg("请重试!");
                    }
                    resultObjectVOList.add(itemResult);
                }
            }
            resultObjectVO.setData(resultObjectVOList);

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 删除单个栏目类型
     */
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null || requestJsonVO.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ColumnTypeVO columnTypeVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnTypeVO.class);
            if (columnTypeVO.getId() == null) {
                logger.info("ID为空 param:{}", JSONObject.toJSONString(columnTypeVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("ID不能为空!");
                return resultObjectVO;
            }

            int ret = columnTypeService.deleteById(columnTypeVO.getId());
            if (ret <= 0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在该类型!");
                return resultObjectVO;
            }

        } catch (Exception e) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    /**
     * 更新栏目类型
     */
    public ResultObjectVO update(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestVo == null || requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ColumnTypeVO columnTypeVO = JSONObject.parseObject(requestVo.getEntityJson(), ColumnTypeVO.class);

            if (columnTypeVO.getId() == null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入ID");
                return resultObjectVO;
            }
            if (StringUtils.isEmpty(columnTypeVO.getCode())) {
                logger.info("编码为空 param:{}", JSONObject.toJSONString(columnTypeVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("编码不能为空!");
                return resultObjectVO;
            }
            if (StringUtils.isEmpty(columnTypeVO.getName())) {
                logger.info("名称为空 param:{}", JSONObject.toJSONString(columnTypeVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("名称不能为空!");
                return resultObjectVO;
            }

            String lockKey = columnTypeVO.getAppCode() + "_" + columnTypeVO.getCode();
            boolean lockStatus = skylarkLock.lock(ColumnTypeLockKey.getUpdateLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }
            try {
                ColumnTypeVO query = new ColumnTypeVO();
                query.setCode(columnTypeVO.getCode());
                List<ColumnTypeVO> columnTypeList = columnTypeService.queryList(query);
                if (!CollectionUtils.isEmpty(columnTypeList)) {
                    for (ColumnTypeVO existing : columnTypeList) {
                        if (existing.getId().longValue() != columnTypeVO.getId().longValue()) {
                            resultObjectVO.setCode(ResultObjectVO.FAILD);
                            resultObjectVO.setMsg("该编码已存在");
                            return resultObjectVO;
                        }
                    }
                }

                columnTypeVO.setUpdateDate(new Date());
                int row = columnTypeService.update(columnTypeVO);
                if (row < 1) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请重试!");
                    return resultObjectVO;
                }

                resultObjectVO.setData(columnTypeVO);

            } finally {
                skylarkLock.unLock(ColumnTypeLockKey.getUpdateLockKey(lockKey), lockKey);
            }

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据ID查询栏目类型
     */
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestVo == null || requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ColumnTypeVO columnTypeVO = JSONObject.parseObject(requestVo.getEntityJson(), ColumnTypeVO.class);
            if (columnTypeVO.getId() == null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            ColumnTypeVO query = new ColumnTypeVO();
            query.setId(columnTypeVO.getId());
            List<ColumnTypeVO> columnTypeList = columnTypeService.queryList(query);
            if (CollectionUtils.isEmpty(columnTypeList)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在!");
                return resultObjectVO;
            }

            resultObjectVO.setData(columnTypeList);

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据编码查询栏目类型
     */
    public ResultTypeObjectVO<ColumnTypeVO> findOneByCode(RequestJsonVO requestVo) {
        ResultTypeObjectVO resultObjectVO = new ResultTypeObjectVO();
        if (requestVo == null || requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ColumnTypeVO columnTypeVO = requestVo.formatEntity(ColumnTypeVO.class);
            if (StringUtils.isEmpty(columnTypeVO.getCode())) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("编码不能为空");
                return resultObjectVO;
            }

            ColumnTypeVO query = new ColumnTypeVO();
            query.setCode(columnTypeVO.getCode());
            List<ColumnTypeVO> columnTypeList = columnTypeService.queryList(query);
            if (CollectionUtils.isEmpty(columnTypeList)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在!");
                return resultObjectVO;
            }

            resultObjectVO.setData(columnTypeList.get(0));

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 分页查询栏目类型
     */
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null || requestJsonVO.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }
        try {
            ColumnTypePageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnTypePageInfo.class);
            PageInfo<ColumnTypeVO> pageInfo = columnTypeService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }
        return resultObjectVO;
    }

    /**
     * 查询栏目类型列表
     */
    public ResultObjectVO queryList(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestJsonVO == null || requestJsonVO.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }
        try {
            ColumnTypeVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnTypeVO.class);
            resultObjectVO.setData(columnTypeService.queryList(query));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }
        return resultObjectVO;
    }

}
