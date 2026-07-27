package com.toucan.shopping.modules.column.controller;

import com.toucan.shopping.modules.column.business.service.HotProductBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 热门商品
 * @author majian
 */
@RestController
@RequestMapping("/hot/product")
public class HotProductController {

    @Autowired
    private HotProductBusinessService hotProductBusinessService;

    @RequestMapping(value = "/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO) {
        return hotProductBusinessService.queryListPage(requestJsonVO);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO) {
        return hotProductBusinessService.save(requestJsonVO);
    }

    @RequestMapping(value = "/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo) {
        return hotProductBusinessService.findById(requestVo);
    }

    @RequestMapping(value = "/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO) {
        return hotProductBusinessService.deleteById(requestJsonVO);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO) {
        return hotProductBusinessService.update(requestJsonVO);
    }

    @RequestMapping(value = "/pc/index/hot/products", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO queryPcIndexHotProducts(@RequestBody RequestJsonVO requestVo) {
        return hotProductBusinessService.queryPcIndexHotProducts(requestVo);
    }

}
