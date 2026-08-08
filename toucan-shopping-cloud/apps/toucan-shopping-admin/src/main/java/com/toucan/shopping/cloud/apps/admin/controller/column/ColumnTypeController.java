package com.toucan.shopping.cloud.apps.admin.controller.column;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.content.api.ColumnTypeServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.column.page.ColumnTypePageInfo;
import com.toucan.shopping.modules.column.vo.ColumnTypeVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AlphabetNumberUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 栏目类型管理
 */
@RestController
@RequestMapping("/column/columnType")
public class ColumnTypeController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ColumnTypeServiceAPI columnTypeService;

    @Autowired
    private AdminServiceAPI adminServiceAPI;


    /**
     * 保存
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:column:columnType:add:api"})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultObjectVO save(HttpServletRequest request, @RequestBody ColumnTypeVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (StringUtils.isEmpty(entity.getCode())) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,请输入编码");
                return resultObjectVO;
            }
            if (!AlphabetNumberUtils.isAlphabetNumber(entity.getCode(), 1, 50)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("添加失败,编码只允许字母、数字、下划线组成,长度1-50位");
                return resultObjectVO;
            }

            entity.setAppCode(toucan.getShoppingPC().getAppCode());
            entity.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = columnTypeService.save(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 修改
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:column:columnType:update:api"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultObjectVO update(HttpServletRequest request, @RequestBody ColumnTypeVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (StringUtils.isEmpty(entity.getCode())) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,请输入编码");
                return resultObjectVO;
            }
            if (!AlphabetNumberUtils.isAlphabetNumber(entity.getCode(), 1, 50)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("修改失败,编码只允许字母、数字、下划线组成,长度1-50位");
                return resultObjectVO;
            }

            entity.setAppCode(toucan.getShoppingPC().getAppCode());
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = columnTypeService.update(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询列表
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:column:columnType:list:api"})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public TableVO list(HttpServletRequest request, ColumnTypePageInfo pageInfo) {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            ResultObjectVO resultObjectVO = columnTypeService.queryListPage(requestJsonVO);
            if (resultObjectVO.getCode() == ResultObjectVO.SUCCESS) {
                if (resultObjectVO.getData() != null) {
                    Map<String, Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total") != null ? resultObjectDataMap.get("total") : "0")));
                    List<ColumnTypeVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), ColumnTypeVO.class);

                    // 查询创建人和修改人
                    List<String> adminIdList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        ColumnTypeVO columnTypeVO = list.get(i);
                        if (columnTypeVO.getCreateAdminId() != null) {
                            adminIdList.add(columnTypeVO.getCreateAdminId());
                        }
                        if (columnTypeVO.getUpdateAdminId() != null) {
                            adminIdList.add(columnTypeVO.getUpdateAdminId());
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
                            for (ColumnTypeVO columnTypeVO : list) {
                                for (AdminVO adminVO : adminVOS) {
                                    if (columnTypeVO.getCreateAdminId() != null && columnTypeVO.getCreateAdminId().equals(adminVO.getAdminId())) {
                                        columnTypeVO.setCreateAdminName(adminVO.getUsername());
                                    }
                                    if (columnTypeVO.getUpdateAdminId() != null && columnTypeVO.getUpdateAdminId().equals(adminVO.getAdminId())) {
                                        columnTypeVO.setUpdateAdminName(adminVO.getUsername());
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
     * 删除
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:column:columnType:delete:api"})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody ColumnTypeVO columnTypeVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (columnTypeVO.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            String entityJson = JSONObject.toJSONString(columnTypeVO);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = columnTypeService.deleteById(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

}
