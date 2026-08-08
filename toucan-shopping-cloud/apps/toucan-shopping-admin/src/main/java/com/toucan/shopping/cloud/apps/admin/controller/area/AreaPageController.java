package com.toucan.shopping.cloud.apps.admin.controller.area;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.FunctionServiceAPI;
import com.toucan.shopping.cloud.apps.admin.controller.base.UIController;
import com.toucan.shopping.cloud.common.data.api.AreaServiceAPI;
import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 地区控制器 - 页面控制器
 */
@Controller
public class AreaPageController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private AreaServiceAPI areaServiceAPI;

    @Autowired
    private FunctionServiceAPI functionServiceAPI;




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/area/listPage",method = RequestMethod.GET)
    public String page(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/area/listPage", functionServiceAPI);

        return "pages/area/list.html";
    }



    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/area/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {
        return "pages/area/add.html";
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/area/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            Area entity = new Area();
            entity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            ResultObjectVO resultObjectVO = areaServiceAPI.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<Area> areas = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),Area.class);
                    if(!CollectionUtils.isEmpty(areas))
                    {
                        AreaVO areaVO = new AreaVO();
                        BeanUtils.copyProperties(areaVO,areas.get(0));
                        //如果是顶级节点,上级节点就是根节点
                        if(areaVO.getPid().longValue()==-1)
                        {
                            areaVO.setParentName("根节点");
                        }else {
                            Area queryParentArea = new Area();
                            queryParentArea.setId(areaVO.getPid());
                            requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryParentArea);
                            resultObjectVO = areaServiceAPI.findById(requestJsonVO);
                            if (resultObjectVO.isSuccess()) {
                                List<Area> parentAreaList = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()), Area.class);
                                if (!CollectionUtils.isEmpty(parentAreaList)) {
                                    if (parentAreaList.get(0).getType().shortValue() == 1) {
                                        areaVO.setParentName(parentAreaList.get(0).getProvince());
                                    } else if (parentAreaList.get(0).getType().shortValue() == 2) {
                                        areaVO.setParentName(parentAreaList.get(0).getCity());
                                    } else if (parentAreaList.get(0).getType().shortValue() == 3) {
                                        areaVO.setParentName(parentAreaList.get(0).getArea());
                                    }
                                }
                            }
                        }
                        request.setAttribute("model",areaVO);
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/area/edit.html";
    }

}
