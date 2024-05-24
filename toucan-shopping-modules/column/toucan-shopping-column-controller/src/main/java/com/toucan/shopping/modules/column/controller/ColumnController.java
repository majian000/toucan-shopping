package com.toucan.shopping.modules.column.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.column.page.ColumnPageInfo;
import com.toucan.shopping.modules.column.redis.ColumnLockKey;
import com.toucan.shopping.modules.column.service.ColumnService;
import com.toucan.shopping.modules.column.vo.ColumnTreeVO;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 栏目控制器
 * @author majian
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




    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO){
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
        if(columnVO.getId()==null){
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("栏目ID不能为空");
            return resultObjectVO;
        }
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
        if(columnVO.getPid().equals(columnVO.getId())){
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("上级节点不能为自己");
            return resultObjectVO;
        }
        String lockKey = columnVO.getAppCode()+"_"+columnVO.getColumnTypeCode();
        try {
            boolean lockStatus = skylarkLock.lock(ColumnLockKey.getUpdateLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            ColumnVO query = new ColumnVO();
            query.setCode(columnVO.getCode());
            query.setColumnTypeCode(columnVO.getColumnTypeCode());
            query.setAppCode(columnVO.getAppCode());
            List<ColumnVO> columnVOS = columnService.queryList(query);
            if(!CollectionUtils.isEmpty(columnVOS))
            {
                for(ColumnVO cvo:columnVOS) {
                    if(!cvo.getId().equals(columnVO.getId())) {
                        resultObjectVO.setCode(ResultObjectVO.FAILD);
                        resultObjectVO.setMsg("该编码已存在");
                        return resultObjectVO;
                    }
                }
            }

            int ret = columnService.update(columnVO);
            if(ret<=0)
            {
                logger.warn("修改栏目失败 requestJson{} id{}",requestJsonVO.getEntityJson(),columnVO.getId());
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
            skylarkLock.unLock(ColumnLockKey.getUpdateLockKey(lockKey), lockKey);
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
            query.setCode(columnVO.getCode());
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




    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            
            ColumnPageInfo queryPageInfo = requestJsonVO.formatEntity(ColumnPageInfo.class);

            List<ColumnVO> columnVoList = new ArrayList<ColumnVO>();
            boolean queryCriteria=false;
            //按指定条件查询
            if(StringUtils.isNotEmpty(queryPageInfo.getTitle())
                    ||StringUtils.isNotEmpty(queryPageInfo.getAppCode()))
            {
                queryCriteria = true;
                ColumnVO queryColumnVO = new ColumnVO();
                BeanUtils.copyProperties(queryColumnVO,queryPageInfo);
                queryColumnVO.setPid(null);
                List<ColumnVO> columnVOS = columnService.queryList(queryColumnVO);
                for (int i = 0; i < columnVOS.size(); i++) {
                    columnVoList.add(columnVOS.get(i));
                }
            }else {
                //查询当前节点下的所有子节点
                ColumnVO queryColumn = new ColumnVO();
                if(queryPageInfo.getPid()!=null) {
                    queryColumn.setPid(queryPageInfo.getPid());
                }else{
                    queryColumn.setPid(-1L);
                }
                //设置分类
                queryColumn.setTitle(queryPageInfo.getTitle());
                queryColumn.setColumnTypeCode(queryPageInfo.getColumnTypeCode());
                List<ColumnVO> columnVOS = columnService.queryList(queryColumn);
                for (int i = 0; i < columnVOS.size(); i++) {
                    ColumnVO columnVO = columnVOS.get(i);

                    queryColumn = new ColumnVO();
                    queryColumn.setPid(columnVO.getId());
                    Long childCount = columnService.queryListCount(queryColumn);
                    if (childCount > 0) {
                        columnVO.setHaveChild(true);
                    }
                    columnVoList.add(columnVO);
                }
            }


            //先查询出属性路径相关
            if(!CollectionUtils.isEmpty(columnVoList))
            {
                List<Long> parentIdList =new LinkedList<>();
                boolean parentIdExists=false;

                for(ColumnVO columnVO:columnVoList)
                {
                    //设置上级节点ID
                    parentIdExists=false;
                    for(Long parentId:parentIdList)
                    {
                        if(columnVO.getPid()!=null&&parentId!=null
                                &&parentId.longValue()==columnVO.getPid().longValue())
                        {
                            parentIdExists=true;
                            break;
                        }
                    }
                    if(!parentIdExists&&columnVO.getPid()!=null&&columnVO.getPid().longValue()!=-1)
                    {
                        parentIdList.add(columnVO.getPid());
                    }
                }
                for(ColumnVO columnVO:columnVoList)
                {
                    if(columnVO.getPid()!=null&&columnVO.getPid().longValue()==-1)
                    {
                        columnVO.setParentTitle("根节点");
                    }
                }
                if(!CollectionUtils.isEmpty(parentIdList)) {
                    ColumnVO queryParentColumnVO = new ColumnVO();
                    queryParentColumnVO.setIdList(parentIdList);
                    List<ColumnVO> parentList = columnService.queryList(queryParentColumnVO);
                    if(!CollectionUtils.isEmpty(parentList))
                    {
                        for(ColumnVO columnVO:columnVoList) {
                            if(columnVO.getPid()!=null
                                    &&columnVO.getPid().longValue()!=-1) {
                                for (ColumnVO parent : parentList) {
                                    if (columnVO.getPid() != null
                                            && columnVO.getPid().longValue() == parent.getId().longValue()) {
                                        columnVO.setParentTitle(parent.getTitle());
                                        break;
                                    }
                                }
                            }else{
                                columnVO.setParentTitle("根节点");
                            }
                        }
                    }
                }
            }


            //如果做了条件查询 就将查询的这些节点设置为顶级节点
            if(queryCriteria) {
                if(!CollectionUtils.isEmpty(columnVoList)) {
                    for (ColumnVO dictVO : columnVoList) {
                        dictVO.setPid(-1L);
                    }
                }
            }

            resultObjectVO.setData(columnVoList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询栏目树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/column/tree/pid",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryColumnTreeByPid(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ColumnTreeVO query = requestJsonVO.formatEntity(ColumnTreeVO.class);
            List<ColumnVO> columnVOS = columnService.queryOneLevelChildrenByIdAndAppCode(query.getParentId(),query.getAppCode(),query.getColumnTypeCode());
            List<ColumnTreeVO> columnTreeVOS = new LinkedList<>();
            for(ColumnVO columnVO:columnVOS)
            {
                ColumnTreeVO columnTreeVO = new ColumnTreeVO();
                BeanUtils.copyProperties(columnTreeVO,columnVO);
                Long childrenCount = columnService.queryOneLevelChildrenCountByIdAndAppCode(columnVO.getId(),columnVO.getAppCode(),query.getColumnTypeCode());
                if(childrenCount!=null&&childrenCount.longValue()>0)
                {
                    columnTreeVO.setIsParent(true);
                }else{
                    columnTreeVO.setIsParent(false);
                }
                columnTreeVOS.add(columnTreeVO);
            }
            resultObjectVO.setData(columnTreeVOS);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultTypeObjectVO<ColumnVO> findById(@RequestBody RequestJsonVO requestVo){
        ResultTypeObjectVO resultObjectVO = new ResultTypeObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ColumnVO columnVO = requestVo.formatEntity(ColumnVO.class);
            if(columnVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            columnVO = columnService.findById(columnVO.getId());
            if(columnVO!=null){
                ColumnVO parentColumn = columnService.findById(columnVO.getPid());
                if(parentColumn!=null){
                    columnVO.setParentTitle(parentColumn.getTitle());
                }else{
                    columnVO.setParentTitle("根节点");
                }
            }
            resultObjectVO.setData(columnVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 删除指定栏目
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ColumnVO columnVO = JSONObject.parseObject(requestVo.getEntityJson(),ColumnVO.class);
            if(columnVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到栏目ID");
                return resultObjectVO;
            }


            List<ColumnVO> chidlren = new ArrayList<ColumnVO>();
            columnService.queryChildren(chidlren,columnVO);
            //把当前的添加进去
            chidlren.add(columnVO);

            List<Long> dictIdList = chidlren.stream().map(ColumnVO::getId).collect(Collectors.toList());
            columnService.deleteByIdList(dictIdList);


            resultObjectVO.setData(columnVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 批量删除栏目
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<ColumnVO> columnVOS = JSONObject.parseArray(requestVo.getEntityJson(),ColumnVO.class);
            if(CollectionUtils.isEmpty(columnVOS))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到栏目ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(ColumnVO columnVO:columnVOS) {
                if(columnVO.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(columnVO);


                    List<ColumnVO> chidlren = new ArrayList<ColumnVO>();
                    columnService.queryChildren(chidlren,columnVO);
                    //把当前的添加进去
                    chidlren.add(columnVO);

                    List<Long> dictIdList = chidlren.stream().map(ColumnVO::getId).collect(Collectors.toList());
                    columnService.deleteByIdList(dictIdList);
                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



}
