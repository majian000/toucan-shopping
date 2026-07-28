package com.toucan.shopping.modules.stock.business.service;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.stock.page.ProductSkuStockLockPageInfo;
import com.toucan.shopping.modules.stock.service.ProductSkuStockLockService;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO lockStock(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        List<ProductSkuStockLockVO> productSkuStockLocks = null;
        try {
            productSkuStockLocks = requestJsonVO.formatEntityList(ProductSkuStockLockVO.class);
            Check.notEmpty(productSkuStockLocks, ResultVO.FAILD, "没有找到要锁定库存的商品!");

            for (ProductSkuStockLockVO stockLock : productSkuStockLocks) {
                stockLock.setId(idGenerator.id());
                stockLock.setCreateDate(new Date());
                int ret = productSkuStockLockService.save(stockLock);
                if (ret <= 0) {
                    throw new IllegalArgumentException("保存失败");
                }
            }
            resultObjectVO.setData(productSkuStockLocks);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "锁定库存出现异常!");

            // 回滚：删除已保存的锁定记录
            if (productSkuStockLocks != null) {
                List<Long> idList = productSkuStockLocks.stream().map(ProductSkuStockLockVO::getId).collect(Collectors.toList());
                logger.warn("数据回滚 {}", JSONObject.toJSONString(productSkuStockLocks));
                try {
                    productSkuStockLockService.deletes(idList);
                } catch (Exception rollbackEx) {
                    logger.error("数据回滚失败 {}", JSONObject.toJSONString(productSkuStockLocks), rollbackEx);
                }
            }
        }
        return resultObjectVO;
    }


    /**
     * 查询列表（分页）
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSkuStockLockPageInfo.class);
            PageInfo<ProductSkuStockLockVO> pageInfo = productSkuStockLockService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "查询失败!");
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
            ProductSkuStockLockVO productSkuStockLockVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSkuStockLockVO.class);
            resultObjectVO.setData(productSkuStockLockService.findById(productSkuStockLockVO.getId()));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "查询失败!");
        }
        return resultObjectVO;
    }


    /**
     * 删除锁定库存（通过ID列表批量还原）
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteLockStock(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            List<ProductSkuStockLockVO> productSkuStockLocks = requestJsonVO.formatEntityList(ProductSkuStockLockVO.class);
            Check.notEmpty(productSkuStockLocks, ResultVO.FAILD, "没有找到要锁定库存的商品!");

            List<Long> ids = productSkuStockLocks.stream().map(ProductSkuStockLockVO::getId).collect(Collectors.toList());
            int ret = productSkuStockLockService.restores(ids);
            if (ret <= 0 || ret != productSkuStockLocks.size()) {
                throw new IllegalArgumentException("删除锁定库存出现异常");
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "删除锁定库存出现异常");
        }
        return resultObjectVO;
    }


    /**
     * 根据SKU ID列表查询锁定库存数量
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findLockStockNumByProductSkuIds(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockVO vo = requestJsonVO.formatEntity(ProductSkuStockLockVO.class);
            Check.notEmpty(vo.getProductSkuIdList(), ResultVO.FAILD, "SKU ID不能为空");

            vo.setRestoreStatus((short) 0);
            resultObjectVO.setData(productSkuStockLockService.queryStockNumByVO(vo));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "查询锁定库存出现异常!");
        }
        return resultObjectVO;
    }


    /**
     * 根据主订单编号列表查询锁定库存数量
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findLockStockNumByMainOrderNos(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockVO vo = requestJsonVO.formatEntity(ProductSkuStockLockVO.class);
            Check.notEmpty(vo.getMainOrderNoList(), ResultVO.FAILD, "主订单编号不能为空");

            vo.setRestoreStatus((short) 0);
            resultObjectVO.setData(productSkuStockLockService.queryStockNumByVO(vo));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "查询锁定库存出现异常!");
        }
        return resultObjectVO;
    }


    /**
     * 根据主订单编号列表查询锁定库存列表
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findLockStockListByMainOrderNos(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockVO vo = requestJsonVO.formatEntity(ProductSkuStockLockVO.class);
            Check.notEmpty(vo.getMainOrderNoList(), ResultVO.FAILD, "主订单编号不能为空");

            vo.setRestoreStatus((short) 0);
            resultObjectVO.setData(productSkuStockLockService.queryListByVO(vo));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "查询锁定库存出现异常!");
        }
        return resultObjectVO;
    }


    /**
     * 根据子订单编号查询锁定库存数量
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findLockStockNumByOrderNo(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockVO vo = requestJsonVO.formatEntity(ProductSkuStockLockVO.class);
            Check.notEmpty(vo.getOrderNo(), ResultVO.FAILD, "订单编号不能为空");

            vo.setRestoreStatus((short) 0);
            resultObjectVO.setData(productSkuStockLockService.queryStockNumByVO(vo));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "查询锁定库存出现异常!");
        }
        return resultObjectVO;
    }


    /**
     * 根据子订单编号删除锁定库存（还原）
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteLockStockByOrderNo(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockVO vo = requestJsonVO.formatEntity(ProductSkuStockLockVO.class);
            Check.notEmpty(vo.getOrderNo(), ResultVO.FAILD, "订单编号不能为空");

            vo.setType(null);
            vo.setRestoreStatus((short) 0);
            deleteLockStockByQuery(vo);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "删除锁定库存出现异常");
        }
        return resultObjectVO;
    }


    /**
     * 根据主订单编号列表删除锁定库存（还原）
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteLockStockByMainOrderNos(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuStockLockVO vo = requestJsonVO.formatEntity(ProductSkuStockLockVO.class);
            Check.notEmpty(vo.getMainOrderNoList(), ResultVO.FAILD, "主订单编号不能为空");

            vo.setType(null);
            vo.setRestoreStatus((short) 0);
            deleteLockStockByQuery(vo);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "删除锁定库存出现异常");
        }
        return resultObjectVO;
    }

}
