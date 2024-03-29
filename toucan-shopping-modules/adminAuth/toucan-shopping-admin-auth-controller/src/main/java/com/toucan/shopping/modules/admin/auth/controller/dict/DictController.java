package com.toucan.shopping.modules.admin.auth.controller.dict;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.Dict;
import com.toucan.shopping.modules.admin.auth.page.DictPageInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AppService;
import com.toucan.shopping.modules.admin.auth.service.DictService;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.admin.auth.vo.DictTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典
 */
@RestController
@RequestMapping("/dict")
public class DictController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DictService dictService;

    @Autowired
    private AppService appService;

    @Autowired
    private AdminAppService adminAppService;

    @Autowired
    private IdGenerator idGenerator;


    /**
     * 添加字典
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            DictVO dictVO = JSONObject.parseObject(requestVo.getEntityJson(),DictVO.class);
            if(StringUtils.isEmpty(dictVO.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请输入字典名称");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(dictVO.getCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请输入字典编码");
                return resultObjectVO;
            }
            if(dictVO.getCategoryId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请选择字典分类");
                return resultObjectVO;
            }
            dictVO.setAppCodes(new LinkedList<>());
            dictVO.getAppCodes().add(dictVO.getAppCode());
            List<DictVO> dicts = dictService.queryListByCodeAndAppCodes(dictVO.getCode(),dictVO.getAppCodes(),dictVO.getPid());
            if(!CollectionUtils.isEmpty(dicts))
            {
                DictVO dcv = dicts.get(0);
                AppVO appVO = appService.findByCodeIngoreDelete(dcv.getAppCode());
                String nodeName="根节点";;
                if(dictVO.getPid().longValue()!=-1) {
                    DictVO parentNode = dictService.findById(dictVO.getPid());
                    nodeName =  parentNode.getName();
                }
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("在"+appVO.getName()+":"+appVO.getCode()+"中的"+nodeName+"下该编码已存在");
                return resultObjectVO;
            }

            dictVO.setId(idGenerator.id());
            dictVO.setCreateDate(new Date());
            dictVO.setDeleteStatus((short)0);
            dictVO.setDictSort(dictService.queryMaxSort()+1);
            dictVO.setDictVersion(1);
            dictVO.setIsActive((short)1);
            dictVO.setBatchId(UUID.randomUUID().toString().replaceAll("-",""));
            int row = dictService.save(dictVO);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            resultObjectVO.setData(dictVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 編輯字典
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            DictVO entity = JSONObject.parseObject(requestVo.getEntityJson(),DictVO.class);

            if(StringUtils.isEmpty(entity.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入字典名称");
                return resultObjectVO;
            }
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入字典ID");
                return resultObjectVO;
            }


            Dict query=new Dict();
            query.setDeleteStatus((short)0);
            query.setPid(entity.getPid());
            query.setCode(entity.getCode());
            query.setAppCode(entity.getAppCode());
            List<DictVO> dictList = dictService.findListByEntity(query);
            if(!CollectionUtils.isEmpty(dictList))
            {
                if(!dictList.get(0).getId().equals(entity.getId())) {
                    DictVO dcv = dictList.get(0);
                    AppVO appVO = appService.findByCodeIngoreDelete(dcv.getAppCode());
                    String nodeName="根节点";;
                    if(entity.getPid().longValue()!=-1) {
                        DictVO parentNode = dictService.findById(entity.getPid());
                        nodeName =  parentNode.getName();
                    }
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("在"+appVO.getName()+":"+appVO.getCode()+"中的"+nodeName+"下该编码已存在");
                    return resultObjectVO;
                }
            }

            boolean isSnapshot=false; //是否快照字典
            DictVO dict = dictService.findById(entity.getId());
            //快照该字典
            if(entity.getIsSnapshot().intValue()==1) {
                //下面的数据变更,就将字典进行快照
                if (!dict.getCode().equals(entity.getCode())
                        || !dict.getName().equals(entity.getName())
                        || !dict.getExtendProperty().equals(entity.getExtendProperty())
                        || !dict.getPid().equals(entity.getPid())) {

                    isSnapshot = true;
                }
            }

            if(isSnapshot)
            {
                //将所有这个批次的字典活动状态为非活动
                dictService.updateIsActiveByBatchId((short)0,dict.getBatchId());
                //逻辑删除这个批次的字典,让上一条数据形成快照
                dictService.deleteByBatchId(dict.getBatchId());
                entity.setDictVersion(dictService.queryMaxVersion(dict.getBatchId())+1);
                entity.setId(idGenerator.id());
                entity.setIsActive((short)1);
                entity.setBatchId(dict.getBatchId());
                entity.setCreateDate(new Date());
                entity.setCreateAdminId(entity.getUpdateAdminId());
                entity.setDeleteStatus((short)0);
                dictService.save(entity);
                //更新子节点的父节点ID为新的ID
                dictService.updateParentId(dict.getId(),entity.getId());

            }else {
                entity.setUpdateDate(new Date());
                int row = dictService.update(entity);
                if (row < 1) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请重试!");
                    return resultObjectVO;
                }
            }


            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO listPage(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            DictPageInfo pageInfo = JSONObject.parseObject(requestVo.getEntityJson(), DictPageInfo.class);
            resultObjectVO.setData(dictService.queryListPage(pageInfo));

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
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            Dict entity = JSONObject.parseObject(requestVo.getEntityJson(),Dict.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到字典ID");
                return resultObjectVO;
            }

            //查询是否存在该字典
            Dict query=new Dict();
            query.setId(entity.getId());
            List<DictVO> list = dictService.findListByEntity(query);
            if(CollectionUtils.isEmpty(list))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("字典不存在!");
                return resultObjectVO;
            }
            DictVO dictVO = list.get(0);
            DictVO parentDictVO = dictService.findById(dictVO.getPid());
            if(parentDictVO!=null) {
                dictVO.setParentName(parentDictVO.getName());
            }else{
                dictVO.setParentName("根节点");
            }
            resultObjectVO.setData(list);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }






    /**
     * 删除指定字典(仅限中台使用)
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
            DictVO dictVO = JSONObject.parseObject(requestVo.getEntityJson(),DictVO.class);
            if(dictVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到字典ID");
                return resultObjectVO;
            }


            List<DictVO> chidlren = new ArrayList<DictVO>();
            dictService.queryChildren(chidlren,dictVO);
            //把当前的添加进去
            chidlren.add(dictVO);

            List<Long> dictIdList = chidlren.stream().map(DictVO::getId).collect(Collectors.toList());
            dictService.deleteByIdList(dictIdList);


            resultObjectVO.setData(dictVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 批量删除字典(仅限中台使用)
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
            List<DictVO> dictVOS = JSONObject.parseArray(requestVo.getEntityJson(),DictVO.class);
            if(CollectionUtils.isEmpty(dictVOS))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到字典ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(DictVO dictVO:dictVOS) {
                if(dictVO.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(dictVO);


                    List<DictVO> chidlren = new ArrayList<DictVO>();
                    dictService.queryChildren(chidlren,dictVO);
                    //把当前的添加进去
                    chidlren.add(dictVO);

                    List<Long> dictIdList = chidlren.stream().map(DictVO::getId).collect(Collectors.toList());
                    dictService.deleteByIdList(dictIdList);
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
            DictPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), DictPageInfo.class);

            List<DictVO> dictVoList = new ArrayList<DictVO>();
            boolean queryCriteria=false;
            //按指定条件查询
            if(StringUtils.isNotEmpty(queryPageInfo.getName())
                    ||StringUtils.isNotEmpty(queryPageInfo.getCode())
                    ||(queryPageInfo.getEnableStatus()!=null&&queryPageInfo.getEnableStatus().intValue()!=-1)
                    ||StringUtils.isNotEmpty(queryPageInfo.getAppCode()))
            {
                queryCriteria = true;
                DictVO queryDictVO = new DictVO();
                BeanUtils.copyProperties(queryDictVO,queryPageInfo);
                queryDictVO.setPid(null);
                List<DictVO> dictVOS = dictService.queryList(queryDictVO);
                for (int i = 0; i < dictVOS.size(); i++) {
                    dictVoList.add(dictVOS.get(i));
                }
            }else {
                //查询当前节点下的所有子节点
                DictVO queryDict = new DictVO();
                if(queryPageInfo.getPid()!=null) {
                    queryDict.setPid(queryPageInfo.getPid());
                }else{
                    queryDict.setPid(-1L);
                }
                List<Integer> categoryIdList = new LinkedList<>();
                if(!CollectionUtils.isEmpty(queryPageInfo.getCategoryIdList()))
                {
                    for(Integer categoryId:queryPageInfo.getCategoryIdList())
                    {
                        if(categoryId!=null&&categoryId.longValue()!=-1) {
                            categoryIdList.add(categoryId);
                        }
                    }
                }
                if(queryPageInfo.getCategoryId()!=null)
                {
                    categoryIdList.add(queryPageInfo.getCategoryId());
                }
                //设置分类
                queryDict.setName(queryPageInfo.getName());
                queryDict.setCode(queryPageInfo.getCode());
                queryDict.setEnableStatus(queryPageInfo.getEnableStatus());
                queryDict.setCategoryIdList(categoryIdList);
                List<DictVO> dictVOS = dictService.queryList(queryDict);
                for (int i = 0; i < dictVOS.size(); i++) {
                    DictVO dictVO = dictVOS.get(i);

                    queryDict = new DictVO();
                    queryDict.setPid(dictVO.getId());
                    Long childCount = dictService.queryListCount(queryDict);
                    if (childCount > 0) {
                        dictVO.setHaveChild(true);
                    }
                    dictVoList.add(dictVO);
                }
            }


            //先查询出属性路径相关
            if(!CollectionUtils.isEmpty(dictVoList))
            {
                List<Long> parentIdList =new LinkedList<>();
                boolean parentIdExists=false;

                for(DictVO dictVO:dictVoList)
                {
                    //设置上级节点ID
                    parentIdExists=false;
                    for(Long parentId:parentIdList)
                    {
                        if(dictVO.getPid()!=null&&parentId!=null
                                &&parentId.longValue()==dictVO.getPid().longValue())
                        {
                            parentIdExists=true;
                            break;
                        }
                    }
                    if(!parentIdExists&&dictVO.getPid()!=null&&dictVO.getPid().longValue()!=-1)
                    {
                        parentIdList.add(dictVO.getPid());
                    }
                }
                for(DictVO dictVO:dictVoList)
                {
                    if(dictVO.getPid()!=null&&dictVO.getPid().longValue()==-1)
                    {
                        dictVO.setParentName("根节点");
                    }
                }
                if(!CollectionUtils.isEmpty(parentIdList)) {
                    DictVO queryParentDictVO = new DictVO();
                    queryParentDictVO.setIdList(parentIdList);
                    List<DictVO> parentList = dictService.queryList(queryParentDictVO);
                    if(!CollectionUtils.isEmpty(parentList))
                    {
                        for(DictVO dictVO:dictVoList) {
                            if(dictVO.getPid()!=null
                                    &&dictVO.getPid().longValue()!=-1) {
                                for (DictVO parent : parentList) {
                                    if (dictVO.getPid() != null
                                            && dictVO.getPid().longValue() == parent.getId().longValue()) {
                                        dictVO.setParentName(parent.getName());
                                        break;
                                    }
                                }
                            }else{
                                dictVO.setParentName("根节点");
                            }
                        }
                    }
                }
            }


            //如果做了条件查询 就将查询的这些节点设置为顶级节点
            if(queryCriteria) {
                if(!org.apache.commons.collections.CollectionUtils.isEmpty(dictVoList)) {
                    for (DictVO dictVO : dictVoList) {
                        dictVO.setPid(-1L);
                    }
                }
            }

            resultObjectVO.setData(dictVoList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 查询指定节点下子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/child",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeChildByPid(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            DictVO dict = requestJsonVO.formatEntity(DictVO.class);
            List<DictTreeVO> areaVOS = new ArrayList<DictTreeVO>();
            if(dict.getPid()==null)
            {
                DictTreeVO areaVO = new DictTreeVO();
                areaVO.setId(-1L);
                areaVO.setName("根节点");
                areaVO.setParentId(-1L);
                areaVO.setCategoryId(dict.getCategoryId());
                Long childCount = dictService.queryOneChildCountByPid(-1L,dict.getAppCode(),dict.getCategoryId());
                if(childCount>0){
                    areaVO.setIsParent(true);
                }
                areaVOS.add(areaVO);
            }else {
                List<DictVO> dictVOS = dictService.queryList(dict);
                for (int i = 0; i < dictVOS.size(); i++) {
                    DictVO dvo = dictVOS.get(i);
                    DictTreeVO dictTreeVO = new DictTreeVO();
                    BeanUtils.copyProperties(dictTreeVO, dvo);
                    Long childCount = 0L;
                    if(StringUtils.isNotEmpty(dict.getAppCode())) {
                        childCount = dictService.queryOneChildCountByPid(dictTreeVO.getId(), dictTreeVO.getAppCode(),dict.getCategoryId());
                    }else{
                        childCount = dictService.queryOneChildCountByPid(dictTreeVO.getId(), null,dict.getCategoryId());
                    }
                    if(childCount>0)
                    {
                        dictTreeVO.setIsParent(true);
                    }else{
                        dictTreeVO.setIsParent(false);
                    }
                    areaVOS.add(dictTreeVO);
                }
            }

            resultObjectVO.setData(areaVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


}
