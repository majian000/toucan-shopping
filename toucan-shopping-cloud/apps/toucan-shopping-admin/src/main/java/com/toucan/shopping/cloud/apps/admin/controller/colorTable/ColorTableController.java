package com.toucan.shopping.cloud.apps.admin.controller.colorTable;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.common.data.api.ColorTableServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.color.table.page.ColorTablePageInfo;
import com.toucan.shopping.modules.color.table.vo.ColorTableVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.content.entity.Banner;
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
 * 颜色表管理
 */
@RestController
@RequestMapping("/colorTable")
public class ColorTableController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ColorTableServiceAPI colorTableService;

    @Autowired
    private AdminServiceAPI adminServiceAPI;


    /**
     * 查询列表
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:colorTable:list:api"})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public TableVO list(HttpServletRequest request, ColorTablePageInfo pageInfo) {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            ResultObjectVO resultObjectVO = colorTableService.queryListPage(requestJsonVO);
            if (resultObjectVO.getCode() == ResultObjectVO.SUCCESS) {
                if (resultObjectVO.getData() != null) {
                    Map<String, Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total") != null ? resultObjectDataMap.get("total") : "0")));
                    List<ColorTableVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), ColorTableVO.class);

                    // 查询创建人和修改人
                    List<String> adminIdList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        ColorTableVO colorTableVO = list.get(i);
                        if (colorTableVO.getCreateAdminId() != null) {
                            adminIdList.add(colorTableVO.getCreateAdminId());
                        }
                        if (colorTableVO.getUpdateAdminId() != null) {
                            adminIdList.add(colorTableVO.getUpdateAdminId());
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
                            for (ColorTableVO colorTableVO : list) {
                                for (AdminVO adminVO : adminVOS) {
                                    if (colorTableVO.getCreateAdminId() != null && colorTableVO.getCreateAdminId().equals(adminVO.getAdminId())) {
                                        colorTableVO.setCreateAdminName(adminVO.getUsername());
                                    }
                                    if (colorTableVO.getUpdateAdminId() != null && colorTableVO.getUpdateAdminId().equals(adminVO.getAdminId())) {
                                        colorTableVO.setUpdateAdminName(adminVO.getUsername());
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:colorTable:save:api"})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultObjectVO save(HttpServletRequest request, @RequestBody ColorTableVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = colorTableService.save(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:colorTable:update:api"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultObjectVO update(HttpServletRequest request, @RequestBody ColorTableVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = colorTableService.update(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:colorTable:delete:api"})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody Banner banner) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (banner.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            banner.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());

            String entityJson = JSONObject.toJSONString(banner);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = colorTableService.deleteById(requestVo);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"shopping:colorTable:deletes:api"})
    @RequestMapping(value = "/delete/ids", method = RequestMethod.POST)
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<ColorTableVO> colorTableVOS) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (CollectionUtils.isEmpty(colorTableVOS)) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            String entityJson = JSONObject.toJSONString(colorTableVOS);
            RequestJsonVO requestVo = new RequestJsonVO();
            requestVo.setAppCode(appCode);
            requestVo.setEntityJson(entityJson);
            resultObjectVO = colorTableService.deleteByIds(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

}
