package com.toucan.shopping.cloud.apps.admin.controller.column;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.common.data.api.AreaServiceAPI;
import com.toucan.shopping.cloud.content.api.ColumnAreaServiceAPI;
import com.toucan.shopping.cloud.content.api.IndexRecommendColumnServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.area.vo.AreaTreeVO;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.column.constant.PcIndexColumnConstant;
import com.toucan.shopping.modules.column.entity.ColumnArea;
import com.toucan.shopping.modules.column.page.ColumnPageInfo;
import com.toucan.shopping.modules.column.vo.*;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 首页推荐栏目
 */
@RestController
@RequestMapping("/column/indexRecommendColumn")
public class IndexRecommendColumnController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private IndexRecommendColumnServiceAPI indexRecommendColumnService;

    @Autowired
    private AdminServiceAPI adminServiceAPI;

    @Autowired
    private AreaServiceAPI areaService;

    @Autowired
    private ColumnAreaServiceAPI columnAreaService;

    @Autowired
    private ImageUploadService imageUploadService;


    /**
     * 查询列表
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:column:list"})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public TableVO list(HttpServletRequest request, ColumnPageInfo pageInfo) {
        TableVO tableVO = new TableVO();
        try {
            pageInfo.setAppCode(toucan.getShoppingPC().getAppCode());
            pageInfo.setColumnTypeCode(PcIndexColumnConstant.INDEX_PRODUCT_RECOMMENT_COLUMN_TYPE_CODE);
            pageInfo.setPosition("1");

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            ResultObjectVO resultObjectVO = indexRecommendColumnService.queryListPage(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    Map<String, Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total") != null ? resultObjectDataMap.get("total") : "0")));
                    List<ColumnVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), ColumnVO.class);

                    // 查询创建人和修改人
                    List<String> adminIdList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        ColumnVO columnVO = list.get(i);
                        if (columnVO.getCreateAdminId() != null) {
                            adminIdList.add(columnVO.getCreateAdminId());
                        }
                        if (columnVO.getUpdateAdminId() != null) {
                            adminIdList.add(columnVO.getUpdateAdminId());
                        }
                    }
                    String[] createOrUpdateAdminIds = new String[adminIdList.size()];
                    adminIdList.toArray(createOrUpdateAdminIds);
                    AdminVO queryAdminVO = new AdminVO();
                    queryAdminVO.setAdminIds(createOrUpdateAdminIds);
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAdminVO);
                    resultObjectVO = adminServiceAPI.queryListByEntity(requestJsonVO);
                    if (resultObjectVO.isSuccess()) {
                        List<AdminVO> adminVOS = resultObjectVO.formatDataList(AdminVO.class);
                        if (!CollectionUtils.isEmpty(adminVOS)) {
                            for (ColumnVO columnVO : list) {
                                for (AdminVO adminVO : adminVOS) {
                                    if (columnVO.getCreateAdminId() != null && columnVO.getCreateAdminId().equals(adminVO.getAdminId())) {
                                        columnVO.setCreateAdminName(adminVO.getUsername());
                                    }
                                    if (columnVO.getUpdateAdminId() != null && columnVO.getUpdateAdminId().equals(adminVO.getAdminId())) {
                                        columnVO.setUpdateAdminName(adminVO.getUsername());
                                    }
                                }
                            }
                        }
                    }

                    if (tableVO.getCount() > 0) {
                        tableVO.setData((List) list);
                    }
                }
            }
        } catch (Exception e) {
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return tableVO;
    }


    /**
     * 保存
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:column:column:add"})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultObjectVO save(HttpServletRequest request, @RequestBody PcIndexColumnVO indexRecommendColumnVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            indexRecommendColumnVO.setAppCode(toucan.getShoppingPC().getAppCode());
            indexRecommendColumnVO.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            indexRecommendColumnVO.setPosition("1");
            indexRecommendColumnVO.setColumnTypeCode(PcIndexColumnConstant.INDEX_PRODUCT_RECOMMENT_COLUMN_TYPE_CODE);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, indexRecommendColumnVO);
            resultObjectVO = indexRecommendColumnService.save(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请稍后重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 修改
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:column:pc:update"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultObjectVO update(HttpServletRequest request, @RequestBody PcIndexColumnVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setAppCode(toucan.getShoppingPC().getAppCode());
            entity.setColumnTypeCode(PcIndexColumnConstant.INDEX_PRODUCT_RECOMMENT_COLUMN_TYPE_CODE);
            entity.setPosition("1");
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = indexRecommendColumnService.update(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询详情
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:column:pc:findById"})
    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public ResultObjectVO findById(HttpServletRequest request, @RequestBody PcIndexColumnVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setAppCode(toucan.getShoppingPC().getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = indexRecommendColumnService.findById(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                PcIndexColumnVO indexRecommendColumnVO = resultObjectVO.formatData(PcIndexColumnVO.class);

                // 右侧顶部预览图
                indexRecommendColumnVO.getRightTopBanner().setHttpImgPath(imageUploadService.getImageHttpPrefix() + indexRecommendColumnVO.getRightTopBanner().getImgPath());
                // 右侧底部预览图
                indexRecommendColumnVO.getRightBottomBanner().setHttpImgPath(imageUploadService.getImageHttpPrefix() + indexRecommendColumnVO.getRightBottomBanner().getImgPath());
                // 左侧轮播图
                for (ColumnBannerVO columnBannerVO : indexRecommendColumnVO.getColumnLeftBannerVOS()) {
                    columnBannerVO.setHttpImgPath(imageUploadService.getImageHttpPrefix() + columnBannerVO.getImgPath());
                }

                // 顶部预览图
                if (indexRecommendColumnVO.getTopBanner() != null && indexRecommendColumnVO.getTopBanner().getImgPath() != null) {
                    indexRecommendColumnVO.getTopBanner().setHttpImgPath(imageUploadService.getImageHttpPrefix() + indexRecommendColumnVO.getTopBanner().getImgPath());
                }

                // 底部预览图
                if (indexRecommendColumnVO.getBottomBanner() != null && indexRecommendColumnVO.getBottomBanner().getImgPath() != null) {
                    indexRecommendColumnVO.getBottomBanner().setHttpImgPath(imageUploadService.getImageHttpPrefix() + indexRecommendColumnVO.getBottomBanner().getImgPath());
                }

                // 商品推荐图
                if (!CollectionUtils.isEmpty(indexRecommendColumnVO.getColumnRecommendProducts())) {
                    for (ColumnRecommendProductVO columnRecommendProductVO : indexRecommendColumnVO.getColumnRecommendProducts()) {
                        columnRecommendProductVO.setHttpImgPath(imageUploadService.getImageHttpPrefix() + columnRecommendProductVO.getImgPath());
                    }
                }

                resultObjectVO.setData(indexRecommendColumnVO);
            }
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 删除
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:column:pc:index:column:delete"})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody ColumnVO columnVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (columnVO.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            String entityJson = JSONObject.toJSONString(columnVO);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);

            resultObjectVO = indexRecommendColumnService.deleteById(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询地区树
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:column:column:area:tree"})
    @RequestMapping(value = "/query/area/tree", method = RequestMethod.POST)
    public ResultObjectVO queryAreaTree(HttpServletRequest request, String columnId) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            // 查询地区树
            AreaVO query = new AreaVO();
            query.setAppCode(toucan.getShoppingPC().getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), query);

            resultObjectVO = areaService.queryTree(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<AreaTreeVO> areaTreeVOList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), AreaTreeVO.class);

                // 重新设置ID,由于这个树是多个表合并而成,可能会存在ID重复
                AtomicLong id = new AtomicLong();
                ColumnAreaVO queryBannerAreaVo = new ColumnAreaVO();
                if (StringUtils.isNotEmpty(columnId)) {
                    queryBannerAreaVo.setColumnId(Long.parseLong(columnId));
                }
                requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryBannerAreaVo);

                resultObjectVO = columnAreaService.queryColumnAreaList(requestJsonVO);
                List<AreaTreeVO> releaseAreaTreeVOList = new ArrayList<>();
                if (resultObjectVO.isSuccess()) {
                    // 只保留省市节点
                    AreaTreeVO rootTree = areaTreeVOList.get(0);
                    if (!CollectionUtils.isEmpty(rootTree.getChildren())) {
                        List<AreaTreeVO> rootChilren = JSONArray.parseArray(JSONObject.toJSONString(rootTree.getChildren()), AreaTreeVO.class);
                        for (AreaTreeVO areaTreeVO : rootChilren) {
                            // 直辖市
                            if (areaTreeVO.getIsMunicipality().shortValue() == 1) {
                                areaTreeVO.setChildren(null);
                            } else { // 省
                                // 遍历所有市节点,删除区县节点
                                if (!CollectionUtils.isEmpty(areaTreeVO.getChildren())) {
                                    List<AreaTreeVO> chilren = JSONArray.parseArray(JSONObject.toJSONString(areaTreeVO.getChildren()), AreaTreeVO.class);
                                    for (AreaVO cityTreeVO : chilren) {
                                        cityTreeVO.setChildren(null);
                                    }
                                    areaTreeVO.setChildren(chilren);
                                }
                            }
                        }
                        rootTree.setChildren(rootChilren);
                    }
                    releaseAreaTreeVOList.add(rootTree);
                    List<ColumnArea> columnAreas = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), ColumnArea.class);
                    if (!CollectionUtils.isEmpty(columnAreas)) {
                        for (AreaTreeVO areaTreeVO : releaseAreaTreeVOList) {
                            areaTreeVO.setId(id.incrementAndGet());
                            areaTreeVO.setNodeId(areaTreeVO.getId());
                            areaTreeVO.setText(areaTreeVO.getTitle());
                            for (ColumnArea columnArea : columnAreas) {
                                if (areaTreeVO.getCode().equals(columnArea.getAreaCode())) {
                                    areaTreeVO.getState().setChecked(true);
                                }
                            }
                            setTreeNodeSelect(id, areaTreeVO, (List) areaTreeVO.getChildren(), columnAreas);
                        }
                    }
                }
                resultObjectVO.setData(releaseAreaTreeVOList);
            }
            return resultObjectVO;
        } catch (Exception e) {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 递归设置树节点选中状态
     */
    private void setTreeNodeSelect(AtomicLong id, AreaTreeVO parentTreeVO, List<AreaTreeVO> areaTreeVOList, List<ColumnArea> columnAreas) {
        for (AreaTreeVO areaTreeVO : areaTreeVOList) {
            areaTreeVO.setId(id.incrementAndGet());
            areaTreeVO.setNodeId(areaTreeVO.getId());
            areaTreeVO.setPid(parentTreeVO.getId());
            areaTreeVO.setParentId(areaTreeVO.getPid());
            for (ColumnArea columnArea : columnAreas) {
                if (areaTreeVO.getCode().equals(columnArea.getAreaCode())) {
                    areaTreeVO.getState().setChecked(true);
                    break;
                }
            }
            if (!CollectionUtils.isEmpty(areaTreeVO.getChildren())) {
                setTreeNodeSelect(id, areaTreeVO, (List) areaTreeVO.getChildren(), columnAreas);
            }
        }
    }

}
