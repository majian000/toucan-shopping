package com.toucan.shopping.cloud.apps.admin.controller.column;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.DictServiceAPI;
import com.toucan.shopping.cloud.common.data.api.AreaServiceAPI;
import com.toucan.shopping.cloud.content.api.ColumnAreaServiceAPI;
import com.toucan.shopping.cloud.content.api.IndexRecommendColumnServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.area.vo.AreaTreeVO;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.column.constant.ColumnDictConstant;
import com.toucan.shopping.modules.column.constant.PcIndexColumnConstant;
import com.toucan.shopping.modules.column.entity.ColumnArea;
import com.toucan.shopping.modules.column.page.ColumnPageInfo;
import com.toucan.shopping.modules.column.vo.*;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
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
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
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

    @Autowired
    private DictServiceAPI dictServiceAPI;


    /**
     * 查询列表
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:column:list"})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public TableVO list(HttpServletRequest request, @RequestBody ColumnPageInfo pageInfo) {
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
                    List<IndexRecommendColumnAdminVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), IndexRecommendColumnAdminVO.class);

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

                    // 填充栏目类型字典对象（参考栏目列表）
                    Map<String, DictVO> columnTypeMap = getColumnTypeDictMap();
                    for (IndexRecommendColumnAdminVO columnVO : list) {
                        if (StringUtils.isNotEmpty(columnVO.getType())) {
                            DictVO dictVO = columnTypeMap.get(columnVO.getType());
                            if (dictVO != null) {
                                List<DictVO> typeDictVos = new LinkedList<>();
                                typeDictVos.add(dictVO);
                                columnVO.setTypeDictVos(typeDictVos);
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
     * 查询栏目类型字典（code -> DictVO）
     */
    private Map<String, DictVO> getColumnTypeDictMap() throws Exception {
        Map<String, DictVO> result = new HashMap<>();
        DictVO queryDict = new DictVO();
        queryDict.setCategoryCode(ColumnDictConstant.COLUMN_DICT_CATEGORY_CODE);
        queryDict.setCodes(new LinkedList<>());
        queryDict.getCodes().add(ColumnDictConstant.COLUMN_DICT_TYPE_CODE);
        queryDict.setAppCode(toucan.getAppCode());
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryDict);
        ResultTypeObjectVO<List<DictVO>> resultObjectVO = dictServiceAPI.queryDictByCodesAndCategoryCode(requestJsonVO);
        if (resultObjectVO.isSuccess() && !CollectionUtils.isEmpty(resultObjectVO.getData())) {
            for (DictVO dictVO : resultObjectVO.getData()) {
                if (ColumnDictConstant.COLUMN_DICT_TYPE_CODE.equals(dictVO.getCode()) && !CollectionUtils.isEmpty(dictVO.getChildren())) {
                    for (DictVO child : dictVO.getChildren()) {
                        result.put(child.getCode(), child);
                    }
                }
            }
        }
        return result;
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
            // 图片base64统一上传到文件服务
            uploadBannerImages(indexRecommendColumnVO);
            uploadProductImages(indexRecommendColumnVO);
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
            // 图片base64统一上传到文件服务
            uploadBannerImages(entity);
            uploadProductImages(entity);
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

                // 顶部预览图
                fillBannerImage(indexRecommendColumnVO.getTopBanner());
                // 右侧顶部预览图
                fillBannerImage(indexRecommendColumnVO.getRightTopBanner());
                // 右侧底部预览图
                fillBannerImage(indexRecommendColumnVO.getRightBottomBanner());
                // 底部预览图
                fillBannerImage(indexRecommendColumnVO.getBottomBanner());
                // 左侧轮播图
                if (!CollectionUtils.isEmpty(indexRecommendColumnVO.getColumnLeftBannerVOS())) {
                    for (ColumnBannerVO columnBannerVO : indexRecommendColumnVO.getColumnLeftBannerVOS()) {
                        fillBannerImage(columnBannerVO);
                    }
                }
                // 商品推荐图
                if (!CollectionUtils.isEmpty(indexRecommendColumnVO.getColumnRecommendProducts())) {
                    for (ColumnRecommendProductVO columnRecommendProductVO : indexRecommendColumnVO.getColumnRecommendProducts()) {
                        fillProductImage(columnRecommendProductVO);
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
     * 将栏目VO中所有轮播图/图片的base64数据统一上传到文件服务，写入imgPath
     */
    private void uploadBannerImages(PcIndexColumnVO vo) throws Exception {
        uploadBannerBase64(vo.getTopBanner());
        uploadBannerBase64(vo.getRightTopBanner());
        uploadBannerBase64(vo.getRightBottomBanner());
        uploadBannerBase64(vo.getBottomBanner());
        if (!CollectionUtils.isEmpty(vo.getColumnLeftBannerVOS())) {
            for (ColumnBannerVO banner : vo.getColumnLeftBannerVOS()) {
                uploadBannerBase64(banner);
            }
        }
    }

    private void uploadBannerBase64(ColumnBannerVO banner) throws Exception {
        if (banner != null && StringUtils.isNotEmpty(banner.getImgBase64())) {
            banner.setImgPath(imageUploadService.uploadBase64(banner.getImgBase64()));
        }
    }

    /**
     * 将栏目VO中所有推荐商品图片的base64数据统一上传到文件服务，写入imgPath
     */
    private void uploadProductImages(PcIndexColumnVO vo) throws Exception {
        if (!CollectionUtils.isEmpty(vo.getColumnRecommendProducts())) {
            for (ColumnRecommendProductVO product : vo.getColumnRecommendProducts()) {
                if (product != null && StringUtils.isNotEmpty(product.getImgBase64())) {
                    product.setImgPath(imageUploadService.uploadBase64(product.getImgBase64()));
                }
            }
        }
    }

    /**
     * 为轮播图填充外网访问地址与base64数据（编辑回显用）
     */
    private void fillBannerImage(ColumnBannerVO banner) {
        if (banner != null && StringUtils.isNotEmpty(banner.getImgPath())) {
            banner.setHttpImgPath(imageUploadService.getImageHttpPrefix() + banner.getImgPath());
            banner.setImgBase64(imgPathToBase64(banner.getImgPath()));
        }
    }

    /**
     * 为推荐商品填充外网访问地址与base64数据（编辑回显用）
     */
    private void fillProductImage(ColumnRecommendProductVO product) {
        if (product != null && StringUtils.isNotEmpty(product.getImgPath())) {
            product.setHttpImgPath(imageUploadService.getImageHttpPrefix() + product.getImgPath());
            product.setImgBase64(imgPathToBase64(product.getImgPath()));
        }
    }

    /**
     * 下载文件服务图片并转成base64数据地址
     */
    private String imgPathToBase64(String imgPath) {
        try {
            byte[] fileBytes = imageUploadService.downloadFile(imgPath);
            if (fileBytes == null || fileBytes.length == 0) {
                return null;
            }
            String ext = "jpg";
            if (imgPath.contains(".")) {
                ext = imgPath.substring(imgPath.lastIndexOf(".") + 1).toLowerCase();
            }
            String mime;
            switch (ext) {
                case "png": mime = "image/png"; break;
                case "gif": mime = "image/gif"; break;
                case "bmp": mime = "image/bmp"; break;
                case "jpeg": mime = "image/jpeg"; break;
                case "jpg": mime = "image/jpeg"; break;
                default: mime = "image/jpeg"; break;
            }
            return "data:" + mime + ";base64," + Base64.getEncoder().encodeToString(fileBytes);
        } catch (Exception e) {
            logger.warn("下载图片转base64失败 {}", imgPath, e);
            return null;
        }
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
