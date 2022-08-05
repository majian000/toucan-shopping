package com.toucan.shopping.modules.column.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.column.page.ColumnPageInfo;
import com.toucan.shopping.modules.column.redis.ColumnLockKey;
import com.toucan.shopping.modules.column.service.ColumnService;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.lang3.StringUtils;
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

/**
 * 用户端栏目操作
 */
@RestController
@RequestMapping("/column")
public class ColumnController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ColumnService columnService;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private IdGenerator idGenerator;



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
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到对象: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到对象!");
            return resultObjectVO;
        }
        try {
            ColumnPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnPageInfo.class);
            PageInfo<ColumnVO> pageInfo =  columnService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }



    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }
        ColumnVO columnVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnVO.class);
        if(StringUtils.isEmpty(columnVO.getTitle()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("栏目标题不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(columnVO.getColumnTypeCode()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("栏目类型编码不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(columnVO.getAppCode()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("所属应用不能为空");
            return resultObjectVO;
        }
        String lockKey = columnVO.getAppCode()+"_"+columnVO.getColumnTypeCode();
        try {
            boolean lockStatus = skylarkLock.lock(ColumnLockKey.getSaveLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            ColumnVO query = new ColumnVO();
            query.setTitle(columnVO.getTitle());
            query.setColumnTypeCode(columnVO.getColumnTypeCode());
            query.setAppCode(columnVO.getAppCode());
            List<ColumnVO> columnVOS = columnService.queryList(query);
            if(!CollectionUtils.isEmpty(columnVOS))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("该编码已存在");
                return resultObjectVO;
            }

            columnVO.setId(idGenerator.id());
            columnVO.setDeleteStatus((short)0);
            columnVO.setCreateDate(new Date());
            int ret = columnService.save(columnVO);
            if(ret<=0)
            {
                logger.warn("保存栏目失败 requestJson{} id{}",requestJsonVO.getEntityJson(),columnVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(columnVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(ColumnLockKey.getSaveLockKey(lockKey), lockKey);
        }
        return resultObjectVO;
    }




}
