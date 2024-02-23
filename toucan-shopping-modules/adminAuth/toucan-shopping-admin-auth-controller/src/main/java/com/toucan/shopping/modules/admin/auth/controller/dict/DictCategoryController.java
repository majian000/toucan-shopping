package com.toucan.shopping.modules.admin.auth.controller.dict;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.Dict;
import com.toucan.shopping.modules.admin.auth.entity.DictApp;
import com.toucan.shopping.modules.admin.auth.entity.DictCategory;
import com.toucan.shopping.modules.admin.auth.entity.DictCategoryApp;
import com.toucan.shopping.modules.admin.auth.page.DictCategoryPageInfo;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.admin.auth.vo.DictCategoryVO;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
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

/**
 * 字典分类
 */
@RestController
@RequestMapping("/dictCategory")
public class DictCategoryController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DictCategoryService dictCategoryService;

    @Autowired
    private DictService dictService;

    @Autowired
    private DictAppService dictAppService;

    @Autowired
    private AppService appService;

    @Autowired
    private DictCategoryAppService dictCategoryAppService;

    @Autowired
    private AdminAppService adminAppService;

    @Autowired
    private IdGenerator idGenerator;

    /**
     * 添加字典分类
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
            DictCategoryVO dictCategoryVO = JSONObject.parseObject(requestVo.getEntityJson(),DictCategoryVO.class);
            if(StringUtils.isEmpty(dictCategoryVO.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请输入字典分类名称");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(dictCategoryVO.getCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请输入字典分类编码");
                return resultObjectVO;
            }

            List<DictCategoryVO> dictCategorys = dictCategoryService.queryListByCodeAndAppCodes(dictCategoryVO.getCode(),dictCategoryVO.getAppCodes());
            if(!CollectionUtils.isEmpty(dictCategorys))
            {
                DictCategoryVO dcv = dictCategorys.get(0);
                DictCategoryApp dictCategoryApp = new DictCategoryApp();
                dictCategoryApp.setDictCategoryId(dcv.getId());
                List<DictCategoryApp> dictCategoryApps = dictCategoryAppService.findListByEntity(dictCategoryApp);
                if(!CollectionUtils.isEmpty(dictCategoryApps))
                {
                    for(DictCategoryApp dca:dictCategoryApps)
                    {
                        AppVO appVO = appService.findByCodeIngoreDelete(dca.getAppCode());
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("在"+appVO.getName()+":"+appVO.getCode()+"中该编码已存在");
                        return resultObjectVO;
                    }
                }else{
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("编码已存在");
                    return resultObjectVO;
                }
            }

            dictCategoryVO.setCreateDate(new Date());
            dictCategoryVO.setDeleteStatus((short)0);
            dictCategoryVO.setDictCategorySort(dictCategoryService.queryMaxSort()+1);
            int row = dictCategoryService.save(dictCategoryVO);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            if(!CollectionUtils.isEmpty(dictCategoryVO.getAppCodes()))
            {
                for(String appCode:dictCategoryVO.getAppCodes())
                {
                    DictCategoryApp dictCategoryApp = new DictCategoryApp();
                    dictCategoryApp.setDictCategoryId(dictCategoryVO.getId());
                    dictCategoryApp.setAppCode(appCode);
                    dictCategoryApp.setCreateDate(new Date());
                    dictCategoryApp.setDeleteStatus((short)0);
                    dictCategoryApp.setCreateAdminId(dictCategoryVO.getCreateAdminId());

                    dictCategoryAppService.save(dictCategoryApp);
                }
            }

            resultObjectVO.setData(dictCategoryVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    


    /**
     * 编辑字典分类
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
            DictCategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),DictCategoryVO.class);

            if(StringUtils.isEmpty(entity.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入字典分类名称");
                return resultObjectVO;
            }
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入字典分类ID");
                return resultObjectVO;
            }


            DictCategory query=new DictCategory();
            query.setId(entity.getId());
            query.setDeleteStatus((short)0);
            List<DictCategoryVO> dictCategoryList = dictCategoryService.findListByEntity(query);
            if(CollectionUtils.isEmpty(dictCategoryList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("该字典分类不存在!");
                return resultObjectVO;
            }

            entity.setUpdateDate(new Date());
            int row = dictCategoryService.update(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            //接收的应用关联为空
            if(CollectionUtils.isEmpty(entity.getAppCodes()))
            {
                //清空字典分类下的字典所有应用的关联
                dictAppService.deleteByCategoryId(entity.getId());
            }else{
                DictCategoryApp queryDictCategoryApp = new DictCategoryApp();
                queryDictCategoryApp.setDictCategoryId(entity.getId());
                List<DictCategoryApp> dictCategoryApps = dictCategoryAppService.findListByEntity(queryDictCategoryApp);
                if(CollectionUtils.isEmpty(dictCategoryApps))  //如果现有的与应用关联为空,重新生成
                {
                    for (String appCode : entity.getAppCodes()) {
                        //保存分类与应用关联
                        DictCategoryApp dictCategoryApp = new DictCategoryApp();
                        dictCategoryApp.setDictCategoryId(entity.getId());
                        dictCategoryApp.setAppCode(appCode);
                        dictCategoryApp.setCreateDate(new Date());
                        dictCategoryApp.setDeleteStatus((short) 0);
                        dictCategoryApp.setCreateAdminId(entity.getUpdateAdminId());

                        dictCategoryAppService.save(dictCategoryApp);
                        //保存分类下字典与应用关联
                        DictVO queryDict = new DictVO();
                        queryDict.setCategoryId(entity.getId());
                        List<DictVO> dicts = dictService.queryList(queryDict);
                        if (!CollectionUtils.isEmpty(dicts)) {
                            List<DictApp> dictApps = new LinkedList<>();
                            for (Dict dvo : dicts) {
                                DictApp dictApp = new DictApp();
                                dictApp.setId(idGenerator.id());
                                dictApp.setDictId(dvo.getId());
                                dictApp.setAppCode(appCode);
                                dictApp.setCreateAdminId(entity.getUpdateAdminId());
                                dictApp.setCreateDate(new Date());
                                dictApp.setDeleteStatus((short) 0);
                                dictApps.add(dictApp);
                            }
                            if (!CollectionUtils.isEmpty(dictApps)) {
                                dictAppService.saves(dictApps);
                            }
                        }
                    }
                }else{ //如果现有的与应用关联不为空,忽略已存在的关联,处理删除或者新增的关联

                    //判断关联是否是新增
                    for (String appCode : entity.getAppCodes()) {
                        boolean isNewAdd =true; //是否新添加
                        for(DictCategoryApp categoryApp:dictCategoryApps){
                            if(appCode.equals(categoryApp.getAppCode()))
                            {
                                isNewAdd = false;
                                break;
                            }
                        }
                        if(isNewAdd) //新添加的应用关联
                        {
                            //保存分类下字典与应用关联
                            DictVO queryDict = new DictVO();
                            queryDict.setCategoryId(entity.getId());
                            List<DictVO> dicts = dictService.queryList(queryDict);
                            if (!CollectionUtils.isEmpty(dicts)) {
                                List<DictApp> dictApps = new LinkedList<>();
                                for (Dict dvo : dicts) {
                                    DictApp dictApp = new DictApp();
                                    dictApp.setId(idGenerator.id());
                                    dictApp.setDictId(dvo.getId());
                                    dictApp.setAppCode(appCode);
                                    dictApp.setCreateAdminId(entity.getUpdateAdminId());
                                    dictApp.setCreateDate(new Date());
                                    dictApp.setDeleteStatus((short) 0);
                                    dictApps.add(dictApp);
                                }
                                if (!CollectionUtils.isEmpty(dictApps)) {
                                    dictAppService.saves(dictApps);
                                }
                            }
                        }

                    }

                    //判断关联是否是被删除
                    for(DictCategoryApp categoryApp:dictCategoryApps){
                        boolean isRemove =true; //是否已删除
                        for (String appCode : entity.getAppCodes()) {
                            if(appCode.equals(categoryApp.getAppCode())){
                                isRemove = false;
                                break;
                            }
                        }
                        if(isRemove){
                            dictCategoryAppService.deleteById(categoryApp.getId());
                            dictAppService.deleteByCategoryId(categoryApp.getDictCategoryId(),categoryApp.getAppCode());
                        }
                    }
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
            DictCategoryPageInfo pageInfo = JSONObject.parseObject(requestVo.getEntityJson(), DictCategoryPageInfo.class);
            resultObjectVO.setData(dictCategoryService.queryListPage(pageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 查询列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryList(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            DictCategoryVO dictCategoryVO = requestVo.formatEntity(DictCategoryVO.class);
            resultObjectVO.setData(dictCategoryService.queryList(dictCategoryVO));

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
            DictCategory entity = JSONObject.parseObject(requestVo.getEntityJson(),DictCategory.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到字典分类ID");
                return resultObjectVO;
            }

            //查询是否存在该字典分类
            DictCategory query=new DictCategory();
            query.setId(entity.getId());
            List<DictCategoryVO> orgnazitionVOS = dictCategoryService.findListByEntity(query);
            if(CollectionUtils.isEmpty(orgnazitionVOS))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("字典分类不存在!");
                return resultObjectVO;
            }
            for(DictCategoryVO dictCategoryVO:orgnazitionVOS) {
                DictCategoryApp queryDictCategoryApp = new DictCategoryApp();
                queryDictCategoryApp.setDictCategoryId(dictCategoryVO.getId());
                List<DictCategoryApp> orgnazitionApps = dictCategoryAppService.findListByEntity(queryDictCategoryApp);
                dictCategoryVO.setDictCategoryApps(orgnazitionApps);
            }
            resultObjectVO.setData(orgnazitionVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }






    /**
     * 删除指定字典分类(仅限中台使用)
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
            DictCategory entity = JSONObject.parseObject(requestVo.getEntityJson(),DictCategory.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到字典分类ID");
                return resultObjectVO;
            }

            dictCategoryService.deleteById(entity.getId());
            dictCategoryAppService.deleteByDictCategoryId(entity.getId());
            dictAppService.deleteByCategoryId(entity.getId());
            dictService.deleteByCategoryId(entity.getId());

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
     * 批量删除字典分类(仅限中台使用)
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
            List<DictCategory> dictCategoryList = JSONObject.parseArray(requestVo.getEntityJson(),DictCategory.class);
            if(CollectionUtils.isEmpty(dictCategoryList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到字典分类ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(DictCategory dictCategory:dictCategoryList) {
                if(dictCategory.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(dictCategory);
                    dictCategoryService.deleteById(dictCategory.getId());
                    dictCategoryAppService.deleteByDictCategoryId(dictCategory.getId());
                    dictAppService.deleteByCategoryId(dictCategory.getId());
                    dictService.deleteByCategoryId(dictCategory.getId());
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
     * 查询列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/dict/category/app/list/by/id",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryCategoryAppListByCategoryId(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            DictCategory dictCategory = requestVo.formatEntity(DictCategory.class);
            if(dictCategory.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("字典分类ID不能为空");
                return resultObjectVO;
            }
            DictCategoryApp dictCategoryApp = new DictCategoryApp();
            dictCategoryApp.setDictCategoryId(dictCategory.getId());
            resultObjectVO.setData(dictCategoryAppService.findListByEntity(dictCategoryApp));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }
}
