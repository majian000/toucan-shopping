package com.toucan.shopping.cloud.apps.admin.controller.user;


import com.toucan.shopping.cloud.apps.admin.helper.PageHelper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.cloud.message.api.MessageUserServiceAPI;
import com.toucan.shopping.cloud.user.api.UserHeadSculptureApproveServiceAPI;
import com.toucan.shopping.modules.admin.auth.holder.AdminLoginHolder;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.persistence.event.entity.EventPublish;
import com.toucan.shopping.modules.common.persistence.event.enums.EventPublishTypeEnum;
import com.toucan.shopping.modules.common.persistence.event.service.EventPublishService;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.message.constant.MessageContentTypeConstant;
import com.toucan.shopping.modules.message.vo.MessageVO;
import com.toucan.shopping.modules.user.page.UserHeadSculptureApprovePageInfo;
import com.toucan.shopping.modules.user.vo.UserHeadSculptureApproveVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户头像审核管理
 */
@RestController
@RequestMapping("/user/head/sculpture/approve")
public class UserHeadSculptureApproveController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String USER_HEAD_SCULPTURE_MESSAGE_TYPE_CODE="30010";

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private UserHeadSculptureApproveServiceAPI userHeadSculptureApproveService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private MessageUserServiceAPI messageUserService;


    @Autowired
    private EventPublishService eventPublishService;

    @Autowired
    private IdGenerator idGenerator;



    /**
     * 查询列表
     * @param pageInfo
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:headSculptureApprove:list"})
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public TableVO list(@RequestBody UserHeadSculptureApprovePageInfo pageInfo)
    {
        TableVO tableVO = new TableVO();
        try {
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),pageInfo);
            ResultObjectVO resultObjectVO = userHeadSculptureApproveService.queryListPage(requestJsonVO);
            if(resultObjectVO.getCode() == ResultObjectVO.SUCCESS)
            {
                if(resultObjectVO.getData()!=null)
                {
                    Map<String,Object> resultObjectDataMap = PageHelper.extractPageData(resultObjectVO.getData());
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
            tableVO.setMsg("请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }









    /**
     * 审核通过
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:headSculptureApprove:pass"})
    @RequestMapping(value = "/pass/{id}",method = RequestMethod.POST)
    public ResultObjectVO passById(@PathVariable String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            UserHeadSculptureApproveVO userHeadSculptureApproveVO =new UserHeadSculptureApproveVO();
            userHeadSculptureApproveVO.setId(Long.parseLong(id));
            //设置审核人
            userHeadSculptureApproveVO.setApproveAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,userHeadSculptureApproveVO);
            resultObjectVO = userHeadSculptureApproveService.passById( requestJsonVO);

        }catch(Exception e)
        {
            resultObjectVO.setMsg("请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
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
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH, verifyType = AdminAuth.VERIFY_TYPE_ANY, permissions = {"toucan:user:headSculptureApprove:reject"})
    @RequestMapping(value = "/reject",method = RequestMethod.POST)
    public ResultObjectVO reject(@RequestBody UserHeadSculptureApproveVO userHeadSculptureApproveVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(userHeadSculptureApproveVO.getId()==null)
            {
                resultObjectVO.setMsg("请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }
            //设置审核人
            userHeadSculptureApproveVO.setApproveAdminId(AdminLoginHolder.getCurrentAdminId());
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(appCode,userHeadSculptureApproveVO);
            resultObjectVO = userHeadSculptureApproveService.rejectById( requestJsonVO);

            if(resultObjectVO.isSuccess())
            {

                //发送消息
                MessageVO messageVO = new MessageVO("头像审核消息",userHeadSculptureApproveVO.getRejectText(), MessageContentTypeConstant.CONTENT_TYPE_1,userHeadSculptureApproveVO.getUserMainId());
                messageVO.setMessageTypeCode(USER_HEAD_SCULPTURE_MESSAGE_TYPE_CODE);

                //保存消息发布事件
                EventPublish eventPublish = saveEventPublish(messageVO);
                if(eventPublish==null)
                {
                    logger.warn("消息发布事件保存失败 payload {} ",JSONObject.toJSONString(messageVO));
                }

                requestJsonVO = RequestJsonVOGenerator.generator(appCode,messageVO);
                resultObjectVO = messageUserService.send(requestJsonVO);

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
