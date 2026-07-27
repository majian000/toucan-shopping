package com.toucan.shopping.modules.column.controller;

import com.toucan.shopping.modules.column.business.service.IndexRecommendColumnBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 首页推荐栏目操作
 */
@RestController
@RequestMapping("/column/index/recommend")
public class IndexRecommendColumnController {

    @Autowired
    private IndexRecommendColumnBusinessService indexRecommendColumnBusinessService;

    @RequestMapping(value = "/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO) {
        return indexRecommendColumnBusinessService.queryListPage(requestJsonVO);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO) {
        return indexRecommendColumnBusinessService.save(requestJsonVO);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO) {
        return indexRecommendColumnBusinessService.update(requestJsonVO);
    }

    @RequestMapping(value = "/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO) {
        return indexRecommendColumnBusinessService.deleteById(requestJsonVO);
    }

    @RequestMapping(value = "/pc/index/columns", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO queryPcIndexColumns(@RequestBody RequestJsonVO requestVo) {
        return indexRecommendColumnBusinessService.queryPcIndexColumns(requestVo);
    }

    @RequestMapping(value = "/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo) {
        return indexRecommendColumnBusinessService.findById(requestVo);
    }

}
