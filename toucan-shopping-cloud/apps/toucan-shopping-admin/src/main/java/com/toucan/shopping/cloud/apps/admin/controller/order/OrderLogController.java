package com.toucan.shopping.cloud.apps.admin.controller.order;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignDictService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.order.api.feign.service.FeignOrderLogService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.admin.auth.vo.AdminVO;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultPageInfoVO;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private FeignUserService feignUserService;


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
            ResultPageInfoVO<OrderLogVO> resultPageInfoVO = feignOrderLogService.queryListPage(requestJsonVO);
            if(resultPageInfoVO.isSuccess()) {
                PageInfo orderLogPageInfo = resultPageInfoVO.getData();
                tableVO.setCount(orderLogPageInfo.getTotal()!=null?orderLogPageInfo.getTotal():0);
                List<OrderLogVO> orderLogs = orderLogPageInfo.getList();

                for (OrderLogVO orderLogVO : orderLogs) {
                    orderLogVO.setOperateUserType(2); //先默认为普通用户操作
                }
                List<String> operateUserIdList = null;
                if(CollectionUtils.isNotEmpty(orderLogs)) {
                    operateUserIdList = orderLogs.stream().map(OrderLogVO::getOperateUserId).collect(Collectors.toList());

                    DictVO query=new DictVO();
                    query.setCategoryCode(OrderDictConstant.ORDER_LOG_DICT_CATEGORY_CODE);
                    query.setCode(OrderDictConstant.ORDER_LOG_DICT_TYPE_CODE);
                    query.setAppCode(toucan.getAppCode());
                    requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), query);
                    ResultObjectVO resultObjectVO = feignDictService.queryDictByCodeAndCategoryCode(requestJsonVO);
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

                    List<AdminVO> admins = this.queryAdminListByAdminId(operateUserIdList);
                    if(CollectionUtils.isNotEmpty(admins)){
                        for (OrderLogVO orderLogVO : orderLogs) {
                            for(AdminVO adminVO:admins){
                                if(orderLogVO.getOperateUserId().equals(adminVO.getAdminId())){
                                    orderLogVO.setOperateUserName(adminVO.getUsername());
                                    orderLogVO.setOperateUserType(1); //如果匹配到管理员ID 在设置为管理员操作
                                    break;
                                }
                            }
                        }
                    }
                }

                tableVO.setData(orderLogs);
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

