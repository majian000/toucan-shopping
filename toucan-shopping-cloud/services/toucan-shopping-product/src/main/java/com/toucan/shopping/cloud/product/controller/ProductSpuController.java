package com.toucan.shopping.cloud.product.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.MD5Util;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultListVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.entity.ProductSpu;
import com.toucan.shopping.modules.product.entity.ProductSpuAttributeKey;
import com.toucan.shopping.modules.product.entity.ProductSpuAttributeValue;
import com.toucan.shopping.modules.product.page.ProductSpuPageInfo;
import com.toucan.shopping.modules.product.redis.ProductSpuRedisLockKey;
import com.toucan.shopping.modules.product.service.ProductSkuService;
import com.toucan.shopping.modules.product.service.ProductSpuAttributeKeyService;
import com.toucan.shopping.modules.product.service.ProductSpuAttributeValueService;
import com.toucan.shopping.modules.product.service.ProductSpuService;
import com.toucan.shopping.modules.product.util.ProductRedisKeyUtil;
import com.toucan.shopping.modules.product.vo.ProductSpuAttributeKeyVO;
import com.toucan.shopping.modules.product.vo.ProductSpuAttributeValueVO;
import com.toucan.shopping.modules.product.vo.ProductSpuVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/productSpu")
public class ProductSpuController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductSpuService productSpuService;

    @Autowired
    private ProductSpuAttributeKeyService productSpuAttributeKeyService;

    @Autowired
    private ProductSpuAttributeValueService productSpuAttributeValueService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;


    /**
     * 保存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.warn("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        String lockKey="-1";
        Long productSpuId = idGenerator.id();
        try {
            logger.info("保存商品SPU {} ",requestJsonVO.getEntityJson());
            ProductSpuVO productSpuVO = requestJsonVO.formatEntity(ProductSpuVO.class);
            if(productSpuVO.getCategoryId()==null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类ID不能为空!");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(productSpuVO.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("SPU名称不能为空!");
                return resultObjectVO;
            }
            lockKey = MD5Util.md5(productSpuVO.getName());
            skylarkLock.lock(ProductSpuRedisLockKey.getSaveProductSpuLockKey(lockKey), lockKey);

            ProductSpuVO queryProductSpu = new ProductSpuVO();
            queryProductSpu.setName(productSpuVO.getName());
            queryProductSpu.setCategoryId(productSpuVO.getCategoryId());
            List<ProductSpuVO> productSpuVOS = productSpuService.queryList(queryProductSpu);
            if(!CollectionUtils.isEmpty(productSpuVOS))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("已存在该SPU!");
                return resultObjectVO;
            }

            ProductSpu productSpu = new ProductSpu();
            BeanUtils.copyProperties(productSpu,productSpuVO);
            productSpu.setId(productSpuId);
            productSpu.setUuid(UUID.randomUUID().toString().replace("-", ""));
            productSpu.setCreateDate(new Date());
            int ret = productSpuService.save(productSpu);

            if(ret<=0)
            {
                logger.warn("保存SPU失败 原因:插入数据库影响行数返回小于等于0 {}",requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("保存SPU失败");
            }else {

                List<ProductSpuAttributeKey> productSpuAttributeKeys = new ArrayList();
                //保存属性名
                if (!CollectionUtils.isEmpty(productSpuVO.getAttributeKeys())) {
                    List<ProductSpuAttributeKeyVO> productSpuAttributeKeyVOList = productSpuVO.getAttributeKeys();
                    for (ProductSpuAttributeKeyVO productSpuAttributeKeyVO : productSpuAttributeKeyVOList) {
                        ProductSpuAttributeKey productSpuAttributeKey = new ProductSpuAttributeKey();
                        BeanUtils.copyProperties(productSpuAttributeKey, productSpuAttributeKeyVO);
                        productSpuAttributeKey.setProductSpuId(productSpu.getId());
                        productSpuAttributeKey.setId(idGenerator.id());
                        productSpuAttributeKey.setCreateDate(new Date());
                        productSpuAttributeKeys.add(productSpuAttributeKey);
                    }

                    ret = productSpuAttributeKeyService.saves(productSpuAttributeKeys);
                    if (ret != productSpuAttributeKeys.size()) {
                        logger.warn("保存SPU失败 原因:保存商品属性名失败 {} 返回数 {} 应成功数", requestJsonVO.getEntityJson(),ret,productSpuAttributeKeys.size());
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("保存SPU失败");

                        //删除属性名
                        int deleteRet = productSpuAttributeKeyService.deleteByProductSpuId(productSpu.getId());
                        logger.warn("删除属性名 返回值 {}  商品ID {} ", deleteRet,productSpu.getId());

                        //删除商品SPU
                        logger.warn("删除商品SPU SPU ID {}", productSpu.getId());
                        deleteRet = productSpuService.deleteById(productSpu.getId());
                        logger.warn("删除商品SPU 返回值 {}  商品ID {} ", deleteRet,productSpu.getId());
                    }
                }

                //保存属性值
                if (!CollectionUtils.isEmpty(productSpuVO.getAttributeValues())) {
                    List<ProductSpuAttributeValueVO> productSpuAttributeValueVOS = productSpuVO.getAttributeValues();
                    List<ProductSpuAttributeValue> productSpuAttributeValues = new ArrayList<ProductSpuAttributeValue>();
                    for(ProductSpuAttributeValueVO productSpuAttributeValueVO:productSpuAttributeValueVOS)
                    {
                        ProductSpuAttributeValue productSpuAttributeValue = new ProductSpuAttributeValue();
                        BeanUtils.copyProperties(productSpuAttributeValueVO,productSpuAttributeValueVO);
                        productSpuAttributeValue.setProductSpuId(productSpu.getId());
                        for(ProductSpuAttributeKey productSpuAttributeKey:productSpuAttributeKeys)
                        {
                            if(productSpuAttributeKey.getAttributeKeyId().longValue()==productSpuAttributeValueVO.getAttributeKeyId().longValue())
                            {
                                productSpuAttributeValue.setProductSpuAttributeKeyId(productSpuAttributeKey.getId());
                                break;
                            }
                        }
                        productSpuAttributeValue.setId(idGenerator.id());
                        productSpuAttributeValue.setCreateDate(new Date());
                        productSpuAttributeValues.add(productSpuAttributeValue);
                    }
                    ret = productSpuAttributeValueService.saves(productSpuAttributeValues);
                    if (ret != productSpuAttributeValues.size()) {
                        logger.warn("保存SPU失败 原因:保存商品属性值失败 {} 返回数 {} 应成功数", requestJsonVO.getEntityJson(),ret,productSpuAttributeValues.size());
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("保存SPU失败");

                        //删除属性名
                        int deleteRet = productSpuAttributeKeyService.deleteByProductSpuId(productSpu.getId());
                        logger.warn("删除属性名 返回值 {}  商品ID {} ", deleteRet,productSpu.getId());

                        //删除属性值
                        deleteRet = productSpuAttributeKeyService.deleteByProductSpuId(productSpu.getId());
                        logger.warn("删除属性值 返回值 {}  商品ID {} ", deleteRet,productSpu.getId());

                        //删除商品SPU
                        deleteRet = productSpuService.deleteById(productSpu.getId());
                        logger.warn("删除商品SPU 返回值 {}  商品ID {} ", deleteRet,productSpu.getId());

                    }
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("保存SPU失败");

            //删除属性名
            int deleteRet = productSpuAttributeKeyService.deleteByProductSpuId(productSpuId);
            logger.warn("删除属性名 返回值 {}  商品ID {} ", deleteRet,productSpuId);

            //删除属性值
            deleteRet = productSpuAttributeKeyService.deleteByProductSpuId(productSpuId);
            logger.warn("删除属性值 返回值 {}  商品ID {} ", deleteRet,productSpuId);

            //删除商品SPU
            deleteRet = productSpuService.deleteById(productSpuId);
            logger.warn("删除商品SPU 返回值 {}  商品ID {} ", deleteRet,productSpuId);

        }finally{
            skylarkLock.unLock(ProductSpuRedisLockKey.getSaveProductSpuLockKey(lockKey), lockKey);
        }
        return resultObjectVO;
    }




    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ProductSpu entity = requestVo.formatEntity(ProductSpu.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            int row = productSpuService.deleteById(entity.getId());
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }






    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<ProductSpuVO> productSpuVOS = JSONObject.parseArray(requestVo.getEntityJson(),ProductSpuVO.class);
            if(CollectionUtils.isEmpty(productSpuVOS))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(ProductSpuVO productSpuVO:productSpuVOS) {
                if(productSpuVO.getId()!=null) {
                    int row = productSpuService.deleteById(productSpuVO.getId());
                    if (row < 1) {
                        logger.warn("删除商品SPU失败 {} ",JSONObject.toJSONString(productSpuVO));
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请重试!");

                        ResultObjectVO resultObjectRowVO = new ResultObjectVO();
                        resultObjectRowVO.setCode(ResultVO.FAILD);
                        resultObjectRowVO.setMsg("请重试!");
                        resultObjectRowVO.setData(productSpuVO.getId());
                        resultObjectVOList.add(resultObjectRowVO);

                        continue;
                    }
                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.warn("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }
        try {
            ProductSpuPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSpuPageInfo.class);
            PageInfo<ProductSpuVO> pageInfo =  productSpuService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }



}
