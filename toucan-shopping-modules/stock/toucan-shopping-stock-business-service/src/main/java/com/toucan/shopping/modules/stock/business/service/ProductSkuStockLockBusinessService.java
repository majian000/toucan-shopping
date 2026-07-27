package com.toucan.shopping.modules.stock.business.service;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.stock.page.ProductSkuStockLockPageInfo;
import com.toucan.shopping.modules.stock.service.ProductSkuStockLockService;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductSkuStockLockBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductSkuStockLockService productSkuStockLockService;

    @Autowired
    private IdGenerator idGenerator;


    // ==================== 私有辅助方法 ====================

    /**
     * 验证基本请求参数（requestJsonVO 非空、appCode 非空）
     *
     * @param requestJsonVO 请求参数
     * @param appType       应用类型描述（如"应用"、"对象"）
     * @return null 表示验证通过；非 null 表示验证失败，调用方应直接返回该错误结果
     */
    private ResultObjectVO validateBasicRequest(RequestJsonVO requestJsonVO, String appType) {
        if (requestJsonVO == null) {
            logger.info("请求参数为空");
            ResultObjectVO resultObjectVO = new ResultObjectVO();
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if (requestJsonVO.getAppCode() == null) {
            logger.info("没有找到{}: param:{}", appType, JSONObject.toJSONString(requestJsonVO));
            ResultObjectVO resultObjectVO = new ResultObjectVO();
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到" + appType + "!");
            return resultObjectVO;
        }
        return null;
    }

    /**
     * 设置通用异常响应结果
     */
    private void setFailureResult(ResultObjectVO resultObjectVO, Exception e, String errorMsg) {
        logger.warn(e.getMessage(), e);
        resultObjectVO.setCode(ResultVO.FAILD);
        resultObjectVO.setMsg(errorMsg);
    }

    /**
     * 根据查询条件查询锁定库存记录并执行删除（还原）操作
     */
    private void deleteLockStockByQuery(ProductSkuStockLockVO queryVO) {
        List<ProductSkuStockLockVO> locks = productSkuStockLockService.queryListByVO(queryVO);
        if (!CollectionUtils.isEmpty(locks)) {
            List<Long> ids = locks.stream().map(ProductSkuStockLockVO::getId).collect(Collectors.toList());
            int ret = productSkuStockLockService.restores(ids);
            if (ret <= 0 || ret != locks.size()) {
                throw new IllegalArgumentException("删除锁定库存出现异常");
            }
        }
    }


    // ==================== 业务方法 ====================

    /**
     * 锁定库存
     */
    public ResultObjectVO lockStock(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = validateBasicRequest(requestJsonVO, "应用");
        if (resultObjectVO != null) {
            return resultObjectVO;
        }

        List<ProductSkuStockLockVO> productSkuStockLocks = requestJsonVO.formatEntityList(ProductSkuStockLockVO.class);
        if (CollectionUtils.isEmpty(productSkuStockLocks)) {
            logger.info("没有找到请求参数: param:{}", JSONObject.toJSONString(requestJsonVO));
            resultObjectVO = new ResultObjectVO();
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到要锁定库存的商品!");
            return resultObjectVO;
        }

        resultObjectVO = new ResultObjectVO();
        try {
            for (ProductSkuStockLockVO stockLock : productSkuStockLocks) {
                stockLock.setId(idGenerator.id());
                stockLock.setCreateDate(new Date());
                int ret = productSkuStockLockService.save(stockLock);
                if (ret <= 0) {
                    throw new IllegalArgumentException("保存失败");
                }
            }
            resultObjectVO.setData(productSkuStockLocks);
            return resultObjectVO;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("锁定库存出现异常!");

            // 回滚：删除已保存的锁定记录
            List<Long> idList = productSkuStockLocks.stream().map(ProductSkuStockLockVO::getId).collect(Collectors.toList());
            logger.warn("数据回滚 {}", JSONObject.toJSONString(productSkuStockLocks));
            try {
                productSkuStockLockService.deletes(idList);
            } catch (Exception rollbackEx) {
                logger.error("数据回滚失败 {}", JSONObject.toJSONString(productSkuStockLocks), rollbackEx);
            }
        }
        return resultObjectVO;
    }


    /**
     * 查询列表（分页）
     */
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = validateBasicRequest(requestJsonVO, "对象");
        if (resultObjectVO != null) {
            return resultObjectVO;
        }

        resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSkuStockLockPageInfo.class);
            PageInfo<ProductSkuStockLockVO> pageInfo = productSkuStockLockService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        } catch (Exception e) {
            setFailureResult(resultObjectVO, e, "查询失败!");
        }
        return resultObjectVO;
    }


    /**
     * 根据ID查询
     */
    public ResultObjectVO findById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = validateBasicRequest(requestJsonVO, "对象");
        if (resultObjectVO != null) {
            return resultObjectVO;
        }

        resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockVO productSkuStockLockVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSkuStockLockVO.class);
            resultObjectVO.setData(productSkuStockLockService.findById(productSkuStockLockVO.getId()));
        } catch (Exception e) {
            setFailureResult(resultObjectVO, e, "查询失败!");
        }
        return resultObjectVO;
    }


    /**
     * 删除锁定库存（通过ID列表批量还原）
     */
    public ResultObjectVO deleteLockStock(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = validateBasicRequest(requestJsonVO, "应用");
        if (resultObjectVO != null) {
            return resultObjectVO;
        }

        List<ProductSkuStockLockVO> productSkuStockLocks = requestJsonVO.formatEntityList(ProductSkuStockLockVO.class);
        if (CollectionUtils.isEmpty(productSkuStockLocks)) {
            logger.info("没有找到请求参数: param:{}", JSONObject.toJSONString(requestJsonVO));
            resultObjectVO = new ResultObjectVO();
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到要锁定库存的商品!");
            return resultObjectVO;
        }

        resultObjectVO = new ResultObjectVO();
        try {
            List<Long> ids = productSkuStockLocks.stream().map(ProductSkuStockLockVO::getId).collect(Collectors.toList());
            int ret = productSkuStockLockService.restores(ids);
            if (ret <= 0 || ret != productSkuStockLocks.size()) {
                throw new IllegalArgumentException("删除锁定库存出现异常");
            }
        } catch (Exception e) {
            setFailureResult(resultObjectVO, e, "删除锁定库存出现异常");
        }
        return resultObjectVO;
    }


    /**
     * 根据SKU ID列表查询锁定库存数量
     */
    public ResultObjectVO findLockStockNumByProductSkuIds(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = validateBasicRequest(requestJsonVO, "应用");
        if (resultObjectVO != null) {
            return resultObjectVO;
        }

        resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockVO vo = requestJsonVO.formatEntity(ProductSkuStockLockVO.class);
            if (CollectionUtils.isEmpty(vo.getProductSkuIdList())) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("SKU ID不能为空");
                return resultObjectVO;
            }
            vo.setRestoreStatus((short) 0);
            resultObjectVO.setData(productSkuStockLockService.queryStockNumByVO(vo));
        } catch (Exception e) {
            setFailureResult(resultObjectVO, e, "查询锁定库存出现异常!");
        }
        return resultObjectVO;
    }


    /**
     * 根据主订单编号列表查询锁定库存数量
     */
    public ResultObjectVO findLockStockNumByMainOrderNos(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = validateBasicRequest(requestJsonVO, "应用");
        if (resultObjectVO != null) {
            return resultObjectVO;
        }

        resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockVO vo = requestJsonVO.formatEntity(ProductSkuStockLockVO.class);
            if (CollectionUtils.isEmpty(vo.getMainOrderNoList())) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("主订单编号不能为空");
                return resultObjectVO;
            }
            vo.setRestoreStatus((short) 0);
            resultObjectVO.setData(productSkuStockLockService.queryStockNumByVO(vo));
        } catch (Exception e) {
            setFailureResult(resultObjectVO, e, "查询锁定库存出现异常!");
        }
        return resultObjectVO;
    }


    /**
     * 根据主订单编号列表查询锁定库存列表
     */
    public ResultObjectVO findLockStockListByMainOrderNos(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = validateBasicRequest(requestJsonVO, "应用");
        if (resultObjectVO != null) {
            return resultObjectVO;
        }

        resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockVO vo = requestJsonVO.formatEntity(ProductSkuStockLockVO.class);
            if (CollectionUtils.isEmpty(vo.getMainOrderNoList())) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("主订单编号不能为空");
                return resultObjectVO;
            }
            vo.setRestoreStatus((short) 0);
            resultObjectVO.setData(productSkuStockLockService.queryListByVO(vo));
        } catch (Exception e) {
            setFailureResult(resultObjectVO, e, "查询锁定库存出现异常!");
        }
        return resultObjectVO;
    }


    /**
     * 根据子订单编号查询锁定库存数量
     */
    public ResultObjectVO findLockStockNumByOrderNo(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = validateBasicRequest(requestJsonVO, "应用");
        if (resultObjectVO != null) {
            return resultObjectVO;
        }

        resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockVO vo = requestJsonVO.formatEntity(ProductSkuStockLockVO.class);
            if (StringUtils.isEmpty(vo.getOrderNo())) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("订单编号不能为空");
                return resultObjectVO;
            }
            vo.setRestoreStatus((short) 0);
            resultObjectVO.setData(productSkuStockLockService.queryStockNumByVO(vo));
        } catch (Exception e) {
            setFailureResult(resultObjectVO, e, "查询锁定库存出现异常!");
        }
        return resultObjectVO;
    }


    /**
     * 根据子订单编号删除锁定库存（还原）
     */
    public ResultObjectVO deleteLockStockByOrderNo(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = validateBasicRequest(requestJsonVO, "应用");
        if (resultObjectVO != null) {
            return resultObjectVO;
        }

        resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockVO vo = requestJsonVO.formatEntity(ProductSkuStockLockVO.class);
            if (StringUtils.isEmpty(vo.getOrderNo())) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("订单编号不能为空");
                return resultObjectVO;
            }
            vo.setType(null);
            vo.setRestoreStatus((short) 0);
            deleteLockStockByQuery(vo);
        } catch (Exception e) {
            setFailureResult(resultObjectVO, e, "删除锁定库存出现异常");
        }
        return resultObjectVO;
    }


    /**
     * 根据主订单编号列表删除锁定库存（还原）
     */
    public ResultObjectVO deleteLockStockByMainOrderNos(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = validateBasicRequest(requestJsonVO, "应用");
        if (resultObjectVO != null) {
            return resultObjectVO;
        }

        resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockVO vo = requestJsonVO.formatEntity(ProductSkuStockLockVO.class);
            if (CollectionUtils.isEmpty(vo.getMainOrderNoList())) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("主订单编号不能为空");
                return resultObjectVO;
            }
            vo.setType(null);
            vo.setRestoreStatus((short) 0);
            deleteLockStockByQuery(vo);
        } catch (Exception e) {
            setFailureResult(resultObjectVO, e, "删除锁定库存出现异常");
        }
        return resultObjectVO;
    }

}
