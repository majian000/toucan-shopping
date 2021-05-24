package com.toucan.shopping.cloud.area.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.area.page.AreaTreeInfo;
import com.toucan.shopping.modules.area.service.AreaService;
import com.toucan.shopping.modules.area.vo.AreaTreeVO;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 管理端地区操作
 */
@RestController
@RequestMapping("/area")
public class AreaController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AreaService areaService;

    @Autowired
    private FeignAdminService feignAdminService;

    @Autowired
    private Toucan toucan;



    /**
     * 保存地区编码
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestHeader(value = "toucan-sign-header",defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用编码: param:"+ JSONObject.toJSON(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        try {
            Area area = JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);

            if(area.getAppCode()==null)
            {
                logger.info("没有找到应用编码: param:"+ JSONObject.toJSON(area));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码!");
                return resultObjectVO;
            }


            if(StringUtils.isEmpty(area.getCode()))
            {
                logger.info("编码为空 param:"+ JSONObject.toJSON(area));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("编码不能为空!");
                return resultObjectVO;
            }

            Area queryArea = new Area();
            queryArea.setCode(area.getCode());
            queryArea.setDeleteStatus((short)0);
            queryArea.setAppCode(area.getAppCode());

            if(!CollectionUtils.isEmpty(areaService.queryList(queryArea)))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("已存在该编码!");
                return resultObjectVO;
            }

            area.setCreateDate(new Date());
            int row = areaService.save(area);
            if (row != 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("保存失败,请重试!");
                return resultObjectVO;
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("保存失败,请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 更新地区编码
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestHeader(value = "toucan-sign-header",defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用编码: param:"+ JSONObject.toJSON(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        try {
            Area area = JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);

            if(area.getAppCode()==null)
            {
                logger.info("没有找到应用编码: param:"+ JSONObject.toJSON(area));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码!");
                return resultObjectVO;
            }


            if(StringUtils.isEmpty(area.getCode()))
            {
                logger.info("编码为空 param:"+ JSONObject.toJSON(area));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("编码不能为空!");
                return resultObjectVO;
            }


            if(area.getId()==null)
            {
                logger.info("类别ID为空 param:"+ JSONObject.toJSON(area));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("类别ID不能为空!");
                return resultObjectVO;
            }

            Area queryArea = new Area();
            queryArea.setCode(area.getCode());
            queryArea.setDeleteStatus((short)0);
            queryArea.setAppCode(area.getAppCode());

            List<Area> areaList = areaService.queryList(queryArea);
            if(!CollectionUtils.isEmpty(areaList))
            {
                if(area.getId() != areaList.get(0).getId())
                {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("该地区编码已存在!");
                    return resultObjectVO;
                }
            }

            area.setUpdateDate(new Date());
            int row = areaService.update(area);
            if (row != 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 根据ID删除类别
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO deleteById(@RequestHeader(value = "toucan-sign-header",defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用编码: param:"+ JSONObject.toJSON(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        try {
            Area area = JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);

            if(area.getAppCode()==null)
            {
                logger.info("没有找到应用编码: param:"+ JSONObject.toJSON(area));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码!");
                return resultObjectVO;
            }


            if(area.getId()==null)
            {
                logger.info("ID为空 param:"+ JSONObject.toJSON(area));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("ID不能为空!");
                return resultObjectVO;
            }



            if(StringUtils.isEmpty(area.getCode()))
            {
                logger.info("编码为空 param:"+ JSONObject.toJSON(area));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("编码不能为空!");
                return resultObjectVO;
            }

            Area queryArea = new Area();
            queryArea.setId(area.getId());
            queryArea.setDeleteStatus((short)0);
            queryArea.setAppCode(area.getAppCode());

            if(CollectionUtils.isEmpty(areaService.queryList(queryArea)))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在该类别!");
                return resultObjectVO;
            }

            areaService.deleteChildrenByParentCode(area.getAppCode(),area.getCode());
            int row = areaService.deleteById(area.getAppCode(),area.getId());
            if (row <=0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请重试!");
                return resultObjectVO;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }

    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryById(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }
        try {
            Area area = JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);
            resultObjectVO.setData(areaService.queryById(area.getId()));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/ids",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByIdList(@RequestHeader("toucan-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }
        try {
            List<Area> areas = JSONArray.parseArray(requestJsonVO.getEntityJson(),Area.class);
            if(!CollectionUtils.isEmpty(areas)) {
                List<Area> areaList = new ArrayList<Area>();
                for(Area area:areas) {
                    Area areaEntity = areaService.queryById(area.getId());
                    if(areaEntity!=null) {
                        areaList.add(areaEntity);
                    }
                }
                resultObjectVO.setData(areaList);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


    /**
     * 查询指定应用下地区树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/all",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryAll(@RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用!");
            return resultObjectVO;
        }
        try {
            List<AreaVO> areaVOS = areaService.queryTree(requestJsonVO.getAppCode());
            resultObjectVO.setData(areaVOS);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


    /**
     * 拿到创建人和修改人
     * @param ids
     * @param areas
     */
    private void getCreateAdminIdAndUpdateAdminId(List<Long> ids,List<AreaVO>  areas)
    {
        if(CollectionUtils.isEmpty(areas)) {
            for (AreaVO areaVO : areas) {
                if (areaVO.getCreateAdminId() != null) {
                    ids.add(areaVO.getCreateAdminId());
                }
                if (areaVO.getUpdateAdminId() != null) {
                    ids.add(areaVO.getUpdateAdminId());
                }
                getCreateAdminIdAndUpdateAdminId(ids, areaVO.getChildren());
            }
        }
    }



    /**
     * 设置创建人和修改人
     * @param adminVos
     * @param areas
     */
    private void setCreateAdminNameAndUpdateAdminName(List<AdminVO> adminVos,List<AreaVO>  areas)
    {
        if(CollectionUtils.isEmpty(areas)) {
            for (AreaVO areaVO : areas) {
                for(AdminVO adminVO:adminVos)
                {
                    if (areaVO.getCreateAdminId() != null&&areaVO.getCreateAdminId().longValue()==adminVO.getId().longValue()) {
                        areaVO.setCreateAdminUsername(adminVO.getUsername());
                    }
                    if (areaVO.getUpdateAdminId() != null&&areaVO.getUpdateAdminId().longValue()==adminVO.getId().longValue()) {
                        areaVO.setUpdateAdminUsername(adminVO.getUsername());
                    }
                }
                setCreateAdminNameAndUpdateAdminName(adminVos, areaVO.getChildren());
            }
        }
    }


    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTable(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AreaTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), AreaTreeInfo.class);

            //查询所有结构树
            List<AreaVO>  areas = areaService.findTreeTable(queryPageInfo);
            List<Long> ids = new ArrayList<Long>();
            //拿到树节点中所有创建人和修改人
            getCreateAdminIdAndUpdateAdminId(ids,areas);

            if(!CollectionUtils.isEmpty(areas))
            {
                AdminVO query = new AdminVO();
                Long[] idArray = new Long[areas.size()];
                query.setIds(idArray);
                RequestJsonVO adminRequestJsonVo = RequestJsonVOGenerator.generator(toucan.getAppCode(),query);
                resultObjectVO = feignAdminService.queryListByEntity(adminRequestJsonVo.sign(),adminRequestJsonVo);
                if(resultObjectVO.isSuccess())
                {
                    List<AdminVO> admins = (List)resultObjectVO.formatData(ArrayList.class);
                    if(!CollectionUtils.isEmpty(admins))
                    {
                        setCreateAdminNameAndUpdateAdminName(admins,areas);
                    }
                }
            }


            resultObjectVO.setData(areas);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 查询地区树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/tree",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTree(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AreaVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), AreaVO.class);

            List<Area> areas = areaService.queryList(query);
            if(!CollectionUtils.isEmpty(areas))
            {
                List<AreaTreeVO> areaTreeVOS = new ArrayList<AreaTreeVO>();
                for(Area area : areas)
                {
                    if("-1".equals(area.getParentCode())) {
                        AreaTreeVO areaTreeVO = new AreaTreeVO();
                        BeanUtils.copyProperties(areaTreeVO, area);
                        if(1==area.getType().shortValue())
                        {
                            areaTreeVO.setTitle(area.getProvince());
                            areaTreeVO.setText(area.getProvince());
                        }else if(2==area.getType().shortValue())
                        {
                            areaTreeVO.setTitle(area.getCity());
                            areaTreeVO.setText(area.getCity());
                        }else if(3==area.getType().shortValue())
                        {
                            areaTreeVO.setTitle(area.getArea());
                            areaTreeVO.setText(area.getArea());
                        }
                        areaTreeVOS.add(areaTreeVO);

                        areaTreeVO.setChildren(new ArrayList<AreaVO>());
                        areaService.setChildren(areas,areaTreeVO);
                    }
                }
                resultObjectVO.setData(areaTreeVOS);

            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }




}
