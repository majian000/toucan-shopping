package com.toucan.shopping.modules.column.business.service;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.column.entity.HotProduct;
import com.toucan.shopping.modules.column.page.HotProductPageInfo;
import com.toucan.shopping.modules.column.redis.HotProductLockKey;
import com.toucan.shopping.modules.column.service.HotProductService;
import com.toucan.shopping.modules.column.vo.HotProductVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

@Service
public class HotProductBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HotProductService hotProductService;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private IdGenerator idGenerator;

    // ----------------------------------------------------------------
    //  private utility methods
    // ----------------------------------------------------------------

    private ResultObjectVO fail(String msg) {
        ResultObjectVO vo = new ResultObjectVO();
        vo.setCode(ResultVO.FAILD);
        vo.setMsg(msg);
        return vo;
    }

    /**
     * 校验请求对象和实体JSON非空，返回错误响应则调用方直接返回
     */
    private ResultObjectVO validateRequestAndEntity(RequestJsonVO requestJsonVO) {
        if (requestJsonVO == null) {
            logger.warn("请求参数为空");
            return fail("请重试!");
        }
        if (requestJsonVO.getEntityJson() == null) {
            logger.warn("没有找到实体对象");
            return fail("没有找到实体对象");
        }
        return null;
    }

    /**
     * 校验应用编码非空
     */
    private ResultObjectVO validateAppCode(RequestJsonVO requestJsonVO) {
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            logger.warn("没有找到应用编码");
            return fail("没有找到应用编码");
        }
        return null;
    }

    /**
     * 校验商品名称非空
     */
    private ResultObjectVO validateProductName(HotProductVO hotProductVO) {
        if (StringUtils.isEmpty(hotProductVO.getProductName())) {
            return fail("商品名称不能为空");
        }
        return null;
    }

    /**
     * 校验VO上的应用编码非空
     */
    private ResultObjectVO validateVoAppCode(HotProductVO hotProductVO) {
        if (StringUtils.isEmpty(hotProductVO.getAppCode())) {
            return fail("所属应用不能为空");
        }
        return null;
    }

    /**
     * 分布式锁包装执行：加锁 -> 执行业务逻辑 -> 解锁
     */
    private ResultObjectVO executeWithLock(String lockKey, String lockValue,
                                           Supplier<ResultObjectVO> businessLogic) {
        if (!skylarkLock.lock(lockKey, lockValue)) {
            return fail("请稍后重试");
        }
        try {
            return businessLogic.get();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return fail("请稍后重试");
        } finally {
            skylarkLock.unLock(lockKey, lockValue);
        }
    }

    /**
     * 按名称+应用编码查重，如果已存在则返回错误响应；否则返回null
     */
    private ResultObjectVO checkDuplicateName(String productName, String appCode, String errorMsg) {
        HotProductVO query = new HotProductVO();
        query.setProductName(productName);
        query.setAppCode(appCode);
        List<HotProductVO> list = hotProductService.queryList(query);
        if (!CollectionUtils.isEmpty(list)) {
            return fail(errorMsg);
        }
        return null;
    }

    /**
     * 保存和更新共用的实体层校验：商品名称 + 应用编码
     */
    private ResultObjectVO validateSaveOrUpdateEntity(HotProductVO hotProductVO) {
        ResultObjectVO vo = validateProductName(hotProductVO);
        if (vo != null) return vo;
        return validateVoAppCode(hotProductVO);
    }

    // ----------------------------------------------------------------
    //  public service methods
    // ----------------------------------------------------------------

    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = validateRequestAndEntity(requestJsonVO);
        if (resultObjectVO != null) return resultObjectVO;

        resultObjectVO = validateAppCode(requestJsonVO);
        if (resultObjectVO != null) return resultObjectVO;

        try {
            HotProductPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), HotProductPageInfo.class);
            PageInfo<HotProductVO> pageInfo = hotProductService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = validateRequestAndEntity(requestJsonVO);
        if (resultObjectVO != null) return resultObjectVO;

        resultObjectVO = validateAppCode(requestJsonVO);
        if (resultObjectVO != null) return resultObjectVO;

        HotProductVO hotProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), HotProductVO.class);

        resultObjectVO = validateSaveOrUpdateEntity(hotProductVO);
        if (resultObjectVO != null) return resultObjectVO;

        String lockValue = hotProductVO.getAppCode();
        return executeWithLock(HotProductLockKey.getSaveLockKey(lockValue), lockValue, () -> {
            ResultObjectVO dup = checkDuplicateName(hotProductVO.getProductName(), hotProductVO.getAppCode(), "该编码已存在");
            if (dup != null) return dup;

            hotProductVO.setId(idGenerator.id());
            hotProductVO.setDeleteStatus((short) 0);
            hotProductVO.setCreateDate(new Date());
            int ret = hotProductService.save(hotProductVO);
            if (ret <= 0) {
                logger.warn("保存热门商品失败 requestJson{} id{}", requestJsonVO.getEntityJson(), hotProductVO.getId());
                return fail("请稍后重试");
            }
            ResultObjectVO ok = new ResultObjectVO();
            ok.setData(hotProductVO);
            return ok;
        });
    }

    public ResultObjectVO findById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = validateRequestAndEntity(requestVo);
        if (resultObjectVO != null) return resultObjectVO;

        try {
            HotProduct hotProduct = JSONObject.parseObject(requestVo.getEntityJson(), HotProduct.class);
            if (hotProduct.getId() == null) {
                return fail("没有找到ID");
            }

            hotProduct = hotProductService.findById(hotProduct.getId());
            if (hotProduct == null) {
                return fail("不存在!");
            }

            resultObjectVO.setData(hotProduct);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = validateRequestAndEntity(requestJsonVO);
        if (resultObjectVO != null) return resultObjectVO;

        resultObjectVO = validateAppCode(requestJsonVO);
        if (resultObjectVO != null) return resultObjectVO;

        try {
            HotProductVO hotProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), HotProductVO.class);
            if (hotProductVO.getId() == null) {
                logger.warn("ID为空 param:{}", JSONObject.toJSONString(hotProductVO));
                return fail("ID不能为空!");
            }

            int ret = hotProductService.deleteById(hotProductVO.getId());
            if (ret <= 0) {
                return fail("不存在该热门商品!");
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
        }
        return resultObjectVO;
    }

    @Transactional
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = validateRequestAndEntity(requestJsonVO);
        if (resultObjectVO != null) return resultObjectVO;

        resultObjectVO = validateAppCode(requestJsonVO);
        if (resultObjectVO != null) return resultObjectVO;

        HotProductVO hotProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), HotProductVO.class);

        if (hotProductVO.getId() == null) {
            return fail("ID不能为空");
        }

        resultObjectVO = validateSaveOrUpdateEntity(hotProductVO);
        if (resultObjectVO != null) return resultObjectVO;

        String lockValue = hotProductVO.getAppCode();
        return executeWithLock(HotProductLockKey.getUpdateLockKey(lockValue), lockValue, () -> {
            HotProductVO query = new HotProductVO();
            query.setProductName(hotProductVO.getProductName());
            query.setAppCode(hotProductVO.getAppCode());
            List<HotProductVO> hotProductVOS = hotProductService.queryList(query);
            if (!CollectionUtils.isEmpty(hotProductVOS)) {
                if (hotProductVOS.get(0).getId().longValue() != hotProductVO.getId().longValue()) {
                    return fail("该商品已存在");
                }
            }

            hotProductVO.setUpdateDate(new Date());
            int ret = hotProductService.update(hotProductVO);
            if (ret <= 0) {
                logger.warn("修改热门商品失败 requestJson{} id{}", requestJsonVO.getEntityJson(), hotProductVO.getId());
                return fail("请稍后重试");
            }

            ResultObjectVO ok = new ResultObjectVO();
            ok.setData(hotProductVO);
            return ok;
        });
    }

    public ResultObjectVO queryPcIndexHotProducts(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = validateRequestAndEntity(requestVo);
        if (resultObjectVO != null) return resultObjectVO;

        // Bug fix: removed dead 'requestVo == null' check (already caught above);
        // only appCode null check remains.
        if (requestVo.getAppCode() == null) {
            logger.warn("没有找到应用编码");
            return fail("没有找到应用编码");
        }

        try {
            HotProductVO hotProductVO = requestVo.formatEntity(HotProductVO.class);
            resultObjectVO.setData(hotProductService.queryPcIndexHotProducts(hotProductVO));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

}
