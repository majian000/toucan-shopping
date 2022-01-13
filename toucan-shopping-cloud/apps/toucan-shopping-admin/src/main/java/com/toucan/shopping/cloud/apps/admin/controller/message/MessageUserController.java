package com.toucan.shopping.cloud.apps.admin.controller.message;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.message.api.feign.service.FeignMessageTypeService;
import com.toucan.shopping.cloud.message.api.feign.service.FeignMessageUserService;
import com.toucan.shopping.modules.area.page.BannerPageInfo;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.message.constant.MessageContentTypeConstant;
import com.toucan.shopping.modules.message.entity.MessageType;
import com.toucan.shopping.modules.message.page.MessageUserPageInfo;
import com.toucan.shopping.modules.message.vo.MessageTypeVO;
import com.toucan.shopping.modules.message.vo.MessageUserVO;
import com.toucan.shopping.modules.message.vo.MessageVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户消息管理
 */
@Controller
@RequestMapping("/message/messageUser")
public class MessageUserController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignMessageUserService feignMessageUserService;

    @Autowired
    private FeignMessageTypeService feignMessageTypeService;

    void initMessageTypes(HttpServletRequest request)
    {
        try {
            MessageTypeVO messageTypeVO = new MessageTypeVO();
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), messageTypeVO);
            ResultObjectVO resultObjectVO = feignMessageTypeService.queryList(requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                if(resultObjectVO.getData()!=null) {
                    request.setAttribute("messageTypes", resultObjectVO.formatDataList(MessageTypeVO.class));
                }else{
                    request.setAttribute("messageTypes",new ArrayList<>());
                }
            }
        }catch(Exception e)
        {
            request.setAttribute("messageTypes",new ArrayList<>());
            logger.warn(e.getMessage(),e);
        }
    }

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/message/messageUser/listPage",feignFunctionService);
        initMessageTypes(request);
        return "pages/message/messageUser/list.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/addPage",method = RequestMethod.GET)
    public String addPage(HttpServletRequest request)
    {
        initMessageTypes(request);
        return "pages/message/messageUser/add.html";
    }


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/selectUserListPage",method = RequestMethod.GET)
    public String selectUserListPage(HttpServletRequest request)
    {
        return "pages/message/messageUser/user_list.html";
    }




    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/editPage/{id}",method = RequestMethod.GET)
    public String editPage(HttpServletRequest request,@PathVariable Long id)
    {
        try {
            MessageUserVO queryEntity = new MessageUserVO();
            queryEntity.setId(id);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, queryEntity);
            ResultObjectVO resultObjectVO = feignMessageUserService.findById(requestJsonVO);
            if(resultObjectVO.getCode().intValue()==ResultObjectVO.SUCCESS.intValue())
            {
                if(resultObjectVO.getData()!=null) {
                    List<MessageUserVO> messageTypeVOS = JSONArray.parseArray(JSONObject.toJSONString(resultObjectVO.getData()),MessageUserVO.class);
                    if(!CollectionUtils.isEmpty(messageTypeVOS))
                    {
                        request.setAttribute("model",messageTypeVOS.get(0));
                    }
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        return "pages/message/messageUser/edit.html";
    }




    /**
     * 修改
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO update(HttpServletRequest request,@RequestBody MessageUserVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            entity.setUpdateDate(new Date());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, entity);
            resultObjectVO = feignMessageUserService.update(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 保存
     * @param entity
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO save(HttpServletRequest request, @RequestBody MessageUserVO entity)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            MessageVO messageVO = new MessageVO(entity.getTitle(),entity.getContent(), MessageContentTypeConstant.CONTENT_TYPE_1,null);
            if(entity.getUserScope()!=null&&entity.getUserScope().intValue()==1){ //指定用户
                if(StringUtils.isNotEmpty(entity.getUserMainIdListString()))
                {
                    String[] userMainIdArray = entity.getUserMainIdListString().split(",");
                    //借助Set进行去重
                    Set<String> userMainIdSet = new HashSet<String>();
                    for(String userMainId:userMainIdArray)
                    {
                        userMainIdSet.add(userMainId);
                    }
                    for(String userMainId:userMainIdSet)
                    {
                        try {
                            messageVO.addReceiveUser(Long.parseLong(userMainId));
                        }catch(Exception e)
                        {
                            logger.info("用户ID为 {}",userMainId);
                            logger.warn(e.getMessage(),e);
                        }
                    }

                }
            }
            messageVO.setUserScope(entity.getUserScope()); //设置用户范围
            //发送消息
            messageVO.setMessageTypeCode(entity.getMessageTypeCode());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode, messageVO);
            resultObjectVO = feignMessageUserService.send(requestJsonVO);
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, MessageUserPageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignMessageUserService.queryListPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<MessageUserVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),MessageUserVO.class);


                    if(tableVO.getCount()>0) {
                        tableVO.setData((List)list);
                    }
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

