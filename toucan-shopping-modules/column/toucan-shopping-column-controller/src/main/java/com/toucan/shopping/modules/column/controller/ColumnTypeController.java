package com.toucan.shopping.modules.column.controller;

import com.toucan.shopping.modules.column.business.service.ColumnTypeBusinessService;
import com.toucan.shopping.modules.column.vo.ColumnTypeVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 栏目类型
 */
@RestController
@RequestMapping("/columnType")
public class ColumnTypeController {

    @Autowired
    private ColumnTypeBusinessService columnTypeBusinessService;

    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO) {
        return columnTypeBusinessService.save(requestJsonVO);
    }

    @RequestMapping(value = "/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo) {
        return columnTypeBusinessService.deleteByIds(requestVo);
    }

    @RequestMapping(value = "/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO) {
        return columnTypeBusinessService.deleteById(requestJsonVO);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo) {
        return columnTypeBusinessService.update(requestVo);
    }

    @RequestMapping(value = "/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo) {
        return columnTypeBusinessService.findById(requestVo);
    }

    @RequestMapping(value = "/find/one/code", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultTypeObjectVO<ColumnTypeVO> findOneByCode(@RequestBody RequestJsonVO requestVo) {
        return columnTypeBusinessService.findOneByCode(requestVo);
    }

    @RequestMapping(value = "/query/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO) {
        return columnTypeBusinessService.queryListPage(requestJsonVO);
    }

    @RequestMapping(value = "/query/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultObjectVO queryList(@RequestBody RequestJsonVO requestJsonVO) {
        return columnTypeBusinessService.queryList(requestJsonVO);
    }

}
