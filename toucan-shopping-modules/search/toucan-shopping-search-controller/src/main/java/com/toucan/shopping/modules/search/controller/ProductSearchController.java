package com.toucan.shopping.modules.search.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.search.business.service.ProductSearchBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping("/productSearch")
public class ProductSearchController {

    @Autowired
    private ProductSearchBusinessService productSearchBusinessService;

    /**
     * 搜索商品
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/search", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO search(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSearchBusinessService.search(requestJsonVO);
    }


    /**
     * 搜索商品数量
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/count", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO count(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSearchBusinessService.count(requestJsonVO);
    }


    /**
     * 保存到搜索
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSearchBusinessService.save(requestJsonVO);
    }





    /**
     * 根据SKUID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/queryBySkuId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryBySkuId(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSearchBusinessService.queryBySkuId(requestJsonVO);
    }





    /**
     * 更新到搜索
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSearchBusinessService.update(requestJsonVO);
    }


    /**
     * 从搜索中删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/removeById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO removeById(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSearchBusinessService.removeById(requestJsonVO);
    }



    /**
     * 清空
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/clear", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO clear(@RequestBody RequestJsonVO requestJsonVO)
    {
        return productSearchBusinessService.clear(requestJsonVO);
    }

}
