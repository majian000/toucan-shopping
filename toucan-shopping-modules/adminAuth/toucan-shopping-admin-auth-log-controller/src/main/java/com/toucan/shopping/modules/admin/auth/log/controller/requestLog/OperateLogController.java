package com.toucan.shopping.modules.admin.auth.log.controller.requestLog;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.log.business.service.OperateLogBusinessService;
import com.toucan.shopping.modules.admin.auth.log.entity.OperateLog;
import com.toucan.shopping.modules.admin.auth.log.service.OperateLogService;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogChartVO;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogPageInfo;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogVO;
import com.toucan.shopping.modules.admin.auth.service.FunctionService;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志管理
 */
@RestController
@RequestMapping("/operateLog")
public class OperateLogController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private OperateLogService operateLogService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private OperateLogBusinessService operateLogBusinessService;


    /**
     * 批量保存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/saves",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO saves(@RequestBody RequestJsonVO requestJsonVO) {
        return operateLogBusinessService.saves(requestJsonVO);
    }



    /**
     * 查询操作日志统计表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/queryOperateChart",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryOperateChart(@RequestBody RequestJsonVO requestJsonVO) {
        return operateLogBusinessService.queryOperateChart(requestJsonVO);
    }


    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        return operateLogBusinessService.findById(requestVo);
    }


    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO listPage(@RequestBody RequestJsonVO requestVo){
        return operateLogBusinessService.listPage(requestVo);
    }

}
