package com.toucan.shopping.cloud.apps.admin.controller.dict;


import com.toucan.shopping.cloud.admin.auth.api.*;
import com.toucan.shopping.modules.admin.auth.entity.Dict;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.admin.auth.page.DictPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.*;
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
@RequestMapping("/dict")
public class DictController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private DictServiceAPI dictServiceAPI;

    @Autowired
    private AppServiceAPI appServiceAPI;

    @Autowired
    private DictCategoryServiceAPI dictCategoryServiceAPI;

    @Autowired
    private AdminServiceAPI adminServiceAPI;


    /**
     * 查询树表格（按父ID）
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dict:tree:list"})
    @RequestMapping(value = "/tree/table/by/pid", method = RequestMethod.POST)
    public ResultObjectVO queryTreeTableByPid(HttpServletRequest request, @RequestBody DictPageInfo pageInfo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (pageInfo.getCategoryId() == null || pageInfo.getCategoryId().longValue() == -1) {
                resultObjectVO.setMsg("字典分类ID不能为空");
                resultObjectVO.setCode(TableVO.FAILD);
                return resultObjectVO;
            }

            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            resultObjectVO = dictServiceAPI.queryTreeTableByPid(requestJsonVO);

            if (resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    Set<String> adminIdList = new HashSet<>();
                    Set<String> appCodes = new HashSet<>();
                    List<DictVO> dictTreeVOS = resultObjectVO.formatDataList(DictVO.class);
                    if (CollectionUtils.isNotEmpty(dictTreeVOS)) {
                        for (DictVO dictTreeVO : dictTreeVOS) {
                            if (dictTreeVO.getCreateAdminId() != null) {
                                adminIdList.add(dictTreeVO.getCreateAdminId());
                            }
                            if (dictTreeVO.getUpdateAdminId() != null) {
                                adminIdList.add(dictTreeVO.getUpdateAdminId());
                            }
                            appCodes.add(dictTreeVO.getAppCode());
                        }
                        this.setAdminNames(adminIdList, dictTreeVOS);
                        this.setAppNames(appCodes, dictTreeVOS);
                        resultObjectVO.setData(dictTreeVOS);
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
     * 查询全部字典树
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dict:tree:list"})
    @RequestMapping(value = "/query/tree/all", method = RequestMethod.POST)
    public ResultObjectVO queryTreeAll(HttpServletRequest request, @RequestBody DictPageInfo pageInfo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (pageInfo.getCategoryId() == null || pageInfo.getCategoryId().longValue() == -1) {
                resultObjectVO.setMsg("字典分类ID不能为空");
                resultObjectVO.setCode(TableVO.FAILD);
                return resultObjectVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            resultObjectVO = dictServiceAPI.queryTreeAll(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 修改
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dict:update"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultObjectVO update(HttpServletRequest request, @RequestBody DictVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = dictServiceAPI.update(requestJsonVO);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 查询分类列表
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dict:category:list"})
    @RequestMapping(value = "/query/category/list", method = RequestMethod.POST)
    public ResultObjectVO queryCategoryTreeByParentId(@RequestBody DictVO dictVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            DictCategoryVO query = new DictCategoryVO();
            query.setAppCode(toucan.getAppCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, query);
            resultObjectVO = dictCategoryServiceAPI.queryList(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    List<DictCategoryTreeVO> dictCategoryTreeVOS = resultObjectVO.formatDataList(DictCategoryTreeVO.class);
                    Set<String> appCodes = new HashSet<>();
                    for (DictCategoryTreeVO dictCategoryTreeVO : dictCategoryTreeVOS) {
                        dictCategoryTreeVO.setOpen(false);
                        dictCategoryTreeVO.setIcon(null);
                        appCodes.add(dictCategoryTreeVO.getAppCode());
                    }

                    if (CollectionUtils.isNotEmpty(appCodes)) {
                        AppVO appVO = new AppVO();
                        appVO.setCodes(new ArrayList<>(appCodes));
                        requestJsonVO = RequestJsonVOGenerator.generator(appCode, appVO);
                        resultObjectVO = appServiceAPI.queryListByCodes(requestJsonVO);
                        if (resultObjectVO.isSuccess()) {
                            List<AppVO> apps = resultObjectVO.formatDataList(AppVO.class);
                            if (CollectionUtils.isNotEmpty(apps)) {
                                for (DictCategoryVO dictCategoryVO : dictCategoryTreeVOS) {
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

                    resultObjectVO.setData(dictCategoryTreeVOS);
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
     * 查询树的子节点列表
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dict:query:tree:child"})
    @RequestMapping(value = "/query/tree/child", method = RequestMethod.POST)
    public ResultObjectVO queryTreeChildById(HttpServletRequest request, DictTreeVO queryParam) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            DictPageInfo dictTreeVO = new DictPageInfo();
            dictTreeVO.setAppCode(toucan.getAppCode());
            dictTreeVO.setPid(queryParam.getId());
            dictTreeVO.setCategoryId(queryParam.getCategoryId());
            dictTreeVO.setIsActive((short) 1);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), dictTreeVO);
            resultObjectVO = dictServiceAPI.queryTreeChildByPid(requestJsonVO);
            return resultObjectVO;
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 保存
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dict:save"})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultObjectVO save(HttpServletRequest request, @RequestBody DictVO entity) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setCreateAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = dictServiceAPI.save(requestJsonVO);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dict:delete"})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultObjectVO deleteById(HttpServletRequest request, @RequestBody Dict dict) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (dict.getId() == null) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            Dict entity = new Dict();
            entity.setId(dict.getId());
            entity.setUpdateAdminId(AdminLoginHolder.getCurrentAdminId());

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = dictServiceAPI.deleteById(requestVo);
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:admin:dict:deletes"})
    @RequestMapping(value = "/delete/ids", method = RequestMethod.POST)
    public ResultObjectVO deleteByIds(HttpServletRequest request, @RequestBody List<Dict> dicts) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (CollectionUtils.isEmpty(dicts)) {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            RequestJsonVO requestVo = RequestJsonVOGenerator.generator(appCode, dicts);
            resultObjectVO = dictServiceAPI.deleteByIds(requestVo);
        } catch (Exception e) {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }


    /**
     * 设置关联应用
     */
    private void setAppNames(Set<String> appCodes, List<DictVO> list) throws Exception {
        if (CollectionUtils.isNotEmpty(appCodes)) {
            AppVO appVO = new AppVO();
            appVO.setCodes(new ArrayList<>(appCodes));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, appVO);
            ResultObjectVO resultObjectVO = appServiceAPI.queryListByCodes(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                List<AppVO> apps = resultObjectVO.formatDataList(AppVO.class);
                if (CollectionUtils.isNotEmpty(apps)) {
                    for (DictVO dictTreeVO : list) {
                        for (AppVO apv : apps) {
                            if (dictTreeVO.getAppCode().equals(apv.getCode())) {
                                dictTreeVO.setAppName(apv.getName());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 设置管理员名称
     */
    private void setAdminNames(Set<String> adminIdList, List<DictVO> list) throws Exception {
        String[] createOrUpdateAdminIds = new String[adminIdList.size()];
        adminIdList.toArray(createOrUpdateAdminIds);
        AdminVO queryAdminVO = new AdminVO();
        queryAdminVO.setAdminIds(createOrUpdateAdminIds);
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryAdminVO);
        ResultObjectVO resultObjectVO = adminServiceAPI.queryListByEntity(requestJsonVO);
        if (resultObjectVO.isSuccess()) {
            List<AdminVO> adminVOS = resultObjectVO.formatDataList(AdminVO.class);
            if (CollectionUtils.isNotEmpty(adminVOS)) {
                for (DictVO dictVO : list) {
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
