package com.toucan.shopping.cloud.apps.admin.controller.banner;


import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.AdminServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.content.api.BannerServiceAPI;
import com.toucan.shopping.cloud.common.data.api.AreaServiceAPI;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.area.vo.AreaTreeVO;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.content.entity.BannerArea;
import com.toucan.shopping.modules.content.vo.BannerAreaVO;
import com.toucan.shopping.modules.content.vo.BannerVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 轮播图管理 - 页面控制器
 */
@Controller
public class BannerPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;

    @Autowired
    private BannerServiceAPI bannerService;

    @Autowired
    private AreaServiceAPI areaService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private AdminServiceAPI adminServiceAPI;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/banner/listPage", method = RequestMethod.GET)
    public String listPage(HttpServletRequest request) {
        super.initButtons(request, toucan, "/banner/listPage", functionServiceAPI);
        return "pages/banner/list.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/banner/editPage/{id}", method = RequestMethod.GET)
    public String editPage(HttpServletRequest request, @PathVariable Long id) {
        try {
            BannerVO banner = new BannerVO();
            banner.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, banner);
            ResultObjectVO resultObjectVO = bannerService.findById(requestJsonVO);
            if (resultObjectVO.getCode().intValue() == ResultObjectVO.SUCCESS.intValue()) {
                if (resultObjectVO.getData() != null) {
                    List<BannerVO> bannerVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), BannerVO.class);
                    if (!CollectionUtils.isEmpty(bannerVOS)) {
                        banner = bannerVOS.get(0);
                        banner.setHttpImgPath(imageUploadService.getImageHttpPrefix() + banner.getImgPath());
                        if (banner.getStartShowDate() != null) {
                            banner.setStartShowDateString(DateUtils.format(banner.getStartShowDate(), DateUtils.FORMATTER_SS.get()));
                        }

                        if (banner.getEndShowDate() != null) {
                            banner.setEndShowDateString(DateUtils.format(banner.getEndShowDate(), DateUtils.FORMATTER_SS.get()));
                        }

                        if (!CollectionUtils.isEmpty(banner.getBannerAreas())) {
                            String[] areaCodeArray = new String[banner.getBannerAreas().size()];
                            for (int i = 0; i < banner.getBannerAreas().size(); i++) {
                                areaCodeArray[i] = banner.getBannerAreas().get(i).getAreaCode();
                            }
                            AreaVO queryArea = new AreaVO();
                            queryArea.setCodeArray(areaCodeArray);
                            requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryArea);

                            resultObjectVO = areaService.findByCodes(requestJsonVO);
                            if (resultObjectVO.isSuccess()) {
                                List<AreaVO> areaVOS = resultObjectVO.formatDataList(AreaVO.class);
                                if (!CollectionUtils.isEmpty(areaVOS)) {
                                    int areaVoSize = areaVOS.size();
                                    StringBuilder areaNamesBuilder = new StringBuilder();
                                    StringBuilder areaCodesBuilder = new StringBuilder();
                                    for (int i = 0; i < areaVoSize; i++) {
                                        AreaVO areaVO = areaVOS.get(i);
                                        areaNamesBuilder.append(areaVO.getName());
                                        areaCodesBuilder.append(areaVO.getCode());

                                        if (i + 1 < areaVoSize) {
                                            areaNamesBuilder.append(",");
                                            areaCodesBuilder.append(",");
                                        }
                                    }
                                    banner.setAreaCodes(areaCodesBuilder.toString());
                                    banner.setAreaNames(areaNamesBuilder.toString());
                                }
                            }
                        }

                        request.setAttribute("model", banner);
                    }
                }

            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return "pages/banner/edit.html";
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, requestType = AdminAuth.REQUEST_FORM, responseType = AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/banner/addPage", method = RequestMethod.GET)
    public String addPage(HttpServletRequest request) {
        return "pages/banner/add.html";
    }

}
