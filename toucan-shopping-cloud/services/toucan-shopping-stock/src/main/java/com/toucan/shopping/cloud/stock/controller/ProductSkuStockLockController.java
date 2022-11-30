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
            resultObjectVO.setData(productSkuStockLockService.queryStockNumByProductSkuIdList(productSkuStockLockVO));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询锁定库存出现异常!");
        }

        return resultObjectVO;
    }

//
//    /**
//     * 还原库存
//     * @param requestJsonVO
//     * @return
//     */
//    @RequestMapping(value="/restore/stock",produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public ResultObjectVO restoreStock(@RequestBody RequestJsonVO requestJsonVO)
//    {
//        ResultObjectVO resultObjectVO = new ResultObjectVO();
//
//        if(requestJsonVO==null)
//        {
//            logger.info("请求参数为空");
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("请重试!");
//            return resultObjectVO;
//        }
//
//        if(requestJsonVO.getAppCode()==null)
//        {
//            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("没有找到应用!");
//            return resultObjectVO;
//        }
//        RestoreStockVO restoreStockVo = JSONObject.parseObject(requestJsonVO.getEntityJson(),RestoreStockVO.class);
//        if(restoreStockVo==null)
//        {
//            logger.info("没有找到请求参数: param:"+ JSONObject.toJSONString(requestJsonVO));
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("没有找到请求参数!");
//            return resultObjectVO;
//        }
//        if(CollectionUtils.isEmpty(restoreStockVo.getProductSkuList()))
//        {
//            logger.info("没有找到商品: param:"+ JSONObject.toJSONString(restoreStockVo));
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("没有找到商品!");
//            return resultObjectVO;
//        }
//
//        try {
//            for (ProductSku productSku : restoreStockVo.getProductSkuList()) {
//                productSkuStockLockService.restoreStock(productSku.getUuid());
//            }
//        }catch(Exception e)
//        {
//            logger.warn(e.getMessage(),e);
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("恢复库存出现异常!");
//        }
//
//        return resultObjectVO;
//    }
//
//
//
//
//
//    /**
//     * 恢复预扣库存
//     * @param requestJsonVO
//     * @return
//     */
//    @RequestMapping(value="/restore/cache/stock",produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public ResultObjectVO restoreCacheStock(@RequestBody RequestJsonVO requestJsonVO)
//    {
//        ResultObjectVO resultObjectVO = new ResultObjectVO();
//
//        if(requestJsonVO==null)
//        {
//            logger.info("请求参数为空");
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("请重试!");
//            return resultObjectVO;
//        }
//
//        if(requestJsonVO.getAppCode()==null)
//        {
//            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("没有找到应用!");
//            return resultObjectVO;
//        }
//
//        try{
//            List<ProductSku> productSkuList = JSONArray.parseArray(JSONObject.toJSONString(requestJsonVO.getEntityJson()), ProductSku.class);
//            if(!CollectionUtils.isEmpty(productSkuList))
//            {
//                for(ProductSku productSku:productSkuList)
//                {
//                    String productStockKey = StockRedisKeyUtil.getStockKey(productSku.getAppCode(),productSku.getUuid());
//                    Object productStockObject = stringRedisTemplate.opsForValue().get(productStockKey);
//                    if(productStockObject!=null)
//                    {
//                        Integer productStock = Integer.parseInt(String.valueOf(productStockObject));
//                        ProductSkuStockLock productSkuStock = productSkuStockLockService.queryBySkuUuid(productSku.getUuid());
//                        //实际库存大于缓存库存数量才进行恢复,避免redis挂掉重新刷库存时候增加多了预扣库存问题(预扣库存比实际库存多了)
//                        if(productSkuStock!=null&&productSkuStock.getStockNum()>productStock) {
//                            productStock++;
//                            stringRedisTemplate.opsForValue().set(productStockKey, String.valueOf(productStock));
//                        }
//                    }
//                }
//            }
//        }catch(Exception e)
//        {
//            logger.warn(e.getMessage(),e);
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("恢复预扣库存失败!");
//        }
//
//        return resultObjectVO;
//    }
//
//
//    /**
//     * 删减库存
//     * @param requestJsonVO
//     * @return
//     */
//    @RequestMapping(method= RequestMethod.POST,value="/inventoryReduction",produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public ResultObjectVO inventoryReduction(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
//    {
//        ResultObjectVO resultObjectVO = new ResultObjectVO();
//        if(requestJsonVO==null|| StringUtils.isEmpty(requestJsonVO.getEntityJson()))
//        {
//            logger.info("请求参数为空");
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("请重试!");
//            return resultObjectVO;
//        }
//        InventoryReductionVo inventoryReductionVo = JSONObject.parseObject(requestJsonVO.getEntityJson(),InventoryReductionVo.class);
//
//        logger.info("删减库存: param:"+ JSONObject.toJSONString(inventoryReductionVo));
//        if(CollectionUtils.isEmpty(inventoryReductionVo.getProductSkuList()))
//        {
//            logger.info("没有找到商品: param:"+ JSONObject.toJSONString(inventoryReductionVo));
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("没有找到商品!");
//            return resultObjectVO;
//        }
//        if(inventoryReductionVo.getAppCode()==null)
//        {
//            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(inventoryReductionVo));
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("没有找到应用!");
//            return resultObjectVO;
//        }
//        if(inventoryReductionVo.getUserId()==null)
//        {
//            logger.info("没有找到用户: param:"+ JSONObject.toJSONString(inventoryReductionVo));
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("没有找到用户!");
//            return resultObjectVO;
//        }
//
//        try {
//
//            for(ProductSku productSku:inventoryReductionVo.getProductSkuList()) {
//                if(productSku.getId()==null)
//                {
//                    logger.warn("skuId为空 param:" + JSONObject.toJSONString(inventoryReductionVo));
//                    throw new IllegalArgumentException(" skuId为空"+JSONObject.toJSONString(inventoryReductionVo));
//                }
//                //扣库存
//                int row = productSkuStockLockService.inventoryReduction(productSku.getUuid());
//                if (row <= 0) {
//                    logger.info("没有库存了 param:" + JSONObject.toJSONString(inventoryReductionVo));
//                    resultObjectVO.setCode(ResultVO.FAILD);
//                    resultObjectVO.setMsg("没有库存了!");
//                } else {
//                    logger.info("减库存: param:" + JSONObject.toJSONString(inventoryReductionVo));
//                }
//            }
//        }catch(Exception e)
//        {
//            logger.warn(e.getMessage(),e);
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("扣库存失败!");
//        }
//        return resultObjectVO;
//    }
//
//
//
//
//
//
//
//
//    /**
//     * 预扣库存
//     * @param requestJsonVO
//     * @return
//     */
//    @RequestMapping(method= RequestMethod.POST,value="/deduct/cache/stock",produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public ResultObjectVO deductCacheStock(@RequestBody RequestJsonVO requestJsonVO)
//    {
//        ResultObjectVO resultObjectVO = new ResultObjectVO();
//        if(requestJsonVO==null|| StringUtils.isEmpty(requestJsonVO.getEntityJson()))
//        {
//            logger.info("请求参数为空");
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("请重试!");
//            return resultObjectVO;
//        }
//
//        if(requestJsonVO.getAppCode()==null)
//        {
//            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("没有找到应用!");
//            return resultObjectVO;
//        }
//
//        try {
//
//            List<ProductSku> productSkus = JSONArray.parseArray(requestJsonVO.getEntityJson(),ProductSku.class);
//            //开始预扣库存,直接减少redis库存数量
//            for(ProductSku productSku:productSkus) {
//                String productStockKey = StockRedisKeyUtil.getStockKey(productSku.getAppCode(),productSku.getUuid());
//                Object productStockObject = stringRedisTemplate.opsForValue().get(productStockKey);
//                Integer productStock = 0;
//                //如果商品不在缓存中 拿到实际库存刷新到缓存
//                if(productStockObject==null)
//                {
//                    stringRedisTemplate.opsForValue().set(productStockKey,String.valueOf(productSku.getStockNum()));
//                    productStock = productSku.getStockNum();
//                }else {
//                    productStock = Integer.parseInt(String.valueOf(productStockObject));
//                }
//                productStock--;
//                //预扣库存
//                stringRedisTemplate.opsForValue().set(productStockKey,String.valueOf(productStock));
//            }
//
//        }catch(Exception e)
//        {
//            logger.warn(e.getMessage(),e);
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("扣库存失败!");
//        }
//        return resultObjectVO;
//    }
//
//
//
//
//
//
//    /**
//     * 根据SKU UUID查询
//     * @param requestJsonVO
//     * @return
//     */
//    @RequestMapping(value="/query/sku/uuids",produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public ResultObjectVO queryBySkuUuidList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
//    {
//        ResultObjectVO resultObjectVO = new ResultObjectVO();
//        if(requestJsonVO==null)
//        {
//            logger.info("请求参数为空");
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("请重试!");
//            return resultObjectVO;
//        }
//        if(requestJsonVO.getAppCode()==null)
//        {
//            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("没有找到应用!");
//            return resultObjectVO;
//        }
//        try {
//            List<ProductSku> productSkus = JSONArray.parseArray(requestJsonVO.getEntityJson(),ProductSku.class);
//            if(!CollectionUtils.isEmpty(productSkus)) {
//                List<ProductSkuStockLock> productSkuStockList = new ArrayList<ProductSkuStockLock>();
//                for(ProductSku productSku:productSkus) {
//                    ProductSkuStockLock productSkuStockEntity = productSkuStockLockService.queryBySkuUuid(productSku.getUuid());
//                    if(productSkuStockEntity!=null) {
//                        productSkuStockList.add(productSkuStockEntity);
//                    }
//                }
//                resultObjectVO.setData(productSkuStockList);
//            }
//        }catch(Exception e)
//        {
//            logger.warn(e.getMessage(),e);
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("查询失败!");
//        }
//
//        return resultObjectVO;
//    }
//
//
//
//    /**
//     * 查询库存缓存(返回预扣库存)
//     * @param requestJsonVO
//     * @return
//     */
//    @RequestMapping(value="/query/stock/cache/sku/uuids",produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public ResultObjectVO queryStockCacheBySkuUuidList(@RequestBody RequestJsonVO requestJsonVO)
//    {
//        ResultObjectVO resultObjectVO = new ResultObjectVO();
//        if(requestJsonVO==null)
//        {
//            logger.info("请求参数为空");
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("请重试!");
//            return resultObjectVO;
//        }
//        if(requestJsonVO.getAppCode()==null)
//        {
//            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("没有找到应用!");
//            return resultObjectVO;
//        }
//        try {
//            List<ProductSku> productSkus = JSONArray.parseArray(requestJsonVO.getEntityJson(),ProductSku.class);
//
//            if(!CollectionUtils.isEmpty(productSkus)) {
//                List<ProductSkuStockLock> productSkuStockList = new ArrayList<ProductSkuStockLock>();
//                for(ProductSku productSku:productSkus) {
//                    String productStockKey = StockRedisKeyUtil.getStockKey(productSku.getAppCode(),productSku.getUuid());
//                    Object productStockObject = stringRedisTemplate.opsForValue().get(productStockKey);
//                    //初始化预扣库存数量
//                    if(productStockObject==null)
//                    {
//                        ProductSkuStockLock productSkuStock = productSkuStockLockService.queryBySkuUuid(productSku.getUuid());
//                        if(productSkuStock!=null) {
//                            stringRedisTemplate.opsForValue().set(productStockKey, String.valueOf(productSkuStock.getStockNum().intValue()));
//                            productStockObject =productSkuStock.getStockNum();
//                        }else{
//                            productStockObject=0;
//                        }
//                    }
//                    ProductSkuStockLock productSkuStock  = new ProductSkuStockLock();
//                    productSkuStock.setSkuUuid(productSku.getUuid());
//                    productSkuStock.setStockNum(Integer.parseInt(String.valueOf(productStockObject)));
//                    productSkuStockList.add(productSkuStock);
//                }
//                resultObjectVO.setData(productSkuStockList);
//            }
//        }catch(Exception e)
//        {
//            logger.warn(e.getMessage(),e);
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("查询失败!");
//        }
//
//        return resultObjectVO;
//    }
//




}