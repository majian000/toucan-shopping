package com.toucan.shopping.cloud.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.search.service.ProductSearchService;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.search.vo.ProductSearchVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productSearch")
public class ProductSearchController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductSearchService productSearchService;

    /**
     * 搜索商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/search",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO search(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ProductSearchVO productSearch = requestJsonVO.formatEntity(ProductSearchVO.class);
        try {
            if(productSearch.getPage()<1)
            {
                productSearch.setPage(1);
            }

            resultObjectVO.setData(productSearchService.search(productSearch));
        }catch(Exception e)
        {
            logger.error(e.getMessage());
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }




    /**
     * 保存到搜索
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ProductSearchResultVO productSearchResultVO = requestJsonVO.formatEntity(ProductSearchResultVO.class);
        try {
            productSearchService.save(productSearchResultVO);
        }catch(Exception e)
        {
            logger.error(e.getMessage());
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }





    /**
     * 根据SKUID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/queryBySkuId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryBySkuId(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        Long skuId = requestJsonVO.formatEntity(Long.class);
        try {
            resultObjectVO.setData(productSearchService.queryBySkuId(skuId));
        }catch(Exception e)
        {
            logger.error(e.getMessage());
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }





    /**
     * 更新到搜索
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        ProductSearchResultVO productSearchResultVO = requestJsonVO.formatEntity(ProductSearchResultVO.class);
        try {
            productSearchService.update(productSearchResultVO);
        }catch(Exception e)
        {
            logger.error(e.getMessage());
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }


    /**
     * 从搜索中删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/removeById",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO removeById(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        Long skuId = requestJsonVO.formatEntity(Long.class);
        try {
            List<Long> deleteFaildList = new ArrayList<>();
            productSearchService.removeById(skuId,deleteFaildList);
            resultObjectVO.setData(deleteFaildList);
        }catch(Exception e)
        {
            logger.error(e.getMessage());
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }



    /**
     * 清空
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/clear",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO clear(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        Long skuId = requestJsonVO.formatEntity(Long.class);
        try {
            List<Long> deleteFaildList = new ArrayList<>();
            productSearchService.deleteIndex();
            productSearchService.createIndex();
            resultObjectVO.setData(deleteFaildList);
        }catch(Exception e)
        {
            logger.error(e.getMessage());
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }


}
