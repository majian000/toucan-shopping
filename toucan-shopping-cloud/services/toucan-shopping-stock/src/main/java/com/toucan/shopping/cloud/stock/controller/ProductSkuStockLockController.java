package com.toucan.shopping.cloud.stock.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.stock.service.ProductSkuStockLockService;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productSkuStockLock")
public class ProductSkuStockLockController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductSkuStockLockService productSkuStockLockService;

    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private SkylarkLock skylarkLock;


    /**
     * 锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/lock/stock",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO lockStock(@RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }

        List<ProductSkuStockLockVO> productSkuStockLocks = requestJsonVO.formatEntityList(ProductSkuStockLockVO.class);
        if(CollectionUtils.isEmpty(productSkuStockLocks))
        {
            logger.info("没有找到请求参数: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到要锁定库存的商品!");
            return resultObjectVO;
        }

        try {
            int ret = 0;
            for (ProductSkuStockLockVO productSkuStockLockVO : productSkuStockLocks) {
                productSkuStockLockVO.setId(idGenerator.id());
                productSkuStockLockVO.setCreateDate(new Date());
                ret = productSkuStockLockService.save(productSkuStockLockVO);
                if(ret<=0)
                {
                    throw new IllegalArgumentException("保存失败");
                }
            }
            resultObjectVO.setData(productSkuStockLocks);
            return resultObjectVO;
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("锁定库存出现异常!");
            logger.warn("数据回滚 {} ",JSONObject.toJSONString(productSkuStockLocks));

            List<Long> idList = productSkuStockLocks.stream().map(ProductSkuStockLockVO::getId).collect(Collectors.toList());

            productSkuStockLockService.deletes(idList);
        }

        return resultObjectVO;
    }




    /**
     * 删除锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/lock/stock",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO deleteLockStock(@RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }

        List<ProductSkuStockLockVO> productSkuStockLocks = requestJsonVO.formatEntityList(ProductSkuStockLockVO.class);
        if(CollectionUtils.isEmpty(productSkuStockLocks))
        {
            logger.info("没有找到请求参数: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到要锁定库存的商品!");
            return resultObjectVO;
        }

        try {
            int ret = productSkuStockLockService.deletes(productSkuStockLocks.stream().map(ProductSkuStockLockVO::getId).collect(Collectors.toList()));
            if(ret<=0||ret!=productSkuStockLocks.size())
            {
                throw new IllegalArgumentException("删除锁定库存出现异常");
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("删除锁定库存出现异常");
        }

        return resultObjectVO;
    }

    /**
     * 根据SKUID查询锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/lock/stock/num",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findLockStockNumByProductSkuIds(@RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }

        try {
            ProductSkuStockLockVO productSkuStockLockVO = requestJsonVO.formatEntity(ProductSkuStockLockVO.class);
            if(CollectionUtils.isEmpty(productSkuStockLockVO.getProductSkuIdList()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("SKU ID不能为空");
                return resultObjectVO;
            }
            resultObjectVO.setData(productSkuStockLockService.queryStockNumByVO(productSkuStockLockVO));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询锁定库存出现异常!");
        }

        return resultObjectVO;
    }


    /**
     * 根据主订单编号查询锁定库存数量
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/find/lock/stock/num/by/mainOrderNos",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findLockStockNumByMainOrderNos(@RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }

        try {
            ProductSkuStockLockVO productSkuStockLockVO = requestJsonVO.formatEntity(ProductSkuStockLockVO.class);
            if(CollectionUtils.isEmpty(productSkuStockLockVO.getMainOrderNoList()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("主订单编号不能为空");
                return resultObjectVO;
            }
            resultObjectVO.setData(productSkuStockLockService.queryStockNumByVO(productSkuStockLockVO));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询锁定库存出现异常!");
        }

        return resultObjectVO;
    }



    /**
     * 删除锁定库存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/lock/stock/by/mainOrderNos",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO deleteLockStockByMainOrderNos(@RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }


        try {
            ProductSkuStockLockVO productSkuStockLockVO = requestJsonVO.formatEntity(ProductSkuStockLockVO.class);
            if(CollectionUtils.isEmpty(productSkuStockLockVO.getMainOrderNoList()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("主订单编号不能为空");
                return resultObjectVO;
            }
            productSkuStockLockVO.setType(null);
            List<ProductSkuStockLockVO> skuStockLockVO = productSkuStockLockService.queryListByVO(productSkuStockLockVO);

            int ret = productSkuStockLockService.deletes(skuStockLockVO.stream().map(ProductSkuStockLockVO::getId).collect(Collectors.toList()));
            if(ret<=0||ret!=skuStockLockVO.size())
            {
                throw new IllegalArgumentException("删除锁定库存出现异常");
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("删除锁定库存出现异常");
        }

        return resultObjectVO;
    }
}
