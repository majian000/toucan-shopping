package com.toucan.shopping.modules.admin.auth.controller.dict;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.DictCategory;
import com.toucan.shopping.modules.admin.auth.entity.DictCategoryApp;
import com.toucan.shopping.modules.admin.auth.page.DictCategoryPageInfo;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.DictCategoryVO;
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
    private AppService appService;

    @Autowired
    private DictCategoryAppService dictCategoryAppService;

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
            resultObjectVO.setMsg("添加失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            DictCategoryVO dictCategoryVO = JSONObject.parseObject(requestVo.getEntityJson(),DictCategoryVO.class);
            if(StringUtils.isEmpty(dictCategoryVO.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,请输入字典分类名称");
                return resultObjectVO;
            }

            dictCategoryVO.setCreateDate(new Date());
            dictCategoryVO.setDeleteStatus((short)0);
            int row = dictCategoryService.save(dictCategoryVO);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,请重试!");
                return resultObjectVO;
            }

            if(!CollectionUtils.isEmpty(dictCategoryVO.getAppCodes()))
            {
                for(String appCode:dictCategoryVO.getAppCodes())
                {
                    DictCategoryApp orgnazitionApp = new DictCategoryApp();
                    orgnazitionApp.setDictCategoryId(dictCategoryVO.getId());
                    orgnazitionApp.setAppCode(appCode);
                    orgnazitionApp.setCreateDate(new Date());
                    orgnazitionApp.setDeleteStatus((short)0);
                    orgnazitionApp.setCreateAdminId(dictCategoryVO.getCreateAdminId());

                    dictCategoryAppService.save(orgnazitionApp);
                }
            }

            resultObjectVO.setData(dictCategoryVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("添加失败,请稍后重试");
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

            //删除字典分类应用关联
            dictCategoryAppService.deleteByDictCategoryId(dictCategoryList.get(0).getId());


            //重新生成关联
            if(!CollectionUtils.isEmpty(entity.getAppCodes()))
            {
                for(String appCode:entity.getAppCodes())
                {
                    DictCategoryApp orgnazitionApp = new DictCategoryApp();
                    orgnazitionApp.setDictCategoryId(dictCategoryList.get(0).getId());
                    orgnazitionApp.setAppCode(appCode);
                    orgnazitionApp.setCreateDate(new Date());
                    orgnazitionApp.setDeleteStatus((short)0);
                    orgnazitionApp.setCreateAdminId(entity.getUpdateAdminId());

                    dictCategoryAppService.save(orgnazitionApp);
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

            dictCategoryAppService.deleteByDictCategoryId(entity.getId());
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

                    dictCategoryAppService.deleteByDictCategoryId(dictCategory.getId());
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
