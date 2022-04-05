package com.toucan.shopping.cloud.product.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.entity.ShopProductApproveSku;
import com.toucan.shopping.modules.product.page.ShopProductApproveSkuPageInfo;
import com.toucan.shopping.modules.product.service.ShopProductApproveSkuService;
import com.toucan.shopping.modules.product.util.ProductRedisKeyUtil;
import com.toucan.shopping.modules.product.vo.ShopProductApproveSkuVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/shopProductApproveSku")
public class ShopProductApproveSkuController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShopProductApproveSkuService shopProductApproveSkuService;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;








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
            ShopProductApproveSkuPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductApproveSkuPageInfo.class);
            PageInfo<ShopProductApproveSkuVO> pageInfo =  shopProductApproveSkuService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
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
            ShopProductApproveSku productSku = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductApproveSku.class);
            resultObjectVO.setData(shopProductApproveSkuService.queryById(productSku.getId()));
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
            List<ShopProductApproveSku> productSkus = JSONArray.parseArray(requestJsonVO.getEntityJson(),ShopProductApproveSku.class);
            if(!CollectionUtils.isEmpty(productSkus)) {
                List<ShopProductApproveSku> productSkuList = new ArrayList<ShopProductApproveSku>();
                for(ShopProductApproveSku productSku:productSkus) {
                    if(StringUtils.isNotEmpty(productSku.getUuid())) {
                        String productSkuKey = ProductRedisKeyUtil.getProductKey(requestJsonVO.getAppCode(),productSku.getUuid());
                        Object productEntityObject = stringRedisTemplate.opsForValue().get(productSkuKey);
                        ShopProductApproveSku productSkuEntity=null;
                        //如果缓存不存在 查询数据库 刷新到缓存
                        if(productEntityObject==null) {
                            productSkuEntity = shopProductApproveSkuService.queryByUuid(productSku.getUuid());
                            //刷新到缓存
                            if(productSkuEntity!=null) {
                                stringRedisTemplate.opsForValue().set(productSkuKey, JSONObject.toJSONString(productSkuEntity));
                            }
                        }else{
                            productSkuEntity=JSONObject.parseObject(String.valueOf(productEntityObject),ShopProductApproveSku.class);
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
