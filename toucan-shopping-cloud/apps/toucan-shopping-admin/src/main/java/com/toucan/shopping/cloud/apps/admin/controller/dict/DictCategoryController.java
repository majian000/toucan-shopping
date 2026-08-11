package com.toucan.shopping.cloud.apps.admin.controller.dict;


import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.AppServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.DictCategoryServiceAPI;
import com.toucan.shopping.modules.admin.auth.entity.DictCategory;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.page.DictCategoryPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import com.toucan.shopping.modules.admin.auth.vo.DictCategoryVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/dictCategory")
public class DictCategoryController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private DictCategoryServiceAPI dictCategoryServiceAPI;

    @Autowired
    private AppServiceAPI appServiceAPI;

    @Autowired
    private AdminServiceAPI adminServiceAPI;


    /**
     * 查询列表
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dictCategory:list"})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public TableVO list(HttpServletRequest request,@RequestBody DictCategoryPageInfo pageInfo) {
        TableVO tableVO = new TableVO();
        try {
            pageInfo.setAppCode(toucan.getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            ResultObjectVO resultObjectVO = dictCategoryServiceAPI.listPage(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    DictCategoryPageInfo dictCategoryPageInfo = resultObjectVO.formatData(DictCategoryPageInfo.class);
                    tableVO.setCount(dictCategoryPageInfo.getTotal());
                    if (tableVO.getCount() > 0) {
                        Set<String> appCodes = new HashSet<>();
                        Set<String> adminIdList = new HashSet<>();
                        for (DictCategoryVO dictCategoryVO : dictCategoryPageInfo.getList()) {
                            if (dictCategoryVO.getCreateAdminId() != null) {
                                adminIdList.add(dictCategoryVO.getCreateAdminId());
                            }
                            if (dictCategoryVO.getUpdateAdminId() != null) {
                                adminIdList.add(dictCategoryVO.getUpdateAdminId());
                            }
                            appCodes.add(dictCategoryVO.getAppCode());
                        }
                        this.setAppNames(appCodes, dictCategoryPageInfo.getList());
                        this.setAdminNames(adminIdList, dictCategoryPageInfo.getList());
                        tableVO.setData(dictCategoryPageInfo.getList());
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dictCategory:save"})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultObjectVO save(HttpServletRequest request, @RequestBody DictCategoryVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setAppCode(toucan.getAppCode());
            entity.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = dictCategoryServiceAPI.save(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dictCategory:update"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultObjectVO update(HttpServletRequest request, @RequestBody DictCategoryVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setAppCode(toucan.getAppCode());
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = dictCategoryServiceAPI.update(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dictCategory:delete"})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody DictCategory dictCategory) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (dictCategory.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            dictCategory.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode, dictCategory);
            resultObjectVO = dictCategoryServiceAPI.deleteById(requestVo);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dictCategory:deletes"})
    @RequestMapping(value = "/delete/ids", method = RequestMethod.POST)
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<DictCategory> dictCategories) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (CollectionUtils.isEmpty(dictCategories)) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode, dictCategories);
            resultObjectVO = dictCategoryServiceAPI.deleteByIds(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询应用列表（按分类ID）
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dictCategory:appList"})
    @RequestMapping(value = "/queryAppListByCategoryId", method = RequestMethod.POST)
    public ResultObjectVO queryAppListByCategoryId(@RequestBody DictCategoryVO query) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (query.getId() == null) {
                resultObjectVO.setMsg("字典分类ID不能为空");
                resultObjectVO.setCode(TableVO.FAILD);
                return resultObjectVO;
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
     * 查询所有分类（字典管理左侧使用）
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dictManage"})
    @RequestMapping(value = "/listAll", method = RequestMethod.POST)
    public ResultObjectVO listAll(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            DictCategoryVO query = new DictCategoryVO();
            query.setAppCode(toucan.getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), query);
            resultObjectVO = dictCategoryServiceAPI.queryList(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dictCategory:row:view"})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResultObjectVO queryDetail(HttpServletRequest request, @RequestBody DictCategory entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = dictCategoryServiceAPI.queryDetail(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 设置管理员名称
     */
    private void setAdminNames(Set<String> adminIdList, List<DictCategoryVO> list) throws Exception {
        String[] createOrUpdateAdminIds = new String[adminIdList.size()];
        adminIdList.toArray(createOrUpdateAdminIds);
        AdminVO queryAdminVO = new AdminVO();
        queryAdminVO.setAdminIds(createOrUpdateAdminIds);
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAdminVO);
        ResultObjectVO resultObjectVO = adminServiceAPI.queryListByEntity(requestJsonVO);
        if (resultObjectVO.isSuccess()) {
            List<AdminVO> adminVOS = resultObjectVO.formatDataList(AdminVO.class);
            if (CollectionUtils.isNotEmpty(adminVOS)) {
                for (DictCategoryVO dictCategoryVO : list) {
                    for (AdminVO adminVO : adminVOS) {
                        if (dictCategoryVO.getCreateAdminId() != null && dictCategoryVO.getCreateAdminId().equals(adminVO.getAdminId())) {
                            dictCategoryVO.setCreateAdminUsername(adminVO.getUsername());
                        }
                        if (dictCategoryVO.getUpdateAdminId() != null && dictCategoryVO.getUpdateAdminId().equals(adminVO.getAdminId())) {
                            dictCategoryVO.setUpdateAdminUsername(adminVO.getUsername());
                        }
                    }
                }
            }
        }
    }


    /**
     * 设置关联应用
     */
    private void setAppNames(Set<String> appCodes, List<DictCategoryVO> list) throws Exception {
        if (CollectionUtils.isNotEmpty(appCodes)) {
            AppVO appVO = new AppVO();
            appVO.setCodes(new ArrayList<>(appCodes));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, appVO);
            ResultObjectVO resultObjectVO = appServiceAPI.queryListByCodes(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<AppVO> apps = resultObjectVO.formatDataList(AppVO.class);
                if (CollectionUtils.isNotEmpty(apps)) {
                    for (DictCategoryVO dictCategoryVO : list) {
                        for (AppVO apv : apps) {
                            if (dictCategoryVO.getAppCode().equals(apv.getCode())) {
                                dictCategoryVO.setAppName(apv.getName());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

}
