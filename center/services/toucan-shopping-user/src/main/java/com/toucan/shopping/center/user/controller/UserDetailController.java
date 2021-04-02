package com.toucan.shopping.center.user.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.center.user.entity.UserDetail;
import com.toucan.shopping.center.user.kafka.constant.UserMessageTopicConstant;
import com.toucan.shopping.center.user.service.*;
import com.toucan.shopping.center.user.vo.*;
import com.toucan.shopping.common.generator.IdGenerator;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 用户详情
 */
@RestController
@RequestMapping("/user/detail")
public class UserDetailController {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private IdGenerator idGenerator;


    @Autowired
    private UserDetailService userDetailService;


    @Autowired
    private KafkaTemplate kafkaTemplate;




    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO updateDetail(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到要修改的用户");
            return resultObjectVO;
        }

        try {
            UserDetailVO userDetailVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserDetailVO.class);

            if(userDetailVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("没有找到用户ID");
                return resultObjectVO;
            }

            if(StringUtils.isEmpty(userDetailVO.getNickName()))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请输入昵称");
                return resultObjectVO;
            }

            if(userDetailVO.getNickName().length()>15)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("昵称过长,请重新输入");
                return resultObjectVO;
            }
            UserDetail userDetail = new UserDetail();
            BeanUtils.copyProperties(userDetail,userDetailVO);

            List<UserDetail> userDetails = userDetailService.queryListByUserId(new Long[]{userDetailVO.getUserMainId()});
            int row =0;
            if(CollectionUtils.isNotEmpty(userDetails))
            {
                row = userDetailService.update(userDetail);

            }else{
                userDetail.setId(idGenerator.id());
                userDetail.setCreateDate(new Date());
                userDetail.setDeleteStatus((short)0);
                row = userDetailService.save(userDetail);
            }

            //发送消息通知用户调度中心更新es缓存
            if(row>=1)
            {
                kafkaTemplate.send(UserMessageTopicConstant.user_detail_modify.name(),JSONObject.toJSONString(userDetail));
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }




}
