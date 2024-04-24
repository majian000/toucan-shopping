package com.toucan.shopping.cloud.apps.admin.controller.order;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignDictService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderLogService;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.order.constant.OrderDictConstant;
import com.toucan.shopping.modules.order.page.OrderLogPageInfo;
import com.toucan.shopping.modules.order.vo.OrderLogVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 订单日志
 */
@Controller
@RequestMapping("/order/orderLog")
public class OrderLogController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignOrderLogService feignOrderLogService;

    @Autowired
    private FeignDictService feignDictService;

    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, OrderLogPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            if(pageInfo==null)
            {
                pageInfo = new OrderLogPageInfo();
            }
            if(StringUtils.isEmpty(pageInfo.getOrderNo())){
                tableVO.setCode(ResultObjectVO.FAILD);
                tableVO.setMsg("订单编号不能为空");
                return tableVO;
            }
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), pageInfo);
            ResultObjectVO resultObjectVO = feignOrderLogService.queryListPage(requestJsonVO);
            if(resultObjectVO.isSuccess()) {
                if (resultObjectVO.getData() != null) {
                    Map<String, Object> resultObjectDataMap = (Map<String, Object>) resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total") != null ? resultObjectDataMap.get("total") : "0")));
                    List<OrderLogVO> orderLogs = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")), OrderLogVO.class);
                    DictVO query=new DictVO();
                    query.setCategoryCode(OrderDictConstant.ORDER_LOG_DICT_CATEGORY_CODE);
                    query.setCode(OrderDictConstant.ORDER_LOG_DICT_TYPE_CODE);
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), query);
                    resultObjectVO = feignDictService.queryDictByCodeAndCategoryCode(requestJsonVO);
                    if(resultObjectVO.isSuccess()) {
                        DictVO dictVO = resultObjectVO.formatData(DictVO.class);
                        if(CollectionUtils.isNotEmpty(dictVO.getChildren())) {
                            for (OrderLogVO orderLogVO : orderLogs) {
                                for(DictVO child:dictVO.getChildren()){
                                    if(child.getCode().equals(String.valueOf(orderLogVO.getType()))){
                                        orderLogVO.setTypeName(child.getName());
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    tableVO.setData(orderLogs);
                }
            }

        }catch(Exception e)
        {
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }



}

