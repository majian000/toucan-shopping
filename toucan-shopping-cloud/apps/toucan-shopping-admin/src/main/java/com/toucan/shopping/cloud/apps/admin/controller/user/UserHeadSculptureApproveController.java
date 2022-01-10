package com.toucan.shopping.cloud.apps.admin.controller.user;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.message.api.feign.service.FeignMessageUserService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserHeadSculptureApproveService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import com.toucan.shopping.modules.common.persistence.event.enums.EventPublishTypeEnum;
import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.AuthHeaderUtil;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.message.constant.MessageContentTypeConstant;
import com.toucan.shopping.modules.message.enums.MessageTypeEnum;
import com.toucan.shopping.modules.message.vo.MessageVO;
import com.toucan.shopping.modules.user.page.UserHeadSculptureApprovePageInfo;
import com.toucan.shopping.modules.user.vo.UserHeadSculptureApproveVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户头像审核管理
 */
@Controller
@RequestMapping("/user/head/sculpture/approve")
public class UserHeadSculptureApproveController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignUserHeadSculptureApproveService feignUserHeadSculptureApproveService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignMessageUserService feignMessageUserService;


    @Autowired
    private EventPublishService eventPublishService;

    @Autowired
    private IdGenerator idGenerator;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/user/head/sculpture/approve/listPage",feignFunctionService);
        return "pages/user/headSculptureApprove/list.html";
    }



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, UserHeadSculptureApprovePageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignUserHeadSculptureApproveService.queryListPage(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<UserHeadSculptureApproveVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),UserHeadSculptureApproveVO.class);
                    if(tableVO.getCount()>0) {
                        for(UserHeadSculptureApproveVO userHeadSculptureApproveVO:list)
                        {
                            if(StringUtils.isNotEmpty(userHeadSculptureApproveVO.getHeadSculpture())) {
                                userHeadSculptureApproveVO.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix()+"/"+userHeadSculptureApproveVO.getHeadSculpture());
                            }
                        }
                        tableVO.setData((List)list);
                    }
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("请求失败,请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }







    /**
     * 审核通过
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/pass/{id}",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO passById(HttpServletRequest request,  @PathVariable String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            UserHeadSculptureApproveVO userHeadSculptureApproveVO =new UserHeadSculptureApproveVO();
            userHeadSculptureApproveVO.setId(Long.parseLong(id));
            //设置审核人
            userHeadSculptureApproveVO.setApproveAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,userHeadSculptureApproveVO);
            resultObjectVO = feignUserHeadSculptureApproveService.passById(requestJsonVO.sign(), requestJsonVO);

        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 跳转到驳回页面
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/reject/page/{id}",method = RequestMethod.GET)
    public String rejectPage(HttpServletRequest request,@PathVariable String id)
    {
        UserHeadSculptureApproveVO userHeadSculptureApproveVO = new UserHeadSculptureApproveVO();
        try {
            userHeadSculptureApproveVO.setId(Long.parseLong(id));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userHeadSculptureApproveVO);
            ResultObjectVO resultObjectVO = feignUserHeadSculptureApproveService.queryById(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<UserHeadSculptureApproveVO> userHeadSculptureApproveVOS = resultObjectVO.formatDataList(UserHeadSculptureApproveVO.class);
                if(CollectionUtils.isNotEmpty(userHeadSculptureApproveVOS)) {
                    userHeadSculptureApproveVO = userHeadSculptureApproveVOS.get(0);

                    if(StringUtils.isNotEmpty(userHeadSculptureApproveVO.getHeadSculpture())) {
                        userHeadSculptureApproveVO.setHttpHeadSculpture(imageUploadService.getImageHttpPrefix()+"/"+userHeadSculptureApproveVO.getHeadSculpture());
                    }
                    request.setAttribute("model",userHeadSculptureApproveVO);
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("model", userHeadSculptureApproveVO);
        }
        return "pages/user/headSculptureApprove/reject.html";
    }





    EventPublish saveEventPublish(MessageVO messageVO)
    {
        String globalTransactionId = UUID.randomUUID().toString().replace("-","");

        EventPublish eventPublish = new EventPublish();
        eventPublish.setCreateDate(new Date());
        eventPublish.setId(idGenerator.id());
        eventPublish.setRemark(messageVO.getTitle());
        eventPublish.setTransactionId(globalTransactionId);
        eventPublish.setPayload(JSONObject.toJSONString(messageVO));
        eventPublish.setStatus((short)0); //待发送
        eventPublish.setType(EventPublishTypeEnum.USER_PROFILEPHOTO_MESSAGE.getCode());
        if(eventPublishService.insert(eventPublish)>0) {
            return eventPublish;
        }
        return null;
    }

    /**
     * 审核驳回
     * @param userHeadSculptureApproveVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/reject",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO reject(HttpServletRequest request,@RequestBody UserHeadSculptureApproveVO userHeadSculptureApproveVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(userHeadSculptureApproveVO.getId()==null)
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            //设置审核人
            userHeadSculptureApproveVO.setApproveAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,userHeadSculptureApproveVO);
            resultObjectVO = feignUserHeadSculptureApproveService.rejectById(requestJsonVO.sign(), requestJsonVO);

            if(resultObjectVO.isSuccess())
            {

                //发送消息
                MessageVO messageVO = new MessageVO(MessageTypeEnum.HEAD_SCULPTURE.getName(),userHeadSculptureApproveVO.getRejectText(), MessageContentTypeConstant.CONTENT_TYPE_1,userHeadSculptureApproveVO.getUserMainId());
                messageVO.setMessageType(MessageTypeEnum.HEAD_SCULPTURE.getCode(),MessageTypeEnum.HEAD_SCULPTURE.getName(),MessageTypeEnum.HEAD_SCULPTURE.getAppCode());

                //保存消息发布事件
                EventPublish eventPublish = saveEventPublish(messageVO);
                if(eventPublish==null)
                {
                    logger.warn("消息发布事件保存失败 payload {} ",JSONObject.toJSONString(messageVO));
                }

                requestJsonVO = RequestJsonVOGenerator.generator(appCode,messageVO);
                resultObjectVO = feignMessageUserService.send(requestJsonVO);

                if (resultObjectVO.isSuccess())
                {
                    //设置消息为已发送
                    eventPublish.setStatus((short)1);
                    eventPublishService.updateStatus(eventPublish);
                }
            }
        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





}

