package com.toucan.shopping.cloud.apps.admin.controller.user;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.message.api.feign.service.FeignMessageTypeService;
import com.toucan.shopping.cloud.message.api.feign.service.FeignMessageUserService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserTrueNameApproveService;
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
import com.toucan.shopping.modules.message.vo.MessageTypeVO;
import com.toucan.shopping.modules.message.vo.MessageVO;
import com.toucan.shopping.modules.user.page.UserTrueNameApprovePageInfo;
import com.toucan.shopping.modules.user.vo.UserTrueNameApproveVO;
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
 * 用户实名审核管理
 */
@Controller
@RequestMapping("/user/true/name/approve")
public class UserTrueNameApproveController extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String TRUENAME_MESSAGE_TYPE_CODE="30011";

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @Autowired
    private FeignUserTrueNameApproveService feignUserTrueNameApproveService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignMessageUserService feignMessageUserService;

    @Autowired
    private EventPublishService eventPublishService;

    @Autowired
    private IdGenerator idGenerator;


    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String listPage(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/user/true/name/approve/listPage",feignFunctionService);
        return "pages/user/trueNameApprove/list.html";
    }



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, UserTrueNameApprovePageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = feignUserTrueNameApproveService.queryListPage(SignUtil.sign(requestJsonVO),requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = (Map<String,Object>)resultObjectVO.getData();
                    tableVO.setCount(Long.parseLong(String.valueOf(resultObjectDataMap.get("total")!=null?resultObjectDataMap.get("total"):"0")));
                    List<UserTrueNameApproveVO> list = JSONArray.parseArray(JSONObject.toJSONString(resultObjectDataMap.get("list")),UserTrueNameApproveVO.class);
                    for(UserTrueNameApproveVO userTrueNameApproveVO:list)
                    {
                        if(userTrueNameApproveVO.getIdcardImg1()!=null) {
                            userTrueNameApproveVO.setHttpIdcardImg1(imageUploadService.getImageHttpPrefix() + userTrueNameApproveVO.getIdcardImg1());
                        }
                        if(userTrueNameApproveVO.getIdcardImg2()!=null) {
                            userTrueNameApproveVO.setHttpIdcardImg2(imageUploadService.getImageHttpPrefix() + userTrueNameApproveVO.getIdcardImg2());
                        }
                    }
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
        eventPublish.setType(EventPublishTypeEnum.USER_TRUESCULPTURE_MESSAGE.getCode());
        if(eventPublishService.insert(eventPublish)>0) {
            return eventPublish;
        }
        return null;
    }


    /**
     * 审核通过
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/pass/{id}/{userMainId}",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO passById(HttpServletRequest request,  @PathVariable String id,@PathVariable Long userMainId)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            UserTrueNameApproveVO userTrueNameApproveVO =new UserTrueNameApproveVO();
            userTrueNameApproveVO.setId(Long.parseLong(id));
            //设置审核人
            userTrueNameApproveVO.setApproveAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,userTrueNameApproveVO);
            resultObjectVO = feignUserTrueNameApproveService.passById(requestJsonVO.sign(), requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                //发送消息
                MessageVO messageVO = new MessageVO("实名认证消息","恭喜您,实名审核完成",MessageContentTypeConstant.CONTENT_TYPE_1,userMainId);
                messageVO.setMessageTypeCode(TRUENAME_MESSAGE_TYPE_CODE);

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
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    /**
     * 跳转到驳回页面
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/reject/page/{id}",method = RequestMethod.GET)
    public String rejectPage(HttpServletRequest request,@PathVariable String id)
    {
        UserTrueNameApproveVO userTrueNameApproveVO = new UserTrueNameApproveVO();
        try {
            userTrueNameApproveVO.setId(Long.parseLong(id));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userTrueNameApproveVO);
            ResultObjectVO resultObjectVO = feignUserTrueNameApproveService.queryById(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<UserTrueNameApproveVO> userTrueNameApproveVOS = resultObjectVO.formatDataList(UserTrueNameApproveVO.class);
                if(CollectionUtils.isNotEmpty(userTrueNameApproveVOS)) {
                    userTrueNameApproveVO = userTrueNameApproveVOS.get(0);
                    if(userTrueNameApproveVO.getIdcardImg1()!=null) {
                        userTrueNameApproveVO.setHttpIdcardImg1(imageUploadService.getImageHttpPrefix() + userTrueNameApproveVO.getIdcardImg1());
                    }
                    if(userTrueNameApproveVO.getIdcardImg2()!=null) {
                        userTrueNameApproveVO.setHttpIdcardImg2(imageUploadService.getImageHttpPrefix() + userTrueNameApproveVO.getIdcardImg2());
                    }
                    request.setAttribute("model",userTrueNameApproveVO);
                }

            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("model", userTrueNameApproveVO);
        }
        return "pages/user/trueNameApprove/reject.html";
    }


    /**
     * 审核驳回
     * @param userTrueNameApproveVO
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM,responseType=AdminAuth.RESPONSE_FORM)
    @RequestMapping(value = "/reject",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO reject(HttpServletRequest request,@RequestBody UserTrueNameApproveVO userTrueNameApproveVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(userTrueNameApproveVO.getId()==null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            //设置审核人
            userTrueNameApproveVO.setApproveAdminId(AuthHeaderUtil.getAdminId(toucan.getAppCode(),request.getHeader(toucan.getAdminAuth().getHttpToucanAuthHeader())));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,userTrueNameApproveVO);
            resultObjectVO = feignUserTrueNameApproveService.rejectById(requestJsonVO.sign(), requestJsonVO);
            if(resultObjectVO.isSuccess())
            {

                //发送消息
                MessageVO messageVO = new MessageVO("实名认证消息",userTrueNameApproveVO.getRejectText(), MessageContentTypeConstant.CONTENT_TYPE_1,userTrueNameApproveVO.getUserMainId());
                messageVO.setMessageTypeCode(TRUENAME_MESSAGE_TYPE_CODE);

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
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }





}

