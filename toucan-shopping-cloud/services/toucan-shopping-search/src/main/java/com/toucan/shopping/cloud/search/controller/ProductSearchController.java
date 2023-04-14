package com.toucan.shopping.cloud.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.search.service.ProductSearchService;
import com.toucan.shopping.modules.search.vo.ProductSearchVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
            resultObjectVO.setData(productSearchService.search(productSearch));
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
        }
        return resultObjectVO;
    }
}
