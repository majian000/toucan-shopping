package com.toucan.shopping.admin.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAdminProductSkuService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignProductSkuService;
import com.toucan.shopping.product.export.entity.ProductSku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productSku")
public class ProductSkuController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignProductSkuService feignProductSkuService;

    @Autowired
    private FeignAdminProductSkuService feignAdminProductSkuService;

    @Autowired
    private Toucan toucan;

    /**
     * 保存SKU
     * @param productSku
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO saveSku(@RequestBody ProductSku productSku)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(productSku==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(productSku.getAppCode()==null)
        {
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(productSku));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }
        try {
            productSku.setCreateUserId(1L);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generatorByAdmin(toucan.getAppCode(), "", productSku);
            resultObjectVO = feignAdminProductSkuService.saveSku(SignUtil.sign(requestJsonVO.getAppCode(),requestJsonVO.getEntityJson()),requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("保存商品SKU失败!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }
}
