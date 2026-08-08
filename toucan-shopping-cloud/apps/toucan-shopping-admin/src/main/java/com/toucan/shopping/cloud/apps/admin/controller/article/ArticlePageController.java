package com.toucan.shopping.cloud.apps.admin.controller.article;


import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.content.api.ArticleServiceAPI;
import com.toucan.shopping.cloud.content.api.ColumnServiceAPI;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.content.vo.ArticleVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

@Controller
public class ArticlePageController extends UIController {

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
    private ArticleServiceAPI articleService;

    @Autowired
    private ImageUploadService imageUploadService;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/article/listPage", method = RequestMethod.GET)
    public String listPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/article/listPage", functionServiceAPI);
        return "pages/article/list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/article/addPage", method = RequestMethod.GET)
    public String addPage(HttpServletRequest request, @RequestParam(required = false) Long columnId) throws NoSuchAlgorithmException {
        ColumnVO queryColumnVO = new ColumnVO();
        queryColumnVO.setId(columnId);
        ResultTypeObjectVO<ColumnVO> resultTypeObjectVO = columnService.findById(RequestJsonVOGenerator.generator(toucan.getAppCode(), queryColumnVO));
        if (resultTypeObjectVO.isSuccess()) {
            if (resultTypeObjectVO.getData() != null) {
                request.setAttribute("columnId", resultTypeObjectVO.getData().getId());
                request.setAttribute("columnName", resultTypeObjectVO.getData().getTitle());
            }
        }

        ResultTypeObjectVO<Long> resultMaxSort = articleService.queryMaxSort(RequestJsonVOGenerator.generator(toucan.getAppCode(), columnId));
        request.setAttribute("maxSort", resultMaxSort.getData() + 1);
        return "pages/article/add.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/article/editPage/{id}", method = RequestMethod.GET)
    public String editPage(HttpServletRequest request, @PathVariable Long id) throws NoSuchAlgorithmException {
        try {
            ArticleVO queryArticleVO = new ArticleVO();
            queryArticleVO.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryArticleVO);
            ResultTypeObjectVO<ArticleVO> resultObjectVO = articleService.findById(requestJsonVO);
            if (resultObjectVO.isSuccess()) {
                ArticleVO articleVO = resultObjectVO.getData();
                if (StringUtils.isNotEmpty(articleVO.getCoverImgUrl())) {
                    articleVO.setHttpCoverImgUrl(imageUploadService.getImageHttpPrefix() + articleVO.getCoverImgUrl());
                }
                if (articleVO.getStartShowDate() != null) {
                    articleVO.setStartShowDateString(DateUtils.FORMATTER_SS.get().format(articleVO.getStartShowDate()));
                }
                if (articleVO.getEndShowDate() != null) {
                    articleVO.setEndShowDateString(DateUtils.FORMATTER_SS.get().format(articleVO.getEndShowDate()));
                }
                ColumnVO queryColumnVO = new ColumnVO();
                queryColumnVO.setId(articleVO.getColumnId());
                ResultTypeObjectVO<ColumnVO> resultTypeObjectVO = columnService.findById(RequestJsonVOGenerator.generator(toucan.getAppCode(), queryColumnVO));
                if (resultTypeObjectVO.isSuccess()) {
                    if (resultTypeObjectVO.getData() != null) {
                        articleVO.setColumnName(resultTypeObjectVO.getData().getTitle());
                    }
                }
                request.setAttribute("model", articleVO);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return "pages/article/edit.html";
    }

}
