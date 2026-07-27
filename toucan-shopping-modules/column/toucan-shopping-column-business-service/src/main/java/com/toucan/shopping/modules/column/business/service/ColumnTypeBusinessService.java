package com.toucan.shopping.modules.column.business.service;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.column.page.ColumnTypePageInfo;
import com.toucan.shopping.modules.column.redis.ColumnTypeLockKey;
import com.toucan.shopping.modules.column.service.ColumnTypeService;
import com.toucan.shopping.modules.column.vo.ColumnTypeVO;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ColumnTypeVO columnTypeVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnTypeVO.class);
        Check.notEmpty(columnTypeVO.getName(), ResultObjectVO.FAILD, "类型名称不能为空");
        Check.notEmpty(columnTypeVO.getCode(), ResultObjectVO.FAILD, "类型编码不能为空");
        Check.notEmpty(columnTypeVO.getAppCode(), ResultObjectVO.FAILD, "所属应用不能为空");
        String lockKey = columnTypeVO.getAppCode() + "_" + columnTypeVO.getCode();
        try {
            boolean lockStatus = skylarkLock.lock(ColumnTypeLockKey.getSaveLockKey(lockKey), lockKey);
            if (!lockStatus) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请稍后重试");
            }

            ColumnTypeVO query = new ColumnTypeVO();
            query.setCode(columnTypeVO.getCode());
            List<ColumnTypeVO> columnTypeList = columnTypeService.queryList(query);
            Check.isTrue(CollectionUtils.isEmpty(columnTypeList), ResultObjectVO.FAILD, "该编码已存在");

            columnTypeVO.setId(idGenerator.id());
            columnTypeVO.setDeleteStatus((short) 0);
            columnTypeVO.setCreateDate(new Date());
            int ret = columnTypeService.save(columnTypeVO);
            if (ret <= 0) {
                logger.warn("保存栏目类型失败 requestJson{} id{}", requestJsonVO.getEntityJson(), columnTypeVO.getId());
                return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
            }
            resultObjectVO.setData(columnTypeVO);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        } finally {
            skylarkLock.unLock(ColumnTypeLockKey.getSaveLockKey(lockKey), lockKey);
        }
        return resultObjectVO;
    }

    /**
     * 批量删除栏目类型
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<ColumnTypeVO> columnTypeVOS = JSONObject.parseArray(requestVo.getEntityJson(), ColumnTypeVO.class);
            Check.notEmpty(columnTypeVOS, ResultVO.FAILD, "没有找到ID");
            List<ResultObjectVO> resultObjectVOList = new ArrayList<>();
            for (ColumnTypeVO columnTypeVO : columnTypeVOS) {
                if (columnTypeVO.getId() != null) {
                    ResultObjectVO itemResult = new ResultObjectVO();
                    itemResult.setData(columnTypeVO);

                    int row = columnTypeService.deleteById(columnTypeVO.getId());
                    if (row < 1) {
                        logger.warn("删除栏目类型失败，id:{}", columnTypeVO.getId());
                        itemResult = ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
                    }
                    resultObjectVOList.add(itemResult);
                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 删除单个栏目类型
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ColumnTypeVO columnTypeVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnTypeVO.class);
            Check.notNull(columnTypeVO.getId(), ResultVO.FAILD, "ID不能为空!");

            int ret = columnTypeService.deleteById(columnTypeVO.getId());
            Check.isTrue(ret > 0, ResultVO.FAILD, "不存在该类型!");

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
        }
        return resultObjectVO;
    }

    /**
     * 更新栏目类型
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ColumnTypeVO columnTypeVO = JSONObject.parseObject(requestVo.getEntityJson(), ColumnTypeVO.class);

        Check.notNull(columnTypeVO.getId(), ResultVO.FAILD, "请传入ID");
        Check.notEmpty(columnTypeVO.getCode(), ResultVO.FAILD, "编码不能为空!");
        Check.notEmpty(columnTypeVO.getName(), ResultVO.FAILD, "名称不能为空!");

        String lockKey = columnTypeVO.getAppCode() + "_" + columnTypeVO.getCode();
        boolean lockStatus = skylarkLock.lock(ColumnTypeLockKey.getUpdateLockKey(lockKey), lockKey);
        if (!lockStatus) {
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        try {
            ColumnTypeVO query = new ColumnTypeVO();
            query.setCode(columnTypeVO.getCode());
            List<ColumnTypeVO> columnTypeList = columnTypeService.queryList(query);
            if (!CollectionUtils.isEmpty(columnTypeList)) {
                for (ColumnTypeVO existing : columnTypeList) {
                    if (existing.getId().longValue() != columnTypeVO.getId().longValue()) {
                        return ResultObjectVO.fail(ResultVO.FAILD, "该编码已存在");
                    }
                }
            }

            columnTypeVO.setUpdateDate(new Date());
            int row = columnTypeService.update(columnTypeVO);
            Check.isTrue(row >= 1, ResultVO.FAILD, "请重试!");

            resultObjectVO.setData(columnTypeVO);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        } finally {
            skylarkLock.unLock(ColumnTypeLockKey.getUpdateLockKey(lockKey), lockKey);
        }
        return resultObjectVO;
    }

    /**
     * 根据ID查询栏目类型
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ColumnTypeVO columnTypeVO = JSONObject.parseObject(requestVo.getEntityJson(), ColumnTypeVO.class);
            Check.notNull(columnTypeVO.getId(), ResultVO.FAILD, "没有找到ID");

            ColumnTypeVO query = new ColumnTypeVO();
            query.setId(columnTypeVO.getId());
            List<ColumnTypeVO> columnTypeList = columnTypeService.queryList(query);
            Check.notEmpty(columnTypeList, ResultVO.FAILD, "不存在!");

            resultObjectVO.setData(columnTypeList);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据编码查询栏目类型
     */
    @RequestCheck(requireEntity = true)
    public ResultTypeObjectVO<ColumnTypeVO> findOneByCode(RequestJsonVO requestVo) {
        ResultTypeObjectVO resultObjectVO = new ResultTypeObjectVO();

        try {
            ColumnTypeVO columnTypeVO = requestVo.formatEntity(ColumnTypeVO.class);
            Check.notEmpty(columnTypeVO.getCode(), ResultVO.FAILD, "编码不能为空");

            ColumnTypeVO query = new ColumnTypeVO();
            query.setCode(columnTypeVO.getCode());
            List<ColumnTypeVO> columnTypeList = columnTypeService.queryList(query);
            Check.notEmpty(columnTypeList, ResultVO.FAILD, "不存在!");

            resultObjectVO.setData(columnTypeList.get(0));

        }catch(BusinessValidationException e){
            return ResultTypeObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ColumnTypePageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnTypePageInfo.class);
            PageInfo<ColumnTypeVO> pageInfo = columnTypeService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "查询失败!");
        }
        return resultObjectVO;
    }

    /**
     * 查询栏目类型列表
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryList(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ColumnTypeVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnTypeVO.class);
            resultObjectVO.setData(columnTypeService.queryList(query));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "查询失败!");
        }
        return resultObjectVO;
    }

}
