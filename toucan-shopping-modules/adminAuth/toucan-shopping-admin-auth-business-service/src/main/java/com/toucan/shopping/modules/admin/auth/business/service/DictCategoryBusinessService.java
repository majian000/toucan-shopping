package com.toucan.shopping.modules.admin.auth.business.service;


import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.toucan.shopping.modules.admin.auth.entity.Dict;
import com.toucan.shopping.modules.admin.auth.entity.DictCategory;
import com.toucan.shopping.modules.admin.auth.page.DictCategoryPageInfo;
import com.toucan.shopping.modules.admin.auth.service.*;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.admin.auth.vo.DictCategoryDetailVO;
import com.toucan.shopping.modules.admin.auth.vo.DictCategoryVO;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.util.GlobalUUID;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 字典分类
 */
@Service
public class DictCategoryBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DictCategoryService dictCategoryService;

    @Autowired
    private DictService dictService;


    @Autowired
    private AppService appService;


    @Autowired
    private AdminAppService adminAppService;

    @Autowired
    private IdGenerator idGenerator;

    /**
     * 添加字典分类
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            DictCategoryVO dictCategoryVO = JSONObject.parseObject(requestVo.getEntityJson(),DictCategoryVO.class);
            Check.notEmpty(dictCategoryVO.getName(), ResultVO.FAILD, "请输入字典分类名称");
            Check.notEmpty(dictCategoryVO.getCode(), ResultVO.FAILD, "请输入字典分类编码");

            dictCategoryVO.setAppCodes(new LinkedList<>());
            dictCategoryVO.getAppCodes().add(dictCategoryVO.getAppCode());
            List<DictCategoryVO> dictCategorys = dictCategoryService.queryListByCodeAndAppCodes(dictCategoryVO.getCode(),dictCategoryVO.getAppCodes());
            if(!CollectionUtils.isEmpty(dictCategorys))
            {
                DictCategoryVO dcv = dictCategorys.get(0);
                AppVO appVO = appService.findByCodeIngoreDelete(dcv.getAppCode());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("在"+appVO.getName()+":"+appVO.getCode()+"中该编码已存在");
                return resultObjectVO;
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


            resultObjectVO.setData(dictCategoryVO);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            DictCategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),DictCategoryVO.class);

            Check.notEmpty(entity.getName(), ResultVO.FAILD, "请传入字典分类名称");
            Check.notNull(entity.getId(), ResultVO.FAILD, "请传入字典分类ID");


            DictCategory query=new DictCategory();
            query.setCode(entity.getCode());
            query.setAppCode(entity.getAppCode());
            List<DictCategoryVO> dictCategoryList = dictCategoryService.findListByEntity(query);
            if(!CollectionUtils.isEmpty(dictCategoryList))
            {
                if(!dictCategoryList.get(0).getId().equals(entity.getId())) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("该字典分类编码已经存在!");
                    return resultObjectVO;
                }
            }

            entity.setUpdateDate(new Date());
            int row = dictCategoryService.update(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            //级联更新字典的应用编码
            dictService.updateAppCodeByCategoryId(entity.getId(),entity.getAppCode());


            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO listPage(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            DictCategoryPageInfo pageInfo = JSONObject.parseObject(requestVo.getEntityJson(), DictCategoryPageInfo.class);
            resultObjectVO.setData(dictCategoryService.queryListPage(pageInfo));

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryList(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            DictCategoryVO dictCategoryVO = requestVo.formatEntity(DictCategoryVO.class);
            resultObjectVO.setData(dictCategoryService.queryList(dictCategoryVO));

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            DictCategory entity = JSONObject.parseObject(requestVo.getEntityJson(),DictCategory.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到字典分类ID");

            //查询是否存在该字典分类
            DictCategory query=new DictCategory();
            query.setId(entity.getId());
            List<DictCategoryVO> list = dictCategoryService.findListByEntity(query);
            if(CollectionUtils.isEmpty(list))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("字典分类不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(list);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            DictCategory entity = JSONObject.parseObject(requestVo.getEntityJson(),DictCategory.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到字典分类ID");

            dictCategoryService.deleteById(entity.getId());
            dictService.deleteByCategoryId(entity.getId());

            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<DictCategory> dictCategoryList = JSON.parseArray(requestVo.getEntityJson(),DictCategory.class);
            Check.notEmpty(dictCategoryList, ResultVO.FAILD, "没有找到字典分类ID");
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(DictCategory dictCategory:dictCategoryList) {
                if(dictCategory.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(dictCategory);
                    dictCategoryService.deleteById(dictCategory.getId());
                    dictService.deleteByCategoryId(dictCategory.getId());
                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询详情
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryDetail(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            DictCategory entity = JSONObject.parseObject(requestVo.getEntityJson(),DictCategory.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到字典分类ID");

            DictCategoryVO dictCategoryVO = dictCategoryService.findDetailById(entity.getId().longValue());
            if(dictCategoryVO==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("字典分类不存在!");
                return resultObjectVO;
            }

            DictCategoryDetailVO detailVO = new DictCategoryDetailVO();
            detailVO.setBasicInfo(dictCategoryVO);
            detailVO.setAppName(dictCategoryVO.getAppName());

            resultObjectVO.setData(detailVO);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


}
