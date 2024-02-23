package com.toucan.shopping.modules.admin.auth.controller.dict;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.Dict;
import com.toucan.shopping.modules.admin.auth.entity.DictApp;
import com.toucan.shopping.modules.admin.auth.page.DictPageInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminAppService;
import com.toucan.shopping.modules.admin.auth.service.AppService;
import com.toucan.shopping.modules.admin.auth.service.DictAppService;
import com.toucan.shopping.modules.admin.auth.service.DictService;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
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
import java.util.List;

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
    private DictAppService dictAppService;

    @Autowired
    private AdminAppService adminAppService;


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
            DictVO dictVO = JSONObject.parseObject(requestVo.getEntityJson(),DictVO.class);
            if(StringUtils.isEmpty(dictVO.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请输入字典分类名称");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(dictVO.getCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请输入字典分类编码");
                return resultObjectVO;
            }

            List<DictVO> dicts = dictService.queryListByCodeAndAppCodes(dictVO.getCode(),dictVO.getAppCodes());
            if(!CollectionUtils.isEmpty(dicts))
            {
                DictVO dcv = dicts.get(0);
                DictApp dictApp = new DictApp();
                dictApp.setDictId(dcv.getId());
                List<DictApp> dictApps = dictAppService.findListByEntity(dictApp);
                if(!CollectionUtils.isEmpty(dictApps))
                {
                    for(DictApp dca:dictApps)
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

            dictVO.setCreateDate(new Date());
            dictVO.setDeleteStatus((short)0);
            dictVO.setDictSort(dictService.queryMaxSort()+1);
            int row = dictService.save(dictVO);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            if(!CollectionUtils.isEmpty(dictVO.getAppCodes()))
            {
                for(String appCode:dictVO.getAppCodes())
                {
                    DictApp dictApp = new DictApp();
                    dictApp.setDictId(dictVO.getId());
                    dictApp.setAppCode(appCode);
                    dictApp.setCreateDate(new Date());
                    dictApp.setDeleteStatus((short)0);
                    dictApp.setCreateAdminId(dictVO.getCreateAdminId());

                    dictAppService.save(dictApp);
                }
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
     * 編輯字典分类
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
                resultObjectVO.setMsg("请传入字典分类名称");
                return resultObjectVO;
            }
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入字典分类ID");
                return resultObjectVO;
            }


            Dict query=new Dict();
            query.setId(entity.getId());
            query.setDeleteStatus((short)0);
            List<DictVO> dictList = dictService.findListByEntity(query);
            if(CollectionUtils.isEmpty(dictList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("该字典分类不存在!");
                return resultObjectVO;
            }

            entity.setUpdateDate(new Date());
            int row = dictService.update(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            //删除字典分类应用关联
            dictAppService.deleteByDictId(dictList.get(0).getId());


            //重新生成关联
            if(!CollectionUtils.isEmpty(entity.getAppCodes()))
            {
                for(String appCode:entity.getAppCodes())
                {
                    DictApp orgnazitionApp = new DictApp();
                    orgnazitionApp.setDictId(dictList.get(0).getId());
                    orgnazitionApp.setAppCode(appCode);
                    orgnazitionApp.setCreateDate(new Date());
                    orgnazitionApp.setDeleteStatus((short)0);
                    orgnazitionApp.setCreateAdminId(entity.getUpdateAdminId());

                    dictAppService.save(orgnazitionApp);
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
                resultObjectVO.setMsg("没有找到字典分类ID");
                return resultObjectVO;
            }

            //查询是否存在该字典分类
            Dict query=new Dict();
            query.setId(entity.getId());
            List<DictVO> orgnazitionVOS = dictService.findListByEntity(query);
            if(CollectionUtils.isEmpty(orgnazitionVOS))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("字典分类不存在!");
                return resultObjectVO;
            }
            for(DictVO dictVO:orgnazitionVOS) {
                DictApp queryDictApp = new DictApp();
                queryDictApp.setDictId(dictVO.getId());
                List<DictApp> orgnazitionApps = dictAppService.findListByEntity(queryDictApp);
                dictVO.setDictApps(orgnazitionApps);
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
            Dict entity = JSONObject.parseObject(requestVo.getEntityJson(),Dict.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到字典分类ID");
                return resultObjectVO;
            }

            dictService.deleteById(entity.getId());
            dictAppService.deleteByDictId(entity.getId());
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
            List<Dict> dictList = JSONObject.parseArray(requestVo.getEntityJson(),Dict.class);
            if(CollectionUtils.isEmpty(dictList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到字典分类ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(Dict dict:dictList) {
                if(dict.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(dict);
                    dictService.deleteById(dict.getId());
                    dictAppService.deleteByDictId(dict.getId());
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
