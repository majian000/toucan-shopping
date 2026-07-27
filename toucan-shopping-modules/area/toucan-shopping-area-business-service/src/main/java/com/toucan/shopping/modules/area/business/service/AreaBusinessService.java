package com.toucan.shopping.modules.area.business.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.area.cache.service.AreaRedisService;
import com.toucan.shopping.modules.area.constant.AreaRedisKey;
import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.area.page.AreaTreeInfo;
import com.toucan.shopping.modules.area.service.AreaService;
import com.toucan.shopping.modules.area.vo.AreaTreeVO;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;

/**
 * 管理端地区操作
 */
@Service
public class AreaBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AreaService areaService;

    @Autowired
    private AreaRedisService areaRedisService;

    @Autowired
    private Toucan toucan;

    // ========================================================================
    //  CRUD 方法
    // ========================================================================

    /**
     * 保存地区编码
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            Area area = JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);

            Check.notNull(area.getAppCode(), ResultVO.FAILD, "没有找到应用编码!");
            Check.notEmpty(area.getCode(), ResultVO.FAILD, "编码不能为空!");

            Area queryArea = new Area();
            queryArea.setCode(area.getCode());
            queryArea.setDeleteStatus((short) 0);
            queryArea.setAppCode(area.getAppCode());

            Check.isTrue(CollectionUtils.isEmpty(areaService.queryList(queryArea)), ResultVO.FAILD, "已存在该编码!");

            area.setCreateDate(new Date());
            int row = areaService.save(area);
            if (row != 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }
            if (!refreshCache(resultObjectVO)) {
                return resultObjectVO;
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    /**
     * 根据ID删除
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            Area area = JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);

            Check.notNull(area.getAppCode(), ResultVO.FAILD, "没有找到应用编码!");
            Check.notNull(area.getId(), ResultVO.FAILD, "ID不能为空!");

            Area queryArea = new Area();
            queryArea.setId(area.getId());
            queryArea.setDeleteStatus((short) 0);

            List<Area> areas = areaService.queryList(queryArea);
            Check.notEmpty(areas, ResultVO.FAILD, "不存在该地区!");

            area = areas.get(0);
            areaService.deleteChildrenByParentCode(area.getAppCode(), area.getCode());
            int row = areaService.deleteById(area.getAppCode(), area.getId());
            if (row <= 0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            if (!refreshCache(resultObjectVO)) {
                return resultObjectVO;
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    /**
     * 根据ID查询
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            Area area = JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);
            resultObjectVO.setData(areaService.queryById(area.getId()));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    /**
     * 批量删除功能项
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            List<Area> areas = JSONObject.parseArray(requestVo.getEntityJson(), Area.class);
            Check.notEmpty(areas, ResultVO.FAILD, "没有找地区ID");
            List<ResultObjectVO> resultObjectVOList = new ArrayList<>();
            for (Area area : areas) {
                if (area.getId() != null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(area);

                    List<Area> children = new ArrayList<>();
                    areaService.queryChildren(children, area);
                    children.add(area);

                    for (Area a : children) {
                        int row = areaService.deleteById(a.getAppCode(), a.getId());
                        if (row < 1) {
                            resultObjectVO.setCode(ResultVO.FAILD);
                            resultObjectVO.setMsg("请重试!");
                            continue;
                        }
                    }
                }
            }

            resultObjectVO.setData(resultObjectVOList);

            if (!refreshCache(resultObjectVO)) {
                return resultObjectVO;
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据ID列表查询
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryByIdList(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            List<Area> areas = JSONArray.parseArray(requestJsonVO.getEntityJson(), Area.class);
            if (!CollectionUtils.isEmpty(areas)) {
                List<Area> areaList = new ArrayList<>();
                for (Area area : areas) {
                    Area areaEntity = areaService.queryById(area.getId());
                    if (areaEntity != null) {
                        areaList.add(areaEntity);
                    }
                }
                resultObjectVO.setData(areaList);
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    // ========================================================================
    //  查询方法
    // ========================================================================

    /**
     * 查询指定应用下地区树
     */
    @RequestCheck
    public ResultObjectVO queryAll(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            List<AreaVO> areaVOS = areaService.queryTree(requestJsonVO.getAppCode());
            resultObjectVO.setData(areaVOS);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    /**
     * 根据ID查询
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AreaVO entity = JSONObject.parseObject(requestVo.getEntityJson(), AreaVO.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到功能项ID");

            Area query = new Area();
            query.setId(entity.getId());
            List<Area> areas = areaService.queryList(query);
            Check.notEmpty(areas, ResultVO.FAILD, "地区不存在!");
            resultObjectVO.setData(areas);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据编码查询
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findByCodes(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AreaVO entity = JSONObject.parseObject(requestVo.getEntityJson(), AreaVO.class);
            Check.isTrue(entity.getCodeArray() != null && entity.getCodeArray().length > 0, ResultVO.FAILD, "没有找到编码数组");

            List<Area> areas = areaService.queryList(entity);
            Check.notEmpty(areas, ResultVO.FAILD, "地区不存在!");
            List<AreaVO> areaVOS = new ArrayList<>();
            for (Area area : areas) {
                AreaVO areaVO = new AreaVO();
                BeanUtils.copyProperties(areaVO, area);
                // 逐级替换为最具体的名称
                if (StringUtils.isNotEmpty(areaVO.getProvince())) {
                    areaVO.setName(area.getProvince());
                }
                if (StringUtils.isNotEmpty(areaVO.getCity())) {
                    areaVO.setName(area.getCity());
                }
                if (StringUtils.isNotEmpty(areaVO.getArea())) {
                    areaVO.setName(area.getArea());
                }
                areaVOS.add(areaVO);
            }
            resultObjectVO.setData(areaVOS);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 编辑
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AreaVO entity = JSONObject.parseObject(requestVo.getEntityJson(), AreaVO.class);

            Check.isTrue(entity.getId().longValue() != entity.getPid().longValue(), ResultVO.FAILD, "上级节点不能为自己!");
            Check.notEmpty(entity.getCode(), ResultVO.FAILD, "编码不能为空!");
            Check.notNull(entity.getId(), ResultVO.FAILD, "请传入ID");

            AreaVO query = new AreaVO();
            query.setId(entity.getId());
            List<Area> areas = areaService.queryList(query);
            Check.notEmpty(areas, ResultVO.FAILD, "该地区不存在!");

            entity.setUpdateDate(new Date());
            int row = areaService.update(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            List<Area> children = new LinkedList<>();
            areaService.queryChildren(children, query);
            if (!CollectionUtils.isEmpty(children)) {
                for (Area child : children) {
                    boolean needsUpdate = (child.getBigAreaCode() == null || child.getCountryCode() == null)
                            || (!child.getCountryCode().equals(entity.getCountryCode())
                                || !child.getCountryName().equals(entity.getCountryName())
                                || !child.getBigAreaCode().equals(entity.getBigAreaCode())
                                || !child.getBigAreaName().equals(entity.getBigAreaName()));
                    if (needsUpdate) {
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

            if (!refreshCache(resultObjectVO)) {
                return resultObjectVO;
            }

            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    // ========================================================================
    //  树形表格查询
    // ========================================================================

    /**
     * 查询树表格
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryTreeTable(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AreaTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), AreaTreeInfo.class);

            List<AreaVO> areas = areaService.findTreeTable(queryPageInfo);
            List<String> adminIds = new ArrayList<>();
            collectAdminIds(areas, adminIds);

            // 将查询的节点设置为顶级节点
            setTopLevelPid(areas, queryPageInfo.getCode());

            resultObjectVO.setData(areas);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 查询树表格（按PID）
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AreaTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), AreaTreeInfo.class);

            List<AreaTreeVO> areaVOs = new ArrayList<>();
            if (StringUtils.isNotEmpty(queryPageInfo.getCode()) || StringUtils.isNotEmpty(queryPageInfo.getName())) {
                // 按指定条件查询
                AreaVO queryArea = new AreaVO();
                queryArea.setCode(queryPageInfo.getCode());
                queryArea.setName(queryPageInfo.getName());

                List<Area> areas = areaService.queryListByVO(queryArea);
                for (Area area : areas) {
                    AreaTreeVO areaTreeVO = new AreaTreeVO();
                    BeanUtils.copyProperties(areaTreeVO, area);
                    setAreaNameByType(areaTreeVO, area);
                    areaVOs.add(areaTreeVO);
                }
            } else {
                // 查询当前节点下的所有子节点
                Area queryArea = new Area();
                queryArea.setPid(queryPageInfo.getPid());
                List<Area> areas = areaService.queryList(queryArea);
                for (Area area : areas) {
                    AreaTreeVO areaTreeVO = new AreaTreeVO();
                    BeanUtils.copyProperties(areaTreeVO, area);
                    setAreaNameByType(areaTreeVO, area);
                    Area childQuery = new Area();
                    childQuery.setPid(area.getId());
                    Long childCount = areaService.queryCount(childQuery);
                    if (childCount > 0) {
                        areaTreeVO.setHaveChild(true);
                    }
                    areaVOs.add(areaTreeVO);
                }
            }

            List<String> adminIds = new ArrayList<>();
            collectAdminIds(areaVOs, adminIds);

            // 将查询的节点设置为顶级节点
            setTopLevelPidFromCode(new ArrayList<>(areaVOs), queryPageInfo.getCode());

            resultObjectVO.setData(areaVOs);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    // ========================================================================
    //  下级节点查询
    // ========================================================================

    /**
     * 查询指定节点下所有子节点
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListByPid(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            Area queryArea = JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);
            List<Area> areas = areaService.queryList(queryArea);
            List<AreaVO> areaVOS = new ArrayList<>();
            for (Area area : areas) {
                AreaVO areaVO = new AreaVO();
                BeanUtils.copyProperties(areaVO, area);
                setAreaNameByType(areaVO, area);
                areaVOS.add(areaVO);
            }
            resultObjectVO.setData(areaVOS);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 查询指定节点下子节点（树形结构）
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryTreeChildByPid(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            Area queryArea = JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);
            List<AreaTreeVO> areaVOs = new ArrayList<>();
            if (queryArea.getPid() == null) {
                AreaTreeVO areaVO = new AreaTreeVO();
                areaVO.setId(-1L);
                areaVO.setName("中国");
                areaVO.setParentId(-1L);
                Long childCount = areaService.queryOneChildCountByPid(-1L, queryArea.getAppCode());
                if (childCount > 0) {
                    areaVO.setIsParent(true);
                }
                areaVOs.add(areaVO);
            } else {
                List<Area> areas = areaService.queryList(queryArea);
                for (Area area : areas) {
                    AreaTreeVO areaTreeVO = new AreaTreeVO();
                    BeanUtils.copyProperties(areaTreeVO, area);
                    setAreaNameByType(areaTreeVO, area);
                    Long childCount = areaService.queryOneChildCountByPid(areaTreeVO.getId(), areaTreeVO.getAppCode());
                    if (childCount > 0) {
                        areaTreeVO.setIsParent(true);
                    } else {
                        areaTreeVO.setIsParent(false);
                    }
                    areaVOs.add(areaTreeVO);
                }
            }

            resultObjectVO.setData(areaVOs);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 查询指定节点下所有子节点（按父编码，优先从缓存获取）
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListByParentCode(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            Area queryArea = JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);
            List<AreaVO> areaVOS = null;
            if ("-1".equals(queryArea.getCode())) {
                areaVOS = areaRedisService.queryProvinceList();
            } else {
                // 先查地市，再查区县（不确定该节点是省还是地市）
                areaVOS = areaRedisService.queryCityListByProvinceCode(queryArea.getCode());
                if (CollectionUtils.isEmpty(areaVOS)) {
                    areaVOS = areaRedisService.queryAreaListByCityCode(queryArea.getCode());
                }
            }
            // 缓存不存在则查询数据库并同步缓存
            if (CollectionUtils.isEmpty(areaVOS)) {
                areaVOS = queryListByParentCodeFromDb(queryArea, resultObjectVO);
                if (areaVOS == null) {
                    return resultObjectVO;
                }
            }

            resultObjectVO.setData(areaVOS);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 从数据库查询子节点列表并构建缓存
     */
    private List<AreaVO> queryListByParentCodeFromDb(Area queryArea, ResultObjectVO resultObjectVO)
            throws InvocationTargetException, IllegalAccessException {
        List<Area> areas;
        Area originalQuery = queryArea;
        if (!"-1".equals(originalQuery.getCode())) {
            areas = areaService.queryList(originalQuery);
            if (CollectionUtils.isEmpty(areas)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到该节点");
                return null;
            }
            Area currentArea = areas.get(0);
            queryArea = new Area();
            queryArea.setPid(currentArea.getId());
        } else {
            queryArea.setCode(null);
            queryArea.setPid(-1L);
        }
        List<AreaVO> areaVOS = new ArrayList<>();
        areas = areaService.queryList(queryArea);
        for (Area area : areas) {
            AreaVO areaVO = new AreaVO();
            BeanUtils.copyProperties(areaVO, area);
            setAreaNameByType(areaVO, area);
            areaVOS.add(areaVO);
        }

        try {
            initAllAreaCache();
        } catch (Exception e) {
            logger.warn("同步地区缓存失败 {}", e.getMessage());
            logger.warn(e.getMessage(), e);
        }
        return areaVOS;
    }

    // ========================================================================
    //  缓存相关
    // ========================================================================

    /**
     * 查询全量缓存
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryFullCache(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            JSONObject.parseObject(requestJsonVO.getEntityJson(), Area.class);

            List<AreaVO> areaVOS = areaRedisService.queryFullCache();
            if (CollectionUtils.isEmpty(areaVOS)) {
                initAllAreaCache();
                areaVOS = areaRedisService.queryFullCache();
            }
            resultObjectVO.setData(areaVOS);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 刷新全部缓存
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO flushAllCache(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            requestVo.formatEntity(AreaVO.class);
            initAllAreaCache();
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    // ========================================================================
    //  地区树
    // ========================================================================

    /**
     * 查询地区树
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryTree(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AreaVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), AreaVO.class);

            List<Area> areas = areaService.queryList(query);
            if (!CollectionUtils.isEmpty(areas)) {
                List<AreaTreeVO> areaTreeVOS = new ArrayList<>();
                for (Area area : areas) {
                    if ("-1".equals(area.getParentCode())) {
                        AreaTreeVO areaTreeVO = new AreaTreeVO();
                        BeanUtils.copyProperties(areaTreeVO, area);
                        setTreeTitleAndText(areaTreeVO, area);
                        areaTreeVOS.add(areaTreeVO);

                        areaTreeVO.setChildren(new ArrayList<>());
                        areaService.setChildren(areas, areaTreeVO);
                    }
                }

                AreaTreeVO rootTreeVO = new AreaTreeVO();
                rootTreeVO.setTitle("中国");
                rootTreeVO.setCode("-1");
                rootTreeVO.setPid(-1L);
                rootTreeVO.setId(-1L);
                rootTreeVO.setText("中国");
                rootTreeVO.setChildren(areaTreeVOS);
                List<AreaTreeVO> rootAreaTreeVOS = new ArrayList<>();
                rootAreaTreeVOS.add(rootTreeVO);
                resultObjectVO.setData(rootAreaTreeVOS);
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据所有市级名称查询出所有市级对象
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryCityListByNames(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AreaVO queryAreaVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), AreaVO.class);
            if (!CollectionUtils.isEmpty(queryAreaVO.getCityNameList())) {
                queryAreaVO.setType((short) 2);
                resultObjectVO.setData(areaService.queryListByVO(queryAreaVO));
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    // ========================================================================
    //  私有工具方法
    // ========================================================================

    /**
     * 根据地区类型设置显示名称（1:省 2:市 3:区县）
     */
    private void setAreaNameByType(AreaVO areaVO, Area area) {
        if (area.getType() == 1) {
            areaVO.setName(area.getProvince());
        } else if (area.getType() == 2) {
            areaVO.setName(area.getCity());
        } else if (area.getType() == 3) {
            areaVO.setName(area.getArea());
        }
    }

    /**
     * 设置树节点的标题和文本（用于ztree/dtree等前端组件）
     */
    private void setTreeTitleAndText(AreaTreeVO treeVO, Area area) {
        if (area.getType() == 1) {
            treeVO.setTitle(area.getProvince());
            treeVO.setText(area.getProvince());
        } else if (area.getType() == 2) {
            treeVO.setTitle(area.getCity());
            treeVO.setText(area.getCity());
        } else if (area.getType() == 3) {
            treeVO.setTitle(area.getArea());
            treeVO.setText(area.getArea());
        }
    }

    /**
     * 刷新地区缓存，失败时填充错误信息到resultObjectVO
     * @return true=成功, false=失败
     */
    private boolean refreshCache(ResultObjectVO resultObjectVO) {
        try {
            initAllAreaCache();
            return true;
        } catch (Exception e) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("刷新缓存失败!");
            return false;
        }
    }

    /**
     * 收集地区VO列表中的创建人和修改人ID
     */
    private void collectAdminIds(Collection<? extends AreaVO> areaVOs, List<String> adminIds) {
        if (CollectionUtils.isEmpty(areaVOs)) {
            return;
        }
        for (AreaVO areaVO : areaVOs) {
            if (areaVO.getCreateAdminId() != null && !"-1".equals(areaVO.getCreateAdminId())
                    && !adminIds.contains(areaVO.getCreateAdminId())) {
                adminIds.add(areaVO.getCreateAdminId());
            }
            if (areaVO.getUpdateAdminId() != null && !"-1".equals(areaVO.getUpdateAdminId())
                    && !adminIds.contains(areaVO.getUpdateAdminId())) {
                adminIds.add(areaVO.getUpdateAdminId());
            }
        }
    }

    /**
     * 将符合条件的地区列表的pid设置为-1（顶级节点）
     */
    private void setTopLevelPid(List<? extends Area> areas, String code) {
        if (StringUtils.isNotEmpty(code) && !CollectionUtils.isEmpty(areas)) {
            for (Area area : areas) {
                area.setPid(-1L);
            }
        }
    }

    /**
     * 将符合条件的地区列表的pid设置为-1（通过code判断），AreaTreeVO列表版本
     */
    private void setTopLevelPidFromCode(List<Area> areas, String code) {
        if (StringUtils.isNotEmpty(code) && !CollectionUtils.isEmpty(areas)) {
            for (Area area : areas) {
                area.setPid(-1L);
            }
        }
    }

    // ========================================================================
    //  缓存初始化（拆分为多个子方法）
    // ========================================================================

    /**
     * 初始化全部地区缓存
     */
    private void initAllAreaCache() throws InvocationTargetException, IllegalAccessException {
        areaRedisService.clearAreaCache();

        Area query = new Area();
        List<Area> areas = areaService.queryList(query);
        List<AreaVO> allAreaVOs = JSONArray.parseArray(JSONObject.toJSONString(areas), AreaVO.class);

        List<AreaVO> provinces = buildProvinceCache(allAreaVOs);
        List<AreaVO> cities = buildCityCache(allAreaVOs, provinces);
        buildAreaCache(allAreaVOs, provinces, cities);
        stripFieldsRecursively(provinces);

        areaRedisService.flushFullAreaCache(provinces);
    }

    /**
     * 构建省级缓存
     */
    private List<AreaVO> buildProvinceCache(List<AreaVO> allAreaVOs) {
        List<AreaVO> provinces = new ArrayList<>();
        for (AreaVO vo : allAreaVOs) {
            if (vo.getPid() == null || vo.getPid().longValue() == -1L) {
                setAreaNameByType(vo, vo);
                provinces.add(vo);
            }
        }
        if (!CollectionUtils.isEmpty(provinces)) {
            areaRedisService.flushProvinceCache(provinces);
        }
        return provinces;
    }

    /**
     * 构建市级缓存，挂载到省级节点下
     */
    private List<AreaVO> buildCityCache(List<AreaVO> allAreaVOs, List<AreaVO> provinces) {
        List<AreaVO> allCities = new ArrayList<>();
        for (AreaVO province : provinces) {
            List<AreaVO> cities = new ArrayList<>();
            for (AreaVO vo : allAreaVOs) {
                if (isDirectChild(province.getId(), vo.getPid())) {
                    // 非直辖市才初始化地市，直辖市直接初始化区县
                    if (province.getIsMunicipality().intValue() == 0) {
                        setAreaNameByType(vo, vo);
                        cities.add(vo);
                        allCities.add(vo);
                    }
                }
            }
            province.setChildren(cities);
            if (!CollectionUtils.isEmpty(cities)) {
                areaRedisService.flushCityCache(AreaRedisKey.getCityCacheKey("ID_" + province.getId()), cities);
                areaRedisService.flushCityCache(AreaRedisKey.getCityCacheKey("CODE_" + province.getCode()), cities);
            }
        }
        return allCities;
    }

    /**
     * 构建区县级缓存，挂载到市级节点下（直辖市直接挂载到省级下）
     */
    private void buildAreaCache(List<AreaVO> allAreaVOs, List<AreaVO> provinces, List<AreaVO> allCities) {
        for (AreaVO province : provinces) {
            if (province.getIsMunicipality().intValue() == 0) {
                // 省：查找每个市下的区县
                for (AreaVO city : allCities) {
                    if (isDirectChild(province.getId(), city.getPid())) {
                        List<AreaVO> areaList = findDirectChildren(allAreaVOs, city.getId());
                        city.setChildren(areaList);
                        flushAreaCacheFor(city, areaList);
                    }
                }
            } else {
                // 直辖市：直接查找省下的区县
                List<AreaVO> areaList = findDirectChildren(allAreaVOs, province.getId());
                province.setChildren(areaList);
                flushAreaCacheFor(province, areaList);
            }
        }
    }

    /**
     * 判断childPid是否是parentId的直接子节点
     */
    private boolean isDirectChild(Long parentId, Long childPid) {
        return parentId != null && childPid != null && parentId.longValue() == childPid.longValue();
    }

    /**
     * 在allAreaVOs中查找parentId的所有直接子节点
     */
    private List<AreaVO> findDirectChildren(List<AreaVO> allAreaVOs, Long parentId) {
        List<AreaVO> children = new ArrayList<>();
        for (AreaVO vo : allAreaVOs) {
            if (isDirectChild(parentId, vo.getPid())) {
                setAreaNameByType(vo, vo);
                children.add(vo);
            }
        }
        return children;
    }

    /**
     * 刷新指定节点的区县缓存
     */
    private void flushAreaCacheFor(AreaVO parent, List<AreaVO> areaList) {
        if (!CollectionUtils.isEmpty(areaList)) {
            areaRedisService.flushAreaCache(AreaRedisKey.getAreaCacheKey("ID_" + parent.getId()), areaList);
            areaRedisService.flushAreaCache(AreaRedisKey.getAreaCacheKey("CODE_" + parent.getCode()), areaList);
        }
    }

    /**
     * 递归清除缓存对象中的冗余字段，仅保留核心字段
     */
    private void stripFieldsRecursively(List<AreaVO> areaVOs) {
        for (AreaVO vo : areaVOs) {
            stripAreaVOFields(vo);
            if (!CollectionUtils.isEmpty(vo.getChildren())) {
                for (AreaVO child : (List<AreaVO>) vo.getChildren()) {
                    stripAreaVOFields(child);
                    if (!CollectionUtils.isEmpty(child.getChildren())) {
                        for (AreaVO grandchild : (List<AreaVO>) child.getChildren()) {
                            stripAreaVOFields(grandchild);
                        }
                    }
                }
            }
        }
    }

    /**
     * 清除AreaVO中不需要缓存到Redis的字段
     */
    private void stripAreaVOFields(AreaVO vo) {
        vo.setPid(null);
        vo.setAppCode(null);
        vo.setParentName(null);
        vo.setCreateAdminName(null);
        vo.setCreateDate(null);
        vo.setCreateAdminId(null);
        vo.setUpdateAdminName(null);
        vo.setUpdateAdminId(null);
        vo.setUpdateDate(null);
        vo.setDeleteStatus(null);
        vo.setCodeArray(null);
        vo.setArea(null);
        vo.setAreaSort(null);
        vo.setCity(null);
        vo.setProvince(null);
        vo.setRemark(null);
    }

}
