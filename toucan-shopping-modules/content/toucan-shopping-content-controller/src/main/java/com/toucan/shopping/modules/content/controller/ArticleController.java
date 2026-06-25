package com.toucan.shopping.modules.content.controller;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.content.business.service.ArticleBusinessService;
import com.toucan.shopping.modules.content.vo.ArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 文章操作
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleBusinessService articleBusinessService;

    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        return articleBusinessService.queryListPage(requestJsonVO);
    }

    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        return articleBusinessService.save(requestJsonVO);
    }

    @RequestMapping(value="/findById",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultTypeObjectVO<ArticleVO> findById(@RequestBody RequestJsonVO requestJsonVO){
        return articleBusinessService.findById(requestJsonVO);
    }

    @RequestMapping(value="/queryMaxSort",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultTypeObjectVO<Long> queryMaxSort(@RequestBody RequestJsonVO requestJsonVO) {
        return articleBusinessService.queryMaxSort(requestJsonVO);
    }

    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO){
        return articleBusinessService.update(requestJsonVO);
    }

    @RequestMapping(value="/deleteById",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO){
        return articleBusinessService.deleteById(requestJsonVO);
    }

    /**
     * 批量删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestJsonVO){
        return articleBusinessService.deleteByIds(requestJsonVO);
    }

}
