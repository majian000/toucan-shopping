package com.toucan.shopping.cloud.apps.admin.controller.column;


import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.DictServiceAPI;
import com.toucan.shopping.cloud.content.api.ColumnServiceAPI;
import com.toucan.shopping.cloud.content.api.ColumnTypeServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.column.constant.ColumnDictConstant;
import com.toucan.shopping.modules.column.entity.Column;
import com.toucan.shopping.modules.column.page.ColumnPageInfo;
import com.toucan.shopping.modules.column.vo.*;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AlphabetNumberUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 首页推荐栏目
 */
@RestController
@RequestMapping("/column")
public class ColumnController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ColumnServiceAPI columnService;

    @Autowired
    private AdminServiceAPI adminServiceAPI;

    @Autowired
    private ColumnTypeServiceAPI columnTypeService;

    @Autowired
    private DictServiceAPI dictServiceAPI;


    /**
     * 获取栏目字典数据
     */
    private Map<String, List<DictVO>> getColumnDictMap() throws NoSuchAlgorithmException {
        Map<String, List<DictVO>> result = new HashMap<>();
        DictVO queryDict = new DictVO();
        queryDict.setCategoryCode(ColumnDictConstant.COLUMN_DICT_CATEGORY_CODE);
        queryDict.setCodes(new LinkedList<>());
        queryDict.getCodes().add(ColumnDictConstant.COLUMN_DICT_TYPE_CODE);
        queryDict.getCodes().add(ColumnDictConstant.COLUMN_DICT_POSITION_CODE);
        queryDict.setAppCode(toucan.getAppCode());
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryDict);
        ResultTypeObjectVO<List<DictVO>> resultObjectVO = dictServiceAPI.queryDictByCodesAndCategoryCode(requestJsonVO);
        if (resultObjectVO.isSuccess() && !CollectionUtils.isEmpty(resultObjectVO.getData())) {
            for (DictVO dictVO : resultObjectVO.getData()) {
                switch (dictVO.getCode()) {
                    case ColumnDictConstant.COLUMN_DICT_TYPE_CODE:
                        result.put("columnTypeList", dictVO.getChildren());
                        break;
                    case ColumnDictConstant.COLUMN_DICT_POSITION_CODE:
                        result.put("columnPositionList", dictVO.getChildren());
                        break;
                }
            }
        }
        return result;
    }


    /**
     * 保存
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:column:add"})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultObjectVO save(HttpServletRequest request, @RequestBody ColumnVO columnVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (StringUtils.isEmpty(columnVO.getColumnTypeCode())) {
                resultObjectVO.setMsg("栏目类型不能为空");
                resultObjectVO.setCode(TableVO.FAILD);
                return resultObjectVO;
            }

            if (!AlphabetNumberUtils.isAlphabetNumber(columnVO.getCode(), 1, 100)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("保存失败,编码只允许字母、数字、下划线组成,长度1-100位");
                return resultObjectVO;
            }

            columnVO.setAppCode(toucan.getShoppingPC().getAppCode());
            columnVO.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, columnVO);
            resultObjectVO = columnService.save(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:column:update"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultObjectVO update(HttpServletRequest request, @RequestBody ColumnVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (StringUtils.isEmpty(entity.getCode())) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,请输入编码");
                return resultObjectVO;
            }
            if (!AlphabetNumberUtils.isAlphabetNumber(entity.getCode(), 1, 100)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,编码只允许字母、数字、下划线组成,长度1-100位");
                return resultObjectVO;
            }

            entity.setAppCode(toucan.getShoppingPC().getAppCode());
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = columnService.update(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:column:delete"})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody ColumnVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (entity.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = columnService.deleteById(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 批量删除
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:column:deletes"})
    @RequestMapping(value = "/delete/ids", method = RequestMethod.POST)
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<Column> columns) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (CollectionUtils.isEmpty(columns)) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode, columns);
            resultObjectVO = columnService.deleteByIds(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询栏目类型列表
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:column:type:list"})
    @RequestMapping(value = "/query/type/list", method = RequestMethod.POST)
    public ResultObjectVO queryColumnTypeList(@RequestParam(defaultValue = "-1") Long id) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ColumnTypeVO query = new ColumnTypeVO();
            query.setAppCode(toucan.getShoppingPC().getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
            resultObjectVO = columnTypeService.queryList(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    List<ColumnTypeTreeVO> columnTypeTreeVOS = resultObjectVO.formatDataList(ColumnTypeTreeVO.class);
                    for (ColumnTypeTreeVO columnTypeTreeVO : columnTypeTreeVOS) {
                        columnTypeTreeVO.setOpen(false);
                        columnTypeTreeVO.setIcon(null);
                    }
                    resultObjectVO.setData(columnTypeTreeVOS);
                }
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
     * 查询栏目树
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:column:tree"})
    @RequestMapping(value = "/query/column/tree", method = RequestMethod.POST)
    public ResultObjectVO queryColumnTree(HttpServletRequest request, ColumnTreeVO queryColumnTreeVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            // 默认查询根节点
            if (queryColumnTreeVO.getId() == null) {
                ColumnTreeVO columnTreeVO = new ColumnTreeVO();
                columnTreeVO.setId(-1L);
                columnTreeVO.setPid(-2L);
                columnTreeVO.setParentId(-2L);
                columnTreeVO.setAppCode(toucan.getShoppingPC().getAppCode());
                columnTreeVO.setTitle("根节点");
                columnTreeVO.setName("根节点");
                columnTreeVO.setColumnTypeCode(queryColumnTreeVO.getColumnTypeCode());
                columnTreeVO.setIsParent(true);
                List<ColumnTreeVO> columnTrees = new LinkedList<>();
                columnTrees.add(columnTreeVO);
                resultObjectVO.setData(columnTrees);
            } else {
                queryColumnTreeVO.setParentId(queryColumnTreeVO.getId());
                queryColumnTreeVO.setAppCode(toucan.getShoppingPC().getAppCode());
                RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryColumnTreeVO);
                resultObjectVO = columnService.queryColumnTreeByPid(requestJsonVO);
                return resultObjectVO;
            }
        } catch (Exception e) {
            resultObjectVO.setMsg("请求失败");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询树表格（按父ID）
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:content:column:list"})
    @RequestMapping(value = "/tree/table/by/pid", method = RequestMethod.POST)
    public ResultObjectVO queryTreeTableByPid(HttpServletRequest request, ColumnPageInfo pageInfo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (StringUtils.isEmpty(pageInfo.getColumnTypeCode())) {
                resultObjectVO.setMsg("栏目类型编码不能为空");
                resultObjectVO.setCode(TableVO.FAILD);
                return resultObjectVO;
            }

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            resultObjectVO = columnService.queryTreeTableByPid(requestJsonVO);

            if (resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    Set<String> adminIdList = new HashSet<>();
                    List<ColumnTreeVO> columnTreeVOS = resultObjectVO.formatDataList(ColumnTreeVO.class);
                    if (!CollectionUtils.isEmpty(columnTreeVOS)) {
                        Map<String, List<DictVO>> dictMap = this.getColumnDictMap();
                        List<DictVO> columnTypeList = dictMap.get("columnTypeList");
                        List<DictVO> columnPositionList = dictMap.get("columnPositionList");

                        Map<String, DictVO> columnTypeMap = null;
                        if (columnTypeList != null) {
                            columnTypeMap = columnTypeList.stream()
                                    .collect(Collectors.toMap(DictVO::getCode, dict -> dict));
                        }

                        Map<String, DictVO> columnPositionMap = null;
                        if (columnPositionList != null) {
                            columnPositionMap = columnPositionList.stream()
                                    .collect(Collectors.toMap(DictVO::getCode, dict -> dict));
                        }
                        for (ColumnTreeVO columnTreeVO : columnTreeVOS) {
                            if (columnTreeVO.getCreateAdminId() != null) {
                                adminIdList.add(columnTreeVO.getCreateAdminId());
                            }
                            if (columnTreeVO.getUpdateAdminId() != null) {
                                adminIdList.add(columnTreeVO.getUpdateAdminId());
                            }
                            // 设置栏目类型名称
                            if (StringUtils.isNotEmpty(columnTreeVO.getType())) {
                                if (columnTypeMap != null) {
                                    String[] types = columnTreeVO.getType().split(",");
                                    String typeNames = "";
                                    for (int i = 0; i < types.length; i++) {
                                        String type = types[i];
                                        DictVO dictVO = columnTypeMap.get(type);
                                        if (dictVO != null) {
                                            typeNames += dictVO.getName();
                                        }
                                        if ((i + 1) < types.length) {
                                            typeNames += ",";
                                        }
                                    }
                                    columnTreeVO.setTypeNames(typeNames);
                                }
                            }
                            // 设置栏目位置
                            if (StringUtils.isNotEmpty(columnTreeVO.getPosition())) {
                                if (columnPositionMap != null) {
                                    String[] positions = columnTreeVO.getPosition().split(",");
                                    String positionNames = "";
                                    for (int i = 0; i < positions.length; i++) {
                                        String position = positions[i];
                                        DictVO dictVO = columnPositionMap.get(position);
                                        if (dictVO != null) {
                                            positionNames += dictVO.getName();
                                        }
                                        if ((i + 1) < positions.length) {
                                            positionNames += ",";
                                        }
                                    }
                                    columnTreeVO.setPositionNames(positionNames);
                                }
                            }
                        }
                        this.setAdminNames(adminIdList, columnTreeVOS);
                        resultObjectVO.setData(columnTreeVOS);
                    }
                }
            }
            return resultObjectVO;
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 设置管理员名称
     */
    private void setAdminNames(Set<String> adminIdList, List<ColumnTreeVO> list) throws Exception {
        // 查询创建人和修改人
        String[] createOrUpdateAdminIds = new String[adminIdList.size()];
        adminIdList.toArray(createOrUpdateAdminIds);
        AdminVO queryAdminVO = new AdminVO();
        queryAdminVO.setAdminIds(createOrUpdateAdminIds);
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAdminVO);
        ResultObjectVO resultObjectVO = adminServiceAPI.queryListByEntity(requestJsonVO);
        if (resultObjectVO.isSuccess()) {
            List<AdminVO> adminVOS = resultObjectVO.formatDataList(AdminVO.class);
            if (!CollectionUtils.isEmpty(adminVOS)) {
                for (ColumnVO dictVO : list) {
                    for (AdminVO adminVO : adminVOS) {
                        if (dictVO.getCreateAdminId() != null && dictVO.getCreateAdminId().equals(adminVO.getAdminId())) {
                            dictVO.setCreateAdminName(adminVO.getUsername());
                        }
                        if (dictVO.getUpdateAdminId() != null && dictVO.getUpdateAdminId().equals(adminVO.getAdminId())) {
                            dictVO.setUpdateAdminName(adminVO.getUsername());
                        }
                    }
                }
            }
        }
    }

}
