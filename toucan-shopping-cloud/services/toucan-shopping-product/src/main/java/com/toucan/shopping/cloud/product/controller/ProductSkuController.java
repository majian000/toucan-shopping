package com.toucan.shopping.cloud.product.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultListVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.service.ProductSkuService;
import com.toucan.shopping.modules.product.util.ProductRedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/productSku")
public class ProductSkuController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductSkuService productSkuService;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    /**
     * 查询所有上架商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/shelves/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultListVO queryShelvesList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
    {
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("status",1);
        params.put("deleteStatus",0);
        params.put("appCode",requestJsonVO.getAppCode());

        ResultListVO<ProductSku> resultListVO = new ResultListVO<>();
        resultListVO.setData(productSkuService.queryList(params));
        return resultListVO;
    }







    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO)
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
            ProductSku productSku = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSku.class);
            resultObjectVO.setData(productSkuService.queryById(productSku.getId()));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/ids",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByIdList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
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
            List<ProductSku> productSkus = JSONArray.parseArray(requestJsonVO.getEntityJson(),ProductSku.class);
            if(!CollectionUtils.isEmpty(productSkus)) {
                List<ProductSku> productSkuList = new ArrayList<ProductSku>();
                for(ProductSku productSku:productSkus) {
                    if(StringUtils.isNotEmpty(productSku.getUuid())) {
                        String productSkuKey = ProductRedisKeyUtil.getProductKey(requestJsonVO.getAppCode(),productSku.getUuid());
                        Object productEntityObject = stringRedisTemplate.opsForValue().get(productSkuKey);
                        ProductSku productSkuEntity=null;
                        //如果缓存不存在 查询数据库 刷新到缓存
                        if(productEntityObject==null) {
                            productSkuEntity = productSkuService.queryByUuid(productSku.getUuid());
                            //刷新到缓存
                            if(productSkuEntity!=null) {
                                stringRedisTemplate.opsForValue().set(productSkuKey, JSONObject.toJSONString(productSkuEntity));
                            }
                        }else{
                            productSkuEntity=JSONObject.parseObject(String.valueOf(productEntityObject),ProductSku.class);
                        }
                        if (productSkuEntity != null) {
                            productSkuList.add(productSkuEntity);
                        }
                    }else{
                        logger.warn("exists product sku uuid is null {}",requestJsonVO.getEntityJson());
                    }
                }
                resultObjectVO.setData(productSkuList);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }







}
