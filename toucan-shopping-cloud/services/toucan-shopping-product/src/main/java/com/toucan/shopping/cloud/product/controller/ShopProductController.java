package com.toucan.shopping.cloud.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.entity.ProductSku;
import com.toucan.shopping.modules.product.entity.ShopProduct;
import com.toucan.shopping.modules.product.redis.PublishProductRedisLockKey;
import com.toucan.shopping.modules.product.service.ProductSkuService;
import com.toucan.shopping.modules.product.service.ShopProductService;
import com.toucan.shopping.modules.product.vo.PublishProductVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

/**
 * 店铺商品
 * @auth majian
 */
@RestController
@RequestMapping("/shopProduct")
public class ShopProductController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private ShopProductService shopProductService;



    /**
     * 发布商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/publish",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO publish(@RequestBody RequestJsonVO requestJsonVO)
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
        String shopId ="";
        try {
            logger.info("发布商品 {} ",requestJsonVO.getEntityJson());
            PublishProductVO publishProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), PublishProductVO.class);
            if(publishProductVO.getShopId()==null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }
            shopId = String.valueOf(publishProductVO.getShopId());
            skylarkLock.lock(PublishProductRedisLockKey.getPublishProductLockKey(shopId), shopId);

            //保存店铺商品
            publishProductVO.setId(idGenerator.id());
            publishProductVO.setUuid(UUID.randomUUID().toString().replace("-",""));
            publishProductVO.setCreateDate(new Date());
            publishProductVO.setApproveStatus((short)1); //审核中
            publishProductVO.setStatus((short)0);
            shopProductService.save(publishProductVO);



        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("发布失败!");
        }finally{
            skylarkLock.unLock(PublishProductRedisLockKey.getPublishProductLockKey(shopId), shopId);
        }
        return resultObjectVO;
    }








}