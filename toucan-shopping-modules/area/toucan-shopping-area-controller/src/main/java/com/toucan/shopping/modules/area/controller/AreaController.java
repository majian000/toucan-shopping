package com.toucan.shopping.modules.area.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.area.cache.service.AreaRedisService;
import com.toucan.shopping.modules.area.constant.AreaRedisKey;
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
    private AreaRedisService areaRedisService;

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
            logger.info("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        try {
            Area area = JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);

            if(area.getAppCode()==null)
            {
                logger.info("没有找到应用编码: param:"+ JSONObject.toJSONString(area));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码!");
                return resultObjectVO;
            }


            if(StringUtils.isEmpty(area.getCode()))
            {
                logger.info("编码为空 param:"+ JSONObject.toJSONString(area));
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
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }
            try{
                //刷新到缓存
                initAllAreaCache();
            }catch (Exception e)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("刷新缓存失败!");
                return resultObjectVO;
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        try {
            Area area = JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);

            if(area.getAppCode()==null)
            {
                logger.info("没有找到应用编码: param:"+ JSONObject.toJSONString(area));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码!");
                return resultObjectVO;
            }


            if(area.getId()==null)
            {
                logger.info("ID为空 param:"+ JSONObject.toJSONString(area));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("ID不能为空!");
                return resultObjectVO;
            }


            Area queryArea = new Area();
            queryArea.setId(area.getId());
            queryArea.setDeleteStatus((short)0);

            List<Area> areas = areaService.queryList(queryArea);
            if(CollectionUtils.isEmpty(areas))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在该地区!");
                return resultObjectVO;
            }

            area = areas.get(0);
            areaService.deleteChildrenByParentCode(area.getAppCode(),area.getCode());
            int row = areaService.deleteById(area.getAppCode(),area.getId());
            if (row <=0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            try{
                //刷新到缓存
                initAllAreaCache();
            }catch (Exception e)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("刷新缓存失败!");
                return resultObjectVO;
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
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
     * 批量删除功能项
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
            List<Area> areas = JSONObject.parseArray(requestVo.getEntityJson(),Area.class);
            if(CollectionUtils.isEmpty(areas))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找地区ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(Area area:areas) {
                if(area.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(area);


                    List<Area> chidlren = new ArrayList<Area>();
                    areaService.queryChildren(chidlren,area);

                    //把当前功能项添加进去,循环这个集合
                    chidlren.add(area);

                    for(Area a:chidlren) {
                        //删除当前功能项
                        int row = areaService.deleteById(a.getAppCode(),a.getId());
                        if (row < 1) {
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg("请重试!");
                            continue;
                        }

                    }

                }
            }

            resultObjectVO.setData(resultObjectVOList);

            try{
                //刷新到缓存
                initAllAreaCache();
            }catch (Exception e)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("刷新缓存失败!");
                return resultObjectVO;
            }
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


    public boolean existsAdminId(List<String> adminIds,String adminId)
    {
        for (String aid : adminIds) {
            if (aid != null && aid.equals(adminId)) {
                return true;
            }
        }
        return false;
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
            AreaVO entity = JSONObject.parseObject(requestVo.getEntityJson(),AreaVO.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到功能项ID");
                return resultObjectVO;
            }

            //查询是否存在该功能项
            Area query=new Area();
            query.setId(entity.getId());
            List<Area> areas = areaService.queryList(query);
            if(CollectionUtils.isEmpty(areas))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("地区不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(areas);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 根据编码查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/codes",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findByCodes(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AreaVO entity = JSONObject.parseObject(requestVo.getEntityJson(),AreaVO.class);
            if(entity.getCodeArray()==null||entity.getCodeArray().length<=0)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到编码数组");
                return resultObjectVO;
            }

            //查询是否存在该功能项
            List<Area> areas = areaService.queryList(entity);
            if(CollectionUtils.isEmpty(areas))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("地区不存在!");
                return resultObjectVO;
            }
            List<AreaVO> areaVOS = new ArrayList<AreaVO>();
            for(Area area:areas)
            {
                AreaVO areaVO = new AreaVO();
                BeanUtils.copyProperties(areaVO,area);
                //逐个替换,最后会替换为这个节点的名称
                if(StringUtils.isNotEmpty(areaVO.getProvince()))
                {
                    areaVO.setName(area.getProvince());
                }
                if(StringUtils.isNotEmpty(areaVO.getCity()))
                {
                    areaVO.setName(area.getCity());
                }
                if(StringUtils.isNotEmpty(areaVO.getArea()))
                {
                    areaVO.setName(area.getArea());
                }
                areaVOS.add(areaVO);
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

    /**
     * 編輯
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
            AreaVO entity = JSONObject.parseObject(requestVo.getEntityJson(),AreaVO.class);


            if(entity.getId().longValue()==entity.getPid().longValue())
            {
                logger.info("上级节点不能为自己 param:"+ JSONObject.toJSONString(entity));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("上级节点不能为自己!");
                return resultObjectVO;
            }


            if(StringUtils.isEmpty(entity.getCode()))
            {
                logger.info("编码为空 param:"+ JSONObject.toJSONString(entity));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("编码不能为空!");
                return resultObjectVO;
            }
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入ID");
                return resultObjectVO;
            }


            AreaVO query=new AreaVO();
            query.setId(entity.getId());
            List<Area> areas = areaService.queryList(query);
            if(CollectionUtils.isEmpty(areas))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("该地区不存在!");
                return resultObjectVO;
            }

            entity.setUpdateDate(new Date());
            int row = areaService.update(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            List<Area> children = new LinkedList<>();
            areaService.queryChildren(children,query);
            if(!CollectionUtils.isEmpty(children))
            {
                for(Area child:children)
                {
                    if(child.getBigAreaCode()==null||child.getCountryCode()==null)
                    {
                        child.setCountryCode(entity.getCountryCode());
                        child.setCountryName(entity.getCountryName());
                        child.setBigAreaCode(entity.getBigAreaCode());
                        child.setBigAreaName(entity.getBigAreaName());
                        child.setUpdateAdminId(entity.getUpdateAdminId());
                        child.setUpdateDate(entity.getUpdateDate());
                        areaService.update(child);
                    }else if((!child.getCountryCode().equals(entity.getCountryCode())||!child.getCountryName().equals(entity.getCountryName()))
                            ||(!child.getBigAreaCode().equals(entity.getBigAreaCode())||!child.getBigAreaName().equals(entity.getBigAreaName())))
                    {
                        child.setCountryCode(entity.getCountryCode());
                        child.setCountryName(entity.getCountryName());
                        child.setBigAreaCode(entity.getBigAreaCode());
                        child.setBigAreaName(entity.getBigAreaName());
                        child.setUpdateAdminId(entity.getUpdateAdminId());
                        child.setUpdateDate(entity.getUpdateDate());
                        areaService.update(child);
                    }
                }
            }

            try{
                //刷新到缓存
                initAllAreaCache();
            }catch (Exception e)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("刷新缓存失败!");
                return resultObjectVO;
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
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AreaTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), AreaTreeInfo.class);

            //查询所有结构树
            List<AreaVO>  areas = areaService.findTreeTable(queryPageInfo);
            List<String> adminIds = new ArrayList<String>();
            //拿到树节点中所有创建人和修改人
            if(!CollectionUtils.isEmpty(areas)) {
                for (AreaVO areaVO : areas) {
                    if (areaVO.getCreateAdminId() != null&&!"-1".equals(areaVO.getCreateAdminId())&&!existsAdminId(adminIds,areaVO.getCreateAdminId())) {
                        adminIds.add(areaVO.getCreateAdminId());
                    }
                    if (areaVO.getUpdateAdminId() != null&&!"-1".equals(areaVO.getUpdateAdminId())&&!existsAdminId(adminIds,areaVO.getUpdateAdminId())) {
                        adminIds.add(areaVO.getUpdateAdminId());
                    }
                }
            }


            //将查询的这个节点设置为顶级节点
            if(StringUtils.isNotEmpty(queryPageInfo.getCode())) {
                if(!CollectionUtils.isEmpty(areas)) {
                    for (Area area : areas) {
                        area.setPid(-1L);
                    }
                }
            }

            resultObjectVO.setData(areas);

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
            AreaTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), AreaTreeInfo.class);

            List<AreaTreeVO> areaVOs = new ArrayList<AreaTreeVO>();
            //按指定条件查询
            if(StringUtils.isNotEmpty(queryPageInfo.getCode())||StringUtils.isNotEmpty(queryPageInfo.getName()))
            {
                AreaVO queryArea = new AreaVO();
                queryArea.setCode(queryPageInfo.getCode());
                queryArea.setName(queryPageInfo.getName());

                List<Area> areas = areaService.queryListByVO(queryArea);
                for (int i = 0; i < areas.size(); i++) {
                    Area area = areas.get(i);
                    AreaTreeVO areaTreeVO = new AreaTreeVO();
                    BeanUtils.copyProperties(areaTreeVO, area);
                    if (area.getType() == 1) {
                        areaTreeVO.setName(area.getProvince());
                    } else if (area.getType() == 2) {
                        areaTreeVO.setName(area.getCity());
                    } else if (area.getType() == 3) {
                        areaTreeVO.setName(area.getArea());
                    }
                    areaVOs.add(areaTreeVO);
                }
            }else {
                //查询当前节点下的所有子节点
                Area queryArea = new Area();
                queryArea.setPid(queryPageInfo.getPid());
                List<Area> areas = areaService.queryList(queryArea);
                for (int i = 0; i < areas.size(); i++) {
                    Area area = areas.get(i);
                    AreaTreeVO areaTreeVO = new AreaTreeVO();
                    BeanUtils.copyProperties(areaTreeVO, area);
                    if (area.getType() == 1) {
                        areaTreeVO.setName(area.getProvince());
                    } else if (area.getType() == 2) {
                        areaTreeVO.setName(area.getCity());
                    } else if (area.getType() == 3) {
                        areaTreeVO.setName(area.getArea());
                    }
                    queryArea = new Area();
                    queryArea.setPid(area.getId());
                    Long childCount = areaService.queryCount(queryArea);
                    if (childCount > 0) {
                        areaTreeVO.setHaveChild(true);
                    }
                    areaVOs.add(areaTreeVO);
                }
            }

            List<String> adminIds = new ArrayList<String>();
            //拿到树节点中所有创建人和修改人
            if(!CollectionUtils.isEmpty(areaVOs)) {
                for (AreaVO areaVO : areaVOs) {
                    if (areaVO.getCreateAdminId() != null&&!"-1".equals(areaVO.getCreateAdminId())&&!existsAdminId(adminIds,areaVO.getCreateAdminId())) {
                        adminIds.add(areaVO.getCreateAdminId());
                    }
                    if (areaVO.getUpdateAdminId() != null&&!"-1".equals(areaVO.getUpdateAdminId())&&!existsAdminId(adminIds,areaVO.getUpdateAdminId())) {
                        adminIds.add(areaVO.getUpdateAdminId());
                    }
                }
            }


            //将查询的这个节点设置为顶级节点
            if(StringUtils.isNotEmpty(queryPageInfo.getCode())) {
                if(!CollectionUtils.isEmpty(areaVOs)) {
                    for (Area area : areaVOs) {
                        area.setPid(-1L);
                    }
                }
            }

            resultObjectVO.setData(areaVOs);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryListByPid(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            Area queryArea = JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);
            List<Area> areas = areaService.queryList(queryArea);
            List<AreaVO> areaVOS = new ArrayList<AreaVO>();
            for (int i = 0; i < areas.size(); i++) {
                Area area = areas.get(i);
                AreaVO areaVO = new AreaVO();
                BeanUtils.copyProperties(areaVO, area);
                if (area.getType() == 1) {
                    areaVO.setName(area.getProvince());
                } else if (area.getType() == 2) {
                    areaVO.setName(area.getCity());
                } else if (area.getType() == 3) {
                    areaVO.setName(area.getArea());
                }
                areaVOS.add(areaVO);
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



    /**
     * 查询指定节点下所有子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/parentCode",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryListByParentCode(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            Area queryArea = JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);
            List<AreaVO> areaVOS =null;
            if("-1".equals(queryArea.getCode()))
            {
                areaVOS = areaRedisService.queryProvinceList();
            }else{
                //先查询地市,之后在查询区县,因为不确定这个节点是指定省还是指定地市
                areaVOS = areaRedisService.queryCityListByProvinceCode(queryArea.getCode());
                if(CollectionUtils.isEmpty(areaVOS))
                {
                    areaVOS = areaRedisService.queryAreaListByCityCode(queryArea.getCode());
                }

            }
            //如果缓存不存在,查询数据库然后同步到缓存
            if(CollectionUtils.isEmpty(areaVOS)) {
                List<Area> areas = null;
                if (!"-1".equals(queryArea.getCode())) {
                    areas = areaService.queryList(queryArea);
                    if (CollectionUtils.isEmpty(areas)) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("没有找到该节点");
                        return resultObjectVO;
                    }
                    Area currentArea = areas.get(0);
                    queryArea = new Area();
                    //设置当前节点到PID
                    queryArea.setPid(currentArea.getId());
                }else{
                    queryArea.setCode(null);
                    queryArea.setPid(-1L); //默认查询所有省
                }
                areaVOS = new ArrayList<AreaVO>();
                areas = areaService.queryList(queryArea);
                for (int i = 0; i < areas.size(); i++) {
                    Area area = areas.get(i);
                    AreaVO areaVO = new AreaVO();
                    BeanUtils.copyProperties(areaVO, area);
                    if (area.getType() == 1) {
                        areaVO.setName(area.getProvince());
                    } else if (area.getType() == 2) {
                        areaVO.setName(area.getCity());
                    } else if (area.getType() == 3) {
                        areaVO.setName(area.getArea());
                    }
                    areaVOS.add(areaVO);
                }

                try{
                    //刷新到缓存
                    initAllAreaCache();
                }catch (Exception e)
                {
                    logger.warn("同步地区缓存失败 {}",e.getMessage());
                    logger.warn(e.getMessage(),e);
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




    /**
     * 查询全量缓存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/full/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryFullCache(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            Area queryArea = JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);

            List<AreaVO> areaVOS = areaRedisService.queryFullCache();
            if(CollectionUtils.isEmpty(areaVOS))
            {
                //刷新下全部缓存
                this.initAllAreaCache();
                areaVOS = areaRedisService.queryFullCache();
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

    /**
     * 刷新全部缓存
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/flush/all/cache",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO flushAllCache(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AreaVO areaVOS = requestVo.formatEntity(AreaVO.class);
            //刷新到缓存
            initAllAreaCache();
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 初始化地区缓存
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void initAllAreaCache() throws InvocationTargetException, IllegalAccessException {
        //清空缓存
        areaRedisService.clearAreaCache();

        //查询所有省、直辖市
        Area query = new Area();
        List<Area> areas = areaService.queryList(query);
        List<AreaVO> areaVOS = JSONArray.parseArray(JSONObject.toJSONString(areas),AreaVO.class);

        //初始化省、直辖市
        List<AreaVO> allProvinces = new ArrayList<AreaVO>();
        for(AreaVO areaVO:areaVOS)
        {
            if(areaVO.getPid()==null||areaVO.getPid().longValue()==-1L)
            {
                if (areaVO.getType() == 1) {
                    areaVO.setName(areaVO.getProvince());
                } else if (areaVO.getType() == 2) {
                    areaVO.setName(areaVO.getCity());
                } else if (areaVO.getType() == 3) {
                    areaVO.setName(areaVO.getArea());
                }
                allProvinces.add(areaVO);
            }
        }
        if(!CollectionUtils.isEmpty(allProvinces)) {
            areaRedisService.flushProvinceCache(allProvinces);
        }

        List<AreaVO> allCitys = new ArrayList<AreaVO>();
        //初始化地市
        for(AreaVO provinceVO:allProvinces)
        {
            List<AreaVO> citys = new ArrayList<AreaVO>();
            for(AreaVO cityVO:areaVOS)
            {
                if(provinceVO.getId()!=null&&cityVO.getPid()!=null&&provinceVO.getId().longValue()==cityVO.getPid().longValue())
                {
                    //如果不是直辖市,就初始化这个省下的地市,否则是直辖市就初始化下面的区县
                    if(provinceVO.getIsMunicipality().intValue()==0) {
                        if (cityVO.getType() == 1) {
                            cityVO.setName(cityVO.getProvince());
                        } else if (cityVO.getType() == 2) {
                            cityVO.setName(cityVO.getCity());
                        } else if (cityVO.getType() == 3) {
                            cityVO.setName(cityVO.getArea());
                        }
                        citys.add(cityVO);
                        allCitys.add(cityVO);
                    }
                }
            }
            //设置子节点
            provinceVO.setChildren(citys);
            if(!CollectionUtils.isEmpty(citys))
            {
                //根据省ID作为Key的一部分
                areaRedisService.flushCityCache(AreaRedisKey.getCityCacheKey("ID_"+String.valueOf(provinceVO.getId())),citys);
                //根据省编码作为Key的一部分
                areaRedisService.flushCityCache(AreaRedisKey.getCityCacheKey("CODE_"+provinceVO.getCode()),citys);
            }
        }


        List<AreaVO> allAreas = new ArrayList<AreaVO>();
        //初始化区县
        for(AreaVO provinceVO:allProvinces)
        {
            //如果是省,不是直辖市的话,就找到下面所有地市再查询区县,如果是直辖市,就直接查询区县
            if (provinceVO.getIsMunicipality().intValue() == 0) {
                for (AreaVO cityVO : allCitys) {
                    //找到地市
                    if (provinceVO.getId() != null && cityVO.getPid() != null && provinceVO.getId().longValue() == cityVO.getPid().longValue()) {
                        List<AreaVO> areaVOList = new ArrayList<AreaVO>();
                        for (AreaVO areaVO : areaVOS) {
                            if (cityVO.getId() != null && areaVO.getPid() != null && cityVO.getId().longValue() == areaVO.getPid().longValue()) {
                                if (areaVO.getType() == 1) {
                                    areaVO.setName(areaVO.getProvince());
                                } else if (areaVO.getType() == 2) {
                                    areaVO.setName(areaVO.getCity());
                                } else if (areaVO.getType() == 3) {
                                    areaVO.setName(areaVO.getArea());
                                }
                                areaVOList.add(areaVO);
                                allAreas.add(areaVO);
                            }
                        }

                        //设置子节点
                        cityVO.setChildren(areaVOList);
                        if (!CollectionUtils.isEmpty(areaVOList)) {
                            //根据省ID作为Key的一部分
                            areaRedisService.flushAreaCache(AreaRedisKey.getAreaCacheKey("ID_" + String.valueOf(cityVO.getId())), areaVOList);
                            //根据省编码作为Key的一部分
                            areaRedisService.flushAreaCache(AreaRedisKey.getAreaCacheKey("CODE_" + cityVO.getCode()), areaVOList);
                        }
                    }
                }
            }else{
                //找到区县
                List<AreaVO> areaVOList = new ArrayList<AreaVO>();
                for (AreaVO areaVO : areaVOS) {
                    if (provinceVO.getId() != null && areaVO.getPid() != null && provinceVO.getId().longValue() == areaVO.getPid().longValue()) {
                        if (areaVO.getType() == 1) {
                            areaVO.setName(areaVO.getProvince());
                        } else if (areaVO.getType() == 2) {
                            areaVO.setName(areaVO.getCity());
                        } else if (areaVO.getType() == 3) {
                            areaVO.setName(areaVO.getArea());
                        }
                        areaVOList.add(areaVO);
                        allAreas.add(areaVO);
                    }
                }

                //设置子节点
                provinceVO.setChildren(areaVOList);
                if (!CollectionUtils.isEmpty(areaVOList)) {
                    //根据省ID作为Key的一部分
                    areaRedisService.flushAreaCache(AreaRedisKey.getAreaCacheKey("ID_" + String.valueOf(provinceVO.getId())), areaVOList);
                    //根据省编码作为Key的一部分
                    areaRedisService.flushAreaCache(AreaRedisKey.getAreaCacheKey("CODE_" + provinceVO.getCode()), areaVOList);
                }
            }
        }

        //去除多余字段,只保留ID、编码、名称
        for(AreaVO provinceVO:allProvinces)
        {
            provinceVO.setPid(null);
            provinceVO.setAppCode(null);
            provinceVO.setParentName(null);
            provinceVO.setCreateAdminName(null);
            provinceVO.setCreateDate(null);
            provinceVO.setCreateAdminId(null);
            provinceVO.setUpdateAdminName(null);
            provinceVO.setUpdateAdminId(null);
            provinceVO.setUpdateDate(null);
            provinceVO.setDeleteStatus(null);
            provinceVO.setCodeArray(null);
            provinceVO.setArea(null);
            provinceVO.setAreaSort(null);
            provinceVO.setIsMunicipality(null);
            provinceVO.setCity(null);
            provinceVO.setProvince(null);
            provinceVO.setRemark(null);
            if(!CollectionUtils.isEmpty(provinceVO.getChildren()))
            {
                List<AreaVO> cityOrAreaVOChildren = (List<AreaVO>)provinceVO.getChildren();
                for(AreaVO cityOrAreaVO:cityOrAreaVOChildren)
                {
                    cityOrAreaVO.setPid(null);
                    cityOrAreaVO.setAppCode(null);
                    cityOrAreaVO.setParentName(null);
                    cityOrAreaVO.setCreateAdminName(null);
                    cityOrAreaVO.setCreateDate(null);
                    cityOrAreaVO.setCreateAdminId(null);
                    cityOrAreaVO.setUpdateAdminName(null);
                    cityOrAreaVO.setUpdateAdminId(null);
                    cityOrAreaVO.setUpdateDate(null);
                    cityOrAreaVO.setDeleteStatus(null);
                    cityOrAreaVO.setCodeArray(null);
                    cityOrAreaVO.setArea(null);
                    cityOrAreaVO.setAreaSort(null);
                    cityOrAreaVO.setIsMunicipality(null);
                    cityOrAreaVO.setCity(null);
                    cityOrAreaVO.setProvince(null);
                    cityOrAreaVO.setRemark(null);

                    if(!CollectionUtils.isEmpty(cityOrAreaVO.getChildren())) {
                        List<AreaVO> areaVOChildren = (List<AreaVO>) cityOrAreaVO.getChildren();
                        for (AreaVO areaVO : areaVOChildren) {
                            areaVO.setPid(null);
                            areaVO.setAppCode(null);
                            areaVO.setParentName(null);
                            areaVO.setCreateAdminName(null);
                            areaVO.setCreateDate(null);
                            areaVO.setCreateAdminId(null);
                            areaVO.setUpdateAdminName(null);
                            areaVO.setUpdateAdminId(null);
                            areaVO.setUpdateDate(null);
                            areaVO.setDeleteStatus(null);
                            areaVO.setCodeArray(null);
                            areaVO.setArea(null);
                            areaVO.setAreaSort(null);
                            areaVO.setIsMunicipality(null);
                            areaVO.setCity(null);
                            areaVO.setProvince(null);
                            areaVO.setRemark(null);
                        }
                    }
                }
            }

        }

        areaRedisService.flushFullAreaCache(allProvinces);
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

                        areaTreeVO.setChildren(new ArrayList<AreaTreeVO>());
                        areaService.setChildren(areas,areaTreeVO);
                    }
                }

                AreaTreeVO rootTreeVO = new AreaTreeVO();
                rootTreeVO.setTitle("中国");
                rootTreeVO.setCode("-1");
                rootTreeVO.setPid(-1L);
                rootTreeVO.setId(-1L);
                rootTreeVO.setText("中国");
                rootTreeVO.setChildren(areaTreeVOS);
                List<AreaTreeVO> rootAreaTreeVOS = new ArrayList<AreaTreeVO>();
                rootAreaTreeVOS.add(rootTreeVO);
                resultObjectVO.setData(rootAreaTreeVOS);

            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




}
