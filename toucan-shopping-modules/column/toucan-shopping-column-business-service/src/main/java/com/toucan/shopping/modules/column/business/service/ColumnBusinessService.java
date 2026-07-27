package com.toucan.shopping.modules.column.business.service;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.column.page.ColumnPageInfo;
import com.toucan.shopping.modules.column.redis.ColumnLockKey;
import com.toucan.shopping.modules.column.service.ColumnService;
import com.toucan.shopping.modules.column.vo.ColumnTreeVO;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ColumnBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ColumnService columnService;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private IdGenerator idGenerator;

    // ==================== 公有业务方法 ====================

    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null) {
            logger.info("请求参数为空");
            return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
        }
        if (requestJsonVO.getAppCode() == null) {
            logger.info("没有找到对象: param:" + JSONObject.toJSONString(requestJsonVO));
            return ResultObjectVO.fail(ResultVO.FAILD, "没有找到对象!");
        }
        try {
            ColumnPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnPageInfo.class);
            PageInfo<ColumnVO> pageInfo = columnService.queryListPage(queryPageInfo);
            return ResultObjectVO.ok(pageInfo);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "查询失败!");
        }
    }

    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null) {
            return ResultObjectVO.fail(ResultVO.FAILD, "没有找到请求对象");
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            return ResultObjectVO.fail(ResultVO.FAILD, "没有找到应用编码");
        }
        ColumnVO columnVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnVO.class);
        if (columnVO.getId() == null) {
            return ResultObjectVO.fail(ResultVO.FAILD, "栏目ID不能为空");
        }
        if (StringUtils.isEmpty(columnVO.getTitle())) {
            return ResultObjectVO.fail(ResultVO.FAILD, "栏目标题不能为空");
        }
        if (StringUtils.isEmpty(columnVO.getColumnTypeCode())) {
            return ResultObjectVO.fail(ResultVO.FAILD, "栏目类型编码不能为空");
        }
        if (StringUtils.isEmpty(columnVO.getAppCode())) {
            return ResultObjectVO.fail(ResultVO.FAILD, "所属应用不能为空");
        }
        if (columnVO.getPid() != null && Objects.equals(columnVO.getPid(), columnVO.getId())) {
            return ResultObjectVO.fail(ResultVO.FAILD, "上级节点不能为自己");
        }
        String lockKey = buildLockKey(columnVO);
        try {
            if (!skylarkLock.lock(ColumnLockKey.getUpdateLockKey(lockKey), lockKey)) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
            }
            ResultObjectVO duplicateCheck = checkCodeExists(columnVO, columnVO.getId());
            if (duplicateCheck != null) {
                return duplicateCheck;
            }
            int ret = columnService.update(columnVO);
            if (ret <= 0) {
                logger.warn("修改栏目失败 requestJson{} id{}", requestJsonVO.getEntityJson(), columnVO.getId());
                return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
            }
            return ResultObjectVO.ok(columnVO);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        } finally {
            skylarkLock.unLock(ColumnLockKey.getUpdateLockKey(lockKey), lockKey);
        }
    }

    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null) {
            return ResultObjectVO.fail(ResultVO.FAILD, "没有找到请求对象");
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            return ResultObjectVO.fail(ResultVO.FAILD, "没有找到应用编码");
        }
        ColumnVO columnVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnVO.class);
        if (StringUtils.isEmpty(columnVO.getTitle())) {
            return ResultObjectVO.fail(ResultVO.FAILD, "栏目标题不能为空");
        }
        if (StringUtils.isEmpty(columnVO.getColumnTypeCode())) {
            return ResultObjectVO.fail(ResultVO.FAILD, "栏目类型编码不能为空");
        }
        if (StringUtils.isEmpty(columnVO.getAppCode())) {
            return ResultObjectVO.fail(ResultVO.FAILD, "所属应用不能为空");
        }
        String lockKey = buildLockKey(columnVO);
        try {
            if (!skylarkLock.lock(ColumnLockKey.getSaveLockKey(lockKey), lockKey)) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
            }
            ResultObjectVO duplicateCheck = checkCodeExists(columnVO, null);
            if (duplicateCheck != null) {
                return duplicateCheck;
            }
            columnVO.setId(idGenerator.id());
            columnVO.setDeleteStatus((short) 0);
            columnVO.setCreateDate(new Date());
            int ret = columnService.save(columnVO);
            if (ret <= 0) {
                logger.warn("保存栏目失败 requestJson{} id{}", requestJsonVO.getEntityJson(), columnVO.getId());
                return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
            }
            return ResultObjectVO.ok(columnVO);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        } finally {
            skylarkLock.unLock(ColumnLockKey.getSaveLockKey(lockKey), lockKey);
        }
    }

    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null || requestJsonVO.getEntityJson() == null) {
            return ResultObjectVO.fail(ResultVO.FAILD, "没有找到实体对象");
        }
        try {
            ColumnPageInfo queryPageInfo = requestJsonVO.formatEntity(ColumnPageInfo.class);
            boolean queryCriteria = StringUtils.isNotEmpty(queryPageInfo.getTitle())
                    || StringUtils.isNotEmpty(queryPageInfo.getAppCode());

            List<ColumnVO> columnVoList;
            if (queryCriteria) {
                columnVoList = queryColumnsByCriteria(queryPageInfo);
            } else {
                columnVoList = queryColumnsByParentId(queryPageInfo);
            }

            resolveParentTitles(columnVoList);

            // 条件查询时，将查询到的节点设置为顶级节点
            if (queryCriteria && !CollectionUtils.isEmpty(columnVoList)) {
                columnVoList.forEach(vo -> vo.setPid(-1L));
            }

            return ResultObjectVO.ok(columnVoList);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
    }

    public ResultObjectVO queryColumnTreeByPid(RequestJsonVO requestJsonVO) {
        try {
            ColumnTreeVO query = requestJsonVO.formatEntity(ColumnTreeVO.class);
            List<ColumnVO> columnVOS = columnService.queryOneLevelChildrenByIdAndAppCode(
                    query.getParentId(), query.getAppCode(), query.getColumnTypeCode());
            List<ColumnTreeVO> columnTreeVOS = new LinkedList<>();
            for (ColumnVO columnVO : columnVOS) {
                ColumnTreeVO columnTreeVO = new ColumnTreeVO();
                BeanUtils.copyProperties(columnTreeVO, columnVO);
                Long childrenCount = columnService.queryOneLevelChildrenCountByIdAndAppCode(
                        columnVO.getId(), columnVO.getAppCode(), query.getColumnTypeCode());
                columnTreeVO.setIsParent(childrenCount != null && childrenCount > 0);
                columnTreeVOS.add(columnTreeVO);
            }
            return ResultObjectVO.ok(columnTreeVOS);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
    }

    public ResultTypeObjectVO<ColumnVO> findById(RequestJsonVO requestVo) {
        ResultTypeObjectVO<ColumnVO> resultObjectVO = new ResultTypeObjectVO<>();
        if (requestVo == null || requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }
        try {
            ColumnVO columnVO = requestVo.formatEntity(ColumnVO.class);
            if (columnVO.getId() == null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }
            columnVO = columnService.findById(columnVO.getId());
            if (columnVO != null) {
                resolveParentTitle(columnVO);
            }
            resultObjectVO.setData(columnVO);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        if (requestVo == null || requestVo.getEntityJson() == null) {
            return ResultObjectVO.fail(ResultVO.FAILD, "没有找到实体对象");
        }
        try {
            ColumnVO columnVO = JSONObject.parseObject(requestVo.getEntityJson(), ColumnVO.class);
            if (columnVO.getId() == null) {
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找到栏目ID");
            }
            List<ColumnVO> children = new ArrayList<>();
            columnService.queryChildren(children, columnVO);
            children.add(columnVO);
            List<Long> columnIdList = children.stream().map(ColumnVO::getId).collect(Collectors.toList());
            columnService.deleteByIdList(columnIdList);
            return ResultObjectVO.ok(columnVO);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
    }

    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        if (requestVo == null || requestVo.getEntityJson() == null) {
            return ResultObjectVO.fail(ResultVO.FAILD, "没有找到实体对象");
        }
        try {
            List<ColumnVO> columnVOS = JSONObject.parseArray(requestVo.getEntityJson(), ColumnVO.class);
            if (CollectionUtils.isEmpty(columnVOS)) {
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找到栏目ID");
            }
            List<ResultObjectVO> resultList = new ArrayList<>();
            for (ColumnVO columnVO : columnVOS) {
                if (columnVO.getId() != null) {
                    List<ColumnVO> children = new ArrayList<>();
                    columnService.queryChildren(children, columnVO);
                    children.add(columnVO);
                    List<Long> columnIdList = children.stream().map(ColumnVO::getId).collect(Collectors.toList());
                    columnService.deleteByIdList(columnIdList);
                    resultList.add(ResultObjectVO.ok(columnVO));
                }
            }
            return ResultObjectVO.ok(resultList);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
    }

    public ResultObjectVO queryListByPid(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null || requestJsonVO.getEntityJson() == null) {
            return ResultObjectVO.fail(ResultVO.FAILD, "没有找到实体对象");
        }
        try {
            ColumnVO queryColumn = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnVO.class);
            List<ColumnVO> columnVOS = columnService.queryListByPidAndAppCode(
                    queryColumn.getPid(), queryColumn.getAppCode());
            List<ColumnTreeVO> columnTreeVOS = new LinkedList<>();
            if (!CollectionUtils.isEmpty(columnVOS)) {
                for (ColumnVO columnVO : columnVOS) {
                    ColumnTreeVO columnTreeVO = new ColumnTreeVO();
                    BeanUtils.copyProperties(columnTreeVO, columnVO);
                    Long childCount = columnService.findCountByParentId(columnTreeVO.getId());
                    columnTreeVO.setIsParent(childCount != null && childCount > 0);
                    columnTreeVOS.add(columnTreeVO);
                }
            }
            return ResultObjectVO.ok(columnTreeVOS);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 构建分布式锁的 key
     */
    private String buildLockKey(ColumnVO columnVO) {
        return columnVO.getAppCode() + "_" + columnVO.getColumnTypeCode();
    }

    /**
     * 检查编码是否已存在。excludeId 为当前记录ID（更新场景排除自身），为 null 表示新增场景。
     *
     * @return 如果编码重复则返回错误 ResultObjectVO，否则返回 null
     */
    private ResultObjectVO checkCodeExists(ColumnVO columnVO, Long excludeId) {
        ColumnVO query = new ColumnVO();
        query.setCode(columnVO.getCode());
        query.setColumnTypeCode(columnVO.getColumnTypeCode());
        query.setAppCode(columnVO.getAppCode());
        List<ColumnVO> existingList = columnService.queryList(query);
        if (CollectionUtils.isEmpty(existingList)) {
            return null;
        }
        for (ColumnVO existing : existingList) {
            if (!Objects.equals(existing.getId(), excludeId)) {
                return ResultObjectVO.fail(ResultVO.FAILD, "该编码已存在");
            }
        }
        return null;
    }

    /**
     * 为栏目列表中的每个栏目解析其父级标题
     */
    private void resolveParentTitles(List<ColumnVO> columnVoList) {
        if (CollectionUtils.isEmpty(columnVoList)) {
            return;
        }

        // 收集所有需要查询的父节点ID（去重）
        List<Long> parentIdList = columnVoList.stream()
                .map(ColumnVO::getPid)
                .filter(pid -> pid != null && pid != -1)
                .distinct()
                .collect(Collectors.toList());

        // 设置根节点
        columnVoList.stream()
                .filter(vo -> vo.getPid() != null && vo.getPid() == -1)
                .forEach(vo -> vo.setParentTitle("根节点"));

        // 批量查询父节点并设置标题
        if (!CollectionUtils.isEmpty(parentIdList)) {
            ColumnVO queryParent = new ColumnVO();
            queryParent.setIdList(parentIdList);
            List<ColumnVO> parentList = columnService.queryList(queryParent);
            if (!CollectionUtils.isEmpty(parentList)) {
                for (ColumnVO columnVO : columnVoList) {
                    if (columnVO.getPid() != null && columnVO.getPid() != -1) {
                        for (ColumnVO parent : parentList) {
                            if (Objects.equals(columnVO.getPid(), parent.getId())) {
                                columnVO.setParentTitle(parent.getTitle());
                                break;
                            }
                        }
                    } else if (columnVO.getPid() != null && columnVO.getPid() == -1) {
                        columnVO.setParentTitle("根节点");
                    }
                }
            }
        }
    }

    /**
     * 为单个栏目解析其父级标题
     */
    private void resolveParentTitle(ColumnVO columnVO) {
        if (columnVO.getPid() != null) {
            ColumnVO parentColumn = columnService.findById(columnVO.getPid());
            if (parentColumn != null) {
                columnVO.setParentTitle(parentColumn.getTitle());
            } else {
                columnVO.setParentTitle("根节点");
            }
        }
    }

    /**
     * 按指定条件（标题、应用编码）查询栏目
     */
    private List<ColumnVO> queryColumnsByCriteria(ColumnPageInfo queryPageInfo) {
        ColumnVO queryColumnVO = new ColumnVO();
        try {
            BeanUtils.copyProperties(queryColumnVO, queryPageInfo);
        } catch (Exception e) {
            logger.warn("BeanUtils.copyProperties error", e);
        }
        queryColumnVO.setPid(null);
        return new ArrayList<>(columnService.queryList(queryColumnVO));
    }

    /**
     * 按父节点ID查询子栏目
     */
    private List<ColumnVO> queryColumnsByParentId(ColumnPageInfo queryPageInfo) {
        ColumnVO queryColumn = new ColumnVO();
        queryColumn.setPid(queryPageInfo.getPid() != null ? queryPageInfo.getPid() : -1L);
        queryColumn.setTitle(queryPageInfo.getTitle());
        queryColumn.setColumnTypeCode(queryPageInfo.getColumnTypeCode());
        List<ColumnVO> columnVOS = columnService.queryList(queryColumn);
        List<ColumnVO> result = new ArrayList<>(columnVOS.size());
        for (ColumnVO columnVO : columnVOS) {
            ColumnVO childQuery = new ColumnVO();
            childQuery.setPid(columnVO.getId());
            Long childCount = columnService.queryListCount(childQuery);
            if (childCount > 0) {
                columnVO.setHaveChild(true);
            }
            result.add(columnVO);
        }
        return result;
    }

}
