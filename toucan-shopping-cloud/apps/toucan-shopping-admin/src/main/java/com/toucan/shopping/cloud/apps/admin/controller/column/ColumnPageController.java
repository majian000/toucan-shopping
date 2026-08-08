package com.toucan.shopping.cloud.apps.admin.controller.column;


import com.toucan.shopping.cloud.admin.auth.api.DictServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.content.api.ColumnServiceAPI;
import com.toucan.shopping.cloud.content.api.ColumnTypeServiceAPI;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.column.constant.ColumnDictConstant;
import com.toucan.shopping.modules.column.vo.ColumnTypeVO;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Controller
public class ColumnPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private ColumnServiceAPI columnService;

    @Autowired
    private ColumnTypeServiceAPI columnTypeService;

    @Autowired
    private DictServiceAPI dictServiceAPI;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/column/listPage", method = RequestMethod.GET)
    public String listPage(HttpServletRequest request) throws NoSuchAlgorithmException {
        super.initButtons(request, toucan, "/column/listPage", functionServiceAPI);
        this.setColumnDictList(request);
        return "pages/column/column/list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/column/addPage", method = RequestMethod.GET)
    public String addPage(HttpServletRequest request, @RequestParam String columnTypeCode) throws NoSuchAlgorithmException {
        this.setColumnDictList(request);
        request.setAttribute("columnTypeCode", columnTypeCode);
        request.setAttribute("defaultPosition", "1");
        ColumnTypeVO queryColumnTypeVO = new ColumnTypeVO();
        queryColumnTypeVO.setCode(columnTypeCode);
        ResultTypeObjectVO<ColumnTypeVO> resultTypeObjectVO = columnTypeService.findOneByCode(RequestJsonVOGenerator.generator(toucan.getAppCode(), queryColumnTypeVO));
        if (resultTypeObjectVO.isSuccess()) {
            if (resultTypeObjectVO.getData() != null) {
                request.setAttribute("columnTypeName", resultTypeObjectVO.getData().getName());
            }
        }
        return "pages/column/column/add.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/column/editPage/{id}", method = RequestMethod.GET)
    public String editPage(HttpServletRequest request, @PathVariable Long id) {
        try {
            this.setColumnDictList(request);
            ColumnVO queryEntity = new ColumnVO();
            queryEntity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryEntity);
            ResultTypeObjectVO<ColumnVO> resultObjectVO = columnService.findById(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                ColumnVO columnVO = resultObjectVO.getData();

                if (columnVO != null) {
                    request.setAttribute("defaultPosition", "1");
                    ColumnTypeVO queryColumnTypeVO = new ColumnTypeVO();
                    queryColumnTypeVO.setCode(columnVO.getColumnTypeCode());
                    ResultTypeObjectVO<ColumnTypeVO> resultTypeObjectVO = columnTypeService.findOneByCode(RequestJsonVOGenerator.generator(toucan.getAppCode(), queryColumnTypeVO));
                    if (resultTypeObjectVO.isSuccess()) {
                        if (resultTypeObjectVO.getData() != null) {
                            request.setAttribute("columnTypeName", resultTypeObjectVO.getData().getName());
                        }
                    }
                    if (columnVO.getStartShowDate() != null) {
                        columnVO.setStartShowDateString(DateUtils.FORMATTER_SS.get().format(columnVO.getStartShowDate()));
                    }
                    if (columnVO.getEndShowDate() != null) {
                        columnVO.setEndShowDateString(DateUtils.FORMATTER_SS.get().format(columnVO.getEndShowDate()));
                    }
                    List<String> selectTypes = new LinkedList<>();
                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(columnVO.getType())) {
                        selectTypes.addAll(Arrays.asList(columnVO.getType().split(",")));
                    }
                    request.setAttribute("selectTypes", selectTypes);
                }
                request.setAttribute("model", columnVO);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return "pages/column/column/edit.html";
    }


    private void setColumnDictList(HttpServletRequest request) throws NoSuchAlgorithmException {
        DictVO queryDict = new DictVO();
        queryDict.setCategoryCode(ColumnDictConstant.COLUMN_DICT_CATEGORY_CODE);
        queryDict.setCodes(new LinkedList<>());
        queryDict.getCodes().add(ColumnDictConstant.COLUMN_DICT_TYPE_CODE);
        queryDict.getCodes().add(ColumnDictConstant.COLUMN_DICT_POSITION_CODE);
        queryDict.setAppCode(toucan.getAppCode());
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryDict);
        ResultTypeObjectVO<List<DictVO>> resultObjectVO = dictServiceAPI.queryDictByCodesAndCategoryCode(requestJsonVO);
        if (resultObjectVO.isSuccess()) {
            if (!CollectionUtils.isEmpty(resultObjectVO.getData())) {
                for (DictVO dictVO : resultObjectVO.getData()) {
                    switch (dictVO.getCode()) {
                        case ColumnDictConstant.COLUMN_DICT_TYPE_CODE:
                            request.setAttribute("columnTypeList", dictVO.getChildren());
                            break;
                        case ColumnDictConstant.COLUMN_DICT_POSITION_CODE:
                            request.setAttribute("columnPositionList", dictVO.getChildren());
                            break;
                    }
                }
            }
        }

    }

}
