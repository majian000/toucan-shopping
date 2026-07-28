package com.toucan.shopping.modules.color.table.business.service;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.toucan.shopping.modules.color.table.entity.ColorTable;
import com.toucan.shopping.modules.color.table.page.ColorTablePageInfo;
import com.toucan.shopping.modules.color.table.service.ColorTableService;
import com.toucan.shopping.modules.color.table.vo.ColorTableVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;


/**
 * 颜色表操作
 */
@Service
public class ColorTableBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // ── 常用错误消息 ──────────────────────────────────────────────

    private static final String MSG_RETRY              = "请重试!";
    private static final String MSG_RETRY_LATER        = "请稍后重试";
    private static final String MSG_ID_NOT_FOUND       = "没有找到ID";
    private static final String MSG_ID_REQUIRED        = "请传入ID";
    private static final String MSG_NAME_EMPTY         = "名称不能为空!";
    private static final String MSG_COLOR_EMPTY        = "颜色值不能为空!";
    private static final String MSG_NAME_EXISTS        = "该名称已存在!";
    private static final String MSG_NOT_EXIST          = "不存在!";
    private static final String MSG_COLOR_TABLE_NOT_EXIST = "颜色表不存在!";
    private static final String MSG_NAMES_EMPTY        = "名称集合不能为空!";
    private static final String MSG_QUERY_FAILED       = "查询失败!";

    @Autowired
    private ColorTableService colorTableService;

    @Autowired
    private IdGenerator idGenerator;


    // ── 业务方法 ──────────────────────────────────────────────────

    /**
     * 保存颜色表
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        Long entityId = -1L;
        try {
            entityId = idGenerator.id();
            ColorTableVO colorTableVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColorTableVO.class);

            Check.notEmpty(colorTableVO.getName(), ResultObjectVO.FAILD, MSG_NAME_EMPTY);

            ColorTableVO queryColorTable = new ColorTableVO();
            queryColorTable.setName(colorTableVO.getName());
            List<ColorTableVO> colorTableVOS = colorTableService.queryList(queryColorTable);
            if (CollectionUtils.isNotEmpty(colorTableVOS)) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, MSG_NAME_EXISTS);
            }

            ColorTable colorTable = new ColorTable();
            BeanUtils.copyProperties(colorTable, colorTableVO);
            colorTable.setId(entityId);
            colorTable.setCreateDate(new Date());
            colorTable.setDeleteStatus((short) 0);
            int row = colorTableService.save(colorTable);
            Check.isTrue(row > 0, ResultObjectVO.FAILD, MSG_RETRY);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultObjectVO.FAILD, MSG_RETRY);
        }
        return new ResultObjectVO();
    }


    /**
     * 根据ID查询
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestJsonVO) {
        try {
            ColorTableVO entityVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColorTableVO.class);
            Check.notNull(entityVO.getId(), ResultObjectVO.FAILD, MSG_ID_NOT_FOUND);

            ColorTableVO query = new ColorTableVO();
            query.setId(entityVO.getId());
            List<ColorTableVO> colorTableVOS = colorTableService.queryList(query);
            Check.notEmpty(colorTableVOS, ResultObjectVO.FAILD, MSG_NOT_EXIST);
            ResultObjectVO result = new ResultObjectVO();
            result.setData(colorTableVOS);
            return result;

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }


    /**
     * 编辑
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        try {
            ColorTableVO entity = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColorTableVO.class);

            Check.notEmpty(entity.getName(), ResultObjectVO.FAILD, MSG_NAME_EMPTY);
            Check.notEmpty(entity.getRgbColor(), ResultObjectVO.FAILD, MSG_COLOR_EMPTY);
            Check.notNull(entity.getId(), ResultObjectVO.FAILD, MSG_ID_REQUIRED);

            // 检查名称是否已被其他记录占用
            ColorTableVO queryColorTable = new ColorTableVO();
            queryColorTable.setName(entity.getName());
            List<ColorTableVO> colorTableVOS = colorTableService.queryList(queryColorTable);
            if (CollectionUtils.isNotEmpty(colorTableVOS)) {
                for (ColorTableVO colorTableVO : colorTableVOS) {
                    if (colorTableVO.getId() != null && !Objects.equals(colorTableVO.getId(), entity.getId())) {
                        return ResultObjectVO.fail(ResultObjectVO.FAILD, MSG_NAME_EXISTS);
                    }
                }
            }

            entity.setUpdateDate(new Date());
            int row = colorTableService.update(entity);
            Check.isTrue(row >= 1, ResultObjectVO.FAILD, MSG_RETRY);

            ResultObjectVO result = new ResultObjectVO();
            result.setData(entity);
            return result;

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }


    /**
     * 查询列表分页
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        try {
            ColorTablePageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColorTablePageInfo.class);
            PageInfo<ColorTableVO> pageInfo = colorTableService.queryListPage(queryPageInfo);
            ResultObjectVO result = new ResultObjectVO();
            result.setData(pageInfo);
            return result;
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultObjectVO.FAILD, MSG_QUERY_FAILED);
        }
    }


    /**
     * 根据名称集合查询列表
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListByNames(RequestJsonVO requestJsonVO) {
        try {
            ColorTableVO colorTableVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColorTableVO.class);
            Check.notEmpty(colorTableVO.getNameList(), ResultObjectVO.FAILD, MSG_NAMES_EMPTY);
            ResultObjectVO result = new ResultObjectVO();
            result.setData(colorTableService.queryList(colorTableVO));
            return result;
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultObjectVO.FAILD, MSG_QUERY_FAILED);
        }
    }


    /**
     * 查询列表
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryList(RequestJsonVO requestJsonVO) {
        try {
            ColorTableVO entityVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColorTableVO.class);
            List<ColorTableVO> entityVOS = colorTableService.queryList(entityVO);
            ResultObjectVO result = new ResultObjectVO();
            result.setData(entityVOS);
            return result;
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultObjectVO.FAILD, MSG_QUERY_FAILED);
        }
    }


    /**
     * 删除指定
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        try {
            ColorTable entity = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColorTable.class);
            Check.notNull(entity.getId(), ResultObjectVO.FAILD, MSG_ID_NOT_FOUND);

            ColorTableVO query = new ColorTableVO();
            query.setId(entity.getId());
            List<ColorTableVO> colorTableList = colorTableService.queryList(query);
            Check.notEmpty(colorTableList, ResultObjectVO.FAILD, MSG_COLOR_TABLE_NOT_EXIST);

            int row = colorTableService.deleteById(entity.getId());
            Check.isTrue(row >= 1, ResultObjectVO.FAILD, MSG_RETRY);

            ResultObjectVO result = new ResultObjectVO();
            result.setData(entity);
            return result;

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }


    /**
     * 批量删除
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestJsonVO) {
        try {
            List<ColorTable> entities = JSON.parseArray(requestJsonVO.getEntityJson(), ColorTable.class);
            Check.notEmpty(entities, ResultObjectVO.FAILD, MSG_ID_NOT_FOUND);

            List<ResultObjectVO> resultList = new ArrayList<>();
            for (ColorTable entity : entities) {
                if (entity.getId() != null) {
                    int row = colorTableService.deleteById(entity.getId());
                    if (row < 1) {
                        logger.warn("删除颜色表失败，id:{}", entity.getId());
                    }
                }
            }

            ResultObjectVO result = new ResultObjectVO();
            result.setData(entities);
            return result;

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, MSG_RETRY_LATER);
        }
    }

}
